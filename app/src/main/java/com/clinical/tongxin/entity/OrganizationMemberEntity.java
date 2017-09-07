package com.clinical.tongxin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构成员实体类
 * Created by linchao on 2017/3/26
 */

public class OrganizationMemberEntity implements Serializable{
    private String name;
    private String phone;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
