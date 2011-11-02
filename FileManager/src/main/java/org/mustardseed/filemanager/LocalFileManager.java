package org.mustardseed.filemanager;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URI;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriUtils;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.multipart.MultipartFile;

import org.mustardseed.utils.MD5Utils;

/**
 *本地文件管理实现.
 *文件名中若有空格,则空格将被转换成下划线.
 *location 标注的是用于在服务器上存储的路径,
 *host 标注的是location指定的目录在所对应的url.
 *@author HermitWayne
 */
public class LocalFileManager
    extends WebApplicationObjectSupport
    implements FileManager {
    private URI location;
    private URI host;

    public String putQuickFile(MultipartFile file) {
	String ret = null;
	try {
	    File temp = File.createTempFile("fmr", "tmp", getTempDir());
	    file.transferTo(temp);
	    String md5 = MD5Utils.hash(temp);
	    ret = putQuickFile(temp);
	    temp.delete();
	} catch (Exception e) {}
	return ret;
	
    }
    public String putQuickFile(File file) {
	String md5 = MD5Utils.hash(file);
	return putFileNode(getRootNodeId(), file, md5);
    }
    public URI getQuickFileURI(String id) {
	return getNodeFileURI(id);
    }
    public URI getQuickFileURL(String id) {
	return getNodeFileURL(id);
    }
    
    public String getRootNodeId() {
	return "";
    }
    public String getParentNodeId(String id) {
	FileNode node = getNode(id);
	String ret = null;
	if (node != null) {
	    URI tmp = getNodeURI(node.getId(), "..");
	    ret = getNodeID(tmp);
	}
	return ret;
    }
    public FileNode getNode(String id) {
	URI norm = getNodeURI(id, null);
	FileNode ret = getFileNode(openNode(norm), getNodeID(norm));
	return ret;
    }
    public FileNode[] listNodes(String id) {
	FileNode[] ret = null;
	FileNode base = getNode(id);
	List<FileNode> nodeList = new ArrayList<FileNode>();
	if (base != null && base.isDirectory()) {
	    File lst = openNode(getNodeURI(base.getId(), null));
	    for (String name : lst.list()) {
		URI norm = getNodeURI(base.getId(), name);
		FileNode tmp = getFileNode(openNode(norm), getNodeID(norm));
		nodeList.add(tmp);
	    }
	    ret = nodeList.toArray(new FileNode[0]);
	}
	return ret;
    }
    public String makeSubNode(String id, String name) {
	FileNode base = getNode(id);
	String ret = null;
	if (base != null && base.isDirectory()) {
	    URI norm = getNodeURI(base.getId(), name);
	    String nid = getNodeID(norm);
	    if (nid != null) {
		File file = new File(norm.getPath());
		file.mkdir();
		FileNode tmp = getNode(nid);
		if (tmp != null)
		    ret = tmp.getId();
	    }
	}
	return ret;
    }
    public String putFileNode(String id, MultipartFile file) {
	return putFileNode(id, file, file.getOriginalFilename());
    }
    public String putFileNode(String id, MultipartFile file, String name) {
	String ret = null;
	try {
	    File temp = File.createTempFile("fmr", "tmp", getTempDir());
	    file.transferTo(temp);
	    ret = putFileNode(id, temp, name);
	    temp.delete();
	} catch (Exception e) {}
	return ret;
    }
    public String putFileNode(String id, File file, String name) {
	String ret = null;
	FileNode base = getNode(id);
	if (base != null && base.isDirectory()) {
	    URI norm = getNodeURI(base.getId(), name);
	    String nid = getNodeID(norm);
	    if (nid != null) {
		File dest = openNode(norm);
		try {
		    FileUtils.copyFile(file, dest);
		    FileNode node = getFileNode(dest, nid);
		    ret = node.getId();
		} catch (Exception e) {}
	    }
	}
	return ret;
    }
    public boolean deleteNode(String id) {
	boolean ret = false;
	URI uri = getNodeURI(id, null);
	if (getNodeID(uri) != null) {
	    File file = openNode(uri);
	    ret = file.delete();
	}
	return ret;
    }
    public URI getNodeFileURI(String id) {
	URI ret = null;
	FileNode node = getNode(id);
	if (node != null && node.isFile()) {
	    ret = getLocation().resolve(node.getId());
	}
	return ret;
    }
    public URI getNodeFileURL(String id) {
	URI ret = null;
	FileNode node = getNode(id);
	if (node != null && node.isFile()) {
	    ret = host.resolve(node.getId());
	}
	return ret;
    }

    /* 内部操作的一些基本函数 */
    private URI getLocation() {
	URI ret = null;
	try {
	    ServletContext ctx = getServletContext();
	    URI base = new File(ctx.getRealPath("/")).toURI();
	    ret = base.resolve(location);
	} catch (Exception e) {
	    ret = location;
	}
	return ret;
    }
    private URI getNodeURI(String id, String ex) {
	
	URI ret = getLocation().resolve(id);
	if (ex != null && !ex.isEmpty()) 
	    ret = ret.resolve(ex.replace(" ", "_"));
	return ret;
    }
    private String getNodeID(URI nodeUri) {
	String ret = null;
	URI normId = getLocation().relativize(nodeUri);
	if (!normId.toString().startsWith(".") &&
	    !normId.toString().startsWith("file:")) {
	    ret = normId.normalize().toString();
	}
	return ret;
    }
    private File openNode(URI nodeUri) {
	File ret = new File(nodeUri.getPath());
	return ret;
    }
    private FileNode getFileNode(File file, String id) {
	FileNode ret = null;
	if (file != null && id != null &&
	    file.exists()) {
	    ret = new FileNode();
	    boolean isDir = file.isDirectory();
	    if (isDir &&
		!id.isEmpty() &&
		!id.endsWith("/")) 
		ret.setId(id + "/");
	    else
		ret.setId(id);
	    ret.setName(id.isEmpty() ? "" : file.getName());
	    ret.setCanBeList(isDir);
	}
	return ret;
    }
    /*getter and setter*/
    public void setLocation(URI location) {
	this.location = location;
    }
    public void setHost(URI host) {
	this.host = host;
    }
}