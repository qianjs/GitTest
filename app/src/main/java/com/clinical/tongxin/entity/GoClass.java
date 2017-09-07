package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class GoClass implements Serializable{
    private Class<?> goToClass;

    public Class<?> getGoToClass() {
        return goToClass;
    }

    public void setGoToClass(Class<?> goToClass) {
        this.goToClass = goToClass;
    }
}
