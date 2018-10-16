package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class Favourite {
    @SerializedName("IsFavourite")
    private String IsFavourite;

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }
}
