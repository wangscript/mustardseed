package org.mustardseed.mvc.view;

import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.View;

import org.mustardseed.utils.StreamUtils;

/**
 *二进制文件视图
 *@author HermitWayne
 */
public class LocalBinaryView implements View {
    
    private String path;
    private String filename;
    private String contentType;
    private String position;

    
    public LocalBinaryView() {}
    public LocalBinaryView(String path) {
	this(path, null, null, null);
    }
    public LocalBinaryView(String path, String filename) {
	this(path, filename, null, null);
    }
    public LocalBinaryView(String path, String filename, String contentType) {
	this(path, filename, contentType, null);
    }
    public LocalBinaryView(String path, String filename, String contentType, String position) {
	this.filename = filename;
	this.contentType = contentType;
	this.path = path;
	this.position = position;
    }
    
    
    public String getContentType() {
	return "application/octet-stream";
    }
    
    public void render(Map<String,?> model,
		       HttpServletRequest request,
		       HttpServletResponse response)
	throws Exception {

	File file = null;
	String name = null, type = null, pos = null;
	InputStream in = null;
	if (path == null) {
	    file = new File((String)model.get("path"));
	    name = (String)model.get("filename");
	    type = (String)model.get("contentType");
	    pos = (String)model.get("position");
	} else {
	    file = new File(path);
	    name = filename;
	    type = contentType;
	    pos = position;
	}
	
	if (type != null)
	    response.setContentType(type);
	response.setHeader("Content-Disposition",
			   getContentDisposition(name, pos));
	in = getInputStream(file);
	if (in != null) {
	    response.setContentLength(in.available());
	    StreamUtils.copyStream(in, response.getOutputStream());
	    in.close();
	}
    }
    
    private InputStream getInputStream(File file) {
	InputStream ret = null;
	try {
	    ret = new FileInputStream(file);
	} catch (Exception e) {}
	return ret;
    }
    
    private String getContentDisposition(String filename, String position) {
	StringBuilder ret = new StringBuilder();
	String name = null;
	if (position != null && !position.isEmpty()) {
	    ret.append(position);
	    ret.append(";");
	}
	try {
	    name = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
	} catch (Exception e) {}
	
	if (name != null) {
	    ret.append("filename=");
	    ret.append(name);
	}
	return ret.toString();
    }

}