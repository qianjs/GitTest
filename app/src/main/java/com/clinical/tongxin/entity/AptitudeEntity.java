package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * Created by linchao on 2016/12/29.
 */

public class AptitudeEntity implements Serializable{
    private String TaskTypeName;
    private String ProjectTypeName;
    private String TaskTypeId;
    private String ProjectTypeId;

    public String getTaskTypeName() {
        return TaskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        TaskTypeName = taskTypeName;
    }

    public String getProjectTypeName() {
        return ProjectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        ProjectTypeName = projectTypeName;
    }

    public String getTaskTypeId() {
        return TaskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        TaskTypeId = taskTypeId;
    }

    public String getProjectTypeId() {
        return ProjectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        ProjectTypeId = projectTypeId;
    }
}
