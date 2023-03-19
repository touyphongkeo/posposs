package com.app.pospos.model;
import com.google.gson.annotations.SerializedName;
public class rate {
    @SerializedName("Id")
    private String Id;
    @SerializedName("ex_date")
    private String ex_date;
    @SerializedName("ex_time")
    private String 	ex_time;
    @SerializedName("ex_kip_bath")
    private String 	ex_kip_bath;
    @SerializedName("ex_kip_us")
    private String ex_kip_us;

    @SerializedName("ex_status")
    private String ex_status;
    @SerializedName("ex_userlogin")
    private String ex_userlogin;

    public String getID() {
        return Id;
    }
    public String getcomex_date() {
        return ex_date;
    }
    public String getcomex_time() {
        return ex_time;
    }
    public String getcomex_kip_bath() {
        return ex_kip_bath;
    }
    public String getcomex_kip_us() {
        return ex_kip_us;
    }
    public String getcomex_status() {
        return ex_status;
    }
    public String getimgex_userlogin() {
        return ex_userlogin;
    }
}