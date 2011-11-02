package org.mustardseed.security.struts;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.ServletActionContext;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;;
import com.opensymphony.xwork2.interceptor.Interceptor;

import org.springframework.web.context.request.ServletWebRequest;
import org.mustardseed.security.AuthError;
import org.mustardseed.security.AuthResult;
import org.mustardseed.security.WebCtrlAuthConfiguration;
import org.mustardseed.security.annotation.RoleAuthStamp;
import org.mustardseed.security.annotation.RoleAuthStamp;

/**
 *在Struts2中拦截Web请求的拦截器实现
 *@author HermitWayne
 *
 *<interceptors>
 *    <interceptor name="roleAuthInterceptor" 
 *		 class="org.mustardseed.security.struts.WebCtrlAuthHandler"/>
 *  <interceptor-stack name="mustardDefaultStack">
 *    <interceptor-ref name="roleAuthInterceptor"/>
 *    <interceptor-ref name="defaultStack"/>
 *  </interceptor-stack>
 *</interceptors>
 *<default-interceptor-ref name="mustardDefaultStack"/>
 */
public class WebCtrlAuthHandler 
    implements Interceptor {

    private WebCtrlAuthConfiguration authConfig;

    //Struts2
    public void destroy() {
    }
    public void init() {
    }
    public String intercept(ActionInvocation invocation)
	throws Exception {
	

	Class actionClass = invocation.getAction().getClass();
	String methodName = invocation.getProxy().getMethod();
	Method method = actionClass.getMethod(methodName);

	HttpServletRequest request = 
	    (HttpServletRequest)invocation.getInvocationContext()
	    .get(ServletActionContext.HTTP_REQUEST);

	HttpServletResponse response = 
	    (HttpServletResponse)invocation.getInvocationContext()
	    .get(ServletActionContext.HTTP_RESPONSE);
	
	AuthResult ar =
	    authConfig.checkAuthentication(actionClass,
					   method,
					   new ServletWebRequest(request, response));
	String ret;
	if (ar.hasError())
	    ret = ar.getTopError().getErrorCode();
	else
	    ret = invocation.invoke();
	return ret;
    }


    public void setWebCtrlAuthConfiguration(WebCtrlAuthConfiguration authConfig) {
	this.authConfig = authConfig;
    }
}