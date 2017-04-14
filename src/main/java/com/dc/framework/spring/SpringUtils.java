// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.dc.framework.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 
 * @ClassName: SpringUtils
 * @Description: spring 工具类
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午3:08:22
 */
@Component
public final class SpringUtils implements DisposableBean, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private SpringUtils() {
	}

	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void destroy() {
		applicationContext = null;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	public static Object getBean(String name, Class type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}

	public static String getMessage(String code, Object... args) {
		LocaleResolver localeresolver = (LocaleResolver) getBean("localeResolver", LocaleResolver.class);
		java.util.Locale locale = localeresolver.resolveLocale(null);
		return applicationContext.getMessage(code, args, locale);
	}
}
