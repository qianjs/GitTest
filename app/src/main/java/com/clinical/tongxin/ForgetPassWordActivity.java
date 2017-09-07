package com.clinical.tongxin;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * 找回密码
 * Created by Administrator on 2016/12/13 0013.
 */

public class ForgetPassWordActivity extends BaseActivity implements View.OnClickListener{
    private int recLen = 30;
    private Button btn_getcode,btn_ok;
    private EditText edtTxt_phone,edtTxt_code,edtTxt_pwd,edtTxt_pwd2,edtTxt_RealName,edtTxt_IDNumber;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        dialog=new MyProgressDialog(this,"请稍等...");
        setContentView(R.layout.activity_forget_password);
        initView();
        initListener();
    }
    private void initView()
    {
        TextView title=(TextView)findViewById(R.id.title);
        btn_getcode=(Button)findViewById(R.id.btn_getcode);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        edtTxt_phone=(EditText)findViewById(R.id.edtTxt_phone);
        edtTxt_code=(EditText)findViewById(R.id.edtTxt_code);
        edtTxt_pwd=(EditText)findViewById(R.id.edtTxt_pwd);
        edtTxt_pwd2=(EditText)findViewById(R.id.edtTxt_pwd2);
        edtTxt_RealName= (EditText) findViewById(R.id.edtTxt_RealName);
        edtTxt_IDNumber= (EditText) findViewById(R.id.edtTxt_IDNumber);
    }
    private void initListener()
    {
        btn_getcode.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        edtTxt_phone.addTextChangedListener(new TextWatcher() {
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
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_getcode:
            {
                sendSMS();
            }
            break;
            case R.id.btn_ok:
            {
                getForgetPassword();
            }
            break;
        }
    }
    //获取验证码
    private void sendSMS()
    {
        if (edtTxt_phone.getText().toString().equals("")) {
            Toast.makeText(ForgetPassWordActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_phone.getText().toString().length()!=11) {
            Toast.makeText(ForgetPassWordActivity.this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        btn_getcode.setEnabled(false);
        recLen = 30;
        handler.postDelayed(runnable, 1000);
        Map<String,String> map=new HashMap<String,String>();
        map.put("Mobile",edtTxt_phone.getText().toString());
        XUtil.Post(UrlUtils.URL_ShowSendMessageCodeByFindpassword, map, new MyCallBack<String>() {
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
    //找回密码
    private void getForgetPassword()
    {
        if (edtTxt_phone.getText().toString().equals("")||edtTxt_phone.getText().toString().equals("输入您的手机号")) {
            Toast.makeText(ForgetPassWordActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_phone.getText().toString().length()!=11) {
            Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_code.getText().toString().equals("")) {
            Toast.makeText(ForgetPassWordActivity.this, "请输入验证码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }if (edtTxt_pwd.getText().toString().equals("")) {
        Toast.makeText(ForgetPassWordActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                .show();
        return;
    }
        String isHan="^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$";
        if (!edtTxt_pwd.getText().toString().matches(isHan)){
            Toast.makeText(ForgetPassWordActivity.this, "密码至少包含字母、数字和符号中的两种", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (edtTxt_pwd.getText().toString().length()<6||edtTxt_pwd.getText().toString().length()>20){
            Toast.makeText(ForgetPassWordActivity.this, "密码长度为6-20位", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (!edtTxt_pwd.getText().toString().equals(edtTxt_pwd2.getText().toString())) {
            Toast.makeText(ForgetPassWordActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT)
                    .show();
            return;
        }


        dialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("ValidateCode",edtTxt_code.getText().toString());
        map.put("Mobile",edtTxt_phone.getText().toString());
//        map.put("RealName",edtTxt_RealName.getText().toString());
//        map.put("IDNumber",edtTxt_IDNumber.getText().toString());
        map.put("LoginPassword",edtTxt_pwd.getText().toString());
        XUtil.Post(UrlUtils.URL_ShowFindpasswordApp, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, "手机号或验证码不正确", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")) {
                        dialog.dismiss();
                        Toast.makeText(ForgetPassWordActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ForgetPassWordActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }

        });
    }
}
