package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListSearchItemsResponse extends MainResponce {
    @SerializedName("data")
    private List<ProductItem> data;

    public List<ProductItem> getData() {
        return data;
    }

    public void setData(List<ProductItem> data) {
        this.data = data;
    }
}
