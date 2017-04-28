package com.dc.framework.plugin;

import java.util.List;

/**
 * 
 * @ClassName: AutoRegisterPlugin
 * @Description: 自动注册插件:其实只是为了弥补Iplugin不能定义属性 还有为了将注册功能独立出来
 * @author: Administrator
 * @date: 2015年5月10日 下午10:57:02
 */
public abstract class AutoRegisterPlugin implements IPlugin {
	protected List<IPluginBundle> bundleList;// 插件庄

	public List<IPluginBundle> getBundleList() {
		return bundleList;
	}

	public void setBundleList(List<IPluginBundle> bundleList) {
		this.bundleList = bundleList;
	}

	/**
	 * 
	 * @Title: register
	 * @Description: 注册插件
	 * @return: void
	 */
	public abstract void register();

}
