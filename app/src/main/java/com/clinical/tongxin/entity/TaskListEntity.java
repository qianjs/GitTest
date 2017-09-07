package com.clinical.tongxin.entity;

/**
 * 二期 任务列表
 * Created by linchao on 2017/6/15.
 */

public class TaskListEntity {
    private String taskId;// 任务id
    private String taskName;// 任务名称
    private String city;// 发布城市
    private String totalAmount;// 总价
    private String typePicUrl;// 图片
    private String projectType;// 项目类型
    private String notifyCount;// 通知数
    private String openCount;// 打开数
    private String biddingCount;// 竞价数
    private String time;// 时间

    private String sortId;
    private int click;
    private String workers; // 需要人员数
    private String price; // 单价 人/天
    private String deadline; // 工期
    private String taskType; // 任务类型
    private String type; // 0普通任务 1租赁

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
