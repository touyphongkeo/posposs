package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Cook {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;


    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;
    public String getCID() {
        return id;
    }
    public String getCName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}