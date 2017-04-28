package com.dc.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.baisha.util.DateHelper;
import org.springframework.ui.ModelMap;

import com.dc.CommonAttributes;
import com.dc.framework.page.Pager;

/**
 * 字符串工具类
 *
 */
public class StringUtil {

	/**
	 * 将obj转换为字符串，如果obj为null，转换为""
	 * 
	 * @param obj
	 * @return
	 */
	public static String getNullStr(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 判断对象是否不为空且不为空串
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if (obj == null || "".equals(obj)) {
			return false;
		}
		return true;
	}

	/**
	 * 根据分割符号分割对象
	 * 
	 * @param obj
	 *            分割对象
	 * @param ch
	 *            分割符号
	 * @return
	 */
	public static String[] split(Object obj, String ch) {
		return StringUtil.getNullStr(obj).split(ch);
	}

	/**
	 * 转换字符串编码
	 * 
	 * @param str
	 * @param oldCharset
	 *            原始编码
	 * @param newCharset
	 *            新编码
	 * @return
	 */
	public static String translate(String str, String oldCharset, String newCharset) {
		if (str == null || str.equals("")) {
			return "";
		}
		try {
			return new String(str.getBytes(oldCharset), newCharset);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	// 处理数组字符
	public static String[] arrContrast(String[] arr1, Long[] arr2) {
		List<String> list = new LinkedList<String>();
		for (String str : arr1) { // 处理第一个数组,list里面的值为1,2,3,4
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (Long str : arr2) { // 如果第二个数组存在和第一个数组相同的值，就删除
			if (list.contains(str)) {
				list.remove(str);
			}
		}
		String[] result = {}; // 创建空数组
		return list.toArray(result); // List to Array
	}

	/**
	 * 将XMLGregorianCalendar类型转换成Date类型
	 * 
	 * @param cal
	 * @return
	 */
	public static String convertToDate(XMLGregorianCalendar cal, String format) {
		if (cal == null) {
			return "";
		}
		GregorianCalendar ca = cal.toGregorianCalendar();
		return DateHelper.date2String(ca.getTime(), format);
	}

	/**
	 * 获取字符串的标识位置
	 * 
	 * @param cal
	 * @return
	 */
	public static List<Integer> stringSeparatePositionIndex(String str, char separate) {
		char[] chars = str.toCharArray();
		List<Integer> positions = new ArrayList<Integer>();
		for (int i = 0, length = chars.length; i < length; i++) {
			char currentChar = chars[i];
			if (currentChar == separate) {
				positions.add(i);
			}
		}
		return positions;
	}

	public static List<String> stringSeparates(String str, char separate) {
		List<String> parentCodes = new ArrayList<String>();
		parentCodes.add(str);
		if (StringUtils.isNotBlank(str)) {
			List<Integer> positions = StringUtil.stringSeparatePositionIndex(str, CommonAttributes.TREE_SEPARATE);
			if (positions != null && positions.size() > 0) {
				for (Integer i : positions) {
					parentCodes.add(str.substring(0, i));
				}
			}
		}
		return parentCodes;
	}

	public static String getByRegEx(String str, String regEx) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	private static StringBuilder contactOneJsonStr(Object obj, String[] fieldNames) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Field[] superFileds = clazz.getSuperclass().getDeclaredFields();
		Field[] allFields = new Field[fields.length + superFileds.length];
		int index = 0;
		for (int i = 0; i < superFileds.length; i++) {
			allFields[i] = superFileds[i];
			index++;
		}
		for (int i = 0; i < fields.length; i++) {
			allFields[index++] = fields[i];
		}
		for (Field field : allFields) {
			field.setAccessible(true);
			for (String filedName : fieldNames) {
				if (field.getName().equals(filedName) && field.get(obj) != null) {
					sb.append(field.getName());
					sb.append(":");
					if (field.getType().getName().equals("java.lang.String"))
						sb.append("'");
					sb.append(field.get(obj));
					if (field.getType().getName().equals("java.lang.String"))
						sb.append("'");
					sb.append(",");
				}
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb;
	}

	public static <T> String contactManyJsonStr(List<T> list, String[] fieldNames) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : list) {
			try {
				StringBuilder tmp = contactOneJsonStr(obj, fieldNames);
				sb.append(tmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append(";");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String contactManyJsonStr(Pager pager, String[] fieldNames) {
		String jsonStr = contactManyJsonStr(pager.getList(), fieldNames);
		jsonStr = jsonStr + "#" + pager.getTotalCount() + "#" + pager.getPageCount();
		return jsonStr;
	}

	// public static StringBuilder contactOneJsonStr(Map<String, Object> map) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("{");
	// for (Map.Entry<String, Object> e : map.entrySet()) {
	// sb.append(e.getKey());
	// sb.append(":");
	// sb.append("'");
	// sb.append(e.getValue());
	// sb.append("'");
	// sb.append(",");
	// }
	// sb = sb.deleteCharAt(sb.length() - 1);
	// sb.append("}");
	// return sb;
	// }
	//
	// public static String contactManyJsonStr(List<Map<String, Object>> list) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("[");
	// for (Map<String, Object> map : list) {
	// sb = contactOneJsonStr(map);
	// sb.append(";");
	// }
	// sb = sb.deleteCharAt(sb.length() - 1);
	// sb.append("]");
	// return sb.toString();
	// }

	/**
	 * @Title: checkPhone
	 * @Description: 检查手机号格式是否正确
	 * @param phone
	 * @param map
	 * @return: boolean
	 */
	public static boolean checkPhone(String phone, ModelMap map) {
		String phoneReg = "^[1][3|5|7|8][0-9]{9}$";
		Pattern p = Pattern.compile(phoneReg);
		Matcher m = p.matcher(phone);
		if (!m.matches()) {
			map.put("msg", "手机号格式不正确");
			return false;
		}
		return true;
	}
}
