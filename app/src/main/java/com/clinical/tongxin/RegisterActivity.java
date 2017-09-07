package com.clinical.tongxin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TaskDetailsContractorEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.clinical.tongxin.R.id.map;

/**
 * 用户注册
 * Created by 马骥 on 2016/8/10 0010.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private int recLen = 30;
    private Button btn_getcode,btn_register;
    private EditText edtTxt_phone,edtTxt_code,edtTxt_pwd,edtTxt_pwd2,edtTxt_name;
    private String projects;
    private EditText et_promotion_num; // 推广人号码
    private TextView tv_promotion_name; // 推广人姓名
    private Button btn_verify_promotion; // 验证推广人
    private boolean verifyPromotion;
    private MyProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_register_new);
        projects = getIntent().getStringExtra("AptitudeList");
        initView();
        initListener();
    }
    private void initView()
    {
        mDialog = new MyProgressDialog(context, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        btn_getcode=(Button)findViewById(R.id.btn_getcode);
        btn_register=(Button)findViewById(R.id.btn_register);
        edtTxt_phone=(EditText)findViewById(R.id.edtTxt_phone);
        edtTxt_code=(EditText)findViewById(R.id.edtTxt_code);
        edtTxt_pwd=(EditText)findViewById(R.id.edtTxt_pwd);
        edtTxt_pwd2=(EditText)findViewById(R.id.edtTxt_pwd2);
        edtTxt_name= (EditText) findViewById(R.id.edtTxt_name);
        et_promotion_num= (EditText) findViewById(R.id.et_promotion_num);
        tv_promotion_name=(TextView)findViewById(R.id.tv_promotion_name);
        btn_verify_promotion=(Button)findViewById(R.id.btn_verify_promotion);
    }
    private void initListener()
    {
        btn_getcode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_verify_promotion.setOnClickListener(this);
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
        et_promotion_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verifyPromotion = false;
                tv_promotion_name.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                    btn_verify_promotion.setBackgroundResource(R.drawable.btn_red);
                    btn_verify_promotion.setEnabled(true);
                }else {
                    btn_verify_promotion.setBackgroundResource(R.drawable.btn_gray);
                    btn_verify_promotion.setEnabled(false);
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
            case R.id.btn_register:
            {
                    register();

            }
            break;
            case R.id.btn_verify_promotion:
                verifyPromotion(et_promotion_num.getText().toString());
            break;
        }
    }

    private void verifyPromotion(String promoteNum) {
        if (et_promotion_num.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入推广人手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (et_promotion_num.getText().toString().length()!=11) {
            Toast.makeText(RegisterActivity.this, "推广人手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("PromoteMobiel",promoteNum);
        XUtil.Get(UrlUtils.URL_queryPromoteMobielCheck, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                verifyPromotion = false;
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<Object>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        Map<String,String> map = (Map<String, String>) resultEntity.getResult();
                        tv_promotion_name.setText(map.get("currentName"));
                        verifyPromotion = true;
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        verifyPromotion = false;
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    verifyPromotion = false;
                    Toast.makeText(context, "推荐人验证失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });
    }

    private void sendSMS()
    {
        if (edtTxt_phone.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_phone.getText().toString().length()!=11) {
            Toast.makeText(RegisterActivity.this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        btn_getcode.setEnabled(false);
        recLen = 30;
        handler.postDelayed(runnable, 1000);
        Map<String,String> map=new HashMap<String,String>();
            map.put("Mobile",edtTxt_phone.getText().toString());
//        map.put("state","3");
            XUtil.Post(UrlUtils.URL_GetVerificationCode, map, new MyCallBack<String>() {


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
    private void register()
    {
        if (edtTxt_phone.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_phone.getText().toString().length()!=11) {
            Toast.makeText(RegisterActivity.this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_code.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_name.getText().toString().equals("")||edtTxt_name.getText().toString().equals("输入您的名称")) {
            Toast.makeText(RegisterActivity.this, "请输入名称", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_pwd.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String isHan="^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$";
        if (!edtTxt_pwd.getText().toString().matches(isHan)){
            Toast.makeText(RegisterActivity.this, "密码至少包含字母、数字和符号中的两种", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_pwd.getText().toString().length()<6||edtTxt_pwd.getText().toString().length()>20){
            Toast.makeText(RegisterActivity.this, "密码长度为6-20位", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!edtTxt_pwd.getText().toString().equals(edtTxt_pwd2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
//        if (!verifyPromotion){
//            Toast.makeText(RegisterActivity.this, "请先验证推荐人", Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }


        Map<String,String> map=new HashMap<String,String>();
        map.put("ValidateCode",edtTxt_code.getText().toString());
        map.put("Mobile",edtTxt_phone.getText().toString());
        map.put("NickName",edtTxt_name.getText().toString());
        map.put("LoginPassword",edtTxt_pwd.getText().toString());
        map.put("DeviceCode",Utils.getPhoneIMEI());
        map.put("AptitudeList",projects);
        map.put("PromoteMobiel",et_promotion_num.getText().toString());
        XUtil.Post(UrlUtils.URL_Create, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, "手机号或验证码不正确", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")){
                        PopWindowAlertClose popwindow=new PopWindowAlertClose(RegisterActivity.this);
                        popwindow.setText("注册成功");
                        popwindow.setButtonText("立即登录");
                        popwindow.setOnAlertClickListener(new PopWindowAlertClose.OnAlertClickListener() {
                            @Override
                            public void onClickConfirm(String paramString) {
                                RegisterActivity.this.finish();
                            }
                        });
                        popwindow.showAtLocation(btn_register, Gravity.CENTER, 0, 0);
                    }else {
                        Toast.makeText(RegisterActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }
}
