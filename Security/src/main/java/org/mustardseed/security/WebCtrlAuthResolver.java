package org.mustardseed.security;

import java.lang.reflect.Method;
import org.springframework.web.context.request.NativeWebRequest;
/**
 *安全模块所要实现的接口
 *@author HermitWayne
 */
public interface WebCtrlAuthResolver {
    public AuthError checkAuthentication(Class clazz,
					  Method method,
					  NativeWebRequest webRequest);
}