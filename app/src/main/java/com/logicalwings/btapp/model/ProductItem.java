package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ProductItem {

	@SerializedName("SegmentType")
	private String segmentType;

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("Size")
	private String size;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("fkItemGroupId")
	private int fkItemGroupId;

	@SerializedName("fkFlapItemId")
	private int fkFlapItemId;

	@SerializedName("ItemId")
	private int itemId;

	@SerializedName("fkTubeItemId")
	private int fkTubeItemId;

	@SerializedName("SapItemCode")
	private String sapItemCode;

	@SerializedName("IsFavourite")
	private Integer isFavourite;

	@SerializedName("Qty")
	private Integer qty;

	public void setSegmentType(String segmentType){
		this.segmentType = segmentType;
	}

	public String getSegmentType(){
		return segmentType;
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

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setFkItemGroupId(int fkItemGroupId){
		this.fkItemGroupId = fkItemGroupId;
	}

	public int getFkItemGroupId(){
		return fkItemGroupId;
	}

	public void setFkFlapItemId(int fkFlapItemId){
		this.fkFlapItemId = fkFlapItemId;
	}

	public int getFkFlapItemId(){
		return fkFlapItemId;
	}

	public void setItemId(int itemId){
		this.itemId = itemId;
	}

	public int getItemId(){
		return itemId;
	}

	public void setFkTubeItemId(int fkTubeItemId){
		this.fkTubeItemId = fkTubeItemId;
	}

	public int getFkTubeItemId(){
		return fkTubeItemId;
	}

	public void setSapItemCode(String sapItemCode){
		this.sapItemCode = sapItemCode;
	}

	public String getSapItemCode(){
		return sapItemCode;
	}

	public Integer getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Integer isFavourite) {
		this.isFavourite = isFavourite;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Override
 	public String toString(){
		return 
			"ProductItem{" +
			"segmentType = '" + segmentType + '\'' + 
			",pattern = '" + pattern + '\'' + 
			",size = '" + size + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",fkItemGroupId = '" + fkItemGroupId + '\'' + 
			",fkFlapItemId = '" + fkFlapItemId + '\'' + 
			",itemId = '" + itemId + '\'' + 
			",fkTubeItemId = '" + fkTubeItemId + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' +
			",IsFavourite = '" + isFavourite + '\'' +
			",Qty = '" + qty + '\'' +
			"}";
		}
}