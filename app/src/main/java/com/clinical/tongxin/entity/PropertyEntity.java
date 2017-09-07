package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class PropertyEntity {
    @SerializedName("OrderId")
    private String BalanceDetailsId;//订单唯一编号
//    @SerializedName("")
    private String CreateDay;
    @SerializedName("OrderTradeNumber")
    private String orderNumber;//订单号或交易号
    @SerializedName("TradeNumber")
    private String tradingNumber;
    @SerializedName("TradeName")
    private String myName;//	名称
    @SerializedName("CustomerName")
    private String exchanageName;//对方
    @SerializedName("Balance")
    private String money;// 金额
    @SerializedName("Status")
    private String status;//状态
    @SerializedName("TradeTime")
    private String createDate;//	创建日期
    private String sortId;//分页标识
    private String TradeType;

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
    }

    public String getBalanceDetailsId() {
        return BalanceDetailsId;
    }

    public void setBalanceDetailsId(String balanceDetailsId) {
        BalanceDetailsId = balanceDetailsId;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getDay() {
        return CreateDay;
    }

    public void setDay(String day) {
        this.CreateDay = day;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTradingNumber() {
        return tradingNumber;
    }

    public void setTradingNumber(String tradingNumber) {
        this.tradingNumber = tradingNumber;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getExchanageName() {
        return exchanageName;
    }

    public void setExchanageName(String exchanageName) {
        this.exchanageName = exchanageName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
