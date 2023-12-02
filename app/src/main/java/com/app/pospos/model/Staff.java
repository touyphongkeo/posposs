package com.app.pospos.model;
import com.google.gson.annotations.SerializedName;

public class Staff {
    @SerializedName("Id")
    private String Id;
    @SerializedName("user_code")
    private String user_code;
    @SerializedName("userid")
    private String userid;
    @SerializedName("username")
    private String username;

    @SerializedName("userpass")
    private String userpass;
    @SerializedName("userstatus")
    private String userstatus;
    @SerializedName("sale")
    private String sale;
    @SerializedName("tbl")
    private String tbl;
    @SerializedName("report")
    private String report;
    @SerializedName("stock")
    private String stock;
    @SerializedName("setup")
    private String setup;
    @SerializedName("users")
    private String users;
    @SerializedName("edit")
    private String edit;


    @SerializedName("img_urls")
    private String img_urls;
    @SerializedName("email")
    private String email;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getId() {
        return Id;
    }

    public String getuser_code() {
        return user_code;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }
    public String getuserpass() {
        return userpass;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public String getSale() {
        return sale;
    }

    public String geTbl() {
        return tbl;
    }

    public String getReport() {
        return report;
    }

    public String getStock() {
        return stock;
    }

    public String getsetup() {
        return setup;
    }

    public String getusers() {
        return users;
    }

    public String getedit() {
        return edit;
    }

    public String getimg_urls() {
        return img_urls;
    }

    public String getemail() {
        return email;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}