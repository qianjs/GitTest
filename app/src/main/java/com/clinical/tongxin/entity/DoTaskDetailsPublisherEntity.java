package com.clinical.tongxin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchao on 2017/6/19.
 */

public class DoTaskDetailsPublisherEntity implements Serializable {
    private String taskTypeName; 	// 任务类型
    private String projectTypeName;	// 项目类型名

    private List<LeaseEntity> contractors; // 竞价人数组

    private String taskName; // 任务名称
    private String address; // 	所在区域
    private String workers; // 人数
    private String price;  // 	租赁金额
    private String deadline; // 	工期

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

    public List<LeaseEntity> getContractors() {
        return contractors;
    }

    public void setContractors(List<LeaseEntity> contractors) {
        this.contractors = contractors;
    }

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
}
