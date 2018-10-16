package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemGroup{

	@SerializedName("SapItemGroupCode")
	private Object sapItemGroupCode;

	@SerializedName("ItemGroupName")
	private String itemGroupName;

	@SerializedName("ItemGroupId")
	private int itemGroupId;

	@SerializedName("Description")
	private String description;

	@SerializedName("fkCategoryId")
	private int fkCategoryId;

	public void setSapItemGroupCode(Object sapItemGroupCode){
		this.sapItemGroupCode = sapItemGroupCode;
	}

	public Object getSapItemGroupCode(){
		return sapItemGroupCode;
	}

	public void setItemGroupName(String itemGroupName){
		this.itemGroupName = itemGroupName;
	}

	public String getItemGroupName(){
		return itemGroupName;
	}

	public void setItemGroupId(int itemGroupId){
		this.itemGroupId = itemGroupId;
	}

	public int getItemGroupId(){
		return itemGroupId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setFkCategoryId(int fkCategoryId){
		this.fkCategoryId = fkCategoryId;
	}

	public int getFkCategoryId(){
		return fkCategoryId;
	}

	@Override
 	public String toString(){
		return 
			"ItemGroup{" + 
			"sapItemGroupCode = '" + sapItemGroupCode + '\'' + 
			",itemGroupName = '" + itemGroupName + '\'' + 
			",itemGroupId = '" + itemGroupId + '\'' + 
			",description = '" + description + '\'' + 
			",fkCategoryId = '" + fkCategoryId + '\'' + 
			"}";
		}
}