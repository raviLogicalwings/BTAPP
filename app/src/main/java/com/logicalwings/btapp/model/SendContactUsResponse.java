package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class SendContactUsResponse extends MainResponce {
    @SerializedName("data")
    private SendContactUs data;

    public SendContactUs getData() {
        return data;
    }

    public void setData(SendContactUs data) {
        this.data = data;
    }
}
