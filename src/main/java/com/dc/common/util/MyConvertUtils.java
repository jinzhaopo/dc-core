package com.dc.common.util;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;

import com.dc.common.converter.EnumConverter;


/**
 * 
 * @ClassName: MyConvertUtils
 * @Description: 自定义转换器
 * @author: zhaopo
 * @date: 2016年11月3日 下午3:55:00
 */
public class MyConvertUtils extends ConvertUtilsBean {

	@SuppressWarnings("rawtypes")
	public String convert(Object value) {
		if (value != null) {
			Class class1 = value.getClass();
			if (class1.isEnum() && super.lookup(class1) == null) {
				super.register(new EnumConverter(class1), class1);
			} else {
				if (class1.isArray() && class1.getComponentType().isEnum()) {
					if (super.lookup(class1) == null) {
						ArrayConverter arrayconverter = new ArrayConverter(class1, new EnumConverter(class1.getComponentType()), 0);
						arrayconverter.setOnlyFirstToString(false);
						super.register(arrayconverter, class1);
					}
					Converter converter = super.lookup(class1);
					return (String) converter.convert(String.class, value);
				}
			}
		}
		return super.convert(value);
	}

	@SuppressWarnings("rawtypes")
	public Object convert(String value, Class clazz) {
		if (clazz.isEnum() && super.lookup(clazz) == null)
			super.register(new EnumConverter(clazz), clazz);
		return super.convert(value, clazz);
	}

	@SuppressWarnings("rawtypes")
	public Object convert(String values[], Class clazz) {
		if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null)
			super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
		return super.convert(values, clazz);
	}

	@SuppressWarnings("rawtypes")
	public Object convert(Object value, Class targetType) {
		if (super.lookup(targetType) == null)
			if (targetType.isEnum()) {
				super.register(new EnumConverter(targetType), targetType);
			} else {
				if (targetType.isArray() && targetType.getComponentType().isEnum()) {
					ArrayConverter arrayconverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
					arrayconverter.setOnlyFirstToString(false);
					super.register(arrayconverter, targetType);
				}
			}
		return super.convert(value, targetType);
	}
}
