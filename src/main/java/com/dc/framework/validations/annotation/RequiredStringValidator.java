package com.dc.framework.validations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: RequiredStringValidator
 * @Description: 字符串非空验证
 * @author: jinzhaopo
 * @date: 2015-5-11 下午12:32:10
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredStringValidator {
	boolean trim() default true;

	String message() default "";

	String key() default "";

	String fieldName() default "";
}
