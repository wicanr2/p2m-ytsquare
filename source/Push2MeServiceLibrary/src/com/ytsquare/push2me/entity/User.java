package com.ytsquare.push2me.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	public static final int USER_REGISTERING = 0;
	public static final int USER_REGISTERED = 1;
	public static final int USER_BANED = 2;	
	
	private String userId;
	private String password;
	private String firstname;
	private String lastname;
	private String nickname;
	
	
	private byte[] image; // for store image
	
	private String country;
	private int sex;
	private String birthday; 
	private String register_day; 
	private int statusId;
	private String Group = "";
	
	public User(){
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRegister_day() {
		return register_day;
	}

	public void setRegister_day(String register_day) {
		this.register_day = register_day;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String group) {
		Group = group;
	}
}
