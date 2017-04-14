package com.dc.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @ClassName: PropertiesHelper
 * @Description: TODO
 * @author: zhaopo
 * @date: 2016年11月14日 上午9:01:11
 */
public class PropertiesHelper {

	/**
	 * 用于存放properties，避免重复读取
	 */
	private static final Map<String, Properties> props = new HashMap<String, Properties>();

	public static Properties load(String name) {
		if (props.get(name) != null) {
			return props.get(name);
		} else {
			Properties prop = new Properties();
			try {
				prop.load(PropertiesHelper.class.getResourceAsStream("/" + name + ".properties"));
				props.put(name, prop);
				return prop;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Properties getProperties(String fileName) {
		Properties properties = props.get(fileName);
		if (properties != null) {
			return properties;
		} else {
			return load(fileName);
		}
	}

	public static String getPropertiesValue(String fileName, String key) {
		Properties properties = getProperties(fileName);
		String value = properties.getProperty(key);
		return value;
	}

	/**
	 * 根据前缀获取数据字典
	 * 
	 * @param fileName
	 * @param prefix
	 * @return
	 */
	public static Properties getPropertiesByPrefix(String fileName, String prefix) {
		Properties properties = getProperties(fileName);

		Properties temp = new Properties();
		for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (key.startsWith(prefix)) {
				temp.put(key, properties.get(key));
			}
		}
		return temp;
	}
}
