package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCategoryResponse extends MainResponce {
    @SerializedName("data")
    private List<Category> data;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
