package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemSizePost{

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("ItemGroupId")
	private int itemGroupId;

	@SerializedName("keyword")
	private String keyword;

	@SerializedName("CategoryId")
	private String CategoryId;

	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public String getPattern(){
		return pattern;
	}

	public void setItemGroupId(int itemGroupId){
		this.itemGroupId = itemGroupId;
	}

	public int getItemGroupId(){
		return itemGroupId;
	}

	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	public String getKeyword(){
		return keyword;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	@Override
 	public String toString(){
		return 
			"ItemSizePost{" + 
			"pattern = '" + pattern + '\'' + 
			",itemGroupId = '" + itemGroupId + '\'' + 
			",keyword = '" + keyword + '\'' + 
			"}";
		}
}