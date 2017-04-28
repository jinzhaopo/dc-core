package com.dc.framework.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: PluginContext
 * @Description: 插件上下文
 * @author: Administrator
 * @date: 2015年5月11日 上午1:39:15
 */
public class PluginContext {

	private static List<IPlugin> pluginContext;
	static {
		pluginContext = new ArrayList<IPlugin>();
	}

	public static void registerPlugin(IPlugin plugin) {
		pluginContext.add(plugin);
	}

	public static List<IPlugin> getPlugins() {
		return pluginContext;
	}

}
