package com.dc.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.framework.spring.SpringUtils;


/**
 * 
 * @ClassName: Message
 * @Description: 请求的消息
 * @author: zhaopo
 * @date: 2016年11月3日 下午3:44:44
 */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Message implements Serializable {
	/**
	 * 
	 * @ClassName: Type
	 * @Description: 消息类型
	 * @author: zhaopo
	 * @date: 2016年11月3日 下午3:44:54
	 */
	public enum Type {
		SUCCESS, WARN, ERROR;
	}

	/**
	 * 消息类型
	 */
	private Type type;
	/**
	 * 内容提示
	 */
	private String content;

	public Message(Type type, String content, Object args[]) {
		this.type = type;
		this.content = SpringUtils.getMessage(content, args);
	}

	public static Message success(String content, Object args[]) {
		return new Message(Type.SUCCESS, content, args);
	}

	public static Message warn(String content, Object args[]) {
		return new Message(Type.WARN, content, args);
	}

	public static Message error(String content, Object args[]) {
		return new Message(Type.ERROR, content, args);
	}
	
	

	public Message() {
		super();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
