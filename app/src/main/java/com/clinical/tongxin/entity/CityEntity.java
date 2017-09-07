package com.clinical.tongxin.entity;


import java.io.Serializable;

/**
 * Created by apple on 2016/12/11.
 */
public class CityEntity implements Serializable{
    private String CityName;
    private String CityCode;
    private String PinYin;
    public CityEntity(String CityName, String PinYin) {
        this.CityName = CityName;
        this.PinYin = PinYin;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getPinYin() {
        return PinYin;
    }

    public void setPinYin(String pinYin) {
        PinYin = pinYin;
    }
}
