package com.dc.framework.page;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 
 * @ClassName: Pager
 * @Description: 分页
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午2:23:41
 */
@XmlRootElement(name = "Pager")
public class Pager {

	// 排序方式
	public enum OrderType {
		asc, desc
	}

	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	private Integer pageNumber = 1;// 当前页码
	private Integer pageSize = 20;// 每页记录数
	private Integer totalCount = 0;// 总记录数
	private Integer pageCount = 0;// 总页数
	private String property = "";// 查找属性名称
	private String keyword = "";// 查找关键字
	private String orderBy = "createDate";// 排序字段
	private OrderType orderType = OrderType.desc;// 排序方式
	private List<SearchFilter> filterList = new ArrayList<SearchFilter>();
	private List<Order> orderList;
	private List list = new ArrayList();// 数据List

	public Pager(List list) {
		super();
		this.list = list;
	}

	public Pager() {
		super();
	}

	public void addOrder(Order order) {
		if (orderList == null) {
			orderList = new ArrayList<Order>();
		}
		orderList.add(order);
	}

	public void addSearchFilter(SearchFilter searchFilter) {
		if (filterList == null) {
			filterList = new ArrayList<SearchFilter>();
		}
		filterList.add(searchFilter);
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return (pageNumber - 1) * pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@XmlTransient
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	@XmlTransient
	public List<SearchFilter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<SearchFilter> filterList) {
		this.filterList = filterList;
	}

	@XmlTransient
	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

}