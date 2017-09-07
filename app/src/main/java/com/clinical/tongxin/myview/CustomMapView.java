package com.clinical.tongxin.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * Created by Administrator on 2017/6/16.
 */

public class CustomMapView extends MapView {
    public CustomMapView(Context context) {
        super(context);
    }

    public CustomMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            //允许ScrollView截断点击事件，ScrollView可滑动
            getParent().requestDisallowInterceptTouchEvent(false);
        }else{
            //不允许ScrollView截断点击事件，点击事件由子View处理
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
