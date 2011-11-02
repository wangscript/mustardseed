package org.mustardseed.security;

/**
 *错误集合的接口对象
 *@author HermitWayne
 */
public interface AuthErrors {
    public void addAllErrors(AuthErrors error);
    public AuthError[] getAllErrors();
    public AuthError[] getErrors(int error);
    public int getErrorCount();
    public int getErrorCount(int error);
    public boolean hasError();
    public boolean hasError(int error);
    public void reject(int error);
    public void reject(int error, String errorCode);
    public void reject(int error, String errorCode, String defaultMessage);
}