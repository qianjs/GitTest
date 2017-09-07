package com.clinical.tongxin.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class GroupEntity implements Serializable{
    private String groupid;
    private String groupName;
    private List<GroupMemberEntity> memberList;
    private boolean unRead;
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<GroupMemberEntity> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GroupMemberEntity> memberList) {
        this.memberList = memberList;
    }
    public boolean isUnRead() {
        return unRead;
    }

    public void setUnRead(boolean unRead) {
        this.unRead = unRead;
    }
}
