package com.clinical.tongxin;

import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.clinical.tongxin.entity.EnumsEntity;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实名认证
 * Created by Administrator on 2016/11/26 0026.
 */
public class NameAuthenticationActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    //日期,
    private TextView txt_date;
    //姓名；身份证号；密码；银行卡号
    private EditText editTxt_name,editTxt_ID,edtTxt_pwd,edtTxt_pwd1;
    //身份证正面；身份证反面；
    private LinearLayout ll_img_add1,ll_img_add2;
    private Button btn_ok;
    //是否长期有效
    private CheckBox checkBox;
    private LinearLayout ll_checkbox;
    TimePickerView pvTime;
    View vMasker;
    private String headPath="";
    private int k=0;
    private ImageView img_add1,img_add2;
    List<String> listpic=new ArrayList<>();
    private MyProgressDialog dialog;
    private String ischeck="0";

    private String IDFrontImgUrl="",IDBackImgUrl="";
    private int type=1;
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
        setContentView(R.layout.activity_name_authentication);
        initView();
        initListener();
        initPickerView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("实名认证");
        editTxt_name= (EditText) findViewById(R.id.editTxt_Name);
        editTxt_ID= (EditText) findViewById(R.id.editTxt_ID);
        txt_date= (TextView) findViewById(R.id.txt_date);
        ll_img_add1= (LinearLayout) findViewById(R.id.ll_img_add1);
        ll_img_add2= (LinearLayout) findViewById(R.id.ll_img_add2);
        img_add1= (ImageView) findViewById(R.id.img_add1);
        img_add2= (ImageView) findViewById(R.id.img_add2);
        edtTxt_pwd= (EditText) findViewById(R.id.edtTxt_pwd);
        edtTxt_pwd1= (EditText) findViewById(R.id.edtTxt_pwd1);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        checkBox= (CheckBox) findViewById(R.id.checkbox);
        ll_checkbox= (LinearLayout) findViewById(R.id.ll_checkbox);
    }

    private void initListener(){
        txt_date.setOnClickListener(this);
        ll_img_add1.setOnClickListener(this);
        ll_img_add2.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        ll_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type==1){
                    txt_date.setText("");
                    txt_date.setHint("");
                    txt_date.setEnabled(false);
                    checkBox.setChecked(true);
                    type=2;
                }else{
                    txt_date.setEnabled(true);
                    txt_date.setHint("请选择日期");
                    checkBox.setChecked(false);
                    type=1;
                }
            }
        });
//        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b){
//                    txt_date.setText("");
//                    txt_date.setHint("");
//                    txt_date.setEnabled(false);
//                }else{
//                    txt_date.setEnabled(true);
//                    txt_date.setHint("请选择日期");
//                }
//            }
//        });
    }

    public void setuserheadpic(){
        Intent intent = new Intent(NameAuthenticationActivity.this, SelectCameraDialog.class);
        Bundle bundleObject = new Bundle();
        GoClass myclass = new GoClass();
        myclass.setGoToClass(NameAuthenticationActivity.class);
        bundleObject.putSerializable("myclass", myclass);
        intent.putExtras(bundleObject);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        String strpath = "";
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            strpath=Utils.getMyString(intent.getExtras().getString("path"),"");

        }
        if (!strpath.equals("")){
            switch (k)
            {
                case 1:
                {
                    IDFrontImgUrl=strpath;
                }break;
                case 2:
                {
                    IDBackImgUrl= strpath;
                }

                break;
            }

            switch (k)
            {
                case 1:
                {
                    ImageLoader.getInstance().displayImage("file://"+IDFrontImgUrl, img_add1, MyApplication.normalOption);
                }break;
                case 2:
                {
                    ImageLoader.getInstance().displayImage("file://"+IDBackImgUrl, img_add2, MyApplication.normalOption);
                }
                break;
            }
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //日期
            case R.id.txt_date:
            {
                pvTime.show();
            }
            break;
            //身份证正面
            case R.id.ll_img_add1:
            {
                k=1;
                setuserheadpic();
//                listpic.clear();
            }
            break;
            //身份证反面
            case R.id.ll_img_add2:
            {
                k=2;
                setuserheadpic();
//                listpic.clear();
            }
            break;
            //提交数据
            case R.id.btn_ok:
            {
                confrm();
            }
            break;
        }
    }

    private void initPickerView() {
        vMasker = findViewById(R.id.vMasker);
        // 控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 20);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() < new Date().getTime()) {
                    Toast.makeText(NameAuthenticationActivity.this,
                            "选择的有效期不能小于当前时间", Toast.LENGTH_SHORT).show();
                } else {
                    txt_date.setText(Utils.getYearMonthDay(date));
                }
            }
        });
    }
    private void confrm(){
        if (editTxt_name.getText().toString().equals("")){
            Toast.makeText(this,"请输入真实姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxt_ID.getText().toString().equals("")){
            Toast.makeText(this,"请输入身份证号",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isIDCard(editTxt_ID.getText().toString())){
            Toast.makeText(this,"身份证号格式不对",Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkBox.isChecked()){
            ischeck="1";
        }else{
            ischeck="0";
        }

        if (!ischeck.equals("1")){
            if (txt_date.getText().toString().equals("")){
                Toast.makeText(this,"请选择身份证有效期",Toast.LENGTH_SHORT).show();
                return;
            }

        }
        if (IDFrontImgUrl.equals("")){
            Toast.makeText(this, "请上传身份证的正面照片", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (IDBackImgUrl.equals("")){
            Toast.makeText(this, "请上传身份证的反面照片", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_pwd.getText().toString().equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        String isHan="^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$";
        if (!edtTxt_pwd.getText().toString().matches(isHan)){
            Toast.makeText(this, "密码至少包含字母、数字和符号中的两种", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (edtTxt_pwd.getText().toString().length()<6||edtTxt_pwd.getText().toString().length()>20){
            Toast.makeText(this, "密码长度为6-20位", Toast.LENGTH_SHORT)
                    .show();
            return;
        }


        if (!edtTxt_pwd.getText().toString().equals(edtTxt_pwd1.getText().toString())){
            Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }



        if (getLoginUserSharedPre().getString("PassWord","").equals(edtTxt_pwd1.getText().toString())){
            Toast.makeText(this, "支付密码不能和登录密码相同", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        dialog.show();
        Map<String,Object> map=new HashMap<>();
        map.put("CustomerId",getLoginUserSharedPre().getString("userId",""));
        map.put("TradePassword",edtTxt_pwd1.getText().toString());
        map.put("RealName", editTxt_name.getText().toString());
        map.put("IDNumber",editTxt_ID.getText().toString());
        map.put("ValidityPeriod",txt_date.getText().toString());
        map.put("IDIsPermanent",ischeck);
        map.put("IDBackImgUrl", new File(IDBackImgUrl));
        map.put("IDFrontImgUrl", new File(IDFrontImgUrl));

        XUtil.UpLoadFileAndText(UrlUtils.URL_IdentityAuthentication, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(NameAuthenticationActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj=new JSONObject(arg0);
                    if (obj.getString("code").equals("200")){
                        checkBox.setChecked(false);
                        ischeck="0";
                        getLoginUserSharedPre().edit().putString("Status", "1").commit();
                        getLoginUserSharedPre().edit().putString("RealName",editTxt_name.getText().toString()).commit();
                        getLoginUserSharedPre().edit().putString("IDNumber",editTxt_ID.getText().toString()).commit();
                        Intent intent = new Intent(NameAuthenticationActivity.this, PaySetUpPwdActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
