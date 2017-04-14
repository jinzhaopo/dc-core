package com.dc.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @ClassName: DynamicDataSource
 * @Description: * 定义动态数据源，实现通过集成Spring提供的AbstractRoutingDataSource，
 *               只需要实现determineCurrentLookupKey方法即可<br/>
 *               由于DynamicDataSource是单例的，线程不安全的，所以采用ThreadLocal保证线程安全，
 *               由DynamicDataSourceHolder完成。
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月13日 下午1:51:44
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		// 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源key
		return DynamicDataSourceHolder.getDataSourceKey();
	}

}
