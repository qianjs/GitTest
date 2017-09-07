package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * 用户实体
 * Created by linchao on 2016/12/27.
 */

public class ManagerMemberEntity implements Serializable{

    private String CustomerId;
    private String NickName;
    private String Mobile;
    private String PermissionName;
    private String sortId;
    private boolean isCheck;
    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPermissionName() {
        return PermissionName;
    }

    public void setPermissionName(String permissionName) {
        PermissionName = permissionName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }


}
