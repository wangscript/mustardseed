package org.mustardseed.security;

import java.util.Set;

/**
 *在MVC中，用于传递错误集合的对象接口
 *@author HermitWayne
 */
public interface AuthResult {
    public void addError(AuthError e);
    public boolean hasError();
    public AuthError[] getErrors();
    public AuthError[] getErrors(int error);
    public AuthError getTopError();
}