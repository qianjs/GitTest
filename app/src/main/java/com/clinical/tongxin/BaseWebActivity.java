package com.clinical.tongxin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseWebActivity extends BaseActivity {
    public static final String FLAG_FROM_WEB_ACTIVITY = "web_activity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.app_web)
    WebView mWebView;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;

    public WebView getmWebView() {
        return mWebView;
    }

    @BindView(R.id.layout_main)
    LinearLayout mLayoutMain;
    private String mHomePage;

    public TextView getTitle1() {
        return title;
    }

    protected ValueCallback mValueCallback;
    private ProgressDialog mProgressDialog;

    public String getHoemPage() {
        return mHomePage;
    }

    public void setHomePage(String homePage) {
        mHomePage = homePage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
//        title.setText("商品详情");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        /**清除cookie*/
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mWebView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncManager.sync();
        /**清除cookie--end*/
//        mWebView.addJavascriptInterface(new JSHook(),"android");

    }
//    public class JSHook{
//            @JavascriptInterface
//            public void startFunction(){
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(BaseWebActivity.this,"show",Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//
//            @JavascriptInterface
//            public void startFunction(final String text){
//
//                Intent intent=new Intent(BaseWebActivity.this, ShopDetailsActivity.class);
//                intent.putExtra("url",text+"");
//                startActivityForResult(intent,1001);
//
//
//            }
//        @JavascriptInterface
//        public void startShoppingCart(final String text){
//            Intent intent=new Intent(BaseWebActivity.this, MainActivity.class);
//            intent.putExtra("id",2);
//            startActivity(intent);
//                       finish();
//
//
//        }
//    }


    protected void initData(String homePage) {

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress(true, "加载中，请稍后...");
                //想在页面开始加载时有操作，在这添加
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgressDialog != null){
                    mProgressDialog.dismiss();
                }

                //想在页面加载结束时有操作，在这添加
//                Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候WebView打开，为false则系统浏览器或第三方浏览器打开。如果要下载页面中的游戏或者继续点击网页中的链接进入下一个网页的话，重写此方法下，不然就会跳到手机自带的浏览器了，而不继续在你这个webview里面展现了
                if (!TextUtils.isEmpty(url)) {

                    return true;
                }

                return false;
            }

//            @Override
//            public void onLoadResource(WebView view, String url) {
//                Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();
//                super.onLoadResource(view, url);
//            }

            @Override

            public void onReceivedError(WebView view, int errorCode,

                                        String description, String failingUrl) {

                //想在收到错误信息的时候，执行一些操作，走此方法

            }
        });


        mWebView.setWebChromeClient(new JsWebChromeClient());
        mHomePage = homePage;
        mWebView.loadUrl(mHomePage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            /**清除cookie*/
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mWebView.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();
            cookieSyncManager.sync();
            /**清除cookie--end*/
            mWebView.clearCache(true);
            //释放WebView，避免内存泄露
            try {
                mLayoutMain.removeAllViews();
                mWebView.removeAllViews();
                mWebView.clearHistory();
                mWebView.destroy();
            } catch (Exception e) {

            }
            mWebView = null;
        }
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public class JsWebChromeClient extends WebChromeClient {

    }


    @Override
    public void onPause() {
        super.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.resumeTimers();
    }

    public void back(View view) {
        finish();
    }

    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }


}
