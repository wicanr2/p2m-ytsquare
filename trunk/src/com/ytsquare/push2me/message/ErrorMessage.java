package com.ytsquare.push2me.message;

public interface ErrorMessage {
	public static final int DATABASE_SERVICE_UNAVAILABLE = 0x0000;
	public static final int DATE_FORMAT_ERROR = 0x0001;
	
	public static final int USER_NOT_FOUND = 0x0100;
	public static final int USER_ALREADY_EXIST = 0x0101;
	public static final int USER_REGISTER_FAILED = 0x0102;
	
	public static final int USER_ADD_FRIEND_FAILED = 0x0103;
	public static final int USER_DELETE_FRIEND_FAILED = 0x0104;
	public static final int USER_UPDATE_STATUS_FAILED = 0x0105;
	
	public static final int MESSAGE_NOT_FOUND = 0x0200;
	public static final int UPDATE_MESSAGE_STATUS_FAILED = 0x0201;

	public static final int SENT_MESSAGE_FAILED = 0x0211;
	public static final int DELETE_MESSAGE_FAILED = 0x0212;
}