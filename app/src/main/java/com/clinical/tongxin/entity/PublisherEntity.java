package com.clinical.tongxin.entity;

import java.util.List;

/**
 * 发包方
 * Created by linchao on 2017/6/20.
 */

public class PublisherEntity {
    private String contractorId;
    private String name;
    private String phone;
    private String image;
    private String score;
    private String biddingAmount;
    private boolean isShowEvaluate;
    private String sortId;
    private List<EvaluateListEntity> evaluateListEntities;
    private String choose; // 是否已被选为中标 0未选 1已选


    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public List<EvaluateListEntity> getEvaluateListEntities() {
        return evaluateListEntities;
    }

    public void setEvaluateListEntities(List<EvaluateListEntity> evaluateListEntities) {
        this.evaluateListEntities = evaluateListEntities;
    }

    public String getContractorId() {
        return contractorId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
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

    public String getBiddingAmount() {
        return biddingAmount;
    }

    public void setBiddingAmount(String biddingAmount) {
        this.biddingAmount = biddingAmount;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public boolean isShowEvaluate() {
        return isShowEvaluate;
    }

    public void setShowEvaluate(boolean showEvaluate) {
        isShowEvaluate = showEvaluate;
    }
}
