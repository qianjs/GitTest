package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * 用户实体
 * Created by linchao on 2016/12/27.
 */

public class KeywordEntity implements Serializable{
    private String field;
    private String op;
    private String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }


}
