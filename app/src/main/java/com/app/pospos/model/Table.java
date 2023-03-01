package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Table {
    @SerializedName("Id")
    private String Id;

    @SerializedName("tbname")
    private String tbname;

    @SerializedName("status_table")
    private String status_table;

    @SerializedName("stt")
    private String stt;

    @SerializedName("linkid")
    private String linkid;

    @SerializedName("tbl")
    private String tbl;

    @SerializedName("sale_bill")
    private String sale_bill;

    @SerializedName("sale_time")
    private String sale_time;


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

    public String getTbname() {
        return tbname;
    }

    public String getStatustable() {
        return status_table;
    }

    public String getStt() {
        return stt;
    }

    public String getLinkid() {
        return linkid;
    }

    public String getTbl() {
        return tbl;
    }

    public String get_sale_bill() {
        return sale_bill;
    }

    public String get_time() {
        return sale_time;
    }


}