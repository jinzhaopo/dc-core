package com.dc.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.dc.CommonAttributes;
import com.dc.common.converter.MyConvertUtils;
import com.dc.framework.spring.SpringUtils;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * 
 * @ClassName: FreemarkerUtils
 * @Description: 前台工具类
 * @author: zhaopo
 * @date: 2016年11月3日 下午3:52:15
 */
public class FreemarkerUtils {

	public static final String SYSTEM_CONFIG_THEME_KEY = "systemTheme";// systemConfig缓存Key
	public static final String SPT = File.separator;
	public static final String THEME = "theme";
	private static final ConvertUtilsBean convertUtilsBean;

	static {
		convertUtilsBean = new MyConvertUtils();
		DateConverter dateconverter = new DateConverter();
		dateconverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(dateconverter, Date.class);
	}

	public static String process(String template, Map model) {
		Configuration configuration = null;
		FreeMarkerConfigurer freemarkerconfigurer = (FreeMarkerConfigurer) SpringUtils.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
		if (freemarkerconfigurer != null) {
			configuration = freemarkerconfigurer.getConfiguration();
		}
		return process(template, model, configuration);
	}

	public static String process(File templateFile, Map model, Configuration configuration) {
		if (templateFile == null || !templateFile.exists()) {
			return null;
		}
		if (configuration == null) {
			configuration = new Configuration();
		}
		StringWriter stringwriter = new StringWriter();
		try {
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile), "UTF-8"));
			new Template("template", reader, configuration).process(model, stringwriter);
		} catch (TemplateException templateexception) {
			templateexception.printStackTrace();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		return stringwriter.toString();
	}

	public static String process(String template, Map model, Configuration configuration) {
		if (template == null) {
			return null;
		}
		if (configuration == null) {
			configuration = new Configuration();
		}
		StringWriter stringwriter = new StringWriter();
		try {
			new Template("template", new StringReader(template), configuration).process(model, stringwriter);
		} catch (TemplateException templateexception) {
			templateexception.printStackTrace();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		return stringwriter.toString();
	}

	@SuppressWarnings("rawtypes")
	public static Object getParameter(String name, Class type, Map params) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(type);
		Assert.notNull(params);
		TemplateModel templatemodel = (TemplateModel) params.get(name);
		if (templatemodel == null) {
			return null;
		} else {
			Object obj = DeepUnwrap.unwrap(templatemodel);
			return convertUtilsBean.convert(obj, type);
		}
	}

	public static TemplateModel getVariable(String name, Environment env) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		return env.getVariable(name);
	}

	public static void setVariable(String name, Object value, Environment env) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		if (value instanceof TemplateModel) {
			env.setVariable(name, (TemplateModel) value);
		} else {
			env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
		}
	}

	@SuppressWarnings("rawtypes")
	public static void setVariables(Map variables, Environment env) throws TemplateModelException {
		Assert.notNull(variables);
		Assert.notNull(env);
		for (Iterator iterator = variables.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String s = (String) entry.getKey();
			Object obj = entry.getValue();
			if (obj instanceof TemplateModel) {
				env.setVariable(s, (TemplateModel) obj);
			} else {
				env.setVariable(s, ObjectWrapper.BEANS_WRAPPER.wrap(obj));
			}
		}

	}

}
