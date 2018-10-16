package com.logicalwings.btapp.model;

public class Cart {
    private String rowid, phoneEmail, itemId, itemName, sapItemCode, fkItemGroupId, size, pattern, segmentType, fkTubeItemId, fkFlapItemId, quantity;

    public Cart(String rowid, String phoneEmail, String itemId, String itemName, String sapItemCode, String fkItemGroupId, String size, String pattern, String segmentType, String fkTubeItemId, String fkFlapItemId, String quantity) {
        this.rowid = rowid;
        this.phoneEmail = phoneEmail;
        this.itemId = itemId;
        this.itemName = itemName;
        this.sapItemCode = sapItemCode;
        this.fkItemGroupId = fkItemGroupId;
        this.size = size;
        this.pattern = pattern;
        this.segmentType = segmentType;
        this.fkTubeItemId = fkTubeItemId;
        this.fkFlapItemId = fkFlapItemId;
        this.quantity = quantity;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getPhoneEmail() {
        return phoneEmail;
    }

    public void setPhoneEmail(String phoneEmail) {
        this.phoneEmail = phoneEmail;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSapItemCode() {
        return sapItemCode;
    }

    public void setSapItemCode(String sapItemCode) {
        this.sapItemCode = sapItemCode;
    }

    public String getFkItemGroupId() {
        return fkItemGroupId;
    }

    public void setFkItemGroupId(String fkItemGroupId) {
        this.fkItemGroupId = fkItemGroupId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getFkTubeItemId() {
        return fkTubeItemId;
    }

    public void setFkTubeItemId(String fkTubeItemId) {
        this.fkTubeItemId = fkTubeItemId;
    }

    public String getFkFlapItemId() {
        return fkFlapItemId;
    }

    public void setFkFlapItemId(String fkFlapItemId) {
        this.fkFlapItemId = fkFlapItemId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
