package com.clinical.tongxin.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/2/19 0019.
 */
@Table(name="MessageEntity")
public class MessageEntity {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "groupName")
    private String groupName; // 群组名
    @Column(name="fromName")
    private String fromName; // 发送人
    @Column(name="type")
    private String type; // 同意 1 忽略0 已加入其它团队3
    @Column(name="groupId")
    private String groupId; // 群组id
    @Column(name="uid")
    private String uid; // 登录人id
    @Column(name="msgId")
    private String msgId; // 消息id
    @Column(name = "groupType")
    private String groupType;//1 总监 2项目经理 3接包团队

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
