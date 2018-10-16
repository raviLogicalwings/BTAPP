package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCartCreateOrderPost {
    @SerializedName("Items")
    public List<CartResponse> items;

    public List<CartResponse> getItems() {
        return items;
    }

    public void setItems(List<CartResponse> items) {
        this.items = items;
    }
}
