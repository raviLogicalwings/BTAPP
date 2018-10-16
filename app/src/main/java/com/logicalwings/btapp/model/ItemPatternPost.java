package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemPatternPost{

	@SerializedName("ItemGroupId")
	private int itemGroupId;

	@SerializedName("Size")
	private String size;

	@SerializedName("keyword")
	private String keyword;

	@SerializedName("CategoryId")
	private String CategoryId;

	public void setItemGroupId(int itemGroupId){
		this.itemGroupId = itemGroupId;
	}

	public int getItemGroupId(){
		return itemGroupId;
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

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	@Override
 	public String toString(){
		return 
			"ItemPatternPost{" + 
			"itemGroupId = '" + itemGroupId + '\'' + 
			",size = '" + size + '\'' + 
			",keyword = '" + keyword + '\'' + 
			"}";
		}
}