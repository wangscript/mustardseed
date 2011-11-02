package org.mustardseed.security.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebArgumentResolver;

import org.mustardseed.security.AuthError;
import org.mustardseed.security.AuthResult;
import org.mustardseed.security.WebCtrlAuthConfiguration;
import org.mustardseed.security.annotation.RoleAuthStamp;

/**
 *在MVC中截获Web请求
 *@author HermitWayne
 *
 *<bean class="org.mustardseed.web.config.AnnotationMethodHandlerAdapterConfig">
 *  <property name="customArgumentResolvers">
 *    <list>
 *      <ref bean="webCtrlAuthHandler"/>
 *    </list>
 *  </property>
 *</bean>
 *<bean name="webCtrlAuthHandler"
 *      class="org.mustardseed.security.spring.WebCtrlAuthHandler">
 *  <property name="webCtrlAuthConfiguration"
 *	      ref="webCtrlAuthConfiguration"/>
 *</bean>
 */

public class WebCtrlAuthHandler 
    implements WebArgumentResolver {

    private WebCtrlAuthConfiguration authConfig;
    
    public Object resolveArgument(MethodParameter methodParameter,
				  NativeWebRequest webRequest)
	throws Exception {
	if (methodParameter.getParameterType().equals(AuthResult.class)) {
	    return authConfig.checkAuthentication(methodParameter.getDeclaringClass(),
						  methodParameter.getMethod(),
						  webRequest);
	    
	}
	return UNRESOLVED;
    }

    public void setWebCtrlAuthConfiguration(WebCtrlAuthConfiguration authConfig) {
	this.authConfig = authConfig;
    }
}