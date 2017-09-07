package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class TaskPropertyEntity {
    @SerializedName("ProjectName")
    private String projectName;
    @SerializedName("NickName")
    private String nickName;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("OrderTotal")
    private String orderTotal;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("Property")
    private List<TaskPropertyListEntity> property;
    @SerializedName("PayPrice")
    private String payPrice;
    @SerializedName("RealName")
    private String realName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<TaskPropertyListEntity> getProperty() {
        return property;
    }

    public void setProperty(List<TaskPropertyListEntity> property) {
        this.property = property;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
