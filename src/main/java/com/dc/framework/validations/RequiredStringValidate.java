package com.dc.framework.validations;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dc.common.WebErrors;
import com.dc.framework.plugin.AutoRegisterPlugin;
import com.dc.framework.validations.annotation.RequiredStringValidator;
import com.dc.framework.validations.annotation.Validations;

/**
 * 
 * @ClassName: RequiredStringValidate
 * @Description: 字符串非空校验
 * @author: jinzhaopo
 * @date: 2015-5-11 下午12:37:05
 */
public class RequiredStringValidate extends AutoRegisterPlugin implements
		IValidate {

	public WebErrors validate(HttpServletRequest request, WebErrors errors,
			Validations validations, Map<String, String> decodedUriVariables) {
		RequiredStringValidator[] rsvs = validations.requiredStrings();
		for (RequiredStringValidator rsv : rsvs) {
			String fieldName = rsv.fieldName();
			String value = request.getParameter(fieldName);
			if (StringUtils.isEmpty(value) && decodedUriVariables != null) {
				value = decodedUriVariables.get(fieldName);
			}
			if (!rsv.trim() && StringUtils.isEmpty(value) || rsv.trim()
					&& StringUtils.isBlank(value)) {
				errors.addErrorString(rsv.message());
			}
		}
		return errors;
	}

	public String getType() {
		return "vallidate";
	}

	public String getId() {
		return "requiredStringValidate";
	}

	public String getName() {
		return "字符串非空校验";
	}

	public String getVersion() {
		return "1.0.0";
	}

	public String getAuthor() {
		return "jinzhaopo";
	}

	public void perform(Object... params) {

	}

	public void register() {

	}

}
