package org.mustardseed.validation.spring;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.bind.support.WebArgumentResolver;

import org.mustardseed.validation.FormValidResult;
import org.mustardseed.validation.Validator;
import org.mustardseed.mvc.config.CustomMethodHandlerConfig;

/**
 *在MVC中拦截Form表单验证请求
 *@author HermitWayne
 *
 *<bean name="validator"
 *      class="org.mustardseed.validation.Validator">
 *  <property name="scriptPath" value="/script/validator.js"/>
 *</bean>
 *  
 *<bean name="validatorHandler"
 *      class="org.mustardseed.validation.spring.ValidationHandler">
 *  <property name="validator" ref="validator"/>
 *</bean>
 */

@CustomMethodHandlerConfig
public class ValidationHandler 
    implements WebArgumentResolver {

    private Validator validator;

    public Object resolveArgument(MethodParameter methodParameter,
				  NativeWebRequest webRequest)
	throws Exception {
	FormValidResult valid = 
	    methodParameter.getParameterAnnotation(FormValidResult.class);
	
	if (valid != null &&
	    methodParameter.getParameterType().equals(Map.class)) {
	    HttpServletRequest request = 
		webRequest.getNativeRequest(HttpServletRequest.class);
	    
	    Map<String, String[]> map = request.getParameterMap();
	    if (valid.script().isEmpty())
		map = validator.validate(valid.value(), map);
	    else
		map = validator.validate(valid.script(), valid.value(), map);
	    return map;
	}
	return UNRESOLVED;
    }

    
    public void setValidator(Validator validator) {
	this.validator = validator;
    }
    public Validator getValidator() {
	return validator;
    }
}