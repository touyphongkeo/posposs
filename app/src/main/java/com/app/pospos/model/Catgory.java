package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Catgory {
    @SerializedName("Id")
    private String Id;
    @SerializedName("category_id")
    private String category_id;
    @SerializedName("category_name")
    private String category_name;
    public String getID() {
        return Id;
    }
    public String getCategory_id() {
        return category_id;
    }
    public String getCategory_name() {
        return category_name;
    }
}