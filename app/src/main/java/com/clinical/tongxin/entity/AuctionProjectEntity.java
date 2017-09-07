package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzj667 on 2016/8/29.
 * 项目拍卖列表
 */
public class AuctionProjectEntity implements Serializable{

    private String apId;

    private String name;

    private String price;

    private String fPrice;
    private String date;
    private String aName;
    private String aType;
    private String sort_id;
    private String dId;
    private List<ImageURLEntity> urlList;

    public List<ImageURLEntity> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<ImageURLEntity> urlList) {
        this.urlList = urlList;
    }

    public String getApId() {
        return Utils.getMyString(apId, "");
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getName() {
        return Utils.getMyString(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Utils.getMyString(price, "");
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getfPrice() {
        return Utils.getMyString(fPrice, "");
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }

    public String getDate() {
        return Utils.getMyString(date, "");
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getaName() {
        return Utils.getMyString(aName, "");
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaType() {
        return Utils.getMyString(aType, "");
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getSort_id() {
        return Utils.getMyString(sort_id, "");
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getdId() {
        return Utils.getMyString(dId, "");
    }

    public void setdId(String dId) {
        this.dId = dId;
    }
}
