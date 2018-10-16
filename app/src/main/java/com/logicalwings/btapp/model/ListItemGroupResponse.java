package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItemGroupResponse extends MainResponce {
    @SerializedName("data")
    private List<ItemGroup> data;

    public List<ItemGroup> getData() {
        return data;
    }

    public void setData(List<ItemGroup> data) {
        this.data = data;
    }
}
