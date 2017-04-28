package com.dc.framework.plugin;

/**
 * 
 * @ClassName: IPlugin
 * @Description: 插件接口：定义了一些方法，类似公共插件的方法
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午10:51:57
 */
public interface IPlugin {
	/**
	 * 
	 * @Title: getType
	 * @Description: 插件类型
	 * @return
	 * @return: String
	 */
	public String getType();

	/**
	 * 
	 * @Title: getId
	 * @Description: 插件id
	 * @return
	 * @return: String
	 */
	public String getId();

	/**
	 * 
	 * @Title: getName
	 * @Description: 插件名称
	 * @return
	 * @return: String
	 */
	public String getName();

	/**
	 * 
	 * @Title: getVersion
	 * @Description: 插件版本
	 * @return
	 * @return: String
	 */
	public String getVersion();

	/**
	 * 
	 * @Title: getAuthor
	 * @Description: 作者
	 * @return
	 * @return: String
	 */
	public String getAuthor();

	/**
	 * 
	 * @Title: perform
	 * @Description: 插件功能
	 * @param params
	 *            业务类传递给插件的参数
	 * @return: void
	 */
	public void perform(Object... params);

}
