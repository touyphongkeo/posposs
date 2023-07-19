package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class totall {
    @SerializedName("totall_price")
    private String totall_price;
    @SerializedName("bath")
    private String bath;

    @SerializedName("usd")
    private String usd;
    public String gettotall_price() {
        return totall_price;
    }
    public String gettbath() {
        return bath;
    }

    public String getusd() {
        return usd;
    }
}