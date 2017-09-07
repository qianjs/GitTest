package com.clinical.tongxin;

import android.os.Bundle;
import android.text.TextUtils;

import com.clinical.tongxin.util.UrlUtils;

import static com.renn.rennsdk.http.HttpRequest.append;

/**
 * 我的简历
 * Created by linchao on 2017/7/11 0011.
 */

public class ResumeActivity extends BaseWebActivity {
    private String url="resume/showResume.html";
    private StringBuffer sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitle1().setText("我的简历");
        String uid = getIntent().getStringExtra("uid");
        sb = new StringBuffer(UrlUtils.BASE_URL)
                .append(url).append("?Ukey=")
                .append(getLoginConfig().getUkey())
                .append("&Uid=")
                .append(TextUtils.isEmpty(uid)?getLoginConfig().getUserId():uid)
                .append("&flag=android");
        initData(sb.toString());
    }

    @Override
    protected void initView() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setUseWideViewPort(true);//设置webview推荐使用的窗口
        mWebView.getSettings().setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        mWebView.getSettings().setDisplayZoomControls(false);//隐藏webview缩放按钮
        mWebView.getSettings().setAllowFileAccess(true); // 允许访问文件
        mWebView.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮
        mWebView.getSettings().setSupportZoom(true); // 支持缩放
        super.initView();
    }
}
