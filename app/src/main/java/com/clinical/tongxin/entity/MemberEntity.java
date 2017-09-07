package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户实体
 * Created by linchao on 2016/12/27.
 */

public class MemberEntity implements Serializable{

    private String CustomerId;
    @SerializedName("RealName")
    private String NickName;
    private String sortId;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }


}
