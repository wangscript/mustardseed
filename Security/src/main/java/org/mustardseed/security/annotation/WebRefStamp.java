package org.mustardseed.security.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 *访问来源控制注解
 *@author HermitWayne
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebRefStamp {
    public String[] value() default {};
    public boolean passWhenNull() default true;
    public RequestMethod[] method() default {RequestMethod.GET, RequestMethod.POST};
    public String error() default "";
}
