package com.dc.common.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.common.WebErrors;

import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: ResponseUtils
 * @Description: HttpServletResponse
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午4:00:39
 */
public final class ResponseUtils {
	public static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String MESSAGE = "message";
	public static final String CONTENT = "content";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";

	/**
	 * 绕过Template,直接输出内容的简便函数.
	 */
	public static String render(String text, String contentType, HttpServletResponse response) {
		try {
			response.setContentType(contentType);
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(text);
			response.getWriter().flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 直接输出字符串.
	 */
	public static String renderText(String text, HttpServletResponse response) {
		return render(text, "text/plain;charset=UTF-8", response);
	}

	/**
	 * 直接输出字符串GBK编码.
	 */
	public static String renderHtmlGBK(String html, HttpServletResponse response) {
		return render(html, "text/html;charset=GBK", response);
	}

	/**
	 * 直接输出字符串GBK编码.
	 */
	public static String renderHtmlUTF8(String html, HttpServletResponse response) {
		return render(html, "text/html;charset=utf-8", response);
	}

	/**
	 * 直接输出XML.
	 */
	public static String renderXML(String xml, HttpServletResponse response) {
		return render(xml, "text/xml;charset=UTF-8", response);
	}

	/**
	 * 直接输出JSON.
	 */
	public static String renderJSON(String jsonString, HttpServletResponse response) {
		return render(jsonString, "text/json;charset=UTF-8", response);
	}

	/**
	 * 直接输出JSON.
	 */
	public static String renderJSON(Object obj, HttpServletResponse response) {
		return render(JsonUtil.getInstance().obj2json(obj), "text/json;charset=UTF-8", response);
	}

	// 根据Map输出JSON，返回null
	public static String ajaxJson(Object obj, HttpServletResponse response) {
		return render(JSONObject.fromObject(obj).toString(), "text/html;charset=UTF-8", response);
	}
	// 根据Map输出JSON，返回null
	// public static String ajaxJson(JSONObject jsonObject, HttpServletResponse
	// response) {
	// return render(jsonObject.toString(), "text/html;charset=UTF-8",
	// response);
	// }

	// 根据jsonArray输出JSON，返回null
	// public static String ajaxJson(JSONArray jsonArray, HttpServletResponse
	// response) {
	// return render(jsonArray.toString(), "text/html;charset=UTF-8", response);
	// }

	// 输出JSON警告消息，返回null
	// public static String ajaxJsonWarnMessage(String message,
	// HttpServletResponse response) {
	// Map<String, String> jsonMap = new HashMap<String, String>();
	// jsonMap.put(STATUS, WARN);
	// jsonMap.put(MESSAGE, message);
	// JSONObject jsonObject = JSONObject.fromObject(jsonMap);
	// return render(jsonObject.toString(), "text/html;charset=UTF-8",
	// response);
	// }

	// 输出JSON成功消息，返回null
	// public static String ajaxJsonSuccessMessage(String message,
	// HttpServletResponse response) {
	// Map<String, String> jsonMap = new HashMap<String, String>();
	// jsonMap.put(STATUS, SUCCESS);
	// jsonMap.put(MESSAGE, message);
	// JSONObject jsonObject = JSONObject.fromObject(jsonMap);
	// return render(jsonObject.toString(), "text/html;charset=UTF-8",
	// response);
	// }

	// 输出后台 JSON错误消息，返回null
	/**
	 * 
	 * @Title: ajaxJsonErrorMessage
	 * @Description: 输出后台 JSON错误消息，返回null
	 * @param message
	 * @param response
	 * @return
	 * @return: String
	 */
	public static String ajaxJsonErrorMessage(String message, HttpServletResponse response) {
		WebErrors errors = WebErrors.create();
		errors.addErrorString(message);
		return errors.showErrorAjax(response);
	}

	/**
	 * 
	 * @Title: ajaxJsonFrontErrorMessage
	 * @Description: 输出前台JSON错误消息，返回null
	 * @param message
	 * @param response
	 * @return
	 * @return: String
	 */
	public static String ajaxJsonFrontErrorMessage(String message, HttpServletResponse response) {
		WebErrors errors = WebErrors.create();
		errors.addErrorString(message);
		return errors.showFrontErrorAjax(response);
	}
}
