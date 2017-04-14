package com.dc.framework.freemarker.directive;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.dc.common.util.FreemarkerUtils;
import com.dc.framework.page.Order;
import com.dc.framework.page.Order.Direction;
import com.dc.framework.page.SearchFilter;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

public abstract class BaseDirective implements TemplateDirectiveModel {

	private static final String UseCache = "useCache";
	private static final String CacheRegion = "cacheRegion";
	private static final String ID = "id";
	private static final String COUNT = "count";
	private static final String OrderBy = "orderBy";
	private static final String MidTrim = "\\s*,\\s*";
	private static final String LRTrim = "\\s+";

	protected boolean getUseCache(Environment environment, Map map) throws TemplateModelException {
		Boolean useCache = (Boolean) FreemarkerUtils.getParameter(UseCache, Boolean.class, map);
		return useCache == null ? true : useCache.booleanValue();
	}

	protected String getCacheRegion(Environment environment, Map map) throws TemplateModelException {
		String cacheRegion = (String) FreemarkerUtils.getParameter(CacheRegion, String.class, map);
		return cacheRegion == null ? environment.getTemplate().getName() : cacheRegion;
	}

	protected Long getId(Map map) throws TemplateModelException {
		return (Long) FreemarkerUtils.getParameter(ID, Long.class, map);
	}

	protected Integer IIIllIll(Map map) throws TemplateModelException {
		return (Integer) FreemarkerUtils.getParameter(COUNT, Integer.class, map);
	}

	protected List<SearchFilter> getFilterList(Map map, Class clazz, String as[]) throws TemplateModelException {
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		PropertyDescriptor[] apropertydescriptor = PropertyUtils.getPropertyDescriptors(clazz);
		for (int i = 0; i < apropertydescriptor.length; i++) {
			PropertyDescriptor propertydescriptor = apropertydescriptor[i];
			String propertyName = propertydescriptor.getName();
			Class class2 = propertydescriptor.getPropertyType();
			if (!ArrayUtils.contains(as, propertyName) && map.containsKey(propertyName)) {
				Object obj = FreemarkerUtils.getParameter(propertyName, class2, map);
				filterList.add(SearchFilter.eq(propertyName, obj));
			}
		}
		return filterList;
	}

	protected List<Order> getOrderList(Map map, String as[]) throws TemplateModelException {
		String orderByStr = StringUtils.trim((String) FreemarkerUtils.getParameter(OrderBy, String.class, map));
		List<Order> orderList = new ArrayList<Order>();
		if (StringUtils.isNotEmpty(orderByStr)) {
			String as1[] = orderByStr.split(MidTrim);
			for (int i = 0; i < as1.length; i++) {
				String s1 = as1[i];
				if (!StringUtils.isNotEmpty(s1)) {
					continue;
				}
				String fieldName = null;
				Direction direction = null;
				String orderStrs[] = s1.split(LRTrim);
				if (orderStrs.length == 1) {
					fieldName = orderStrs[0];
				} else {
					if (orderStrs.length < 2)
						continue;
					fieldName = orderStrs[0];
					try {
						direction = Direction.valueOf(orderStrs[1]);
					} catch (IllegalArgumentException illegalargumentexception) {
						continue;
					}
				}
				if (!ArrayUtils.contains(as, fieldName)) {
					orderList.add(new Order(fieldName, direction));
				}
			}

		}
		return orderList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void renderParamToBody(Map map, Environment environment, TemplateDirectiveBody templatedirectivebody) throws TemplateException, IOException {
		HashMap hashmap = new HashMap();
		String s;
		freemarker.template.TemplateModel templatemodel;
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); hashmap.put(s, templatemodel)) {
			s = (String) iterator.next();
			templatemodel = FreemarkerUtils.getVariable(s, environment);
		}

		FreemarkerUtils.setVariables(map, environment);
		templatedirectivebody.render(environment.getOut());
		FreemarkerUtils.setVariables(hashmap, environment);
	}

	protected void renderParamToBody(String s, Object obj, Environment environment, TemplateDirectiveBody templatedirectivebody) {
		freemarker.template.TemplateModel templatemodel;
		try {
			templatemodel = FreemarkerUtils.getVariable(s, environment);
			FreemarkerUtils.setVariable(s, obj, environment);
			templatedirectivebody.render(environment.getOut());
			FreemarkerUtils.setVariable(s, templatemodel, environment);
		} catch (TemplateModelException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
