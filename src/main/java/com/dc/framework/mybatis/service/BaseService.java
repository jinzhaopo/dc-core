package com.dc.framework.mybatis.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dc.framework.mybatis.model.BaseEntity;
import com.dc.framework.page.Pager;
import com.dc.framework.page.SearchFilter;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @ClassName: BaseService
 * @Description: 基础服务接口
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午2:23:12
 * @param <T>
 */
@Service
public interface BaseService<T extends BaseEntity> {
	/**
	 * 根据id查询数据
	 * 
	 * @param id
	 *            ID
	 * @return 返回实体对象
	 */
	public T queryById(Long id);

	/**
	 * 
	 * @Title: queryByIds
	 * @Description: 根据ids查询数据
	 * @param ids
	 * @return
	 * @return: List<T>
	 */
	public List<T> queryByIds(List<Long> ids);

	/**
	 * 查询所有数据
	 * 
	 * @return 实体对象集合
	 */
	public List<T> queryAll();

	/**
	 * 根据条件查询一条数据，如果有多条数据会抛出异常
	 * 
	 * @param record
	 *            记录
	 * @return 实体对象
	 */
	public T queryOne(T record);

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param record
	 *            实体对象
	 * @return 实体对象集合
	 */
	public List<T> queryListByWhere(T record);

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            条数
	 * @param record
	 *            查询条件
	 * @return 分页信息
	 */
	public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record);

	/**
	 * 新增数据，返回成功的条数
	 * 
	 * @param record
	 *            记录
	 * @return 成功条数
	 */
	public Integer save(T record);

	/**
	 * 新增数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 *            记录
	 * @return 成功条数
	 */
	public Integer saveSelective(T record);

	/**
	 * 修改数据，返回成功的条数
	 * 
	 * @param record
	 *            记录
	 * @return 成功的条数
	 */
	public Integer update(T record);

	/**
	 * 修改数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 *            记录
	 * @return 成功条数
	 */
	public Integer updateSelective(T record);

	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 *            ID
	 * @return 成功的条数
	 */
	public Integer deleteById(Long id);

	/**
	 * 批量删除
	 * 
	 * @param clazz
	 *            类
	 * @param property
	 *            属性
	 * @param values
	 *            属性的 值
	 * @return 成功的条数
	 */
	public Integer deleteByIds(Class<T> clazz, String property, List<Object> values);

	/**
	 * 根据条件做删除
	 * 
	 * @param record
	 *            记录
	 * @return 成功的条数
	 */
	public Integer deleteByWhere(T record);

	/**
	 * 根据条件查询
	 * 
	 * @param example
	 *            查询条件
	 * @return 对象集合
	 */
	public List<T> selectByExample(Object example);

	/**
	 * 
	 * @Title: get
	 * @Description: TODO
	 * @param property
	 * @param value
	 * @return
	 * @return: T
	 */
	public T get(String property, Object value);

	/**
	 * 
	 * @Title: getList
	 * @Description: 查询
	 * @param property
	 * @param value
	 * @return
	 * @return: List<T>
	 */
	public List<T> getList(String property, Object value);

	/**
	 * 
	 * @Title: getList
	 * @Description: 获取列表
	 * @param serachFilter
	 * @return
	 * @return: List<T>
	 */
	public List<T> getList(List<SearchFilter> searchFilters);

	/**
	 * 
	 * @Title: find
	 * @Description: 分页查询
	 * @param page
	 * @param rows
	 * @param searchFilter
	 * @return
	 * @return: PageInfo<T>
	 */
	public Pager find(Pager pager, List<SearchFilter> searchFilters);

	/**
	 * 
	 * @Title: findByXml
	 * @Description: 分页查询(默认调用getListByXml)
	 * @param pager
	 * @param filterParams
	 * @return
	 * @return: Pager
	 */
	public Pager findByXml(Pager pager, Map<String, Object> filterParams);

	/**
	 * 
	 * @Title: isUnique
	 * @Description: 是否是唯一
	 * @param property
	 * @param oldValue
	 * @param newValue
	 * @return
	 * @return: boolean
	 */
	public boolean isUnique(String property, String oldValue, String newValue);

}
