package org.mustardseed.validation.struts;
import java.util.Map;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.Action;

import org.mustardseed.validation.FormValidResult;
import org.mustardseed.validation.Validator;

/**
 *在Struts2中统一表单验证拦截器
 *@author HermitWayne
 *
 *<interceptors>
 *    <interceptor name="validationInterceptor" 
 *		 class="org.mustardseed.validation.struts.ValidationHandler"/>
 *  <interceptor-stack name="mustardDefaultStack">
 *    <interceptor-ref name="validationInterceptor"/>
 *    <interceptor-ref name="defaultStack"/>
 *  </interceptor-stack>
 *</interceptors>
 *<default-interceptor-ref name="mustardDefaultStack"/>
 */
public class ValidationHandler 
    implements Interceptor {
    
    private Validator validator;
    
    //Struts2
    public void destroy() {
    }
    public void init() {
    }
    public String intercept(ActionInvocation invocation)
	throws Exception {
	
	Object action = invocation.getAction();
	Class actionClass = action.getClass();
	String methodName = invocation.getProxy().getMethod();
	Method method = actionClass.getMethod(methodName);

	HttpServletRequest request = 
	    (HttpServletRequest)invocation.getInvocationContext()
	    .get(ServletActionContext.HTTP_REQUEST);

	String ret = null;
	FormValidResult valid =
	    method.getAnnotation(FormValidResult.class);
	
	if (valid != null) {
	    Map<String, String[]> map = request.getParameterMap();
	    if (valid.script().isEmpty())
		map = validator.validate(valid.value(), map);
	    else
		map = validator.validate(valid.script(), valid.value(), map);
	    if (map != null &&
		action instanceof ValidationAware) {
		ValidationAware va = (ValidationAware)action;
		for (Map.Entry<String, String[]> item : map.entrySet()) {
		    for (String msg : item.getValue()) {
			va.addFieldError(item.getKey(), msg);
		    }
		}
		ret = Action.INPUT;
	    }
	}
	if (ret == null) {
	    ret = invocation.invoke();
	}
	
	return ret;
    }

    public void setValidator(Validator validator) {
	this.validator = validator;
    }
}