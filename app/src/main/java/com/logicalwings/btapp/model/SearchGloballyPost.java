package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class SearchGloballyPost{

	@SerializedName("keyword")
	private String keyword;

	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	public String getKeyword(){
		return keyword;
	}

	@Override
 	public String toString(){
		return 
			"SearchGloballyPost{" + 
			"keyword = '" + keyword + '\'' + 
			"}";
		}
}