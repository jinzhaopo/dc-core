package com.dc.framework.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: AutoRegisterPluginsBundle
 * @Description: 自动注册插件庄
 * @author: Administrator
 * @date: 2015年5月10日 下午11:31:42
 */
public abstract class AutoRegisterPluginsBundle implements IPluginBundle {

	protected List<IPlugin> plugins;// 插件

	abstract public String getName();

	/**
	 * 
	 * @Title: registerPlugin
	 * @Description: 注册插件
	 * @param plugin
	 * @see framework.plugin.IPluginBundle#registerPlugin(framework.plugin.IPlugin)
	 */
	public void registerPlugin(IPlugin plugin) {
		if (plugins == null) {
			plugins = new ArrayList<IPlugin>();
		}
		plugins.add(plugin);
	}

	/**
	 * 
	 * @Title: unRegisterPlugin
	 * @Description: 存储插件
	 * @param plugin
	 * @see framework.plugin.IPluginBundle#unRegisterPlugin(framework.plugin.IPlugin)
	 */
	public void unRegisterPlugin(IPlugin plugin) {
		plugins.remove(plugin);
	}

}
