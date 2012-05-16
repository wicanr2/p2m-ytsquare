package com.ytsquare.push2me;

import com.ytsquare.push2me.entity.*;
import com.ytsquare.push2me.message.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class Push2MeClient {
	/* --------------------------------------------------------------------------------- */
	private String push2MeBaseURL="http://localhost:8080";
	private final String push2MeService					=	"Push2MeService";
	
	private final String push2MeAddFriend				=	"addFriend";
	private final String push2MeUpdateUserStatus		=	"updateUserStatus";
	private final String push2MeGetAllFriends			=	"getAllFriends";
	private final String push2MeFindFriends				=	"findFriends";
	private final String push2MeDeleteFriend			=	"deleteFriend";
	
	private final String push2MeCreateGroup				=	"createGroup";
	private final String push2MeChangeGroupName			=	"changeGroupName";
	private final String push2MeAddFriendToGroup		=	"addFriendToGroup";
	private final String push2MeDeleteFriendFromGroup	=	"deleteFriendFromGroup";
	private final String push2MeDeleteGroup				=	"deleteGroup";

	private final String push2MeGetReceivedMessagesByDate	=	"getReceivedMessagesByDate";
//	private final String push2MeUpdateMessageStatus		=	"updateMessageStatus";
	private final String push2MeSetReceivedMessageRead	=	"setReceivedMessageRead";
	private final String push2MeDeleteReceivedMessageById	=	"deleteReceivedMessageById"; 
	private final String push2MeGetSentMessagesByDate	=	"getSentMessagesByDate";
	private final String push2MeSendMessage				=	"sendMessage";
	private final String push2MeDeleteSentMessage		=	"deleteSentMessage";
	
	private final String push2MeRegisterUser			=   "registerUser";
	private final String push2MeUpdateUser				=   "updateUser";
	private final String pust2MeLogin					=	"login";
	private final String pust2MeLogout					=	"logout";
	/* --------------------------------------------------------------------------------- */
	/** 
	 * @param url : the base url of the push2me service
	 * */
	public void setPush2MeBaseURL( String url ) {
		this.push2MeBaseURL = url;
	}
	private String getPush2MeURL() {
		return this.push2MeBaseURL+"/"+this.push2MeService;
	}
	private String getPush2MeURL(String user){
		return this.push2MeBaseURL+"/"+this.push2MeService+"/"+user;
	}
	private String getPush2MeURL(String user, String action) {
		return this.push2MeBaseURL+"/"+this.push2MeService+"/"+user+"/"+action;
	}
	private String getPush2MeURLAction(String action) {
		return this.push2MeBaseURL+"/"+this.push2MeService+"/"+action;
	}
	
	/* --------------------------------------------------------------------------------- */
	// user profile
	public ResponseMessage registerUser(User user) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURLAction( this.push2MeRegisterUser );
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).put(ResponseMessage.class, user);
			return rm;
		} catch ( Exception e ){
			
		}
		return null;
	}
	
	// user profile
	public ResponseMessage updateUser(User user) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURLAction( this.push2MeRegisterUser );
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());
				
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).put(ResponseMessage.class,user);
			return rm;
		} catch ( Exception e ){
				
		}
		return null;
	}
	
	/* --------------------------------------------------------------------------------- */
	// friend maintenance 
	public ResponseMessage addFriend(User user, String friend_userId ) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeAddFriend);
			finalUrl= finalUrl+"/"+friend_userId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).put(ResponseMessage.class);
			return rm;
		} catch ( Exception e ){
			
		}
		return null;
	}
	
	public ResponseMessage push2MeUpdateUserStatus(User user) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeUpdateUserStatus);
			finalUrl= finalUrl + "/" + user.getStatusId();
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class);
			return rm;
		} catch ( Exception e ){
			
		}
		return null;
	}
	
	public List<User> getAllFriend(User user) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeGetAllFriends);
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).get(ResponseMessage.class);
			
			return rm.getUserList();
			
		} catch ( Exception e ) {
			
		}
		return null;
	}
	
	public List<User> findFriends(User user, String partialFriendUserId, int startRow, int rowCount) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeFindFriends);
			finalUrl = finalUrl + "/" + partialFriendUserId + "/" + startRow + "/" + rowCount;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).get(ResponseMessage.class);
			return rm.getUserList();
		} catch ( Exception e ) {
			
		}
		return null;
	}
	
	public boolean deleteFriend(User user, String friend_userId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeDeleteFriend);
			finalUrl = finalUrl + "/" + friend_userId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).delete(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}

	/* --------------------------------------------------------------------------------- */
	/**
	 *  Group access library
	 */	
	/* --------------------------------------------------------------------------------- */
	
	public boolean createGroup(User user, String groupName) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeCreateGroup);
			finalUrl = finalUrl + "/" + groupName;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).put(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}
	
	public boolean changeGroupName(User user, String oldGroupName, String newGroupName) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeChangeGroupName);
			finalUrl = finalUrl + "/" + oldGroupName + "/" + newGroupName;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}
	
	public boolean addFriendToGroup(User user, String groupName, String friendId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeAddFriendToGroup);
			finalUrl = finalUrl + "/" + groupName + "/" + friendId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}

	public boolean deleteFriendFromGroup(User user, String groupName, String friendId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeDeleteFriendFromGroup);
			finalUrl = finalUrl + "/" + groupName + "/" + friendId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).delete(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}
	
	public boolean deleteGroup(User user, String groupName) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeDeleteGroup);
			finalUrl = finalUrl + "/" + groupName;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).delete(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}
	

	
	
	/* --------------------------------------------------------------------------------- */
	/**
	 *  Message access library
	 */	
	
	public List<Message> getReceivedMessagesByDate(User user,String startDate, String endDate) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeGetReceivedMessagesByDate);
			finalUrl = finalUrl + "/" + startDate + "/" + endDate;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).get(ResponseMessage.class);
			return rm.getMessageList();
		} catch ( Exception e ) {
			
		}
		return null;
	}
	
	public boolean setReceivedMessageRead(User user, String messageId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeSetReceivedMessageRead);
			finalUrl = finalUrl + "/" + messageId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}

	public boolean deleteReceivedMessageById(User user, String messageId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeDeleteReceivedMessageById);
			finalUrl = finalUrl + "/" + messageId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).delete(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}
	
	public List<Message> getSentMessagesByDate(User user, String startDate, String endDate) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeGetSentMessagesByDate);
			finalUrl = finalUrl + "/" + startDate + "/" + endDate;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).get(ResponseMessage.class);
			return rm.getMessageList();
		} catch ( Exception e ) {
			
		}
		return null;
	}
	
	public boolean sendMessage(User user, Message message) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeSendMessage);
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).post(ResponseMessage.class, message);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}

	public boolean deleteSentMessage(User user, String messageId) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			String finalUrl = getPush2MeURL(user.getUserId(), this.push2MeDeleteSentMessage);
			finalUrl = finalUrl + "/" + messageId;
			WebResource service = client.resource(UriBuilder.fromUri(finalUrl).build());	
			
			ResponseMessage rm = service.accept(MediaType.APPLICATION_JSON).delete(ResponseMessage.class);
			return (rm.getMessageType() == ResponseMessage.MESSAGE_TYPE_DATA);
		} catch ( Exception e ) {
			
		}
		return false;
	}


	
	/* --------------------------------------------------------------------------------- */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Push2MeClient client = new Push2MeClient();
	}
}
