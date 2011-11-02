package org.mustardseed.demo;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

public class TempValid 
    implements Validator {
    
    //判断该校验器是否支持该类型
    public boolean supports(Class clazz) {
	//检验该类是否支持校验
	return TestObject.class.isAssignableFrom(clazz);
    }
    //进行数据校验
    public void validate(Object target, Errors errors) {
	TestObject obj = (TestObject)target;
	if (obj.getAge() < 10) {
	    errors.rejectValue("age", "field.required", "age要大于10");
	}
    }
}