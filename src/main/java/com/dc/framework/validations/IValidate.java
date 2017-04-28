package com.dc.framework.validations;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dc.common.WebErrors;
import com.dc.framework.validations.annotation.Validations;

/**
 * 
 * @ClassName: IValidate
 * @Description: 验证接口(所有验证器的公共方法：接口编程)
 * @author: Administrator
 * @date: 2015年5月10日 下午6:11:53
 */
public interface IValidate {

	/**
	 * 参数验证
	 * 
	 * @param request
	 * @param errors
	 * @param validations
	 * @return
	 */
	public WebErrors validate(HttpServletRequest request, WebErrors errors,
			Validations validations, Map<String, String> decodedUriVariables);

}
