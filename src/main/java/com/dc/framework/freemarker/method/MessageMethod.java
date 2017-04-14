package com.dc.framework.freemarker.method;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.dc.framework.spring.SpringUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;

/**
 * 国际化freemarker method 调用
 * 
 * @author jinzhaopo
 * @date 2016年7月8日上午8:54:45
 * @version 3.0.0
 */
@Repository
public class MessageMethod implements TemplateMethodModelEx {

	public Object exec(List arguments) {
		if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null
				&& StringUtils.isNotEmpty(arguments.get(0).toString())) {
			String s = null;
			String s1 = arguments.get(0).toString();
			if (arguments.size() > 1) {
				Object aobj[] = arguments.subList(1, arguments.size()).toArray();
				s = SpringUtils.getMessage(s1, aobj);
			} else {
				s = SpringUtils.getMessage(s1, new Object[] {});
			}
			return new SimpleScalar(s);
		} else {
			return null;
		}
	}

}
