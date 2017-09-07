package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class XYDataEntity {
    private List<String> categories;
    private List<Float> seriesData;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Float> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<Float> seriesData) {
        this.seriesData = seriesData;
    }
}
