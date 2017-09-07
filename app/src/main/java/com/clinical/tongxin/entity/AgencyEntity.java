package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class AgencyEntity {
    private String aId;
    private String name;
    private String num;
    private String url;
    private String contact;
    private String phone;
    private String bset;
    private String sort_id;
    private String type;
    private List<DoctorRecommendEntity> recommendList;
    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSort_id() {
        return sort_id;
    }

    public String getBset() {
        return bset;
    }

    public void setBset(String bset) {
        this.bset = bset;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DoctorRecommendEntity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<DoctorRecommendEntity> recommendList) {
        this.recommendList = recommendList;
    }
}
