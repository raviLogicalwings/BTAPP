package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCartPost {
    @SerializedName("Items")
    public List<CartPost> items;

    public List<CartPost> getItems() {
        return items;
    }

    public void setItems(List<CartPost> items) {
        this.items = items;
    }
}
