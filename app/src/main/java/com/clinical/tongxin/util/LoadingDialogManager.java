package com.clinical.tongxin.util;

import android.content.Context;

import com.clinical.tongxin.myview.MyProgressDialog;

/**
 * Created by qjs on 2017/8/22.
 */

public class LoadingDialogManager {
    private static LoadingDialogManager mInstance = null;
    // 进度
    private static MyProgressDialog mDialog;

    private LoadingDialogManager(){
    }

    public static synchronized LoadingDialogManager getInstance(){
        if(mInstance == null){
            synchronized (LoadingDialogManager.class){
                if(mInstance == null){
                    mInstance = new LoadingDialogManager();
                }
            }
        }
        return mInstance;
    }

    public static void show(Context context){
        if(mDialog == null){
            // 加载中
            mDialog = new MyProgressDialog(context, "请稍后...");
            mDialog.show();
        }
    }

    public static void dismiss(){
        if(mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
