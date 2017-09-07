package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/10/2 0002.
 */
public class CaseEntity {
    private String cId;
    private String dId;
    private String dName;
    private String dPic;
    private String cBeforePic;
    private String cAfterPic;
    private String cDetails;
    private String cViewNumber;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdPic() {
        return dPic;
    }

    public void setdPic(String dPic) {
        this.dPic = dPic;
    }

    public String getcBeforePic() {
        return cBeforePic;
    }

    public void setcBeforePic(String cBeforePic) {
        this.cBeforePic = cBeforePic;
    }

    public String getcAfterPic() {
        return cAfterPic;
    }

    public void setcAfterPic(String cAfterPic) {
        this.cAfterPic = cAfterPic;
    }

    public String getcDetails() {
        return cDetails;
    }

    public void setcDetails(String cDetails) {
        this.cDetails = cDetails;
    }

    public String getcViewNumber() {
        return cViewNumber;
    }

    public void setcViewNumber(String cViewNumber) {
        this.cViewNumber = cViewNumber;
    }
}
