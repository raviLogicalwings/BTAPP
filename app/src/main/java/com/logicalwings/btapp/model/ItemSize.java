package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemSize{

	@SerializedName("Size")
	private String size;

	@SerializedName("fkItemGroupId")
	private int fkItemGroupId;

	@SerializedName("ItemId")
	private int itemId;

	@SerializedName("SapItemCode")
	private String sapItemCode;

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
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
			"ItemSize{" + 
			"size = '" + size + '\'' + 
			",fkItemGroupId = '" + fkItemGroupId + '\'' + 
			",itemId = '" + itemId + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' + 
			"}";
		}
}