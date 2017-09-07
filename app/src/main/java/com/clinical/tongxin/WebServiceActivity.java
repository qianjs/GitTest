package com.clinical.tongxin;

import android.os.Bundle;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class WebServiceActivity extends BaseWebActivity {
    private String url="https://www.baidu.com/";
    private String ContentName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getIntent().getExtras().getString("url","");
        ContentName=getIntent().getExtras().getString("ContentName","");
        getTitle1().setText(ContentName);
        initData(url);

    }
}
