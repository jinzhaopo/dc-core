package com.dc.framework.validations;

import java.util.List;

import com.dc.framework.plugin.AutoRegisterPluginsBundle;
import com.dc.framework.plugin.IPlugin;

/**
 * 
 * @ClassName: ValidationBundle
 * @Description: 交验插件庄
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午11:29:35
 */
public class ValidationBundle extends AutoRegisterPluginsBundle {

	public String getName() {
		return "校验插件桩";
	}

	public List<IPlugin> getPluginList() {
		return this.plugins;
	}

}
