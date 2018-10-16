package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class CartResponse {

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("Size")
	private String size;

	@SerializedName("GSTAmount")
	private double gSTAmount;

	@SerializedName("fkItemGroupId")
	private int fkItemGroupId;

	@SerializedName("fkFlapItemId")
	private int fkFlapItemId;

	@SerializedName("TotalAmount")
	private double totalAmount;

	@SerializedName("ItemId")
	private int itemId;

	@SerializedName("SapItemCode")
	private String sapItemCode;

	@SerializedName("SegmentType")
	private String segmentType;

	@SerializedName("Qty")
	private int qty;

	@SerializedName("TotalPrice")
	private double totalPrice;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("fkTubeItemId")
	private int fkTubeItemId;

//	public CartResponse(String segmentType, String pattern, String size, int qty, String itemName, int fkItemGroupId, int fkFlapItemId, int itemId, int fkTubeItemId, String sapItemCode, int totalAmount, int totalPrice, int gstAmount) {
//		this.segmentType = segmentType;
//		this.pattern = pattern;
//		this.size = size;
//		this.qty = qty;
//		this.itemName = itemName;
//		this.fkItemGroupId = fkItemGroupId;
//		this.fkFlapItemId = fkFlapItemId;
//		this.itemId = itemId;
//		this.fkTubeItemId = fkTubeItemId;
//		this.sapItemCode = sapItemCode;
//		this.totalAmount = totalAmount;
//		this.totalPrice = totalPrice;
//		this.gSTAmount = gstAmount;
//	}

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

	public void setGSTAmount(int gSTAmount){
		this.gSTAmount = gSTAmount;
	}

	public double getGSTAmount(){
		return gSTAmount;
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

	public void setTotalAmount(int totalAmount){
		this.totalAmount = totalAmount;
	}

	public double getTotalAmount(){
		return totalAmount;
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

	public void setSegmentType(String segmentType){
		this.segmentType = segmentType;
	}

	public String getSegmentType(){
		return segmentType;
	}

	public void setQty(int qty){
		this.qty = qty;
	}

	public int getQty(){
		return qty;
	}

	public void setTotalPrice(int totalPrice){
		this.totalPrice = totalPrice;
	}

	public double getTotalPrice(){
		return totalPrice;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setFkTubeItemId(int fkTubeItemId){
		this.fkTubeItemId = fkTubeItemId;
	}

	public int getFkTubeItemId(){
		return fkTubeItemId;
	}

	@Override
 	public String toString(){
		return 
			"CartResponse{" + 
			"pattern = '" + pattern + '\'' + 
			",size = '" + size + '\'' + 
			",gSTAmount = '" + gSTAmount + '\'' + 
			",fkItemGroupId = '" + fkItemGroupId + '\'' + 
			",fkFlapItemId = '" + fkFlapItemId + '\'' + 
			",totalAmount = '" + totalAmount + '\'' + 
			",itemId = '" + itemId + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' + 
			",segmentType = '" + segmentType + '\'' + 
			",qty = '" + qty + '\'' + 
			",totalPrice = '" + totalPrice + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",fkTubeItemId = '" + fkTubeItemId + '\'' + 
			"}";
		}
}