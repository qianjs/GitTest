package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchao on 2017/6/19.
 */

public class TaskDetailsContractorEntity implements Serializable {
    @SerializedName("TaskTypeName")
    private String taskTypeName; 	// 任务类型
    @SerializedName("ProjectTypeName")
    private String projectTypeName;	// 项目类型名
    @SerializedName("CityName")
    private String city;	// 项目类型名
    @SerializedName("Amount")
    private String amount;	// 总价
    @SerializedName("Scale")
    private String scale;// 规模
    @SerializedName("CreateOrderTime")
    private String createOrderTime;// 发布时间
    @SerializedName("CompleteTime")
    private String completeTime;// 完成时间
    @SerializedName("PublisherId")
    private String publisherId;// 	发布人评分
    @SerializedName("Score")
    private String score;// 	发布人评分
    @SerializedName("TaskItem")
    private List<TaskSiteItemEntity> taskItem;
    @SerializedName("Status")
    private String taskStatusName;// 任务状态名
    @SerializedName("PublishedType")
    private String publishedType; // 1竞价中 2指定接包
    @SerializedName("PayAccount")
    private String payAccount; // 需要支付金额
    @SerializedName("IsAcceptScore")
    private String isAcceptScore;// 接包方评价 0未评价 1已评价
    @SerializedName("IsSendScore")
    private String isSendScore;// 发包方评价 0未评价 1已评价
    @SerializedName("ContractorHxNum")
    private String contractorHxNum;

    @SerializedName("HXNumber")
    private String hXNumber;	// 发布人环信账号
    private String BidCustomerId;
    @SerializedName("BidPrice")
    private String bidPrice;

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidCustomerId() {
        return BidCustomerId;
    }

    public void setBidCustomerId(String bidCustomerId) {
        BidCustomerId = bidCustomerId;
    }

    public String getContractorHxNum() {
        return contractorHxNum;
    }

    public void setContractorHxNum(String contractorHxNum) {
        this.contractorHxNum = contractorHxNum;
    }

    public String gethXNumber() {
        return hXNumber;
    }

    public void sethXNumber(String hXNumber) {
        this.hXNumber = hXNumber;
    }

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

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPublishedType() {
        return publishedType;
    }

    public void setPublishedType(String publishedType) {
        this.publishedType = publishedType;
    }


    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(String createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<TaskSiteItemEntity> getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(List<TaskSiteItemEntity> taskItem) {
        this.taskItem = taskItem;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
