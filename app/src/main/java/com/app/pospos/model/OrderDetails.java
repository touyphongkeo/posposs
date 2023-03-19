package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("sale_qty")
    private String sale_qty;

    @SerializedName("sale_name")
    private String sale_name;

    @SerializedName("sale_price")
    private String sale_price;




    public String getsale_qty() {
        return sale_qty;
    }
    public String getsale_name() {
        return sale_name;
    }
    public String getsale_price() {
        return sale_price;
    }


}