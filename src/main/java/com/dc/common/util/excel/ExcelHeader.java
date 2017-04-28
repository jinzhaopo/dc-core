package com.dc.common.util.excel;

/**
 * 用来存储Excel标题的对象，通过该对象可以获取标题和方法的对应关系
 * @author Administrator
 *
 */
public class ExcelHeader implements Comparable<ExcelHeader>{
	/**
	 * excel的标题名称
	 */
	private String title;
	/**
	 * 每一个标题的顺序
	 */
	private int order;
	/**
	 * 说对应方法名称
	 */
	private String methodName;
	/**
	 * 参数类型
	 */
	@SuppressWarnings("rawtypes")
	private Class returnType;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int compareTo(ExcelHeader o) {
		return order > o.order ? 1 : (order < o.order ? -1 : 0);
	}

	@SuppressWarnings("rawtypes")
	public Class getReturnType() {
		return returnType;
	}

	@SuppressWarnings("rawtypes")
	public void setReturnType(Class returnType) {
		this.returnType = returnType;
	}

	@SuppressWarnings("rawtypes")
	public ExcelHeader(String title, int order, String methodName, Class returnType) {
		super();
		this.title = title;
		this.order = order;
		this.methodName = methodName;
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return "ExcelHeader [title=" + title + ", order=" + order
				+ ", methodName=" + methodName + "]";
	}
	
	
}
