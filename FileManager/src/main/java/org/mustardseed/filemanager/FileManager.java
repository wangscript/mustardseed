package org.mustardseed.filemanager;

import java.io.File;
import java.net.URI;
import org.springframework.web.multipart.MultipartFile;


/**
 *文件上传管理的接口.
 *QuickFile 是指不需要保存文件名等信息的文件.
 *NodeFile 是指那些需要保存在树状结构中的文件，并且保留文件名.
 *ID为文件数据在FileManager中唯一的标识符.
 *URI是适合服务端访问数据的路径对象.
 *URL是适合客户端访问数据的路径对象.
 *当操作发生错误时将返回null.
 *
 *@author HermitWayne
 */
public interface FileManager {
    public String putQuickFile(MultipartFile file);
    public String putQuickFile(File file);
    public URI getQuickFileURI(String id);
    public URI getQuickFileURL(String id);
    
    public String getRootNodeId();
    public String getParentNodeId(String id);
    public FileNode getNode(String id);
    public FileNode[] listNodes(String id);
    public String makeSubNode(String id, String name);
    public String putFileNode(String id, MultipartFile file);
    public String putFileNode(String id, MultipartFile file, String name);
    public String putFileNode(String id, File file, String name);
    public boolean deleteNode(String id);
    public URI getNodeFileURI(String id);
    public URI getNodeFileURL(String id);
}