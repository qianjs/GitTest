package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linchao on 2017/8/21.
 */

public class SignInEntity implements Cloneable{
    @SerializedName("signTime")
    private String date; // 日期
    @SerializedName("status")
    private String type; //0未签到 1未确认 2未支付 3完成

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public SignInEntity clone() throws CloneNotSupportedException {
        SignInEntity signInEntity = null;
        try {
            signInEntity = (SignInEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return  signInEntity;
    }
}
