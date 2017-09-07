package com.clinical.tongxin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.HXMyFriend;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.SPUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_INFO;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    // 进度
    private MyProgressDialog mDialog;
    private int recLen = 30;
    private Button btn_getcode, btn_login;
    private EditText edtTxt_phone, edtTxt_code, edtTxt_pwd;
    private TextView txt_register;
    private LinearLayout ll_type2,ll_code;
    private String type = "1";//1密码登录 2验证码登录
    private TextView tv_loginType;//登录类型（验证码或密码）
    private String msg;
    //可能增加表单验证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_login);
        msgRecieve();
        initView();
        initListener();
        //登录
        String pwd=getLoginUserSharedPre().getString("PassWord","");
        String Mobile=getLoginUserSharedPre().getString("Mobile","");
        if (!pwd.equals("")&&!Mobile.equals("")){
            //loginPwd(Mobile,pwd);
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("LoginType","1");
//            startActivity(intent);
            HXLogin("1");
            finish();
        }

    }

    private void msgRecieve() {
        try {
            msg = getIntent().getStringExtra("msg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        btn_getcode = (Button) findViewById(R.id.btn_getcode);
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_register = (TextView) findViewById(R.id.txt_register);
        edtTxt_phone = (EditText) findViewById(R.id.edtTxt_phone);
        edtTxt_code = (EditText) findViewById(R.id.edtTxt_code);
        edtTxt_pwd = (EditText) findViewById(R.id.edtTxt_pwd);
        ll_type2 = (LinearLayout) findViewById(R.id.ll_type2);
        ll_code = (LinearLayout) findViewById(R.id.ll_code);
        tv_loginType = (TextView) findViewById(R.id.tv_loginType);
    }

    private void initListener() {
        btn_getcode.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        txt_register.setOnClickListener(this);
        tv_loginType.setOnClickListener(this);
        findViewById(R.id.resume_btn).setOnClickListener(this);
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
        switch (view.getId()) {
            case R.id.btn_getcode: {
                sendSMS();
            }
            break;
            case R.id.btn_login: {
                {
                    loginPwd(edtTxt_phone.getText().toString(),edtTxt_pwd.getText().toString());
//                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
                }
            }
            break;
            case R.id.txt_register: {
                Intent intent = new Intent(LoginActivity.this, RegisterXYActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_loginType: {
                Intent intent = new Intent(LoginActivity.this, ForgetPassWordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.resume_btn: {
                Intent intent = new Intent(LoginActivity.this, ResumeInfoActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    private void sendSMS() {
        if (edtTxt_phone.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_phone.getText().toString().length()!=11) {
            Toast.makeText(LoginActivity.this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        btn_getcode.setEnabled(false);
        recLen = 30;
        handler.postDelayed(runnable, 1000);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Mobile", edtTxt_phone.getText().toString());
        XUtil.Get(UrlUtils.URL_GetLoginVerificationCode, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson.getCode().equals("200")) {
                    Toast.makeText(context, "验证码已发送请留意你的短消息", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void loginPwd(String Mobile, final String pwd ) {
        if (Mobile.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (Mobile.length()!=11) {
            Toast.makeText(LoginActivity.this, "手机号不正确", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (pwd.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入登录密码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("Mobile",Mobile);
        map.put("ValidateCode", edtTxt_code.getText().toString());
        map.put("DeviceCode", Utils.getPhoneIMEI());
        map.put("LoginPassword", pwd);
//        map.put("ValidateCode", edtTxt_code.getText().toString());
        XUtil.Post(UrlUtils.URL_UserLogin, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, "手机号或密码不正确", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")) {
                        try {

//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("LoginType","0");
//                            startActivity(intent);
//                            getLoginUserSharedPre().edit().putInt()
                            String ss=new JSONObject(myjson.getResult()).optString("Status");
                            preferences.edit().putString("Status", new JSONObject(myjson.getResult()).optString("Status")).commit();
                            preferences.edit().putString("userId", new JSONObject(myjson.getResult()).optString("CustomerId")).commit();
                            preferences.edit().putString("Ukey", new JSONObject(myjson.getResult()).optString("Ukey")).commit();
                            preferences.edit().putString("Mobile", new JSONObject(myjson.getResult()).optString("Mobile")).commit();
                            preferences.edit().putString("NickName", new JSONObject(myjson.getResult()).optString("NickName")).commit();
                            preferences.edit().putString("RealName", new JSONObject(myjson.getResult()).optString("RealName")).commit();
                            preferences.edit().putString("IDNumber", new JSONObject(myjson.getResult()).optString("IDNumber")).commit();
                            preferences.edit().putString("HXNumber", new JSONObject(myjson.getResult()).optString("HXNumber")).commit();
                            preferences.edit().putString("BankName", new JSONObject(myjson.getResult()).optString("BankName")).commit();
                            preferences.edit().putString("CardNumber", new JSONObject(myjson.getResult()).optString("CardNumber")).commit();
                            preferences.edit().putString("PhotoUrl", new JSONObject(myjson.getResult()).optString("PhotoUrl")).commit();
                            preferences.edit().putString("IsRefuseTask", new JSONObject(myjson.getResult()).optString("IsRefuseTask")).commit();
                            preferences.edit().putString("role", new JSONObject(myjson.getResult()).optString("Role")).commit();
                            preferences.edit().putString("PassWord", pwd).commit();
                            preferences.edit().putString("Rating", new JSONObject(myjson.getResult()).optString("Rating")).commit();
                            preferences.edit().putString("RatingType", new JSONObject(myjson.getResult()).optString("RatingType")).commit();
                            HXLogin("0");
                            //finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if(myjson.getCode().equals("-1")){
                        ll_code.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "为了您的账户安全请输入验证码", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();

                    }else {
                        mDialog.dismiss();
                    }

                }
                //mDialog.dismiss();
            }
        });
    }

    private void GoToClass() {
        Bundle bundleObject = getIntent().getExtras();
        GoClass goClass = (GoClass) bundleObject.getSerializable("myclass");
        HashMap<String, String> map = (HashMap<String, String>) bundleObject.getSerializable("putExtras");
        Intent intent = new Intent(LoginActivity.this, goClass.getGoToClass());
        if (map != null) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                intent.putExtra(key.toString(), value.toString());
            }
        }
        startActivity(intent);
        finish();
    }

    private void HXLogin(final String type) {
        final UserEntity user = getLoginConfig();
        if (user.getPhone().equals("")) {
            return;
        }
        EMClient.getInstance().login(user.getPhone(), user.getPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                mDialog.dismiss();
                //IMHelper.getInstance().setUserInfo(getLoginConfig());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("LoginType",type);
                if (!TextUtils.isEmpty(msg)){
                    intent.putExtra("msg",msg);
                }
                startActivity(intent);
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("linchao","环信账号登录成功");
                SPUtils.putObject(user.getPhone(),user.getNickName());
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                mDialog.dismiss();
//                Toast.makeText(LoginActivity.this,"聊天服务登录失败！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("LoginType",type);
                if (!TextUtils.isEmpty(msg)){
                    intent.putExtra("msg",msg);
                }
                startActivity(intent);
                Log.d("linchao", "登录聊天服务器失败！");
                finish();
            }
        });
    }
}
