package com.logicalwings.btapp.model;

import com.google.gson.annotations.SerializedName;

public class Test {
    @SerializedName("CategoryId")
    private String CategoryId;

    @SerializedName("Size")
    private String Size;

    @SerializedName("Pattern")
    private String Pattern;

    @SerializedName("keyword")
    private String keyword;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getPattern() {
        return Pattern;
    }

    public void setPattern(String pattern) {
        Pattern = pattern;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
