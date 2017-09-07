package com.clinical.tongxin.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;

/**
 * @author linchao
 * @commentary 高德地图工具类
 * @createdate 2017/1/3
 */
public class GpsUtils implements AMapLocationListener {
    private static GpsUtils gaodeMapUtil;
    private LocationListener mLocationListener;
    private Context context;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    public static GpsUtils getInstance(Context context,LocationListener listener){
        if(gaodeMapUtil==null){
            gaodeMapUtil= new GpsUtils(context,listener);
        }
        return gaodeMapUtil;

    }
    public GpsUtils(Context context, LocationListener listener){
        this.context=context;
        mLocationListener = listener;
    }

    public static GpsUtils getInstance(Context context){
        if(gaodeMapUtil==null){
            gaodeMapUtil= new GpsUtils(context);
        }
        return gaodeMapUtil;

    }
    public GpsUtils(Context context){
        this.context = context;
    }
    /**
     * 初始化定位
     */
    public void startLocation() {
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        //关闭单次定位监听
        locationOption.setOnceLocation(false);
        locationOption.setGpsFirst(false);
        locationOption.setInterval(5000);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc&&loc.getErrorCode() == 0) {
            if(loc.getLocationType()!= AMapLocation.LOCATION_TYPE_FIX_CACHE || loc.getLocationType()!= AMapLocation.LOCATION_TYPE_OFFLINE){
                if(loc.getLongitude()!=0 && loc.getLatitude()!=0){

                    mLocationListener.retBackLocation(loc);
                    removeRegist();
                }
            }else {
                locationClient.startLocation();
            }
        }else{
            mLocationListener.fail();
        }
    }

    public void removeRegist(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    public interface LocationListener{
        void retBackLocation(AMapLocation loc);
        void fail();
    }

    /**
     * 地图应用是否安装
     * @return
     */
    public boolean isGdMapInstalled(){
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.autonavi.minimap", 0);
        }catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
        }else{
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 获取打开高德地图应用uri
     */
    private  String getGdMapUri(String appName, String slat, String slon, String sname, String dlat, String dlon, String dname){
        String uri = "androidamap://route?sourceApplication=%1$s&slat=%2$s&slon=%3$s&sname=%4$s&dlat=%5$s&dlon=%6$s&dname=%7$s&dev=0&m=0&t=2";
        return String.format(uri, appName, slat, slon, sname, dlat, dlon, dname);
    }

    /**
     * 打开高德地图
     */
    public void openGaoDeMap(double slat, double slon, String sname, double dlat, double dlon, String dname) {
        if(isGdMapInstalled()){
            try {
                String uri = getGdMapUri("我有任务", String.valueOf(slat), String.valueOf(slon),sname, String.valueOf(dlat), String.valueOf(dlon), dname);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.autonavi.minimap");
                intent.setData(Uri.parse(uri));
                context.startActivity(intent); //启动调用

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(context,"请安装高德地图",Toast.LENGTH_SHORT).show();
        }
    }

}