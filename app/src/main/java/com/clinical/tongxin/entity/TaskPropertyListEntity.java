package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class TaskPropertyListEntity {
    @SerializedName("Balance")
    private String balance;
    @SerializedName("BalanceDetailsId")
    private String balanceDetailsId;
    @SerializedName("Charge")
    private String charge;
    @SerializedName("CustomerRemark")
    private String customerRemark;
    @SerializedName("Expenses")
    private String expenses;
    @SerializedName("Income")
    private String income;
    @SerializedName("Number")
    private String number;
    @SerializedName("Status")
    private String status;
    @SerializedName("TradeTime")
    private String tradeTime;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceDetailsId() {
        return balanceDetailsId;
    }

    public void setBalanceDetailsId(String balanceDetailsId) {
        this.balanceDetailsId = balanceDetailsId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getCustomerRemark() {
        return customerRemark;
    }

    public void setCustomerRemark(String customerRemark) {
        this.customerRemark = customerRemark;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
}
