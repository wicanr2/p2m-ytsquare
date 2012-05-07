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
	
	public static final int USER_CREATE_GROUP_FAILED = 0x0110;
	public static final int USER_DELETE_GROUP_FAILED = 0x0111;
	public static final int GROUP_NOT_EMPTY = 0x0112;
	public static final int GROUP_EXIST = 0x0113;
	public static final int GROUP_NOT_EXIST = 0x0114;
	public static final int ADD_USER_TO_GROUP_FAILED = 0x0115;
	public static final int CHANGE_GROUP_NAME_FAILED = 0x0116;
	
	public static final int MESSAGE_NOT_FOUND = 0x0200;
	public static final int UPDATE_MESSAGE_STATUS_FAILED = 0x0201;

	public static final int SENT_MESSAGE_FAILED = 0x0211;
	public static final int DELETE_MESSAGE_FAILED = 0x0212;
}
