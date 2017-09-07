package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 订阅城市
 * Created by linchao on 2016/12/11.
 */
public class SubscribeCityEntity implements Serializable{

    private String CityName;
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
