package com.ytsquare.push2me;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

import com.ytsquare.push2me.entity.Message;
import com.ytsquare.push2me.entity.User;
import com.ytsquare.push2me.message.ErrorMessage;
import com.ytsquare.push2me.message.ResponseMessage;

/**
 * 
 * @author n200
 * Create = PUT
 * Retrieve = GET
 * Update = POST
 * Delete = DELETE
 * 
 */
@Path("/Push2MeService/{userId}/")
// sets the path for this service
// anr / getMessage
// anr / putMessage
public class Push2MeService {
	
	private boolean hasFriend(Connection conn, String userId, String friendId) throws SQLException {
		boolean result = false;
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(DBUtils.QUERY_USER_FRIEND_COUNT);
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
	
	/* **************************************************************************** */

	/* **************************************************************************** */
	@Path("/addFriend/{friend_userId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage addFriend(@PathParam("userId") String userId,
			@PathParam("friend_userId") String friend_userId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				if (DBUtils.isUserRegistered(conn, friend_userId)) {
					stmt = conn.prepareStatement(DBUtils.ADD_FRIEND);
					stmt.setString(1, userId);
					stmt.setString(2, friend_userId);
					stmt.setString(3, "");	
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.USER_ADD_FRIEND_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.USER_ADD_FRIEND_FAILED);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;
	}

	@Path("/updateUserStatus/{userStatusId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage updateUserStatus(@PathParam("userId") String userId, @PathParam("userStatusId") int statusId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.UPDATE_USER_STATUS);
				stmt.setInt(1, statusId);
				stmt.setString(2, userId);
				if (stmt.executeUpdate() > 0) {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
				}
				else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.USER_UPDATE_STATUS_FAILED);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
		
	}
	
