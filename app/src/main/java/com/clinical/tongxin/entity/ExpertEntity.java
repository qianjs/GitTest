package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class ExpertEntity {
    private String eId;
    private String name;
    private String title;
    private String address;
    private String best;
    private String degree;
    private String isAdvice;
    private String url;
    private String urlDetails;
    private String star;
    private int sort_id;
    private String hxAccount;
    private List<DoctorRecommendEntity> recommendList;

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }

    public String getIsAdvice() {
        return isAdvice;
    }

    public void setIsAdvice(String isAdvice) {
        this.isAdvice = isAdvice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort_id() {
        return sort_id;
    }

    public void setSort_id(int sort_id) {
        this.sort_id = sort_id;
    }

    public String getUrlDetails() {
        return urlDetails;
    }

    public void setUrlDetails(String urlDetails) {
        this.urlDetails = urlDetails;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getHxAccount() {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount) {
        this.hxAccount = hxAccount;
    }

    public List<DoctorRecommendEntity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<DoctorRecommendEntity> recommendList) {
        this.recommendList = recommendList;
    }
}
