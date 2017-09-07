package com.clinical.tongxin;

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

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 * Created by Administrator on 2017/1/15 0015.
 */

public class PasswordReciveActivity extends BaseActivity{
    // 进度
    private MyProgressDialog mDialog;
    private TextView title;
    private EditText edtTxt_oldPwd,edtTxt_newPwd,edtTxt_Pwd2;
    private Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdrecive);
        initView();
        initlistener();
    }

    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        title= (TextView) findViewById(R.id.title);
        title.setText("修改密码");
        title.setVisibility(View.GONE);
        edtTxt_oldPwd= (EditText) findViewById(R.id.edtTxt_oldPwd);
        edtTxt_newPwd= (EditText) findViewById(R.id.edtTxt_newPwd);
        edtTxt_Pwd2= (EditText) findViewById(R.id.edtTxt_Pwd2);
        btn_ok= (Button) findViewById(R.id.btn_ok);
    }

    private void initlistener() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setpasssord();
            }
        });
    }
    //修改密码
    private void setpasssord(){
        if (edtTxt_oldPwd.getText().toString().equals("")){
            Toast.makeText(this,"请输入原密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtTxt_newPwd.getText().toString().equals("")){
            Toast.makeText(PasswordReciveActivity.this, "请输入新密码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String isHan="^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$";
        if (!edtTxt_newPwd.getText().toString().matches(isHan)){
            Toast.makeText(PasswordReciveActivity.this, "密码至少包含字母、数字和符号中的两种", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_newPwd.getText().toString().length()<6||edtTxt_newPwd.getText().toString().length()>20){
            Toast.makeText(PasswordReciveActivity.this, "密码长度为6-20位", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!edtTxt_newPwd.getText().toString().equals(edtTxt_Pwd2.getText().toString())) {
            Toast.makeText(PasswordReciveActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("OldPassword",edtTxt_oldPwd.getText().toString());
        map.put("NewPassword",edtTxt_Pwd2.getText().toString());
        XUtil.Post(UrlUtils.URL_AppUpdateLoginPwd,map,new MyCallBack<String>(){
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
                    Toast.makeText(PasswordReciveActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                mDialog.dismiss();
            }
        });
    }
}
