package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemGroupPost{

	@SerializedName("CategoryId")
	private int categoryId;

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("Size")
	private String size;

	@SerializedName("keyword")
	private String keyword;

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public String getPattern(){
		return pattern;
	}

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
	}

	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	public String getKeyword(){
		return keyword;
	}

	@Override
 	public String toString(){
		return 
			"ItemGroupPost{" + 
			"categoryId = '" + categoryId + '\'' + 
			",pattern = '" + pattern + '\'' + 
			",size = '" + size + '\'' + 
			",keyword = '" + keyword + '\'' + 
			"}";
		}
}