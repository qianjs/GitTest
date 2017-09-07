package com.clinical.tongxin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 我的积分
 * Created by Administrator on 2017/1/15 0015.
 */

public class MyIntegralActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private TextView txt_integralNum,txt_integralRecord,txt_integralRule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intergral);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("我的积分");
        txt_integralNum= (TextView) findViewById(R.id.txt_integralNum);
        txt_integralRecord= (TextView) findViewById(R.id.txt_integralRecord);
        txt_integralRule= (TextView) findViewById(R.id.txt_integralRule);
    }
    private void initListener(){
        txt_integralNum.setOnClickListener(this);
        txt_integralRecord.setOnClickListener(this);
        txt_integralRule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_integralNum:
            {

            }
            break;
            case R.id.txt_integralRecord:
            {

            }
            break;
            case R.id.txt_integralRule:
            {

            }
            break;

        }
    }
}
