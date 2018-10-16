package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListGetOrdersResponse extends MainResponce {
    @SerializedName("data")
    private List<GetOrdersResponse> data;

    public List<GetOrdersResponse> getData() {
        return data;
    }

    public void setData(List<GetOrdersResponse> data) {
        this.data = data;
    }
}
