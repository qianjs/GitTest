package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/10/2 0002.
 */
public class ProjectEntity {
    private String pId;
    private String title;
    private String subTitle;
    private String number;
    private String price;
    private String url;
    private String hospitalPrice;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHospitalPrice() {
        return hospitalPrice;
    }

    public void setHospitalPrice(String hospitalPrice) {
        this.hospitalPrice = hospitalPrice;
    }
}
