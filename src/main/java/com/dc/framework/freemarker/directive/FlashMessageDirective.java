package com.dc.framework.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dc.common.Message;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModel;

@Component
public class FlashMessageDirective extends BaseDirective {

	public static final String FLASH_MESSAGE_ATTRIBUTE_NAME = (new StringBuilder(FlashMessageDirective.class.getName())).append(".FLASH_MESSAGE").toString();
	private static final String flashMessage = "flashMessage";

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel loopVars[], TemplateDirectiveBody body) {
		RequestAttributes requestattributes = RequestContextHolder.currentRequestAttributes();
		if (requestattributes != null) {
			Message message = (Message) requestattributes.getAttribute(FLASH_MESSAGE_ATTRIBUTE_NAME, 0);
			if (body != null) {
				renderParamToBody(flashMessage, message, env, body);
			} else if (message != null) {
				Writer writer = env.getOut();
				try {
					writer.write((new StringBuilder("$.message(\"")).append(message.getType()).append("\", \"").append(message.getContent()).append("\");").toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
