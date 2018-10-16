package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCartResponse extends MainResponce {
    @SerializedName("data")
    private List<CartResponse> data;

    public List<CartResponse> getData() {
        return data;
    }

    public void setData(List<CartResponse> data) {
        this.data = data;
    }
}
