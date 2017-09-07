package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 团队管理实体
 * @author LINCHAO
 * 2016/12/26
 */

public class TeamEntity implements Serializable{
    @SerializedName("DirectorGroupId")
    private String directorGroupId; // 团队编号
    @SerializedName("Name")
    private String name;    // 团队名字
    @SerializedName("ProjectManagerCustomerName")
    private String projectManagerCustomerName; //项目经理名称
    @SerializedName("Finish")
    private String finish; // 已完成数量
    @SerializedName("ComplaitCnt")
    private String complaitCnt;// 投诉数量
    @SerializedName("Aptitude")
    private List<AptitudeEntity> aptitudes; // 资质
    @SerializedName("Remark")
    private String remark; // 备注
    private String sortId; // 分页标识

    public String getDirectorGroupId() {
        return directorGroupId;
    }

    public void setDirectorGroupId(String directorGroupId) {
        this.directorGroupId = directorGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectManagerCustomerName() {
        return projectManagerCustomerName;
    }

    public void setProjectManagerCustomerName(String projectManagerCustomerName) {
        this.projectManagerCustomerName = projectManagerCustomerName;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getComplaitCnt() {
        return complaitCnt;
    }

    public void setComplaitCnt(String complaitCnt) {
        this.complaitCnt = complaitCnt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public List<AptitudeEntity> getAptitudes() {
        return aptitudes;
    }

    public void setAptitudes(List<AptitudeEntity> aptitudes) {
        this.aptitudes = aptitudes;
    }
}
