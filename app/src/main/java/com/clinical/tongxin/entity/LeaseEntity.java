package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 租赁人员
 * Created by linchao on 2017/6/20.
 */

public class LeaseEntity {
    @SerializedName("contractorId")
    private String memberId; // 人员id
    private String name; //  名称
    private String phone; // 手机
    private String image; // 图片
    private String score; // 评分
    private String contractorPrice; // 租金
    private String confirmCount; // 待确定数
    private String status; // 	接包人租赁状态 进行中0 终止1仲裁2 已完成3
    private String isAcceptScore;// 接包方评价 0未评价 1已评价
    private String isSendScore;// 发包方评价 0未评价 1已评价

    public String getIsAcceptScore() {
        return isAcceptScore;
    }

    public void setIsAcceptScore(String isAcceptScore) {
        this.isAcceptScore = isAcceptScore;
    }

    public String getIsSendScore() {
        return isSendScore;
    }

    public void setIsSendScore(String isSendScore) {
        this.isSendScore = isSendScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContractorPrice() {
        return contractorPrice;
    }

    public void setContractorPrice(String contractorPrice) {
        this.contractorPrice = contractorPrice;
    }

    public String getConfirmCount() {
        return confirmCount;
    }

    public void setConfirmCount(String confirmCount) {
        this.confirmCount = confirmCount;
    }
}
