package com.logicalwings.btapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetOrderDetailsResponse{

	@SerializedName("OrderStatus")
	private String orderStatus;

	@SerializedName("SalesOrderId")
	private int salesOrderId;

	@SerializedName("Items")
	private List<ItemsItem> items;

	@SerializedName("SapOrderNo")
	private String sapOrderNo;

	@SerializedName("TotalAmount")
	private double totalAmount;

	@SerializedName("fkCustomerId")
	private int fkCustomerId;

	@SerializedName("SapOrderDate")
	private String sapOrderDate;

	public void setOrderStatus(String orderStatus){
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus(){
		return orderStatus;
	}

	public void setSalesOrderId(int salesOrderId){
		this.salesOrderId = salesOrderId;
	}

	public int getSalesOrderId(){
		return salesOrderId;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public void setSapOrderNo(String sapOrderNo){
		this.sapOrderNo = sapOrderNo;
	}

	public String getSapOrderNo(){
		return sapOrderNo;
	}

	public void setTotalAmount(int totalAmount){
		this.totalAmount = totalAmount;
	}

	public double getTotalAmount(){
		return totalAmount;
	}

	public void setFkCustomerId(int fkCustomerId){
		this.fkCustomerId = fkCustomerId;
	}

	public int getFkCustomerId(){
		return fkCustomerId;
	}

	public void setSapOrderDate(String sapOrderDate){
		this.sapOrderDate = sapOrderDate;
	}

	public String getSapOrderDate(){
		return sapOrderDate;
	}

	@Override
 	public String toString(){
		return 
			"GetOrderDetailsResponse{" + 
			"orderStatus = '" + orderStatus + '\'' + 
			",salesOrderId = '" + salesOrderId + '\'' + 
			",items = '" + items + '\'' + 
			",sapOrderNo = '" + sapOrderNo + '\'' + 
			",totalAmount = '" + totalAmount + '\'' + 
			",fkCustomerId = '" + fkCustomerId + '\'' + 
			",sapOrderDate = '" + sapOrderDate + '\'' + 
			"}";
		}
}