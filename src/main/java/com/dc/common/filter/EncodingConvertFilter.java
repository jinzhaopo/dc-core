package com.dc.common.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * @ClassName: EncodingConvertFilter
 * @Description: 请求编码过滤器
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午2:12:33
 */
@Component
public class EncodingConvertFilter extends OncePerRequestFilter {
	/**
	 * 页面过来的编码
	 */
	private String fromEncoding;
	/**
	 * 要转变的编码
	 */
	private String toEncoding;

	/*
	 * 重写方法 (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		if (request.getMethod().equalsIgnoreCase("GET")) {
			for (Iterator<String[]> iterator = request.getParameterMap().values().iterator(); iterator.hasNext();) {
				String as[] = (String[]) iterator.next();
				for (int i = 0; i < as.length; i++) {
					try {
						as[i] = new String(as[i].trim().getBytes(fromEncoding), toEncoding);
					} catch (UnsupportedEncodingException unsupportedencodingexception) {
						unsupportedencodingexception.printStackTrace();
					}
				}
			}
		}
		try {
			filterChain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取页面请求的编码
	 * 
	 * @return 页面编码
	 */
	public String getFromEncoding() {
		return fromEncoding;
	}

	/**
	 * 设置页面请求编码
	 * 
	 * @param fromEncoding
	 *            页面编码
	 */
	public void setFromEncoding(String fromEncoding) {
		this.fromEncoding = fromEncoding;
	}

	/**
	 * 获取要转变的编码
	 * 
	 * @return 要转变的编码
	 */
	public String getToEncoding() {
		return toEncoding;
	}

	/**
	 * 设置要转变的编码
	 * 
	 * @param toEncoding
	 *            要转变的编码
	 */
	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}
}
