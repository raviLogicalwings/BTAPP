package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ContactUs{

	@SerializedName("EmailId")
	private Object emailId;

	@SerializedName("ContactUsId")
	private int contactUsId;

	@SerializedName("Address")
	private String address;

	@SerializedName("MobileNos")
	private String mobileNos;

	@SerializedName("OfficeName")
	private String officeName;

	public void setEmailId(Object emailId){
		this.emailId = emailId;
	}

	public Object getEmailId(){
		return emailId;
	}

	public void setContactUsId(int contactUsId){
		this.contactUsId = contactUsId;
	}

	public int getContactUsId(){
		return contactUsId;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setMobileNos(String mobileNos){
		this.mobileNos = mobileNos;
	}

	public String getMobileNos(){
		return mobileNos;
	}

	public void setOfficeName(String officeName){
		this.officeName = officeName;
	}

	public String getOfficeName(){
		return officeName;
	}

	@Override
 	public String toString(){
		return 
			"ContactUs{" + 
			"emailId = '" + emailId + '\'' + 
			",contactUsId = '" + contactUsId + '\'' + 
			",address = '" + address + '\'' + 
			",mobileNos = '" + mobileNos + '\'' + 
			",officeName = '" + officeName + '\'' + 
			"}";
		}
}