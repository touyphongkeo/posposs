package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Customer {


    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("customer_tel")
    private String customer_tel;

    @SerializedName("customer_time")
    private String customer_time;

    @SerializedName("customer_date")
    private String customer_date;

    @SerializedName("number_card")
    private String number_card;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getcustomer_tel() {
        return customer_tel;
    }

    public String getcustomer_time() {
        return customer_time;
    }

    public String getcustomer_date() {
        return customer_date;
    }

    public String getnumber_card() {
        return number_card;
    }


    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}