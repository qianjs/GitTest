package com.clinical.tongxin.entity;

/**
 * 租赁支付信息
 * Created by linchao on 2017/8/23.
 */

public class LeasePayInfoEntity {
    private String name; // 签到人名
    private String signInCount; // 签到数
    private String confirmCount; // 	已确认签到数
    private String price; // 	单价
    private String payAmount; // 支付金额

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(String signInCount) {
        this.signInCount = signInCount;
    }

    public String getConfirmCount() {
        return confirmCount;
    }

    public void setConfirmCount(String confirmCount) {
        this.confirmCount = confirmCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }
}
