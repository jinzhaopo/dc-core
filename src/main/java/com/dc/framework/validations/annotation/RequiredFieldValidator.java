package com.dc.framework.validations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: RequiredFieldValidator
 * @Description: 对象非空验证器
 * @author: Administrator
 * @date: 2015年5月10日 下午1:58:16
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredFieldValidator {
	String message() default "";

	String key() default "";

	String fieldName() default "";

}
