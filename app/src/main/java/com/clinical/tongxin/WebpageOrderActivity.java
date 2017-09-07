package com.clinical.tongxin;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;

/**
 * Created by lzj667 on 2016/9/2.
 */
public class WebpageOrderActivity  extends BaseActivity{
    private TextView title;
    private WebView web_order;
    private String url="";
    private int isshow=0;
    private MyProgressDialog dialog;
    private String eId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpageorder);
        dialog=new MyProgressDialog(this,"请稍后...");
        initView();
        initlistener();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("下单");
        web_order= (WebView) findViewById(R.id.web_order);
        eId=getIntent().getStringExtra("eId");
        url= UrlUtils.BASE_URL+"WebPage/ProjectOrder.aspx?CustomerId="+getLoginUserSharedPre().getString("userId","")+"&DoctorId="+eId;
        web_order.getSettings().setJavaScriptEnabled(true);
        web_order.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("file:///android_asset/error.html");
            }
        });
        web_order.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    dialog.dismiss();
                } else {
                    if (isshow == 0) {
                        dialog.show();
                        isshow++;
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        WebSettings webSettings = web_order.getSettings();

        webSettings.setDomStorageEnabled(true);
        web_order.loadUrl(url);

    }


    private void initlistener() {

    }
}
