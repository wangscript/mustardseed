package org.mustardseed.security.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *Web角色访问控制注解
 *@author HermitWayne
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAuthStamp {
    public String[] value() default {};
    public String[] except() default {};
    public String error() default "";
}
