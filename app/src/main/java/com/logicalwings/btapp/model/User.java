package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("SapCustomerCode")
	private String sapCustomerCode;

	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("Email")
	private String email;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("SapCustomerName")
	private Object sapCustomerName;

	@SerializedName("VfcCode")
	private String vfcCode;

	@SerializedName("CustomerId")
	private int customerId;

	@SerializedName("IsBlocked")
	private int isBlocked;

	@SerializedName("Password")
	private String password;

	@SerializedName("ConfirmPassword")
	private String confirmPassword;

	@SerializedName("token")
	private String token;

	@SerializedName("OldPassword")
	private String oldPassword;

	@SerializedName("CategoryId")
	private String CategoryId;

	@SerializedName("Size")
	private String Size;

	@SerializedName("Pattern")
	private String Pattern;

	@SerializedName("keyword")
	private String keyword;

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getPattern() {
		return Pattern;
	}

	public void setPattern(String pattern) {
		Pattern = pattern;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setSapCustomerCode(String sapCustomerCode){
		this.sapCustomerCode = sapCustomerCode;
	}

	public String getSapCustomerCode(){
		return sapCustomerCode;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setSapCustomerName(Object sapCustomerName){
		this.sapCustomerName = sapCustomerName;
	}

	public Object getSapCustomerName(){
		return sapCustomerName;
	}

	public void setVfcCode(String vfcCode){
		this.vfcCode = vfcCode;
	}

	public String getVfcCode(){
		return vfcCode;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setIsBlocked(int isBlocked){
		this.isBlocked = isBlocked;
	}

	public int getIsBlocked(){
		return isBlocked;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"sapCustomerCode = '" + sapCustomerCode + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",email = '" + email + '\'' + 
			",isActive = '" + isActive + '\'' + 
			",sapCustomerName = '" + sapCustomerName + '\'' + 
			",vfcCode = '" + vfcCode + '\'' + 
			",customerId = '" + customerId + '\'' + 
			",isBlocked = '" + isBlocked + '\'' + 
			",password = '" + password + '\'' + 
			"}";
		}
}