package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构经理实体类
 * Created by linchao on 2017/3/26
 */

public class OrganizationManagerEntity implements Serializable{
    private String managerName;
    private String managerPosition;
    private String managerId;
    private String managerPhone;
    @SerializedName("DirectorGroupId")
    private String directorGroupId;

    public String getDirectorGroupId() {
        return directorGroupId;
    }

    public void setDirectorGroupId(String directorGroupId) {
        this.directorGroupId = directorGroupId;
    }

    private List<OrganizationMemberEntity> managerMembers;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPosition() {
        return managerPosition;
    }

    public void setManagerPosition(String managerPosition) {
        this.managerPosition = managerPosition;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public List<OrganizationMemberEntity> getManagerMembers() {
        return managerMembers;
    }

    public void setManagerMembers(List<OrganizationMemberEntity> managerMembers) {
        this.managerMembers = managerMembers;
    }
}
