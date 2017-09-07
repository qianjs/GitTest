package com.clinical.tongxin.entity;

/**
 * 租赁任务详情实体
 * linchao
 * Created by linchao on 2017/8/14.
 */

public class TaskLeaseEntity {
    private String taskName; // 任务名称
    private String projectTypeName; // 	项目类型名
    private String taskTypeName; // 	任务类型
    private String address; //	所在区域
    private String workers; // 	人数
    private String price; // 	租赁金额
    private String deadline; // 	工期
    private String score; // 	发布人评分
    private String publisherId; // 	发布人id

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
