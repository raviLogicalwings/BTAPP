package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class ItemsItem{

	@SerializedName("SalesOrderItemId")
	private int salesOrderItemId;

	@SerializedName("fkItemId")
	private int fkItemId;

	@SerializedName("Qty")
	private int qty;

	@SerializedName("TotalPrice")
	private double totalPrice;

	@SerializedName("GSTAmount")
	private double gSTAmount;

	@SerializedName("fkSalesOrderId")
	private int fkSalesOrderId;

	@SerializedName("ItemName")
	private String itemName;

	@SerializedName("TotalAmount")
	private double totalAmount;

	@SerializedName("SapItemCode")
	private String sapItemCode;

	public void setSalesOrderItemId(int salesOrderItemId){
		this.salesOrderItemId = salesOrderItemId;
	}

	public int getSalesOrderItemId(){
		return salesOrderItemId;
	}

	public void setFkItemId(int fkItemId){
		this.fkItemId = fkItemId;
	}

	public int getFkItemId(){
		return fkItemId;
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

	public void setGSTAmount(int gSTAmount){
		this.gSTAmount = gSTAmount;
	}

	public double getGSTAmount(){
		return gSTAmount;
	}

	public void setFkSalesOrderId(int fkSalesOrderId){
		this.fkSalesOrderId = fkSalesOrderId;
	}

	public int getFkSalesOrderId(){
		return fkSalesOrderId;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setTotalAmount(int totalAmount){
		this.totalAmount = totalAmount;
	}

	public double getTotalAmount(){
		return totalAmount;
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
			"ItemsItem{" + 
			"salesOrderItemId = '" + salesOrderItemId + '\'' + 
			",fkItemId = '" + fkItemId + '\'' + 
			",qty = '" + qty + '\'' + 
			",totalPrice = '" + totalPrice + '\'' + 
			",gSTAmount = '" + gSTAmount + '\'' + 
			",fkSalesOrderId = '" + fkSalesOrderId + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",totalAmount = '" + totalAmount + '\'' + 
			",sapItemCode = '" + sapItemCode + '\'' + 
			"}";
		}
}