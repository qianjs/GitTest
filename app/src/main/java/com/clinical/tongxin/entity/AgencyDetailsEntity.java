package com.clinical.tongxin.entity;

import java.util.List;

/**
 * 机构详情实体类
 * Created by 马骥 on 2016/10/2 0002.
 */
public class AgencyDetailsEntity {
    private String aId;
    private String url;
    private String name;
    private String address;
    private String star;
    private List<DoctorRecommendEntity> recommendList;
    private String projectNumber;
    private List<ProjectEntity> projectList;
    private List<ImageURLEntity> picList;
    private String details;
    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public List<DoctorRecommendEntity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<DoctorRecommendEntity> recommendList) {
        this.recommendList = recommendList;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public List<ProjectEntity> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectEntity> projectList) {
        this.projectList = projectList;
    }

    public List<ImageURLEntity> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageURLEntity> picList) {
        this.picList = picList;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
