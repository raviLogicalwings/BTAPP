package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListContactusResponse extends MainResponce {
    @SerializedName("data")
    private List<ContactUs> data;

    public List<ContactUs> getData() {
        return data;
    }

    public void setData(List<ContactUs> data) {
        this.data = data;
    }
}
