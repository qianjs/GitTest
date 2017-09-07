package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class ResultJsonAptitude {

    private int code;
    private String msg;
    private List<AptitudeEntity> result;
//    private String r;
//    private ResultJsonC i;

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

    public List<AptitudeEntity> getResult() {
        return result;
    }

    public void setResult(List<AptitudeEntity> result) {
        this.result = result;
    }
}
