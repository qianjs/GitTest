package com.clinical.tongxin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceTextActivity extends Activity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.txt_content)
    TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_text);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        try {
            String content = getIntent().getStringExtra("content");
            String contentName = getIntent().getStringExtra("contentName");
            title.setText(contentName);
            txtContent.setText(Html.fromHtml(Utils.getMyString(content,"")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
