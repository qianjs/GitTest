package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by linchao on 2017/1/5.
 */

public class TaskSiteItemEntity implements Serializable {

    @SerializedName("Address")
    private String address;	// 任务点站址
    @SerializedName("Name")
    private String name;	// 任务点站名
    @SerializedName("Longitude")
    private String longitude;	// 任务点经度
    @SerializedName("Latitude")
    private String latitude;	// 任务点纬度
    @SerializedName("Linkman")
    private String linkman;// 任务点联系人
    @SerializedName("LinkTelephone")
    private String linkTelephone;	// 任务点联系电话
    @SerializedName("ItemStatus")
    private String itemStatus;	// 任务点状态 0=发布 1=上报 2=验收 3=分配 4=验收不通过 5=被分配人接受

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkTelephone() {
        return linkTelephone;
    }

    public void setLinkTelephone(String linkTelephone) {
        this.linkTelephone = linkTelephone;
    }
}
