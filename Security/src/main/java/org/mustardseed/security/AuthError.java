package org.mustardseed.security;

/**
 *权限管理的错误信息
 *@author HermitWayne
 */

public class AuthError {
    //角色访问控制
    public static int WebCtrlRole = 0;
    //来源访问控制
    public static int WebCtrlRef = 1;
    
    private int error;
    private String errorCode;
    private String defaultMessage;

    public AuthError(int error, String errorCode, String defaultMessage) {
	this.error = error;
	this.errorCode = errorCode;
	this.defaultMessage = defaultMessage;
    }
    public AuthError(int error) {
	this(error, "", "");
    }
    public AuthError(int error, String errorCode) {
	this(error, errorCode, "");
    }
    
    public int hashCode() {
	return errorCode.hashCode() + defaultMessage.hashCode();
    }
    public boolean equals(Object obj) {
	boolean ret = false;
	AuthError tmp = (AuthError)obj;
	ret = obj instanceof AuthError &&
	    errorCode.equals(tmp.errorCode) &&
	    defaultMessage.equals(tmp.defaultMessage);
	return ret;
				 
    }
    public int getError() {
	return error;
    }
    public String getErrorCode() {
	return errorCode;
    }
    public String getDefaultMessage() {
	return defaultMessage;
    }
}