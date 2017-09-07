package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 充值
 * Created by Administrator on 2017/1/3 0003.
 */

public class PropertyRechargerActivity extends BaseActivity implements View.OnClickListener{
    private MyProgressDialog mDialog;
    private TextView title;
    private TextView txt_Recharge;
    private EditText editTxt_Recharage;
    private Button btn_ok;
    private int type;
    private double sumM=0;
    private double max=100000000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_recharger);
        initView();
        initListener();

    }

    private void initView() {
        type=getIntent().getExtras().getInt("type");
        mDialog=new MyProgressDialog(this,"请稍等...");
        title= (TextView) findViewById(R.id.title);
        txt_Recharge= (TextView) findViewById(R.id.txt_Recharge);
        editTxt_Recharage= (EditText) findViewById(R.id.editTxt_Recharage);
        btn_ok= (Button) findViewById(R.id.btn_ok);
            title.setText("充值");
        sumM=Double.parseDouble(Utils.getMyString(getIntent().getExtras().getString("summoney"),"0"));
    }

    private void initListener() {
        btn_ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
            {if (type==0){
                initData();
            }else{

            }

            }
            break;
        }
    }

    //获取数据 充值
    private void initData(){
        if ((Double.parseDouble(Utils.getMyString(editTxt_Recharage.getText().toString(),"0"))+sumM)>max){
            Toast.makeText(this,"充值金额不合法，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(Utils.getMyString(editTxt_Recharage.getText().toString(),"0"))<=0){
            Toast.makeText(this,"充值金额不合法，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxt_Recharage.getText().toString().equals("")){
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Amount", editTxt_Recharage.getText().toString());
        String url="";
        if (getLoginConfig().getRole().equals("接包方")){
            url=UrlUtils.URL_rechargeApp;
        }else {
            url=UrlUtils.URL_apprecharge;
        }
        XUtil.Post(url, map, new MyCallBack<String>() {
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
                        Toast.makeText(PropertyRechargerActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PropertyRechargerActivity.this,PropertyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                        finish();
                }else{
                    Toast.makeText(PropertyRechargerActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    private void initData1(){

    }
}
