package com.app.pospos.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("code1")
    private String code1;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("bprice")
    private String bprice;

    @SerializedName("price")
    private String price;

    @SerializedName("qty")
    private String qty;

    @SerializedName("size")
    private String size;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("cook")
    private String cook;

    @SerializedName("cut_qty")
    private String cut_qty;


    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }

    public String getCode1() {
        return code1;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getBprice() {
        return bprice;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getSize() {
        return size;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getCook() {
        return cook;
    }

    public String getCut_qty() {
        return cut_qty;
    }
}