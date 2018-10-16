package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class SendContactUs{

	@SerializedName("EmailId")
	private String emailId;

	@SerializedName("MobileNo")
	private String MobileNo;

	@SerializedName("CustomerName")
	private String CustomerName;

	@SerializedName("Message")
	private String message;

	public void setEmailId(String emailId){
		this.emailId = emailId;
	}

	public String getEmailId(){
		return emailId;
	}

	public String getMobileNo() {
		return MobileNo;
	}

	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
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
			"SendContactUs{" + 
			"emailId = '" + emailId + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}