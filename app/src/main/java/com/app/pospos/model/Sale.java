package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Sale {

    @SerializedName("Id")
    private String Id;

    @SerializedName("sale_bill")
    private String sale_bill;

    @SerializedName("sale_date")
    private String sale_date;

    @SerializedName("sale_table")
    private String sale_table;

    @SerializedName("sale_proid")
    private String sale_proid;

    @SerializedName("sale_name")
    private String sale_name;

    @SerializedName("sale_price")
    private String sale_price;

    @SerializedName("sale_qty")
    private String sale_qty;

    @SerializedName("sale_status")
    private String sale_status;

    @SerializedName("edit_sale")
    private String edit_sale;

    @SerializedName("sendid")
    private String sendid;

    @SerializedName("sendtime")
    private String sendtime;

    @SerializedName("print")
    private String print;

    @SerializedName("username")
    private String username;

    @SerializedName("statusorder")
    private String statusorder;

    @SerializedName("remark")
    private String remark;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("sum_amount")
    private String sum_amount;





    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getValue() {
        return value;
    }
    public String getMassage() {
        return massage;
    }

    public String getId() {
        return Id;
    }

    public String getSale_bill() {
        return sale_bill;
    }

    public String getSale_date() {
        return sale_date;
    }

    public String getSale_table() {
        return sale_table;
    }

    public String getSale_proid() {
        return sale_proid;
    }

    public String getSale_name() {
        return sale_name;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getSale_qty() {
        return sale_qty;
    }

    public String getSale_status() {
        return sale_status;
    }

    public String getEdit_sale() {
        return edit_sale;
    }

    public String getSendid() {
        return sendid;
    }

    public String getSendtime() {
        return sendtime;
    }

    public String getPrint() {
        return print;
    }

    public String getUsername() {
        return username;
    }

    public String getStatusorder() {
        return statusorder;
    }

    public String getRemark() {
        return remark;
    }

    public String get_Img_url() {
        return img_url;
    }
    public String getSumamount() {
        return sum_amount;
    }


}