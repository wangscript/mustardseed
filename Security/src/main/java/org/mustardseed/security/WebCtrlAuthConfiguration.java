package org.mustardseed.security;

import java.util.List;
import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebArgumentResolver;

/**
 *访问控制的注册对象.
 *用于注册所要使用的安全模块.
 *@author HermitWayne
 *
 *<bean name="webCtrlAuthConfiguration"
 *	class="org.mustardseed.security.WebCtrlAuthConfiguration">
 *  <property name="resolverList">
 *    <list>
 *	<ref bean="webCtrlRoleResolver"/>
 *	<ref bean="webCtrlRefResolver"/>
 *    </list>
 *  </property>
 *</bean>
 *
 */

public class WebCtrlAuthConfiguration {
    private List<WebCtrlAuthResolver> resolverList;
    
    public AuthResult checkAuthentication(Class clazz,
					  Method method,
					  NativeWebRequest webRequest) {
	DefaultAuthResult ret = new DefaultAuthResult();
	for(WebCtrlAuthResolver i : resolverList) {
	    AuthError tmp = i.checkAuthentication(clazz, method, webRequest);
	    if (tmp != null) ret.addError(tmp);
	}
	return ret;
    }
    
    public void setResolverList(List<WebCtrlAuthResolver> resolverList) {
	this.resolverList = resolverList;
    }
    public List<WebCtrlAuthResolver> getResolverList() {
	return resolverList;
    }
}
