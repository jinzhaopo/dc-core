package com.dc.framework.validations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 正则验证
 * @author fangchen
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexFieldValidator {
	
	String message() default "";

	String key() default "";

	String fieldName() default "";

	String expression();
}
