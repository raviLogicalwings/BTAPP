package com.logicalwings.btapp.model;


import com.google.gson.annotations.SerializedName;


public class LoginResponce extends MainResponce{
	@SerializedName("data")
	private User data;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}