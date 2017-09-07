package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 2016/12/9.
 */

public class TaskEntity implements Serializable{
    @SerializedName("TaskId")
    private String taskId; //任务编号
    @SerializedName("ProjectName")
    private String projectName;//项目名
    @SerializedName("Status")
    private String statusName; // 任务状态名称
    @SerializedName("Amount")
    private String amount;//总价
    @SerializedName("TypePicUrl")
    private String typePicUrl;//任务类型图片地址
    @SerializedName("BidCount")
    private String bidCount;//竞价人数
    @SerializedName("sortId")
    private String sortId;// 分页标识
    @SerializedName("IsAcceptScore")
    private String isAcceptScore;// 接包方评价 0未评价 1已评价
    @SerializedName("IsSendScore")
    private String isSendScore;// 发包方评价 0未评价 1已评价
    @SerializedName("ProjectTypeName")
    private String projectTypeName;// 项目类型名称
    @SerializedName("TaskName")
    private String taskName;
    @SerializedName("CityName")
    private String city;
    @SerializedName("CreatedTime")
    private String time;
    private int click;

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
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

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getBidCount() {
        return bidCount;
    }

    public void setBidCount(String bidCount) {
        this.bidCount = bidCount;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
