package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItemPatternResponse extends MainResponce {
    @SerializedName("data")
    private List<ItemPattern> data;

    public List<ItemPattern> getData() {
        return data;
    }

    public void setData(List<ItemPattern> data) {
        this.data = data;
    }
}
