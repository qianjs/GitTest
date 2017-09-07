package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class QualificationListEntity {
    private String projectTypeId;//项目id
    private String projectTypeName;//项目名称
    private List<ItemQualificationsEntity> list;//任务集合

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public List<ItemQualificationsEntity> getList() {
        return list;
    }

    public void setList(List<ItemQualificationsEntity> list) {
        this.list = list;
    }
}
