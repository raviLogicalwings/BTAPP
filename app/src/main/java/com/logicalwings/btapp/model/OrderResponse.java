package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class OrderResponse extends MainResponce{
    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
