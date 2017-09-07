package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public class SubscribeEntity {
    private List<CityEntity> areaReceiver;
    private List<AptitudeEntity> projectReceiver;

    public List<CityEntity> getAreaReceiver() {
        return areaReceiver;
    }

    public void setAreaReceiver(List<CityEntity> areaReceiver) {
        this.areaReceiver = areaReceiver;
    }

    public List<AptitudeEntity> getProjectReceiver() {
        return projectReceiver;
    }

    public void setProjectReceiver(List<AptitudeEntity> projectReceiver) {
        this.projectReceiver = projectReceiver;
    }
}
