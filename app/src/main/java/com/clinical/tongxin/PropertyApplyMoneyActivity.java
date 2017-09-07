package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class PropertyApplyMoneyActivity extends BaseActivity {
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
    @BindView(R.id.editTxt_Recharage)
    EditText editTxtRecharage;
    @BindView(R.id.editTxt_password)
    EditText editTxtPassword;
    @BindView(R.id.btn_ok)
    Button btnOk;
    private MyProgressDialog mDialog;
    private double sumM=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applymoney_recharger);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mDialog=new MyProgressDialog(this,"请稍等...");
        sumM=Double.parseDouble(Utils.getMyString(getIntent().getExtras().getString("summoney"),"0"));
        title.setText("提现");

    }


    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        initData();
    }


    //获取数据 充值
    private void initData(){
        if (Double.parseDouble(Utils.getMyString(editTxtRecharage.getText().toString(),"0"))==0){
            Toast.makeText(this,"输入金额不能等于0",Toast.LENGTH_SHORT).show();
            return;
        }

        if (Double.parseDouble(Utils.getMyString(editTxtRecharage.getText().toString(),"0"))>sumM){
            Toast.makeText(this,"输入金额不能大于总余额",Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxtRecharage.getText().toString().equals("")){
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxtPassword.getText().toString().equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Amount", editTxtRecharage.getText().toString());
        map.put("TradePassword", editTxtPassword.getText().toString());

        XUtil.Post(UrlUtils.URL_applyMoneyApp, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1= Utils.wsJsonToModel1(arg0);
                if (resultJsonP1.getCode().equals("200")){
                    Toast.makeText(PropertyApplyMoneyActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PropertyApplyMoneyActivity.this,PropertyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(PropertyApplyMoneyActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }
}
