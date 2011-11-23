package org.mustardseed.validation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *用于标注需要表单验证的method.
 *在Struts中,标注在method上.在MVC中,标注在存放错误信息的Map参数上.
 *@author HermitWayne
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormValidResult {
    //定义所要调用的校验函数名
    public String value() default "";
    //定义所使用脚本的路径,默认情况下将使用全局配置
    public String script() default "";
}