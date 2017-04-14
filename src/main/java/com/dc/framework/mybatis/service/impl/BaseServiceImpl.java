package com.dc.framework.mybatis.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.dc.framework.mybatis.mapper.MyMapper;
import com.dc.framework.mybatis.model.BaseEntity;
import com.dc.framework.mybatis.model.PriorityEntity;
import com.dc.framework.mybatis.service.BaseService;
import com.dc.framework.page.Pager;
import com.dc.framework.page.SearchFilter;
import com.dc.framework.page.SearchFilter.Operator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 基础服务接口实现类
 * 
 * @author jinzhaopo
 * @date 2016年6月30日上午10:06:54
 * @version 3.0.0
 * @param <T>
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	@Autowired
	private MyMapper<T> mapper;

	public MyMapper<T> getMapper() {
		return mapper;
	}

	public void setMapper(MyMapper<T> mapper) {
		this.mapper = mapper;
	}

	/**
	 * 获取下一条记录ID
	 * 
	 * @return
	 */
	public Long getNextId() {
		return mapper.selectNextId();
	}

	/*
	 * 根据id查询数据(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#queryById(java.lang.Long)
	 */
	public T queryById(Long id) {
		return this.mapper.selectByPrimaryKey(id);
	}

	/**
	 * 
	 * @Title: queryByIds
	 * @Description: 工具主键ids查询
	 * @param ids
	 * @return
	 * @see framework.service.BaseService#queryByIds(java.util.List)
	 */
	public List<T> queryByIds(List<Long> ids) {
		if (ids == null || ids.size() <= 0) {
			return null;
		}
		SearchFilter sf = SearchFilter.in("id", ids);
		List<SearchFilter> sfList = new ArrayList<SearchFilter>();
		sfList.add(sf);

		return getList(sfList);
	}

	/*
	 * 查询所有数据(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#queryAll()
	 */
	public List<T> queryAll() {
		return this.mapper.select(null);
	}

	/*
	 * 根据条件查询一条数据，如果有多条数据会抛出异常(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#queryOne(com.yundao.common.pojo.
	 * BaseEntity)
	 */
	public T queryOne(T record) {
		return this.mapper.selectOne(record);
	}

	/*
	 * 根据条件查询数据列表(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#queryListByWhere(com.yundao.common.
	 * pojo.BaseEntity)
	 */
	public List<T> queryListByWhere(T record) {
		return this.mapper.select(record);
	}

	/*
	 * 分页查询(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#queryPageListByWhere(java.lang.
	 * Integer, java.lang.Integer, com.yundao.common.pojo.BaseEntity)
	 */
	public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record) {
		// 设置分页条件
		PageHelper.startPage(page, rows);
		List<T> list = this.queryListByWhere(record);
		return new PageInfo<T>(list);
	}

	/*
	 * 新增数据，返回成功的条数(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#save(com.yundao.common.pojo.
	 * BaseEntity)
	 */
	public Integer save(T record) {
		// record.setId(this.mapper.selectNextId());
		record.setCreateDate(new Date());
		record.setModifyDate(record.getCreateDate());
		return this.mapper.insert(record);
	}

	/*
	 * 新增数据，使用不为null的字段，返回成功的条数(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#saveSelective(com.yundao.common.
	 * pojo.BaseEntity)
	 */
	public Integer saveSelective(T record) {
		// record.setId(this.mapper.selectNextId());
		record.setCreateDate(new Date());
		record.setModifyDate(record.getCreateDate());
		return this.mapper.insertSelective(record);
	}

	/*
	 * 修改数据，返回成功的条数(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#update(com.yundao.common.pojo.
	 * BaseEntity)
	 */
	public Integer update(T record) {
		record.setModifyDate(new Date());
		return this.mapper.updateByPrimaryKey(record);
	}

	/*
	 * 修改数据，使用不为null的字段，返回成功的条数(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#updateSelective(com.yundao.common.
	 * pojo.BaseEntity)
	 */
	public Integer updateSelective(T record) {
		record.setModifyDate(new Date());
		return this.mapper.updateByPrimaryKeySelective(record);
	}

	/*
	 * 工具id删除(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#deleteById(java.lang.Long)
	 */
	public Integer deleteById(Long id) {
		return this.mapper.deleteByPrimaryKey(id);
	}

	/*
	 * 批量删除(non-Javadoc)
	 * 
	 * @see com.yundao.common.service.BaseService#deleteByIds(java.lang.Class,
	 * java.lang.String, java.util.List)
	 */
	public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, values);
		return this.mapper.deleteByExample(example);
	}

	/*
	 * 根据条件做删除(non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#deleteByWhere(com.yundao.common.
	 * pojo.BaseEntity)
	 */
	public Integer deleteByWhere(T record) {
		return this.mapper.delete(record);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yundao.common.service.BaseService#selectByExample(java.lang.Object)
	 */
	public List<T> selectByExample(Object example) {
		return mapper.selectByExample(example);
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 查询(不考虑忽略大小写)
	 * @param serachFilter
	 * @return
	 * @see framework.service.BaseService#getList(framework.page.SearchFilter)
	 */
	public List<T> getList(List<SearchFilter> searchFilters) {
		Class _class = getTClass();
		Example example = new Example(_class);
		Criteria createCriteria = example.createCriteria();
		for (SearchFilter searchFilter : searchFilters) {
			String property = searchFilter.getFieldName();
			Object value = searchFilter.getValue();
			Operator operator = searchFilter.getOperator();
			Boolean ignoreCase = searchFilter.getIgnoreCase();
			List values = searchFilter.getValues();
			switch (operator.ordinal()) {
			case 0:// EQ
				createCriteria.andEqualTo(property, value);
				break;
			case 1:// LIKE
				createCriteria.andLike(property, (String) value);
				break;
			case 2:// GT>
				createCriteria.andGreaterThan(property, value);
				break;
			case 3:// LT("<")
				createCriteria.andLessThan(property, value);
				break;
			case 4:// NE("<>")
				createCriteria.andNotEqualTo(property, value);
				break;
			case 5:// GE(">=")
				createCriteria.andGreaterThanOrEqualTo(property, value);
				break;
			case 6:// LE("<=")
				createCriteria.andLessThanOrEqualTo(property, value);
				break;
			case 7:// IN("in")
				createCriteria.andIn(property, values);
				break;
			case 8:// ISNULL("is null")
				createCriteria.andIsNull(property);
				break;

			case 9:// ISNOTNULL("is not null")
				createCriteria.andIsNotNull(property);
				break;
			default:
				break;
			}
		}
		try {
			if (_class.newInstance() instanceof PriorityEntity) {
				example.orderBy("priority").asc();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		example.orderBy("createDate").desc();
		return mapper.selectByExample(example);
	}

	/**
	 * 
	 * @Title: find
	 * @Description: 分页查询
	 * @param pager
	 * @param searchFilters
	 * @return
	 * @see framework.service.BaseService#find(framework.page.Pager,
	 *      java.util.List)
	 */
	public Pager find(Pager pager, List<SearchFilter> searchFilters) {
		PageHelper.startPage(pager.getPageNumber(), pager.getPageSize());
		List<T> list = this.getList(searchFilters);
		PageInfo pi = new PageInfo<T>(list);

		pager.setList(pi.getList());
		pager.setTotalCount(Long.valueOf(pi.getTotal()).intValue());
		pager.setPageCount(pi.getPages());

		return pager;
	}

	/**
	 * 
	 * @Title: findByXml
	 * @Description: 分页查询（跨表查询）
	 * @param pager
	 * @param filterParams
	 * @return
	 * @see framework.service.BaseService#findByXml(framework.page.Pager,
	 *      java.util.Map)
	 */
	public Pager findByXml(Pager pager, Map<String, Object> filterParams) {
		PageHelper.startPage(pager.getPageNumber(), pager.getPageSize());
		List list = mapper.getListByXml(filterParams);
		PageInfo pi = new PageInfo<T>(list);

		pager.setList(pi.getList());
		pager.setTotalCount(Long.valueOf(pi.getTotal()).intValue());
		pager.setPageCount(pi.getPages());

		return pager;
	}

	/**
	 * 
	 * @Title: get
	 * @Description: 查询
	 * @param property
	 * @param value
	 * @return
	 * @see framework.service.BaseService#get(java.lang.String,
	 *      java.lang.String)
	 */
	public T get(String property, Object value) {
		List<T> list = getList(property, value);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 查询
	 * @param property
	 * @param value
	 * @return
	 * @see framework.service.BaseService#getList(java.lang.String,
	 *      java.lang.String)
	 */
	public List<T> getList(String property, Object value) {
		if (value == null)
			return null;
		SearchFilter sf = new SearchFilter();
		List<SearchFilter> sfList = new ArrayList<SearchFilter>();
		sf.setFieldName(property);
		sf.setValue(value);
		if (value instanceof Collection) {
			sf.setOperator(Operator.IN);
		} else {
			sf.setOperator(Operator.EQ);
		}
		sfList.add(sf);

		return getList(sfList);
	}

	/**
	 * 
	 * @Title: isUnique
	 * @Description: 判断一个值是不是唯一
	 * @param property
	 * @param oldValue
	 * @param newValue
	 * @return
	 * @see framework.service.BaseService#isUnique(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean isUnique(String property, String oldValue, String newValue) {
		Assert.hasText(property, "propertyName must not be empty");
		Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		T t = get(property, newValue);
		return (t == null);
	}

	public Class getTClass() {

		// 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		Type genType = getClass().getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		// 返回表示此类型实际类型参数的 Type 对象的数组。
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		return (Class) params[0];
	}
}
