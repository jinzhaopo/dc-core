package com.dc.framework.validations;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dc.common.WebErrors;
import com.dc.framework.plugin.AutoRegisterPlugin;
import com.dc.framework.validations.annotation.RequiredFieldValidator;
import com.dc.framework.validations.annotation.Validations;

/**
 * 
 * @ClassName: RequiredFieldValidate
 * @Description: 对象
 * @author: jinzhaopo
 * @date: 2015年5月10日 下午11:10:14
 */
public class RequiredFieldValidate extends AutoRegisterPlugin implements
		IValidate {

	public WebErrors validate(HttpServletRequest request, WebErrors errors,
			Validations validations, Map<String, String> decodedUriVariables) {
		RequiredFieldValidator[] rFiled = validations.requiredFields();
		for (RequiredFieldValidator rfv : rFiled) {
			String fieldName = rfv.fieldName();
			String value = request.getParameter(fieldName);
			if (value == null && decodedUriVariables != null) {
				value = decodedUriVariables.get(fieldName);
			}
			if (value == null) {
				// TODO 这边还要好好看一下 因为这边是直接取出来的 不是国际化里面的
				errors.addErrorString(rfv.message());
			}
		}
		return errors;
	}

	public String getType() {
		return "validate";
	}

	public String getId() {
		return "requiredFiledValidate";
	}
	public String getName() {
		return "对象非空验证";
	}

	public String getVersion() {
		return "1.0.0";
	}

	public String getAuthor() {
		return "jinzhaopo";
	}

	public void perform(Object... params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register() {
		// TODO Auto-generated method stub

	}

}
