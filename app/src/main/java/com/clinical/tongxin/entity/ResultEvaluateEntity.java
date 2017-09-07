package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class ResultEvaluateEntity<T> {

    private int code;
    private String msg;
    private T result;
    @SerializedName("BeingEvaluated")
    private String beingEvaluated;

    public String getBeingEvaluated() {
        return beingEvaluated;
    }

    public void setBeingEvaluated(String beingEvaluated) {
        this.beingEvaluated = beingEvaluated;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
