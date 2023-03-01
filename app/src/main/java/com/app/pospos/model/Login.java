package com.app.pospos.model;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("user_code")
    private String user_code;

    @SerializedName("userid")
    private String userid;

    @SerializedName("userpass")
    private String userpass;

    @SerializedName("username")
    private String username;

    @SerializedName("userstatus")
    private String userstatus;

    @SerializedName("sale")
    private String sale;

    @SerializedName("tbl")
    private String tbl;

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

    public String getValue() {
        return value;
    }
    public String getMassage() {return massage;}
    public String getuser_code() {return user_code;}
    public String getUserid() {return userid;}
    public String getuserpass() {return userpass;}
    public String getusername() {return username;}
    public String getuserstatus() {return userstatus;}
    public String getsale() {return sale;}
    public String gettbl() {return tbl;}
    public String getstock() {return stock;}
    public String getsetup() {return setup;}
    public String getusers() {return users;}
    public String getedit() {return edit;}
    public String getimg_urls() {return img_urls;}
    public String getimgemail() {return email;}
}