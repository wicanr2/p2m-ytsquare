package com.ytsquare.push2me.message;

public interface ErrorMessage {
	public static final int DATABASE_SERVICE_UNAVAILABLE = 0x0000;
	
	public static final int USER_NOT_EXIST = 0x0100;
	public static final int USER_ALREADY_EXIST = 0x0101;
	public static final int USER_REGISTER_FAILED = 0x0102;
	
	public static final int USER_ADD_FRIEND_FAILED = 0x0103;
}
