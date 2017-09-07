package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private TextView txt_property,txt_Recharge,txt_Withdrawals;
    private String property="";
    private String role;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        initListener();
    }

    private void initView() {
        property=getIntent().getExtras().getString("property");

        title= (TextView) findViewById(R.id.title);
        title.setText("资产管理");
        txt_property= (TextView) findViewById(R.id.txt_property);
        txt_property.setText(property);
        txt_Recharge= (TextView) findViewById(R.id.txt_Recharge);
        txt_Withdrawals= (TextView) findViewById(R.id.txt_Withdrawals);
        view=findViewById(R.id.view);

    }

    @Override
    protected void onResume() {
        role=getLoginConfig().getRole();
        if (role.equals("接包方")){
            txt_Withdrawals.setVisibility(View.VISIBLE);
        }else {
            txt_Withdrawals.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private void initListener() {
        txt_Recharge.setOnClickListener(this);
        txt_Withdrawals.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_Recharge:
            {
                Intent intent=new Intent(this,PropertyRechargerActivity.class);
                intent.putExtra("summoney",property);
                intent.putExtra("type",0);
                startActivity(intent);
            }
            break;
            case R.id.txt_Withdrawals:
            {
                Intent intent=new Intent(this,PropertyApplyMoneyActivity.class);
                intent.putExtra("summoney",property);
                startActivity(intent);
            }
            break;
        }
    }
}
