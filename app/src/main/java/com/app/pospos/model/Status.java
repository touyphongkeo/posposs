package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;


    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;
    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}