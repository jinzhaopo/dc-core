package com.dc.common.converter;

import org.apache.commons.beanutils.converters.AbstractConverter;

/**
 * 
 * @ClassName: EnumConverter
 * @Description: 枚举类型转换器
 * @author: zhaopo
 * @date: 2016年11月3日 下午3:54:30
 */
public class EnumConverter extends AbstractConverter {

	private final Class enumClass;

	public EnumConverter(Class enumClass) {
		this(enumClass, null);
	}

	public EnumConverter(Class enumClass, Object defaultValue) {
		super(defaultValue);
		this.enumClass = enumClass;
	}

	protected Class getDefaultType() {
		return enumClass;
	}

	protected Object convertToType(Class type, Object value) {
		String s = value.toString().trim();
		return Enum.valueOf(type, s);
	}

	protected String convertToString(Object value) {
		return value.toString();
	}
}
