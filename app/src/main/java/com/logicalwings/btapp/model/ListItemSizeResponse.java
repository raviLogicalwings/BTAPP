package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItemSizeResponse extends MainResponce {
    @SerializedName("data")
    private List<ItemSize> data;

    public List<ItemSize> getData() {
        return data;
    }

    public void setData(List<ItemSize> data) {
        this.data = data;
    }
}
