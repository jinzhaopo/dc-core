package com.dc.framework.plugin;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 
 * @ClassName: PluginLoader
 * @Description: 自动导入插件
 * @author: jinzhaopo
 * @date: 2015年5月11日 上午1:36:49
 */
public class PluginLoader implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof AutoRegisterPlugin) {
			AutoRegisterPlugin plugin = (AutoRegisterPlugin) bean;
			if (plugin.getBundleList() == null) {
			} else {

				plugin.register();
				List<IPluginBundle> pluginBundelList = plugin.getBundleList();
				for (IPluginBundle bundle : pluginBundelList) {
					bundle.registerPlugin(plugin);
				}
				PluginContext.registerPlugin(plugin);
			}
		}

		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
