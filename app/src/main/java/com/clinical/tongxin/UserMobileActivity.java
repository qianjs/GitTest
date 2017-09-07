package com.clinical.tongxin;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by Administrator on 2017/1/15 0015.
 */

public class UserMobileActivity extends BaseActivity {
    // 进度
    private MyProgressDialog mDialog;
    private TextView title;
    private EditText editTxt_user_mobile,edtTxt_code;
    private Button btn_ok,btn_getcode;
    private int recLen = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermobile);
        initView();
        initListener();
    }

    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        title= (TextView) findViewById(R.id.title);
        title.setVisibility(View.GONE);
        editTxt_user_mobile= (EditText) findViewById(R.id.editTxt_user_mobile);
        edtTxt_code= (EditText) findViewById(R.id.edtTxt_code);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_getcode= (Button) findViewById(R.id.btn_getcode);

    }
    private void initListener(){
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserName();
            }
        });
        btn_getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
        editTxt_user_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    btn_getcode.setBackgroundResource(R.drawable.btn_red);
                    btn_getcode.setEnabled(true);
                }else {
                    btn_getcode.setBackgroundResource(R.drawable.btn_gray);
                    btn_getcode.setEnabled(false);
                }
            }
        });
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen == 1) {
                btn_getcode.setEnabled(true);
                btn_getcode.setText("验证码");
                handler.removeCallbacks(runnable);
            } else {
                recLen--;
                btn_getcode.setText(recLen + "秒");
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void sendSMS()
    {
        if (editTxt_user_mobile.getText().toString().equals("")) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (editTxt_user_mobile.getText().toString().length()!=11) {
            Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        btn_getcode.setEnabled(false);
        recLen = 30;
        handler.postDelayed(runnable, 1000);
        Map<String,String> map=new HashMap<String,String>();
        map.put("Mobile",editTxt_user_mobile.getText().toString());
//        map.put("state","3");
        XUtil.Post(UrlUtils.URL_AppSendCodeOfUpdateMobile, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
//                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
//                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.getString("code").equals("200")) {
                        Toast.makeText(context, "验证码已发送请留意你的短消息", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    private void setUserName(){
        if (editTxt_user_mobile.getText().toString().equals("")){
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtTxt_code.getText().toString().equals("")) {
            Toast.makeText(this, "请输验证码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (editTxt_user_mobile.getText().toString().length()!=11) {
            Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Mobile",editTxt_user_mobile.getText().toString());
        map.put("Code",edtTxt_code.getText().toString());
        XUtil.Post(UrlUtils.URL_AppUpdateMobile,map,new MyCallBack<String>(){
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
                    Toast.makeText(UserMobileActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                mDialog.dismiss();
            }
        });
    }
}
