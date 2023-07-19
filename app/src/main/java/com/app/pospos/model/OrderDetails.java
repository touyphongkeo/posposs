package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("sale_qty")
    private String sale_qty;

    @SerializedName("sale_name")
    private String sale_name;

    @SerializedName("sale_price")
    private String sale_price;


    @SerializedName("totall_price")
    private String totall_price;


    @SerializedName("bath")
    private String bath;


    @SerializedName("usd")
    private String usd;

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }


    public String getsale_qty() {
        return sale_qty;
    }
    public String getsale_name() {
        return sale_name;
    }
    public String getsale_price() {
        return sale_price;
    }

    public String getBath() {
        return bath;
    }
    public String getUsd() {
        return usd;
    }

    public String geTotall_price() {
        return totall_price;
    }

}