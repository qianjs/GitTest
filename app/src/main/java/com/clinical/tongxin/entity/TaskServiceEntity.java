package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 2016/12/9.
 */

public class TaskServiceEntity implements Serializable{
    private String taskId; //任务id
    private String orderId; // 订单编号
    private String projectName;//项目名
    private String statusName; // 任务状态名称
    private String typePicUrl;//任务类型图片地址
    private String sortId;// 分页标识
    private String projectTypeName;// 项目类型名称
    private String taskName;//任务名称
    private String workers;
    private String price;
    private String city;
    private String deadline;
    private String leaseStatus;
    private String absenceCount; //未签订数
    private String publisherId; // 发布人id
    private String publisher; // 发布人名字
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(String absenceCount) {
        this.absenceCount = absenceCount;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getLeaseStatus() {
        return leaseStatus;
    }

    public void setLeaseStatus(String leaseStatus) {
        this.leaseStatus = leaseStatus;
    }
}
