package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by apple on 2016/12/11.
 */

public class ProvinceEntity {
    private String ProvinceName;
    private String ProvinceCode;
    private List<CityEntity> citys;

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        ProvinceCode = provinceCode;
    }

    public List<CityEntity> getCitys() {
        return citys;
    }

    public void setCitys(List<CityEntity> citys) {
        this.citys = citys;
    }
}
