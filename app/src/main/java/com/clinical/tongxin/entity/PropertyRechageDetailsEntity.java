package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class PropertyRechageDetailsEntity {
    @SerializedName("TradeType")
    private String tradeType;//交易类型
    @SerializedName("Number")
    private String number;//交易号
    @SerializedName("TradeTime")
    private String tradeTime;//交易时间
    @SerializedName("Balance")
    private String balance;//总额
    @SerializedName("PayPrice")
    private String payPrice;//支付金额
    @SerializedName("Charge")
    private String charge;//服务费

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
