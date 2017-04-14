package com.dc.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

import com.dc.common.util.JsonUtil;
import com.dc.common.util.ResponseUtils;
import com.dc.framework.spring.SpringUtils;

public class WebErrors {
	/**
	 * 默认错误页面
	 */
	public static final String ERROR_PAGE = "/admin/common/error";
	/**
	 * 默认错误信息属性名称
	 */
	public static final String ERROR_ATTR_NAME = "errors";

	/**
	 * email正则表达式
	 */
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$");
	/**
	 * username正则表达式
	 */
	public static final Pattern USERNAME_PATTERN = Pattern.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$");

	/**
	 * 通过HttpServletRequest创建WebErrors
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 * @return 如果LocaleResolver存在则返回国际化WebErrors
	 */
	public static WebErrors create() {
		return new WebErrors();
	}

	public WebErrors() {
	}

	protected String getErrorAttrName() {
		return ERROR_ATTR_NAME;
	}

	protected String getErrorPage() {
		return ERROR_PAGE;
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @param args
	 *            错误参数
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code, Object... args) {
		getErrors().add(SpringUtils.getMessage(code, args));
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code) {
		getErrors().add(SpringUtils.getMessage(code, null));
	}

	/**
	 * 添加错误字符串
	 * 
	 * @param error
	 */
	public void addErrorString(String error) {
		getErrors().add(error);
	}

	/**
	 * 添加错误，根据MessageSource是否存在，自动判断为code还是string。
	 * 
	 * @param error
	 */
	public void addError(String error) {
		// if messageSource exist
		if (messageSource != null) {
			error = messageSource.getMessage(error, null, error, locale);
		}
		getErrors().add(error);
	}

	/**
	 * 是否存在错误
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	/**
	 * 错误数量
	 * 
	 * @return
	 */
	public int getCount() {
		return errors == null ? 0 : errors.size();
	}

	/**
	 * 错误列表
	 * 
	 * @return
	 */
	public List<String> getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		return errors;
	}

	/**
	 * 将错误信息保存至ModelMap，并返回错误页面。
	 * 
	 * @param model
	 * @return 错误页面地址
	 * @see org.springframework.ui.ModelMap
	 */
	public String showErrorPage(ModelMap model) {
		toModel(model);
		return getErrorPage();
	}

	public String showErrorPage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("errors", this);
		return getErrorPage();
	}

	/**
	 * 将错误信息保存至ModelMap，并返回错误页面。
	 * 
	 * @param response
	 * @return 错误消息
	 */
	public String showErrorAjax(HttpServletResponse response) {
		String json = JsonUtil.getInstance().obj2json(getErrors());
		response.setStatus(400);
		response.addHeader("Error-Json", json);
		return ResponseUtils.renderJSON("{error:" + json + "}", response);
	}

	public String showFrontErrorAjax(HttpServletResponse response) {
		String json = JsonUtil.getInstance().obj2json(getErrors());
		response.setStatus(400);
		response.addHeader("errorStatus", "systemError");
		return ResponseUtils.renderJSON("{error:" + json + "}", response);
	}

	/**
	 * 将错误信息保存至ModelMap
	 * 
	 * @param model
	 */
	public void toModel(Map<String, Object> model) {
		Assert.notNull(model);
		if (!hasErrors()) {
			throw new IllegalStateException("no errors found!");
		}
		model.put(getErrorAttrName(), getErrors());
	}

	public boolean ifNull(Object o, String field) {
		if (o == null) {
			addErrorCode("error.required", field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifEmpty(Object[] o, String field) {
		if (o == null || o.length <= 0) {
			addErrorCode("error.required", field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifBlank(String s, String field, int maxLength) {
		if (StringUtils.isBlank(s)) {
			addErrorCode("error.required", field);
			return true;
		}
		if (ifMaxLength(s, field, maxLength)) {
			return true;
		}
		return false;
	}

	public boolean ifMaxLength(String s, String field, int maxLength) {
		if (s != null && s.length() > maxLength) {
			addErrorCode("error.maxLength", field, maxLength);
			return true;
		}
		return false;
	}

	public boolean ifOutOfLength(String s, String field, int minLength, int maxLength) {
		if (s == null) {
			addErrorCode("error.required", field);
			return true;
		}
		int len = s.length();
		if (len < minLength || len > maxLength) {
			addErrorCode("error.outOfLength", field, minLength, maxLength);
			return true;
		}
		return false;
	}

	public boolean ifNotEmail(String email, String field, int maxLength) {
		if (ifBlank(email, field, maxLength)) {
			return true;
		}
		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			addErrorCode("error.email", field);
			return true;
		}
		return false;
	}

	public boolean ifNotUsername(String username, String field, int minLength, int maxLength) {
		if (ifOutOfLength(username, field, minLength, maxLength)) {
			return true;
		}
		Matcher m = USERNAME_PATTERN.matcher(username);
		if (!m.matches()) {
			addErrorCode("error.username", field);
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public boolean validate(Object obj, final Class<?>... groups) {
		Validator validator = (Validator) SpringUtils.getBean("validator");
		Set<ConstraintViolation<Object>> set = validator.validate(obj, groups);
		if (set.isEmpty()) {
			for (ConstraintViolation constraint : set) {
				addError(constraint.getMessage());
			}
			return true;
		} else {
			return false;
		}
	}

	public void clean() {
		if (errors != null) {
			errors.clear();
		}
	}

	public boolean ifNotExist(Object o, Class<?> clazz, Serializable id) {
		if (o == null) {
			addErrorCode("error.notExist", clazz.getSimpleName(), id);
			return true;
		} else {
			return false;
		}
	}

	public void noPermission(Class<?> clazz, Serializable id) {
		addErrorCode("error.noPermission", clazz.getSimpleName(), id);
	}

	private MessageSource messageSource;
	private Locale locale;
	private List<String> errors;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 获得本地化信息
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 设置本地化信息
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
