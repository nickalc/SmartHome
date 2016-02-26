package com.nick.smarthome.bean;

public class User {

	public String phoneNumber ;
	public String password  ;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String phoneNumber, String password) {
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"phoneNumber='" + phoneNumber + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
