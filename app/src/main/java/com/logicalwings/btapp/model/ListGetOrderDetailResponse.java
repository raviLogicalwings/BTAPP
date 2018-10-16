package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListGetOrderDetailResponse extends MainResponce {
    @SerializedName("data")
    private GetOrderDetailsResponse data;

    public GetOrderDetailsResponse getData() {
        return data;
    }

    public void setData(GetOrderDetailsResponse data) {
        this.data = data;
    }
}
