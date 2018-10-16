package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class Category{

	@SerializedName("CategoryId")
	private int categoryId;

	@SerializedName("CategoryName")
	private String categoryName;

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}

	@Override
 	public String toString(){
		return 
			"Category{" + 
			"categoryId = '" + categoryId + '\'' + 
			",categoryName = '" + categoryName + '\'' + 
			"}";
		}
}