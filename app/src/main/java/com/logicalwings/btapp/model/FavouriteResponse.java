package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class FavouriteResponse extends MainResponce{
    @SerializedName("data")
    private Favourite data;

    public Favourite getData() {
        return data;
    }

    public void setData(Favourite data) {
        this.data = data;
    }
}
