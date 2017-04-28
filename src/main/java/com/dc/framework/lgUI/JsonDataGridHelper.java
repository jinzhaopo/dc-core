package com.dc.framework.lgUI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ognl.OgnlException;
import org.baisha.util.DateHelper;

import com.dc.common.util.AppContext;
import com.dc.common.util.PropertiesHelper;
import com.dc.framework.page.Pager;
import com.dc.framework.spring.SpringUtils;

/**
 * 
 * @ClassName: JsonDataGridHelper
 * @Description: LG -- JSON
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2016年11月19日 上午11:27:50
 */
public class JsonDataGridHelper {

	public static Pager createJSONData(Pager pager) {
		if (pager == null) {
			return null;
		}
		List<Object> dataList = pager.getList();
		pager.setList(createJSONData(dataList));
		return pager;
	}

	/**
	 * 将page类转换成ligerUI Grid框架所需的JSON格式数据。
	 * 
	 * @param data
	 * @return
	 */
	public static List<Object> createJSONData(List dataList) {
		List<Object> list = new ArrayList<Object>();
		HttpServletRequest request = AppContext.getRequest();
		String filedStr = request.getParameter("field");
		if (StringUtils.isEmpty(filedStr)) {
			return null;
		}
		String[] field = filedStr.split(",");
		String[] dict = StringUtils.splitPreserveAllTokens(request.getParameter("dict"), ",");
		String[] i18n = StringUtils.splitPreserveAllTokens(request.getParameter("i18n"), ",");
		for (Object data : dataList) {
			Map<String, String> keyValue = new HashMap<String, String>();
			for (int i = 0; i < field.length; i++) {
				String value = getValueByOgnl(data, field[i], "");
				if (i18n != null && i18n[i].trim().length() > 0 && !StringUtils.isBlank(value)) {
					String propertiesValue = SpringUtils.getMessage(StringUtils.capitalize(i18n[i]) + "." + value, null);
					keyValue.put(field[i], propertiesValue);
				} else if (dict != null && dict[i].trim().length() > 0 && !StringUtils.isBlank(value)) {
					String propertiesValue = PropertiesHelper.getPropertiesValue("i18n_zh_CN", dict[i] + "." + getValueByOgnl(data, field[i], ""));

					if (StringUtils.isNotEmpty(propertiesValue) && propertiesValue.contains("${base}")) {
						propertiesValue = StringUtils.replace(propertiesValue, "${base}", request.getContextPath());
					}
					keyValue.put(field[i], propertiesValue);
				} else {
					keyValue.put(field[i], getValueByOgnl(data, field[i], ""));
				}
			}
			list.add(keyValue);
		}
		return list;
	}

	// /**
	// * 根据field将List转换成需要的json字符串。
	// * @param dataList
	// * @return
	// */
	// public static JsonDataGridHelper createJSONDataByFields(List dataList){
	// if(dataList == null || dataList.size() == 0){
	// return null;
	// }
	// HttpServletRequest request = AppContext.getRequest();
	// String[] field = request.getParameter("field").split(",");
	// JsonDataGridHelper gridData = new JsonDataGridHelper();
	// for(Object data: dataList){
	// Map<String,String> keyValue = new HashMap<String,String>();
	// for(int i = 0; i < field.length;i++){
	// keyValue.put(field[i], getValueByOgnl(data, field[i],""));
	// }
	// gridData.getRows().add(keyValue);
	// }
	// return gridData;
	// }

	// /**
	// * 转换List Grid 列表
	// * 〈一句话功能简述〉
	// * 〈功能详细描述〉
	// * @param dataList
	// * @return JSONObject
	// * 如果有违例，请使用@exception/throws [违例类型] [违例说明：异常的注释必须说明该异常的含义及什么条件下抛出该
	// * @see [类、类#方法、类#成员]
	// */
	// public static JSONObject getJSONFromList(List list) {
	//
	// HttpServletRequest request = ServletActionContext.getRequest();
	// String[] field = request.getParameter("field").split(",");
	//// String[] cdd =
	// StringUtils.splitPreserveAllTokens(request.getParameter("cdd"), ",");
	//
	// JsonDataGridHelper gridData = new JsonDataGridHelper();
	// List dataList = list;
	// String[] cpp =
	// StringUtils.splitPreserveAllTokens(request.getParameter("cpp"), ",");
	//
	// for(Object data: dataList){
	// Map<String,String> keyValue = new HashMap<String,String>();
	// for(int i = 0; i < field.length;i++){
	//// if(field[i].lastIndexOf("_cdd") != -1 ){
	//// if(cdd[i].trim().length()>0) {
	//// String fieldName = field[i].split("_cdd")[0];
	//// //通过数字字典获取数据
	//// String[] cddValue = StringUtils.splitPreserveAllTokens(cdd[i], "|");
	//// String realValue = DictionaryHelper.getContent(cddValue[0],
	// cddValue[1], getValueByOgnl(data, fieldName,""));
	//// keyValue.put(field[i], realValue);
	//// }else{
	//// keyValue.put(field[i], "");
	//// }
	//// }else
	// if(cpp[i].trim().length()>0){
	// keyValue.put(field[i], PropertiesHelper
	// .getPropertiesValue("i18n_zh_CN.properties", cpp[i] + "_" + field[i]));
	// }else{
	// keyValue.put(field[i], getValueByOgnl(data, field[i], ""));
	// }
	// }
	// gridData.getRows().add(keyValue);
	// }
	// return convertKeyToCapitalize(JSONObject.fromObject(gridData));
	// }
	//
	// public static JSONObject getJSONFromMapList(List list){
	// JsonDataGridHelper gridData = new JsonDataGridHelper();
	// gridData.setRows(list);
	// return convertKeyToCapitalize(JSONObject.fromObject(gridData));
	// }

	/**
	 * 根据数据源中所列的字段获取值,为NULL时 需要设置默认值(待完善)
	 * 
	 * @param data
	 * @return
	 * @throws OgnlException
	 */
	@SuppressWarnings("unused")
	private static String getValueByOgnl(Object target, String field, String defaultValue) {
		String resultValue = "";

		Object value = null;
		try {
			value = BeanUtils.getProperty(target, field);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		Object result = null;
		try {
			String newStr = "get" + field.substring(0, 1).toUpperCase() + field.replaceFirst("\\w", "");
			Method method = target.getClass().getMethod(newStr);
			result = method.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != value) {
			if (result != null && (result instanceof Date || result instanceof java.sql.Date)) {
				resultValue = DateHelper.date2String((Date) result, "yyyy-MM-dd HH:mm:ss");
			} else {
				resultValue = String.valueOf(value);
			}
		}
		return resultValue;
	}

}
