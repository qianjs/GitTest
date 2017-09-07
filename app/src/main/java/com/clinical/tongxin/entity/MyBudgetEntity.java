package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MyBudgetEntity implements Serializable {
    private double ing;
    private double use;
    private double unuse;

    public double getIng() {
        return ing;
    }

    public void setIng(double ing) {
        this.ing = ing;
    }

    public double getUse() {
        return use;
    }

    public void setUse(double use) {
        this.use = use;
    }

    public double getUnuse() {
        return unuse;
    }

    public void setUnuse(double unuse) {
        this.unuse = unuse;
    }
}
