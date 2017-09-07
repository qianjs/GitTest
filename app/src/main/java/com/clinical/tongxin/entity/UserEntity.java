package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class UserEntity implements Serializable {
    // 用户编号

    @SerializedName("CustomerId")
    private String userId;
    // ukey
    @SerializedName("Ukey")
    private String ukey;

    // 用户名
    @SerializedName("RealName")
    private String userName;
    // 用户手机号
    @SerializedName("Mobile")
    private String phone;
    // 银行名字
    @SerializedName("BankName")
    private String bankName;
    // 卡号
    @SerializedName("CardNumber")
    private String cardNumber;
    // 昵称
    @SerializedName("NickName")
    private String nickName;
    // 头像
    @SerializedName("PhotoUrl")
    private String photoUrl;
    // id num
    @SerializedName("IDNumber")
    private String iDNumber;
    @SerializedName("IsRefuseTask")
    private String isRefuseTask;
    // 状态
    @SerializedName("Status")
    private String status;
    // 用户角色
    @SerializedName("Role")
    private String role;
    @SerializedName("HXNumber")
    private String hxAccount;
    private String pwd;
    // 评分
    @SerializedName("Rating")
    private String rating;
    // 分值 100 或 5
    @SerializedName("RatingType")
    private String ratingType;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getIsRefuseTask() {
        return isRefuseTask;
    }

    public void setIsRefuseTask(String isRefuseTask) {
        this.isRefuseTask = isRefuseTask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHxAccount() {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount) {
        this.hxAccount = hxAccount;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingType() {
        return ratingType;
    }

    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }
}
