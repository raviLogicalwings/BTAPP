package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class SearchItemPost{

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("ItemGroupId")
	private String itemGroupId;

	@SerializedName("Size")
	private String size;

	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public String getPattern(){
		return pattern;
	}

	public void setItemGroupId(String itemGroupId){
		this.itemGroupId = itemGroupId;
	}

	public String getItemGroupId(){
		return itemGroupId;
	}

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
	}

	@Override
 	public String toString(){
		return 
			"SearchItemPost{" + 
			"pattern = '" + pattern + '\'' + 
			",itemGroupId = '" + itemGroupId + '\'' + 
			",size = '" + size + '\'' + 
			"}";
		}
}