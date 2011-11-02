package org.mustardseed.filemanager;

/**
 *保存文件节点的信息
 *@author HermitWayne
 */
public class FileNode {
    private String id;
    private String name;
    //是否目录
    private boolean canBeList;
    
    public String getId() {
	return id;
    }
    public void setId(String id) {
	this.id = id;
    }
    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }
    public boolean isDirectory() {
	return canBeList;
    }
    public boolean isFile() {
	return !canBeList;
    }
    public void setCanBeList(boolean canBeList) {
	this.canBeList = canBeList;
    }
}