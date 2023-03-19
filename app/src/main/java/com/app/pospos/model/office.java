package com.app.pospos.model;
import com.google.gson.annotations.SerializedName;
public class office {
    @SerializedName("Id")
    private String Id;
    @SerializedName("com_name_la")
    private String com_name_la;
    @SerializedName("com_name_en")
    private String 	com_name_en;
    @SerializedName("com_address")
    private String 	com_address;
    @SerializedName("com_tel")
    private String com_tel;
    @SerializedName("com_email")
    private String com_email;
    @SerializedName("img_url")
    private String img_url;
    @SerializedName("persians")
    private String 	persians;

    public String getID() {
        return Id;
    }
    public String getcom_name_la() {
        return com_name_la;
    }
    public String getcom_name_en() {
        return com_name_en;
    }
    public String getcom_address() {
        return com_address;
    }
    public String getcom_tel() {
        return com_tel;
    }
    public String getcom_email() {
        return com_email;
    }
    public String getimg_url() {
        return img_url;
    }
    public String getpersians() {
        return persians;
    }

}