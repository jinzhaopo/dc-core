package com.dc.common.util;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @ClassName: AppContext
 * @Description: 提供Servlet的公共方法，类似ServletContext
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午10:05:49
 */
@Component
public class AppContext implements ServletContextAware {

	private static ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 
	 * @Title: getAppRealPath
	 * @Description: 获取url
	 * @param path
	 * @return
	 * @return: String
	 */
	public static String getAppRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	/**
	 * 
	 * @Title: getContextPath
	 * @Description: 获取上下文路径
	 * @return
	 * @return: String
	 */
	public static String getContextPath() {
		return servletContext.getContextPath();
		// return servletContext.getContextPath("/");
	}

	/**
	 * 
	 * @Title: getRequest
	 * @Description: 获取request
	 * @return
	 * @return: HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	// /**
	// *
	// * @Title: getRemoteIp
	// * @Description: 获取ip
	// * @return
	// * @return: String
	// */
	// public static String getRemoteIp() {
	// HttpServletRequest request = getRequest();
	// String ip = request.getHeader("x-forwarded-for");
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getHeader("Proxy-Client-IP");
	// }
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getHeader("WL-Proxy-Client-IP");
	// }
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getRemoteAddr();
	// }
	// return ip;
	// }

	/**
	 * 
	 * @Title: getSessionAttr
	 * @Description: 获取session中的值
	 * @param key
	 * @return
	 * @return: Object
	 */
	public static Object getSessionAttr(String key) {
		HttpSession session = getRequest().getSession(false);
		if (session == null) {
			return null;
		} else {
			return session.getAttribute(key);
		}
	}

	/**
	 * 
	 * @Title: getSessionId
	 * @Description: 获取sessionid判断是否session存在
	 * @param isCreate
	 * @return
	 * @return: String
	 */
	public static String getSessionId(boolean isCreate) {
		HttpSession session = getRequest().getSession(isCreate);
		if (session == null) {
			return null;
		} else {
			return session.getId();
		}
	}

	/**
	 * 
	 * @Title: setSessionAttr
	 * @Description: 设置session中的key-value
	 * @param key
	 * @param value
	 * @return: void
	 */
	public static void setSessionAttr(String key, Object value) {
		HttpSession session = getRequest().getSession(false);
		if (session != null)
			session.setAttribute(key, value);
	}

	/**
	 * 
	 * @Title: removeAttribute
	 * @Description: 删除session中的obj
	 * @param key
	 * @return: void
	 */
	public static void removeAttribute(String key) {
		HttpSession session = getRequest().getSession(false);
		if (session != null)
			session.removeAttribute(key);
	}

	/**
	 * 
	 * @Title: getCookie
	 * @Description: 获取cookie对象
	 * @param name
	 * @return
	 * @return: Cookie
	 */
	public static Cookie getCookie(String name) {
		Cookie[] cookies = getRequest().getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

}
