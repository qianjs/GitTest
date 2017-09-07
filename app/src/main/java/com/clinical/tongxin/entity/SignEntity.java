package com.clinical.tongxin.entity;


import com.clinical.tongxin.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class SignEntity implements Serializable{
    @SerializedName("CustomerId")
    private String id;
    @SerializedName("NickName")
    private String Name;
    private String SortId;
    private String Mobile;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSortId() {
        return SortId;
    }

    public void setSortId(String sortId) {
        SortId = sortId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Utils.getMyString(Name, "");
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Utils.getMyString(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }
}
