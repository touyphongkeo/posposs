package com.app.pospos.model;
import com.google.gson.annotations.SerializedName;
public class OrderDetails {
    @SerializedName("Id")
    private String id;
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
    @SerializedName("amount")
    private String amount;
    @SerializedName("bath_kip")
    private String bath_kip;
    @SerializedName("usd_kip")
    private String usd_kip;
    @SerializedName("cn_kip")
    private String cn_kip;
    @SerializedName("tax")
    private String tax;

    @SerializedName("tper")
    private String tper;

    @SerializedName("tpers")
    private String tpers;

    @SerializedName("ttotal")
    private String ttotal;

    @SerializedName("options")
    private String options;
    @SerializedName("value")
    private String value;
    public String getId() {
        return id;
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
    public String geTamount() {
        return amount;
    }
    public String geTbath_kip() {
        return bath_kip;
    }
    public String geTusd_kip() {
        return usd_kip;
    }
    public String geTcn_kip() {
        return cn_kip;
    }

    public String geTax() {
        return tax;
    }
    public String gettper() {
        return tper;
    }

    public String gettpers() {
        return tpers;
    }

    public String getttotal() {
        return ttotal;
    }
    public String getoptions() {
        return options;
    }


    public String getValue() {
        return value;
    }



}