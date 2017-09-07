package com.clinical.tongxin.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
@Table(name="SearchRecord")
public class SearchRecord {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "scontent")
    private String scontent;
    @Column(name = "num")
    private String num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScontent() {
        return scontent;
    }

    public void setScontent(String scontent) {
        this.scontent = scontent;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
