package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchao on 2017/6/19.
 */

public class TaskDetailsPublisherEntity implements Serializable {
    private String taskTypeName; 	// 任务类型
    private String projectTypeName;	// 项目类型名
    private String amount;	// 总价
    private String scale;// 规模
    private String publishTime;// 发布时间

    private String notifyCount;// 通知数
    private String openCount;// 查看数
    private String biddingCount;// 竞价数
    private List<PublisherEntity> contractors; // 竞价人数组

    private String taskName; // 任务名称
    private String address; // 	所在区域
    private String workers; // 人数
    private String price;  // 	租赁金额
    private String deadline; // 	工期

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(String notifyCount) {
        this.notifyCount = notifyCount;
    }

    public String getOpenCount() {
        return openCount;
    }

    public void setOpenCount(String openCount) {
        this.openCount = openCount;
    }

    public String getBiddingCount() {
        return biddingCount;
    }

    public void setBiddingCount(String biddingCount) {
        this.biddingCount = biddingCount;
    }

    public List<PublisherEntity> getContractors() {
        return contractors;
    }

    public void setContractors(List<PublisherEntity> contractors) {
        this.contractors = contractors;
    }
}
