package com.ytsquare.push2me.message;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.ytsquare.push2me.entity.Message;
import com.ytsquare.push2me.entity.User;

@XmlRootElement
public class ResponseMessage {
	public static final int MESSAGE_TYPE_ERROR =0;
	public static final int MESSAGE_TYPE_DATA = 1;	
	
	private int messageType;
	private int errorMessage;
	private String content;
	private List<User> userList = new ArrayList<User>();
	private List<Message> messageList = new ArrayList<Message>();
	
	// JAXB needs this
	public ResponseMessage() {
		
	}
	
	public ResponseMessage(int messageType, String content) {
		this.setMessageType(messageType);
		this.setContent(content); 
	}
	public List<User> getUserList() {
		return userList;
	}
	public List<Message> getMessageList() {
		return messageList;
	}
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(int errorMessage) {
		this.errorMessage = errorMessage;
	}
}
