package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by linchao on 2017/1/22.
 */

public class ArbitrationEntity implements Serializable {
    @SerializedName("Maker")
    private String maker;
    @SerializedName("MakeTime")
    private String makeTime;
    @SerializedName("MakeReason")
    private String makeReason;
    @SerializedName("Disposer")
    private String disposer;
    @SerializedName("DisposeTime")
    private String disposeTime;
    @SerializedName("DisposeReason")
    private String disposeReason;
    @SerializedName("Qestion")
    private String qestion;

    public String getQestion() {
        return qestion;
    }

    public void setQestion(String qestion) {
        this.qestion = qestion;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getMakeReason() {
        return makeReason;
    }

    public void setMakeReason(String makeReason) {
        this.makeReason = makeReason;
    }

    public String getDisposer() {
        return disposer;
    }

    public void setDisposer(String disposer) {
        this.disposer = disposer;
    }

    public String getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(String disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getDisposeReason() {
        return disposeReason;
    }

    public void setDisposeReason(String disposeReason) {
        this.disposeReason = disposeReason;
    }

}
