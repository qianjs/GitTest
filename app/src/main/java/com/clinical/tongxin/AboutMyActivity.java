package com.clinical.tongxin;

import android.os.Bundle;
import android.widget.TextView;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class AboutMyActivity extends BaseActivity {
    private TextView title;
    private TextView txt_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_my);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("关于我们");
        txt_version= (TextView) findViewById(R.id.txt_version);
        txt_version.setText("版本号：v "+Utils.getAppVersionCode(this));
    }
}
