package com.dc.framework.plugin;

/**
 * 
 * @ClassName: IPluginBundle
 * @Description: 插件桩接口，需要插件桩化的业务类实现此接口，桩化后的业务类可以注册实现了IPlugin接口的插件
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午10:59:44
 */
public interface IPluginBundle {
	/**
	 * 
	 * @Title: registerPlugin
	 * @Description: 注册插件
	 * @param plugin
	 * @return: void
	 */
	public void registerPlugin(IPlugin plugin);

	/**
	 * 
	 * @Title: unRegisterPlugin
	 * @Description: 删除插件
	 * @param plugin
	 * @return: void
	 */
	public void unRegisterPlugin(IPlugin plugin);

}
