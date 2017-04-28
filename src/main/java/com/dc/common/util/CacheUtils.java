package com.dc.common.util;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class CacheUtils {

	private static final CacheManager cacheManager = CacheManager.getInstance();
	
	/**
	 * 获取缓存对象
	 * @param cacheName
	 * @return
	 */
	public static Ehcache getEhcache(String cacheName){
		return cacheManager.getEhcache(cacheName);
	}
	
	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object getValue(String cacheName, Object key){
		Ehcache ehcache = getEhcache(cacheName);
		Element ele = ehcache.get(key);
		if(ele != null){
			return ele.getObjectValue();
		}
		return null;
	}
	
	/**
	 * 设置缓存
	 */
	public static void setElement(String cacheName, Object key, Object value){
		Ehcache ehcache = getEhcache(cacheName);
		ehcache.put(new Element(key, value));
	}
	
	/**
	 * 清除缓存
	 * @param cacheName
	 * @param key
	 */
	public static void cleanEhCache(String cacheName, Object key){
		Ehcache ehcache = getEhcache(cacheName);
		if(key == null){
			ehcache.removeAll();
		}else{
			ehcache.remove(key);
		}
	}
}
