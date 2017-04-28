package com.dc.framework.validations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: Validations
 * @Description: 验证器主要是在cu的时候要加验证
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午1:51:28
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Validations {
	/**
	 * 
	 * @Title: requiredFields
	 * @Description: 对象非空验证器
	 * @return
	 * @return: RequiredFieldValidator[]
	 */
	public RequiredFieldValidator[] requiredFields() default {};

	/**
	 * 
	 * @Title: requiredStrings
	 * @Description: 字符串非空校验
	 * @return
	 * @return: RequiredStringValidator[]
	 */
	public RequiredStringValidator[] requiredStrings() default {};

	/**
	 * 
	 * @Title: regexFields
	 * @Description: 正则表达式的验证
	 * @return
	 * @return: RegexFieldValidator[]
	 */
	public RegexFieldValidator[] regexFields() default {};

}
