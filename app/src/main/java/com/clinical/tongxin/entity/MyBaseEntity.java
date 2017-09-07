package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MyBaseEntity implements Serializable {
    private String id;
    private String text;
    private String isSelected;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
