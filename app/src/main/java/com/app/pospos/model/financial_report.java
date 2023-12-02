package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class financial_report {
    @SerializedName("Id")
    private String Id;
    @SerializedName("sale_save_bill")
    private String sale_save_bill;

    @SerializedName("money")
    private String money;

    @SerializedName("pay_day")
    private String pay_day;

    @SerializedName("save_currency")
    private String save_currency;

    @SerializedName("online_pay")
    private String online_pay;

    @SerializedName("rates")
    private String rates;

    @SerializedName("receive")
    private String receive;

    @SerializedName("sale_save_status")
    private String sale_save_status;


    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getId() {
        return Id;
    }

    public String getSale_save_bill() {
        return sale_save_bill;
    }

    public String getMoney() {
        return money;
    }

    public String getPay_day() {
        return pay_day;
    }

    public String getSave_currency() {
        return save_currency;
    }

    public String getOnline_pay() {
        return online_pay;
    }

    public String getRates() {
        return rates;
    }

    public String getReceive() {
        return receive;
    }

    public String getSale_save_status() {
        return sale_save_status;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}