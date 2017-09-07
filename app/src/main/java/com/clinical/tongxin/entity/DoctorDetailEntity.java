package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class DoctorDetailEntity {
    private String dId;
    private String url;
    private String name;
    private String title;
    private String agency;
    private String star;
    private List<DoctorRecommendEntity> recommendList;
    private String physicianLevel;
    private String occupationalClass;
    private String certificateCode;
    private String approvalAuthority;
    private String degree;
    private String details;
    private String projectNumber;
    private List<DoctorProjectEntity> projectList;
    private List<CaseEntity> caseList;
    private String bigPath;
    private String modbile;
    private String hxAccount;
    private String IsQuery;
    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
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

    public String getPhysicianLevel() {
        return physicianLevel;
    }

    public void setPhysicianLevel(String physicianLevel) {
        this.physicianLevel = physicianLevel;
    }

    public String getOccupationalClass() {
        return occupationalClass;
    }

    public void setOccupationalClass(String occupationalClass) {
        this.occupationalClass = occupationalClass;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getApprovalAuthority() {
        return approvalAuthority;
    }

    public void setApprovalAuthority(String approvalAuthority) {
        this.approvalAuthority = approvalAuthority;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public List<DoctorProjectEntity> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<DoctorProjectEntity> projectList) {
        this.projectList = projectList;
    }

    public String getBigPath() {
        return Utils.getMyString(bigPath, "");
    }

    public void setBigPath(String bigPath) {
        this.bigPath = bigPath;
    }

    public List<CaseEntity> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<CaseEntity> caseList) {
        this.caseList = caseList;
    }

    public String getModbile() {
        return modbile;
    }

    public void setModbile(String modbile) {
        this.modbile = modbile;
    }

    public String getHxAccount() {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount) {
        this.hxAccount = hxAccount;
    }

    public String getIsQuery() {
        return Utils.getMyString(IsQuery, "");
    }

    public void setIsQuery(String isQuery) {
        IsQuery = isQuery;
    }
}
