package com.clinical.tongxin.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.clinical.tongxin.MyApplication;

/**
 * Created by linchao on 2017/1/19.
 */

public class SPUtils {
    public static final String SP_NAME = "woyourenwu";
    /**
     * 保存对象
     *
     * @param key 键
     * @param obj 值
     */
    public static void putObject(String key, Object obj) {
            SharedPreferences sp = MyApplication.applicationContext.getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        }
        editor.commit();
    }


    /**
     * 读取对象
     *
     * @param key        键
     * @param defaultObj 默认值
     * @param <T>        返回值类型
     * @return
     */
    public static <T> T getObject(String key, T defaultObj) {
        SharedPreferences sp = MyApplication.applicationContext.getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);

        if (defaultObj instanceof String) {
            return (T) sp.getString(key, (String) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return (T) (Integer) sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Boolean) {
            return (T) (Boolean) sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return (T) (Float) sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Long) {
            return (T) (Long) sp.getLong(key, (Long) defaultObj);
        }
        return null;
    }

    /**
     *
     * @param key
     */
    public static  void remove(String key) {
        SharedPreferences sp = MyApplication.applicationContext.getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear() {
        SharedPreferences sp = MyApplication.applicationContext.getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
