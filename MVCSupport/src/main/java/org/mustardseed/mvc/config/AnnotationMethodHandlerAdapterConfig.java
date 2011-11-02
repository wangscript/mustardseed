package org.mustardseed.mvc.config;

import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 *在注解配置的MVC环境下对AnnotationMethodHandlerAdapter进行配置的配置器
 *@author HermitWayne
 *
 *<bean class="org.mustardseed.web.config.AnnotationMethodHandlerAdapterConfig">
 *  <property name="customArgumentResolvers">
 *    <list>
 *      <ref bean="webCtrlAuthHandler"/>
 *    </list>
 *  </property>
 *</bean>
 */
public class AnnotationMethodHandlerAdapterConfig 
    implements InitializingBean,
	       ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<String, AnnotationMethodHandlerAdapter> annMethodHandler;
    private WebArgumentResolver[] argumentResolvers;

    public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers) {
	this.argumentResolvers = argumentResolvers;
    }
    
    public void afterPropertiesSet()
	throws Exception {
	annMethodHandler = 
	    applicationContext.getBeansOfType(AnnotationMethodHandlerAdapter.class);
	for (String name : annMethodHandler.keySet()) {
	    annMethodHandler.get(name)
		.setCustomArgumentResolvers(argumentResolvers);
	}
    }
    
    
    public void setApplicationContext(ApplicationContext applicationContext)
	throws BeansException {
	this.applicationContext = applicationContext;
    }
}