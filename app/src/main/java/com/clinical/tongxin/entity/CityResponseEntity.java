package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 2016/12/11.
 */
public class CityResponseEntity implements Serializable{
    @SerializedName("cityName")
    private String CityName;
    @SerializedName("cityId")
    private String CityCode;

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
}
