package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Salelist {
    @SerializedName("sale_save_bill")
    private String sale_save_bill;
    @SerializedName("sale_date")
    private String sale_date;
    @SerializedName("sale_save_table")
    private String sale_save_table;

    @SerializedName("sale_qty")
    private String sale_qty;

    @SerializedName("sale_amount")
    private String sale_amount;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;
    public String getsale_save_bill() {
        return sale_save_bill;
    }
    public String getsale_date() {
        return sale_date;
    }
    public String getsale_save_table() {
        return sale_save_table;
    }
    public String getsale_qty() {
        return sale_qty;
    }
    public String getsale_amount() {
        return sale_amount;
    }
    public String getValue() {
        return value;
    }
    public String getMassage() {
        return massage;
    }
}