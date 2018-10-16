package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class CartPost{

	@SerializedName("SegmentType")
	private String segmentType;

	@SerializedName("Pattern")
	private String pattern;

	@SerializedName("Size")
	private String size;

	@SerializedName("Qty")
	private long qty;

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

    public CartPost(String segmentType, String pattern, String size, long qty, String itemName, int fkItemGroupId, int fkFlapItemId, int itemId, int fkTubeItemId, String sapItemCode) {
        this.segmentType = segmentType;
        this.pattern = pattern;
        this.size = size;
        this.qty = qty;
        this.itemName = itemName;
        this.fkItemGroupId = fkItemGroupId;
        this.fkFlapItemId = fkFlapItemId;
        this.itemId = itemId;
        this.fkTubeItemId = fkTubeItemId;
        this.sapItemCode = sapItemCode;
    }

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

	public void setQty(long qty){
		this.qty = qty;
	}

	public long getQty(){
		return qty;
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

	@Override
 	public String toString(){
		return 
			"CartPost{" + 
			"segmentType = '" + segmentType + '\'' + 
			",pattern = '" + pattern + '\'' + 
			",size = '" + size + '\'' + 
			",qty = '" + qty + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",fkItemGroupId = '" + fkItemGroupId + '\'' + 
			",fkFlapItemId = '" + fkFlapItemId + '\'' + 
			",itemId = '" + itemId + '\'' + 
			",fkTubeItemId = '" + fkTubeItemId + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' + 
			"}";
		}

}