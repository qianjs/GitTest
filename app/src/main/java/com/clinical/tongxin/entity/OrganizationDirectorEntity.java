package com.clinical.tongxin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构实体类
 * Created by linchao on 2017/3/26
 */

public class OrganizationDirectorEntity implements Serializable{
    private String directorName; // 总监名字
    private String directorPosition; // 总监职位
    private String directorPhone; // 总监电话
    private List<OrganizationManagerEntity> managers;

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorPosition() {
        return directorPosition;
    }

    public void setDirectorPosition(String directorPosition) {
        this.directorPosition = directorPosition;
    }

    public String getDirectorPhone() {
        return directorPhone;
    }

    public void setDirectorPhone(String directorPhone) {
        this.directorPhone = directorPhone;
    }

    public List<OrganizationManagerEntity> getManagers() {
        return managers;
    }

    public void setManagers(List<OrganizationManagerEntity> managers) {
        this.managers = managers;
    }
}
