package com.clinical.tongxin.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
@Table(name="HXMyFriend")
public class HXMyFriend {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name="nickname")
    private String nickname;
    @Column(name="headurl")
    private String headurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
