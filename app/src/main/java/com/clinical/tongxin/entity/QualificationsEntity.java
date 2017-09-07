package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class QualificationsEntity {
    @SerializedName("ProjectTypeName")
    private String projectTypeName;//项目名称
    @SerializedName("ProjectTypeId")
    private String projectTypeId;//项目id
    @SerializedName("TaskTypeId")
    private String taskTypeId;//任务id
    @SerializedName("TaskTypeName")
    private String taskTypenName;//任务名称
    @SerializedName("Type")
    private String type;//任务是否选中

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

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
