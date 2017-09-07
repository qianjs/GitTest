package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class ResultEntity<T> {

    private int code;
    private String msg;
    private T result;


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
