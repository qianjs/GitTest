package com.clinical.tongxin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 实名认证信息
 * Created by Administrator on 2016/12/14 0014.
 */

public class RealInformationActivity extends BaseActivity {
    private TextView title;
    //真实姓名， 身份证号， 身份证有效期， 银行名称，银行支行名称， 银行卡号
    private TextView txt_RealName,txt_IDNumber,txt_ValidityPeriod,txt_BankName,txt_BankBranchName,txt_CardNumber;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new MyProgressDialog(this,"请稍等...");
        setContentView(R.layout.activity_informaction);
        initView();
        findData();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("实名认证信息");
        txt_RealName= (TextView) findViewById(R.id.txt_RealName);
        txt_IDNumber= (TextView) findViewById(R.id.txt_IDNumber);
        txt_ValidityPeriod= (TextView) findViewById(R.id.txt_ValidityPeriod);
        txt_BankName= (TextView) findViewById(R.id.txt_BankName);
        txt_BankBranchName= (TextView) findViewById(R.id.txt_BankBranchName);
        txt_CardNumber= (TextView) findViewById(R.id.txt_CardNumber);
    }

    //获取实名认证信息
    private void findData(){
        dialog.show();
        Map<String,String> map=new HashMap<>();
        map.put("CustomerId",getLoginUserSharedPre().getString("userId",""));
        map.put("Ukey",getLoginUserSharedPre().getString("Ukey",""));
        XUtil.Post(UrlUtils.URL_GetrealCustomerMessage,map, new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(RealInformationActivity.this,arg0.toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject json=new JSONObject(arg0);
                    if (json.getString("code").equals("200")){
                        JSONObject jsonObject=new JSONObject(json.getString("result"));
                        txt_RealName.setText("真实姓名："+jsonObject.getString("RealName"));
                        txt_IDNumber.setText("身份证号："+jsonObject.getString("IDNumber"));
                        txt_BankBranchName.setText("银行支行名称："+jsonObject.getString("BankBranchName"));
                        txt_BankName.setText("银行名称："+jsonObject.getString("BankName"));
                        txt_CardNumber.setText("银行卡号："+jsonObject.getString("CardNumber"));
                        if (jsonObject.getString("IDIsPermanent").equals("0")){
                            txt_ValidityPeriod.setText("身份证有效期："+jsonObject.getString("ValidityPeriod"));
                        }else{
                        txt_ValidityPeriod.setText("身份证有效期：长期有效");
                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });
    }
}
