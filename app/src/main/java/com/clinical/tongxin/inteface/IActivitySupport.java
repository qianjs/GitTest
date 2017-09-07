package com.clinical.tongxin.inteface;

import android.content.SharedPreferences;

import com.clinical.tongxin.entity.UserEntity;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public interface IActivitySupport {


    /**
     * 显示Toast
     * */
    public abstract void showToast(String text, int longint);

    /**
     * 短时间显示Toast
     * */
    public abstract void showToast(String text);

    /**
     * 获取用户编号
     * @return
     */
    public abstract String getUserCode();

    /**
     *
     * 获取当前登录用户的SharedPreferences配置.
     *
     */
    public SharedPreferences getLoginUserSharedPre();

    /**
     *
     * 保存用户配置.
     *
     */
    public void saveLoginConfig(UserEntity user);

    /**
     *
     * 获取用户配置.
     *
     * @paramloginConfig
     * @author shimiso
     * @update 2012-7-6 上午9:59:49
     */
    public UserEntity getLoginConfig();



}
