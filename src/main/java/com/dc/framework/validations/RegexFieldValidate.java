package com.dc.framework.validations;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dc.common.WebErrors;
import com.dc.framework.plugin.AutoRegisterPlugin;
import com.dc.framework.validations.annotation.RegexFieldValidator;
import com.dc.framework.validations.annotation.Validations;

public class RegexFieldValidate extends AutoRegisterPlugin implements IValidate {
	
	public WebErrors validate(HttpServletRequest request, WebErrors errors,
			Validations validations, Map<String, String> decodedUriVariables) {
		RegexFieldValidator[] rfvs = validations.regexFields();
		for(RegexFieldValidator rfv : rfvs){
			String fieldName = rfv.fieldName();
			String regex = rfv.expression();
			String value = request.getParameter(fieldName);
			if(StringUtils.isEmpty(value) && decodedUriVariables != null){
				value = decodedUriVariables.get(fieldName);
			}
			if(StringUtils.isEmpty(value) 
					|| !Pattern.matches(regex, value)){
				errors.addErrorString(rfv.message());
			}
		}
		return errors;
	}

	public String getType() {
		return "vallidate";
	}

	public String getId() {
		return "regexValidate";
	}

	public String getName() {
		return "正则校验";
	}

	public String getVersion() {
		return "1.0.0";
	}

	public String getAuthor() {
		return "fangchen";
	}

	public void perform(Object... params) {
		
	}

	@Override
	public void register() {
		
	}

}
