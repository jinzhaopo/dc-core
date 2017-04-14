package com.dc.framework.mybatis.model;

import javax.persistence.OrderBy;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * 
 * @ClassName: PriorityEntity
 * @Description: 排序基类
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午2:19:01
 */
@SuppressWarnings("rawtypes")
public abstract class PriorityEntity extends BaseEntity implements Comparable {
	private static final long serialVersionUID = -5236614399469983267L;
	/**
	 * 排序
	 */
	@OrderBy(value = "asc")
	private Long priority;

	/**
	 * 获取排序字段
	 * 
	 * @return 排序
	 */
	public Long getPriority() {
		return priority;
	}

	/**
	 * 设置排序字段
	 * 
	 * @param order
	 *            排序
	 */
	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/*
	 * 重写方法 (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		return compareTo((PriorityEntity) obj);
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param priorityEntity
	 *            排序对象
	 * @return 排序结果
	 */
	public int compareTo(PriorityEntity priorityEntity) {
		return new CompareToBuilder().append(getPriority(), priorityEntity.getPriority()).append(getId(), priorityEntity.getId()).toComparison();
	}
}
