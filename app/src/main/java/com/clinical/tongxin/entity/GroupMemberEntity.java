package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class GroupMemberEntity {
    private String MemberName;
    private String MemberId;
    private String SortId;
    private String Mobile;
    private String HXNumber;
    private boolean unRead;

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getSortId() {
        return SortId;
    }

    public void setSortId(String sortId) {
        SortId = sortId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getHXNumber() {
        return HXNumber;
    }

    public void setHXNumber(String HXNumber) {
        this.HXNumber = HXNumber;
    }

    public boolean isUnRead() {
        return unRead;
    }

    public void setUnRead(boolean unRead) {
        this.unRead = unRead;
    }
}
