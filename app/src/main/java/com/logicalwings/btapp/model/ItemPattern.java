package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemPattern{

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("fkItemGroupId")
	private int fkItemGroupId;

	@SerializedName("ItemId")
	private int itemId;

	@SerializedName("SapItemCode")
	private String sapItemCode;

	public void setPattern(String pattern){
		this.pattern = pattern;
	}

	public String getPattern(){
		return pattern;
	}

	public void setFkItemGroupId(int fkItemGroupId){
		this.fkItemGroupId = fkItemGroupId;
	}

	public int getFkItemGroupId(){
		return fkItemGroupId;
	}

	public void setItemId(int itemId){
		this.itemId = itemId;
	}

	public int getItemId(){
		return itemId;
	}

	public void setSapItemCode(String sapItemCode){
		this.sapItemCode = sapItemCode;
	}

	public String getSapItemCode(){
		return sapItemCode;
	}

	@Override
 	public String toString(){
		return 
			"ItemPattern{" + 
			"pattern = '" + pattern + '\'' + 
			",fkItemGroupId = '" + fkItemGroupId + '\'' + 
			",itemId = '" + itemId + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' + 
			"}";
		}
}