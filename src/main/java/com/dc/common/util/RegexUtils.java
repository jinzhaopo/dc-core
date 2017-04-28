package com.dc.common.util;

/**
 * 
 * @ClassName: RegexUtils
 * @Description: 正则验证工具类
 * @author: zhaopo
 * @date: 2016年11月28日 上午10:31:36
 */
public class RegexUtils {
	/** 邮箱正则表达式 */
	private static final String REGEX_EXP_EMAIL = "^[\\w-]+[-.\\w]*@[-\\w]+\\.[-.\\w]*[\\w-]+$";
	/** 手机正则表达式 */
	private static final String REGEX_EXP_MOBILE = "^[1]\\d{10}$";
	/** 门店|云卡卡号正则表达式 */
	private static final String REGEX_EXP_CARD_NO = "^\\d{7}|\\d{8}$";

	/**
	 * 邮箱格式验证
	 * 
	 * @param email
	 * @return
	 */
	public static boolean regexEmail(String email) {
		return email.matches(REGEX_EXP_EMAIL);
	}

	/**
	 * 手机格式验证
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean regexMobile(String mobile) {
		return mobile.matches(REGEX_EXP_MOBILE);
	}

}
