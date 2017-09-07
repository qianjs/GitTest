package com.clinical.tongxin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/2 0002.
 */
public class ProjectDetailEntity implements Serializable{
    private String pId;
    private String name;
    private String subTitle;
    private String price;
    private String unit;
    private String city;
    private String feeDetails;
    private String aPrice;
    private List<ImageURLEntity> urlList;
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFeeDetails() {
        return feeDetails;
    }

    public void setFeeDetails(String feeDetails) {
        this.feeDetails = feeDetails;
    }

    public String getaPrice() {
        return aPrice;
    }

    public void setaPrice(String aPrice) {
        this.aPrice = aPrice;
    }

    public List<ImageURLEntity> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<ImageURLEntity> urlList) {
        this.urlList = urlList;
    }
}

