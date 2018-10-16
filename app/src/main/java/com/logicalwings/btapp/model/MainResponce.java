package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class MainResponce{

	@SerializedName("statuscode")
	private int statuscode;

	@SerializedName("message")
	private String message;

	public void setStatuscode(int statuscode){
		this.statuscode = statuscode;
	}

	public int getStatuscode(){
		return statuscode;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"MainResponce{" + 
			"statuscode = '" + statuscode + '\'' + 
			",message = '" + message + '\'' +
			"}";
		}
}