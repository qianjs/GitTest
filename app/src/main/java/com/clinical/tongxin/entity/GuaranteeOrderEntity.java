package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class GuaranteeOrderEntity implements Serializable{
    private String gId;
    private String nationsName;
    private String project;
    private String doctor;
    private String doctorPhone;
    private String agency;
    private String agencyPerson;
    private String agencyPhone;
    private String price;
    private String userName;
    @SerializedName("date")
    private String date;
    private String sort_id;
    private String state;
    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getNationsName() {
        return nationsName;
    }

    public void setNationsName(String nationsName) {
        this.nationsName = nationsName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAgencyPerson() {
        return agencyPerson;
    }

    public void setAgencyPerson(String agencyPerson) {
        this.agencyPerson = agencyPerson;
    }

    public String getAgencyPhone() {
        return agencyPhone;
    }

    public void setAgencyPhone(String agencyPhone) {
        this.agencyPhone = agencyPhone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
