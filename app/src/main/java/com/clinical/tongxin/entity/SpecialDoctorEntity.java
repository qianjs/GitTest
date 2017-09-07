package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class SpecialDoctorEntity {
    private String dId;
    private String dName;
    private String dTtile;
    private String dSubTitle;
    private String dAgency;
    private String Path;
    private String bigPath;
    private String best;
    private String isKorea;

    public String getdId() {
        return Utils.getMyString(dId, "");
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getdName() {
        return Utils.getMyString(dName, "");
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdTtile() {
        return Utils.getMyString(dTtile, "");
    }

    public void setdTtile(String dTtile) {
        this.dTtile = dTtile;
    }

    public String getdSubTitle() {
        return Utils.getMyString(dSubTitle, "");
    }

    public void setdSubTitle(String dSubTitle) {
        this.dSubTitle = dSubTitle;
    }

    public String getdAgency() {
        return Utils.getMyString(dAgency, "");
    }

    public void setdAgency(String dAgency) {
        this.dAgency = dAgency;
    }

    public String getPath() {
        return Utils.getMyString(Path, "");
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getBigPath() {
        return Utils.getMyString(bigPath, "");
    }

    public void setBigPath(String bigPath) {
        this.bigPath = bigPath;
    }

    public String getBest() {
        return Utils.getMyString(best, "");
    }

    public void setBest(String best) {
        this.best = best;
    }

    public String getIsKorea() {
        return Utils.getMyString(isKorea, "");
    }

    public void setIsKorea(String isKorea) {
        this.isKorea = isKorea;
    }
}
