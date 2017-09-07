package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class ItemQualificationsEntity {
    private String taskTypeId;//任务id
    private String taskTypenName;//任务名称
    private String type;//任务是否选中

    public String getTaskTypeId() {

        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskTypenName() {
        return taskTypenName;
    }

    public void setTaskTypenName(String taskTypenName) {
        this.taskTypenName = taskTypenName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
