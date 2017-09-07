package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class QueriesEntity implements Serializable{
    private String qId;
    private String title;
    private String isAnswer;
    private String date;
    private String voiceId;
    private String doctorName;
    private String voiceUrl;
    private String voiceSecond;
    private String isActive;
    private String sort_id;
    private String doctorDetails;
    private String doctorUrl;

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getVoiceSecond() {
        return voiceSecond;
    }

    public void setVoiceSecond(String voiceSecond) {
        this.voiceSecond = voiceSecond;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getDoctorDetails() {
        return Utils.getMyString(doctorDetails, "");
    }

    public void setDoctorDetails(String doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public String getDoctorUrl() {
        return Utils.getMyString(doctorUrl, "");
    }

    public void setDoctorUrl(String doctorUrl) {
        this.doctorUrl = doctorUrl;
    }
}
