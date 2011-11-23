package org.mustardseed.mvc.config;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 *用于自动配置AnnotationMethodHandlerAdapter中的customArgumentResolver
 *@author HermitWayne
 *
 *<bean class="org.mustardseed.web.config.MethodHandlerConfig"/>
 */
public class MethodHandlerConfig 
    implements InitializingBean,
	       ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void afterPropertiesSet()
	throws Exception {
	Map<String, AnnotationMethodHandlerAdapter> annMap = 
	    applicationContext.getBeansOfType(AnnotationMethodHandlerAdapter.class);
	
	for (Map.Entry item : annMap.entrySet()) {
	    AnnotationMethodHandlerAdapter ann = 
		(AnnotationMethodHandlerAdapter)item.getValue();
	    WebArgumentResolver[] args = 
		findArgResolver().toArray(new WebArgumentResolver[0]);
	    ann.setCustomArgumentResolvers(args);
	}
    }

    private List<WebArgumentResolver> findArgResolver() {
	List<WebArgumentResolver> ret = new ArrayList<WebArgumentResolver>();
	Map<String, Object> args =
	    applicationContext.getBeansWithAnnotation(CustomMethodHandlerConfig.class);
	
	for (Map.Entry item : args.entrySet()) {
	    if (item.getValue() instanceof WebArgumentResolver) {
		
		ret.add((WebArgumentResolver)item.getValue());
	    }
	}
	return ret;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
	throws BeansException {
	this.applicationContext = applicationContext;
    }
}