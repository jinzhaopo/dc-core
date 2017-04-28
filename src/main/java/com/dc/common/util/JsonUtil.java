package com.dc.common.util;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

/**
 * json工
 * 
 * @author jinzhaopo
 * @date 2016年7月4日上午10:36:11
 * @version 3.0.0
 */
public class JsonUtil {
	private static JsonUtil ju;
	private static JsonFactory jf;
	private static ObjectMapper mapper;

	private JsonUtil() {
	}

	public static JsonUtil getInstance() {
		if (ju == null)
			ju = new JsonUtil();
		return ju;
	}

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}

	public static JsonFactory getFactory() {
		if (jf == null)
			jf = new JsonFactory();
		return jf;
	}

	public String obj2json(Object obj) {
		JsonGenerator jg = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jf = getFactory();
			mapper = getMapper();
			StringWriter out = new StringWriter();
			jg = jf.createGenerator(out);
			mapper.writeValue(jg, obj);			
			
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (jg != null)
					jg.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<?> json2Obj(String json, Class<?> clz) {
		mapper = getMapper();
		try {
			return mapper.readValue(json, new TypeReference<List<?>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object json2obj(String json, Class<?> clz) {
		try {
			mapper = getMapper();
			return mapper.readValue(json, clz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: getJsonObject
	 * @Description: 获取jsonobject对象
	 * @param json
	 * @return
	 * @return: JSONObject
	 */
	public JSONObject getJsonObject(String json) {
		JSONObject jo = JSONObject.fromObject(json);
		return jo;
	}

	private static Object parseJsonStrToObject(String jsonStr, String className, String[] fieldNames) throws Exception {
		Class<?> clazz = Class.forName(className);
		Object obj = clazz.newInstance();
		Class<?> superClass = clazz.getSuperclass();
		Field[] superFields = superClass.getDeclaredFields();
		JSONObject json = JSONObject.fromObject(jsonStr);
		// Map<String, String> map = parseJsonToMap(jsonStr);
		Field[] fields = clazz.getDeclaredFields();
		for (String fieldName : fieldNames) {
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					field.setAccessible(true);
					if (field.getType().getName().equals("java.lang.Long"))
						field.set(obj, Long.parseLong(String.valueOf(json.get(fieldName))));
					else
						field.set(obj, json.get(fieldName));
				}
			}
			for (Field field : superFields) {
				if (field.getName().equals(fieldName)) {
					field.setAccessible(true);
					if (field.getType().getName().equals("java.lang.Long"))
						field.set(obj, Long.parseLong(String.valueOf(json.get(fieldName))));
					else
						field.set(obj, json.get(fieldName));
				}
			}
		}
		return obj;
	}

	public static <T> List<T> parseJsonStrToList(String jsonStrs, String className, String[] fieldNames) {
		List<T> list = new LinkedList<T>();
		if (jsonStrs.indexOf(";") >= 0) {
			String[] jsonArray = jsonStrs.split(";");
			for (String jsonStr : jsonArray) {
				try {
					T t = (T) parseJsonStrToObject(jsonStr, className, fieldNames);
					list.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				T t = (T) parseJsonStrToObject(jsonStrs, className, fieldNames);
				list.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", 123);
		map.put("name", "eeee");
		System.out.println(getInstance().obj2json(map));

		Map<String, Object> map1 = (Map<String, Object>) getInstance().json2obj(getInstance().obj2json(map), Map.class);
		System.out.println(map1.get("key"));
	}
}
