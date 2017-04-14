package com.dc.framework.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class SearchFilter {

	/**
	 * 
	 * @ClassName: OperatorType
	 * @Description: 操作符号
	 * @author: jinzhaopo
	 * @date: 2015年5月17日 下午7:59:41
	 */
	public enum Operator {
		// =，类似，》，《，>=,<=,!=,
		EQ("="), LIKE("like"), GT(">"), LT("<"), NE("<>"), GE(">="), LE("<="), IN("in"), ISNULL("is null"), ISNOTNULL("is not null"), ENUM("enum");

		private Operator(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public String fieldName;
	public Object value;
	public Operator operator;
	public List values;
	private Boolean ignoreCase = true;

	public SearchFilter() {
		this.ignoreCase = false;
	}

	public SearchFilter(final String fieldName, Operator operator, Object value, boolean ignoreCase) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
		this.ignoreCase = ignoreCase;
	}

	public SearchFilter(final String fieldName, final Operator operator, final Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	public static SearchFilter eq(String property, Object value) {
		return new SearchFilter(property, Operator.EQ, value);
	}

	public static SearchFilter eq(String property, Object value, boolean ignoreCase) {
		return new SearchFilter(property, Operator.EQ, value, ignoreCase);
	}

	public static SearchFilter ne(String property, Object value) {
		return new SearchFilter(property, Operator.NE, value);
	}

	public static SearchFilter ne(String property, Object value, boolean ignoreCase) {
		return new SearchFilter(property, Operator.NE, value, ignoreCase);
	}

	public static SearchFilter gt(String property, Object value) {
		return new SearchFilter(property, Operator.GT, value);
	}

	public static SearchFilter lt(String property, Object value) {
		return new SearchFilter(property, Operator.LT, value);
	}

	public static SearchFilter ge(String property, Object value) {
		return new SearchFilter(property, Operator.GE, value);
	}

	public static SearchFilter le(String property, Object value) {
		return new SearchFilter(property, Operator.LE, value);
	}

	public static SearchFilter like(String property, Object value) {
		return new SearchFilter(property, Operator.LIKE, value);
	}

	public static SearchFilter in(String property, List value) {
		return new SearchFilter(property, null, Operator.IN, value, false);
	}

	public static SearchFilter isNull(String property) {
		return new SearchFilter(property, Operator.ISNULL, null);
	}

	public static SearchFilter isNotNull(String property) {
		return new SearchFilter(property, Operator.ISNOTNULL, null);
	}

	public static List<SearchFilter> parse(final Map<String, Object> filterParams) {
		final List<SearchFilter> filters = new ArrayList<SearchFilter>();

		for (final Entry<String, Object> entry : filterParams.entrySet()) {
			int index = entry.getKey().indexOf("_");
			String operator = entry.getKey().substring(0, index);
			String fieldName = entry.getKey().substring(index + 1);
			// final String[] names = StringUtils.split(entry.getKey(), "_");
			// if (names.length != 2) {
			// throw new IllegalArgumentException(entry.getKey()
			// + " is not a valid search filter name");
			// }
			if (StringUtils.isBlank((String) entry.getValue())) {
				continue;
			}
			final SearchFilter filter = new SearchFilter(fieldName, Operator.valueOf(operator), entry.getValue());
			filters.add(filter);
		}
		return filters;
	}

	public SearchFilter(String fieldName, Object value, Operator operator, List values, Boolean ignoreCase) {
		super();
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
		this.values = values;
		this.ignoreCase = ignoreCase;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List getValues() {
		return values;
	}

	public void setValues(List values) {
		this.values = values;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

}
