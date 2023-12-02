package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Catgory {
    @SerializedName("Id")
    private String Id;
    @SerializedName("category_id")
    private String category_id;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;
    public String getID() {
        return Id;
    }


    public String getCategory_id() {
        return category_id;
    }
    public String getCategory_name() {
        return category_name;
    }
    public String getValue() {
        return value;
    }
    public String getMassage() {
        return massage;
    }
}