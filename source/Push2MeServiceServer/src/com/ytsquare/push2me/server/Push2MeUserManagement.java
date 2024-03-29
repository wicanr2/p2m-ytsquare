package com.ytsquare.push2me.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

import com.ytsquare.push2me.entity.User;
import com.ytsquare.push2me.message.ErrorMessage;
import com.ytsquare.push2me.message.ResponseMessage;

@Path("/Push2MeRegister/")
// sets the path for this service
public class Push2MeUserManagement {
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage login() {
		try {
			ResponseMessage responseMessage = new ResponseMessage();	
			return responseMessage;
		} catch ( Exception e ){
			
		}
		return null;
	}
	@Path("/logout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage logout() {
		try {
			ResponseMessage responseMessage = new ResponseMessage();	
			return responseMessage;
		} catch ( Exception e ){
			
		}
		return null;
	}
	
	
	@Path("/registerUser")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage registerUser(User user) {
		ResponseMessage responseMessage = new ResponseMessage();		
		Connection conn = DBUtils.getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			try {
				if (!DBUtils.isUserRegistered(conn, user.getUserId())) {				
					stmt = conn.prepareStatement(DBUtils.REGISTER_USER);
					// set userid(email)
					stmt.setString(1, user.getUserId());
					// set password
					stmt.setString(2, user.getPassword());
					stmt.setString(3, user.getFirstname());
					stmt.setString(4, user.getLastname());
					stmt.setString(5, user.getNickname());
					InputStream image = new ByteArrayInputStream(user.getImage());
					stmt.setBinaryStream(6, image);
					stmt.setString(7, user.getCountry());
					stmt.setInt(8, user.getSex());
					// set birthday, format yyyy-mm-dd
//					java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthday());
//					stmt.setDate(9, new java.sql.Date(date1.getTime()));
					stmt.setString(9, user.getBirthday());
					stmt.setInt(10, user.getStatusId());
					
					if (stmt.executeUpdate() > 0) {					
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_DATA);
					}
					else {
						responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
						responseMessage.setErrorMessage(ErrorMessage.USER_REGISTER_FAILED);						
					}
				} else {
					responseMessage.setMessageType(ResponseMessage.MESSAGE_TYPE_ERROR);
					responseMessage.setErrorMessage(ErrorMessage.USER_ALREADY_EXIST);						
				}
			} catch (Exception e) {
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
		}
		return responseMessage;
	}	
	
	
	@Path("/updateUser")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseMessage modifyUser(User user) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();	
			return responseMessage;
		} catch ( Exception e ){
			
		}
		return null;
	}
}