	/**
	 * get all friends by user ID
	 * according the userid and date to query firends push the message into
	 * list and return
	 * 
	 * @param userId user ID
	 * @return return all friends
	 */
	@Path("/getAllFriends/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage getAllFriends(@PathParam("userId") String userId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.GET_ALL_FRIENDS);
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					User user = new User();
					user.setUserId(rs.getString(1));
					user.setFirstname(rs.getString(3));
					user.setLastname(rs.getString(4));
					user.setNickname(rs.getString(5));
					user.setImage(rs.getBytes(6));
					user.setCountry(rs.getString(7));
					user.setSex(rs.getInt(8));
					user.setBirthday(rs.getString(9));
					user.setStatusId(rs.getInt(10));
					user.setGroup(rs.getString(11));
					responseMessage.getUserList().add(user);
				}
				rs.close();
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	/**
	 * find friend by partial ID
	 * 
	 * @param userId user ID
	 * @return return 10 friends each time
	 */
	@Path("/findFriends/{partialId}/{startRow}/{rowCount}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage findFriends(@PathParam("partialId") String partialId,
			@PathParam("startRow") int startRow, @PathParam("rowCount") int rowCount) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.FIND_10FRIENDS);
				stmt.setString(1, "%" + partialId + "%");
				stmt.setInt(2, startRow);
				stmt.setInt(3, rowCount);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					User user = new User();
					user.setUserId(rs.getString(1));
					user.setFirstname(rs.getString(3));
					user.setLastname(rs.getString(4));
					user.setNickname(rs.getString(5));
					user.setImage(rs.getBytes(6));
					user.setCountry(rs.getString(7));
					user.setSex(rs.getInt(8));
					user.setBirthday(rs.getString(9));
					user.setStatusId(rs.getInt(10));
					responseMessage.getUserList().add(user);
				}
				rs.close();
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.USER_NOT_FOUND);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	@Path("/deleteFriend/{friendUserId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage deleteFriend(
			@PathParam("userId") String userId,
			@PathParam("friendUserId") String friendUserId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.DELETE_FRIEND);
				stmt.setString(1, userId);
				stmt.setString(2, friendUserId);
				if (stmt.executeUpdate() > 0) {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
				}
				else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.USER_DELETE_FRIEND_FAILED);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.USER_NOT_FOUND);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	private boolean hasGroup(Connection conn, String userId, String groupName) throws SQLException {
		boolean result = true;
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(DBUtils.GROUP_USER_COUNT);
		stmt.setString(1, userId);
		stmt.setString(2, groupName);
		ResultSet rs = stmt.executeQuery();
		if (rs != null) {
			rs.next();
			// find user id registered
			if (rs.getInt(1) > 0) {
				result = false;
			}
	    }
		stmt.close();
		return result;
	}
    
    @Path("/createGroup/{groupName}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage createGroup(@PathParam("userId") String userId,
			@PathParam("groupName") String groupName) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				if (!hasGroup(conn, userId, groupName)) {
					stmt = conn.prepareStatement(DBUtils.CREATE_GROUP);
					stmt.setString(1, userId);
					stmt.setString(2, UUID.randomUUID().toString());
					stmt.setString(3, groupName);	
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.USER_CREATE_GROUP_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.GROUP_EXIST);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;
    }
    
	private String getGroupIdByName(Connection conn, String userId, String groupName) throws SQLException {
		String result = "";
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement(DBUtils.GET_GROUP_ID_BY_NAME);
		stmt.setString(1, userId);
		stmt.setString(2, groupName);
		ResultSet rs = stmt.executeQuery();
		if (rs != null) {
			rs.next();
			// find user id registered
			result = rs.getString(1);
	    }
		stmt.close();
		return result;
	}	

    @Path("/changeGroupName/{oldGroupName}/{newGroupName}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage changeGroupName(@PathParam("userId") String userId,
			@PathParam("groupName") String oldGroupName,
			@PathParam("groupName") String newGroupName) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				String groupId = getGroupIdByName(conn, userId, oldGroupName);
				if (!groupId.equals("")) {
					stmt = conn.prepareStatement(DBUtils.CHANGE_GROUP_NAME);
					stmt.setString(1, newGroupName);
					stmt.setString(2, userId);
					stmt.setString(3, groupId);	
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.CHANGE_GROUP_NAME_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.GROUP_NOT_EXIST);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;
    }    
    
    @Path("/addFriendToGroup/{groupName}/{friendId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage addFriendToGroup(@PathParam("userId") String userId,
			@PathParam("groupName") String groupName,
			@PathParam("friendId") String friendId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				String groupId = getGroupIdByName(conn, userId, groupName);
				if (!groupId.equals("")) {
					stmt = conn.prepareStatement(DBUtils.ADD_FRIEND_TO_GROUP);
					stmt.setString(1, groupId);
					stmt.setString(2, userId);
					stmt.setString(3, friendId);	
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.ADD_USER_TO_GROUP_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.GROUP_NOT_EXIST);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;
    }
    
    @Path("/deleteFriendFromGroup/{groupName}/{friendId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage deleteFriendFromGroup(@PathParam("userId") String userId,
    		@PathParam("groupName") String groupName,
    		@PathParam("friendId") String friendId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				String groupId = getGroupIdByName(conn, userId, groupName);
				if (!groupId.equals("")) {
					stmt = conn.prepareStatement(DBUtils.REMOVE_FRIEND_FROM_GROUP);
					stmt.setString(1, "");
					stmt.setString(2, userId);
					stmt.setString(3, friendId);	
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.ADD_USER_TO_GROUP_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.GROUP_NOT_EXIST);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;
    }
    
	@Path("/deleteGroup/{groupName}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage deleteGroup(
			@PathParam("userId") String userId,
			@PathParam("groupName") String groupName) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				if (hasGroup(conn, userId, groupName)) {
					stmt = conn.prepareStatement(DBUtils.DELETE_GROUP);
					stmt.setString(1, userId);
					stmt.setString(2, groupName);
					if (stmt.executeUpdate() > 0) {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					} else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.USER_DELETE_GROUP_FAILED);
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.GROUP_NOT_EMPTY);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	
	
	/**
	 * according the userid and date to query message in some duration, and
	 * push the message into list and return
	 */
	@Path("/getReceivedMessagesByDate/{startDate}/{endDate}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMessage getReceivedMessagesByDate(@PathParam("userId") String userId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.GET_RECEIVED_MESSAGES_BY_DATE);
				stmt.setString(1, userId);
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate); 
				stmt.setString(2, startDate);
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate); 
				stmt.setString(3, endDate);
				stmt.setInt(4, Message.STATUS_DELETE);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Message message = new Message();
					message.setMessageId(rs.getString(1));
					message.setTitle(rs.getString(2));
					message.setContent(rs.getString(3));
					message.setMessageDate(rs.getTimestamp(4).toString());
					User user = new User();
					user.setUserId(rs.getString(5));
					user.setNickname(rs.getString(7));
					message.setFromUser(user);
					message.setPriorityId(rs.getInt(6));
					responseMessage.getMessageList().add(message);
				}
				rs.close();
			} catch (ParseException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATE_FORMAT_ERROR);
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}
	
	private ResponseMessage internalUpdateReceivedMessageStatus(String userId,
			String messageId, int statusId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.UPDATE_RECEIVED_MESSAGE_STATUS);
				stmt.setInt(1, statusId);
				stmt.setString(2, userId);
				stmt.setString(3, messageId);
				if (stmt.executeUpdate() > 0) {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
				}
				else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.UPDATE_MESSAGE_STATUS_FAILED);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	@Path("/updateMessageStatus/{messageId}/{statusId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage updateMessageStatus(@PathParam("userId") String userId,
		@PathParam("messageId") String messageId, @PathParam("statusId") int statusId) {
		return internalUpdateReceivedMessageStatus(userId, messageId, statusId);
	}

	@Path("/setReceivedMessageRead/{messageId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage setReceivedMessageRead(@PathParam("userId") String userId,
		@PathParam("messageId") String messageId) {
		return internalUpdateReceivedMessageStatus(userId, messageId, Message.STATUS_READ);
	}

	@Path("/deleteReceivedMessageById/{messageId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage deleteReceivedMessageById(@PathParam("userId") String userId,
			@PathParam("messageId") String messageId) {
		return internalUpdateReceivedMessageStatus(userId, messageId, Message.STATUS_DELETE);
	}
	
	// getSentMessagesByDate in duration
	@Path("/getSentMessagesByDate/{startDate}/{endDate}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMessage getSentMessagesByDate(
			@PathParam("userId") String userId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.GET_SENT_MESSAGES_BY_DATE);
				stmt.setString(1, userId);
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate); 
				stmt.setString(2, startDate);
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate); 					
				stmt.setString(3, endDate);
				stmt.setInt(4, Message.STATUS_DELETE);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Message message = new Message();
					message.setMessageId(rs.getString(1));
					message.setTitle(rs.getString(2));
					message.setContent(rs.getString(3));
					message.setMessageDate(rs.getTimestamp(4).toString());
					User user = new User();
					user.setUserId(rs.getString(5));
					user.setNickname(rs.getString(7));
					message.setFromUser(user);
					message.setPriorityId(rs.getInt(6));
					responseMessage.getMessageList().add(message);
				}
				rs.close();
			} catch (ParseException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATE_FORMAT_ERROR);
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

	@Path("/sendMessage")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage sendMessage(@PathParam("userId") String userId,
			Message message) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			try {
				conn.setAutoCommit(false);
				// use batch statement
				stmt1 = conn.prepareStatement(DBUtils.ADD_MESSAGE);
				// message Id
				stmt1.setString(1, message.getMessageId());
				// message title
				stmt1.setString(2, message.getTitle());
				// message content
				stmt1.setString(3, message.getContent());
				// message date time
				stmt1.setString(4, message.getMessageDate());
				// from user Id
				stmt1.setString(5, userId);
				// message priority
				stmt1.setInt(6, message.getPriorityId());
				// set message status
				stmt1.setInt(7, Message.STATUS_SENT);
				if (stmt1.executeUpdate() <= 0) {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.SENT_MESSAGE_FAILED);
				}
				
				stmt2 = conn.prepareStatement(DBUtils.SENT_MESSAGE_TO);
				Iterator<User> iterator = message.getToUser().iterator();
			    while(iterator.hasNext()) {
					stmt2.setString(1, userId);
					stmt2.setString(2, message.getMessageId());
					stmt2.setInt(3, Message.STATUS_UNHANDLE);
					stmt2.addBatch();
			    }
			    stmt2.executeBatch();
			    conn.commit();			    
			} catch (SQLException e) {
				if (conn != null) {
	                try {
	                	conn.rollback();
	                } catch (SQLException ex1) {
	                }
	            }
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
			} finally {
				if (stmt1 != null) {
					try {
						stmt1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.setAutoCommit(true);
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;		
	}

	@Path("/deleteSentMessage/{messageId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	// content type to output
	public ResponseMessage deleteSentMessage(@PathParam("userId") String userId,
			@PathParam("messageId") String messageId) {
		ResponseMessage responseMessage = new ResponseMessage();
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DBUtils.DELETE_SENT_MESSAGE);
				stmt.setInt(1, Message.STATUS_DELETE);
				stmt.setString(2, messageId);
				if (stmt.executeUpdate() > 0) {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
				}
				else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.DELETE_MESSAGE_FAILED);
				}
			} catch (SQLException e) {
				responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
				responseMessage.setErrorMessage(ErrorMessage.MESSAGE_NOT_FOUND);
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
			responseMessage.setErrorMessage(ErrorMessage.DATABASE_SERVICE_UNAVAILABLE);
		}
		return responseMessage;	
	}

}
