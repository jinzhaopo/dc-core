package com.dc.framework.page;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
/**
 * 
 * @ClassName: Order
 * @Description: TODO
 * @author: zhaopo
 * @date: 2016年11月3日 下午4:05:31
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 0xd5478786394c55d0L;
	
	public enum Direction {
		asc, desc;
	}
	
	private String fieldName;
	private Direction direction;

	public Order() {
		direction = Direction.asc;
	}

	public Order(String fieldName, Direction direction) {
		this.fieldName = fieldName;
		this.direction = direction;
	}

	public static Order asc(String fieldName) {
		return new Order(fieldName, Direction.asc);
	}

	public static Order desc(String fieldName) {
		return new Order(fieldName, Direction.desc);
	}


	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (this == obj) {
			return true;
		} else {
			Order order = (Order)obj;
			return (new EqualsBuilder()).append(getFieldName(), order.getFieldName()).append(getDirection(), order.getDirection()).isEquals();
		}
	}

	public int hashCode() {
		return (new HashCodeBuilder(17, 37)).append(getFieldName()).append(getDirection()).toHashCode();
	}
}
