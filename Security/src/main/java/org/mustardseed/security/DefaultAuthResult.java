package org.mustardseed.security;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Arrays;

/**
 *AuthResult的实现
 *@author HermitWayne
 */
public class DefaultAuthResult 
    implements AuthErrors, AuthResult {
    
    private Set<AuthError> errors;
    
    public DefaultAuthResult() {
	errors = new LinkedHashSet();
    }
    
    public void addAllErrors(AuthErrors error) {
	errors.addAll(Arrays.asList(error.getAllErrors()));
    }
    
    public AuthError[] getAllErrors() {
	return (AuthError[])errors.toArray(new AuthError[0]);
    }
    
    public AuthError[] getErrors(int error) {
	Set tmp = new LinkedHashSet();
	for (AuthError err : errors) {
	    if (err.getError() == error)
		tmp.add(err);
	}
	return (AuthError[])tmp.toArray(new AuthError[0]);
    }
    
    public AuthError[] getErrors() {
	return getAllErrors();
    }
    public AuthError getTopError() {
	AuthError ret = null;
	if (hasError())
	    for (AuthError i : errors) {
		ret = i;
		break;
	    }
	return ret;
    }
    
    public int getErrorCount() {
	return errors.size();
    }
    public int getErrorCount(int error) {
	int sum = 0;
	for (AuthError err : errors) {
	    if (err.getError() == error)
		sum += 1;
	}
	return sum;
    }
    
    public boolean hasError() {
	return getErrorCount() > 0;
    }

    public boolean hasError(int error) {
	return getErrorCount(error) > 0;
    }
    public void addError(AuthError e) {
	errors.add(e);
    }
    public void reject(int error) {
	addError(new AuthError(error));
    }
    
    public void reject(int error, String errorCode) {
	addError(new AuthError(error, errorCode));
    }
    
    public void reject(int error, String errorCode, String defaultMessage) {
	addError(new AuthError(error, errorCode, defaultMessage));
    }
    
}