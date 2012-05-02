package com.ytsquare.push2me;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtils {
	public final static String QUERY_USER_COUNT = "select COUNT(*) from user where userId = ?";
	public final static String QUERY_USER_FRIEND_COUNT = "select COUNT(*) from userFriend where userId = ? and friendUserId = ?";

	// Using SHA1 for password encryption needs 40 bytes in MySQL
	public final static String REGISTER_USER = "insert into user values(?, SHA1(?), ?, ?, ?, ?, ?, ?, ?, ?)";
	public final static String ADD_FRIEND = "insert into userFriend values(?, ?, ?)";
	public final static String UPDATE_USER_STATUS = "update user set statusId = ? where userId = ?";
	public final static String GET_ALL_FRIENDS = "select b.* from userFriend a, user b where a.userId = ? and a.friendUserId = b.userId";
	public final static String DELETE_FRIEND = "delete from userFriend where userId = ? and friendUserId = ?";
	public final static String FIND_10FRIENDS = "select * from user where userId like ? limit ?,?";
	
	public final static String GET_RECEIVED_MESSAGES_BY_DATE = "select b.*, c.nickname from userMessage a, message b, user c where a.userId = ? and a.messageId = b.messageId and b.messageDate >= ? and messageDate <= ? and messageStatusId <> ? and b.fromUserId = c.userId";
	public final static String UPDATE_RECEIVED_MESSAGE_STATUS = "update userMessage set messageStatusId = ? where userId = ? and messageId = ?";
	public final static String DELETE_RECEIVED_MESSAGE = "update userMessage set messageStatusId = ? where userId = ? and messageId = ?";

	public final static String ADD_MESSAGE = "insert into message values(?, ?, ?, ?, ?, ?, ?)";
	public final static String SENT_MESSAGE_TO = "insert into userMessage values(?, ?, ?)";
	public final static String GET_SENT_MESSAGES_BY_DATE = "select * from message where fromUserId = ? and messageDate >= ? and messageDate <= ? and messageStatusId <> ?";
	public final static String DELETE_SENT_MESSAGE = "update message set messageStatusId = ? where messageId = ?";

	public static Connection getConnection() {
		try {
			InitialContext initCtx = new InitialContext();
			// Look up our data source
			DataSource ds = (DataSource) initCtx
					.lookup("java:comp/env/jdbc/ytsquare");
			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isUserRegistered(Connection conn, String userId) throws SQLException {
		boolean result = false;
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(QUERY_USER_COUNT);
		stmt.setString(1, userId);
		ResultSet rs = stmt.executeQuery();
		if (rs != null) {
			rs.next();
			// find user id registered
			if (rs.getInt(1) > 0) {
				result = true;
			}
	    }
		stmt.close();
		return result;
	}

	public static boolean hasFriend(Connection conn, String userId, String friendId) throws SQLException {
		boolean result = false;
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(QUERY_USER_FRIEND_COUNT);
		stmt.setString(1, userId);
		ResultSet rs = stmt.executeQuery();
		if (rs != null) {
			rs.next();
			// find user id registered
			if (rs.getInt(1) > 0) {
				result = true;
			}
	    }
		stmt.close();
		return result;
	}
}

