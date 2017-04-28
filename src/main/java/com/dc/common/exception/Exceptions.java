package com.dc.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @ClassName: Exceptions
 * @Description: 关于异常的工具类.
 * @author: zhaopo
 * @date: 2016年11月18日 上午10:28:40
 */
public class Exceptions {

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(final Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(final Exception e) {
		final StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(final Exception ex, final Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (final Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
}
