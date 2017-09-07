package com.clinical.tongxin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.PropertyEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配预算调整
 * Created by Administrator on 2017/1/4 0004.
 */

public class AdjustmentActivity extends  BaseActivity implements View.OnClickListener{
    // 进度
    private MyProgressDialog mDialog;
    private TextView title;
    private RadioGroup radioGroup;
    private RadioButton radio_add,radio_reduce;
    //名称；进行中；已完成；未使用；预算额
    private TextView txt_Name,txt_in_money,txt_over_money,txt_not_money,txt_ExpectAmount;
    private String name,in_money,over_money,not_money,ExpectAmount;
    //输入金额
    private EditText editTxt_money;
    private Button btn_ok;
    //增加（0）减少（1）
    private String isChange="0";
    private String DirectorGroupId;
    private double max=999999999;
    private double daiyusuan=0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment);
        initView();
        initListener();


    }

    private void initView() {
        // 加载中
        try {
            mDialog = new MyProgressDialog(this, "请稍后...");
            title= (TextView) findViewById(R.id.title);
            title.setText("调整预算");
            txt_Name= (TextView) findViewById(R.id.txt_Name);
            txt_in_money= (TextView) findViewById(R.id.txt_in_money);
            txt_over_money= (TextView) findViewById(R.id.txt_over_money);
            txt_not_money= (TextView) findViewById(R.id.txt_not_money);
            txt_ExpectAmount= (TextView) findViewById(R.id.txt_ExpectAmount);
            radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
            radio_add= (RadioButton) findViewById(R.id.radio_add);
            radio_add.setChecked(true);
            radio_reduce= (RadioButton) findViewById(R.id.radio_reduce);
            radio_reduce.setChecked(false);
            editTxt_money= (EditText) findViewById(R.id.editTxt_money);
            btn_ok= (Button) findViewById(R.id.btn_ok);
            name=getIntent().getExtras().getString("name");
            in_money=getIntent().getExtras().getString("inmoney");
            over_money=getIntent().getExtras().getString("overmoney");
            not_money=getIntent().getExtras().getString("notmoney");
            ExpectAmount=getIntent().getExtras().getString("ExpectAmount");
            DirectorGroupId=getIntent().getExtras().getString("DirectorGroupId");
            daiyusuan=Double.parseDouble(getIntent().getExtras().getString("daiyusuan"));
            txt_Name.setText(name);
            txt_in_money.setText(Utils.getDoubleTo2(Double.parseDouble(in_money)));
            txt_over_money.setText(Utils.getDoubleTo2(Double.parseDouble(over_money)));
            txt_not_money.setText(Utils.getDoubleTo2(Double.parseDouble(not_money)));
            txt_ExpectAmount.setText(Utils.getDoubleTo2(Double.parseDouble(ExpectAmount)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

    }

    private void initListener() {
        btn_ok.setOnClickListener(this);
        //调整点击事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radio_add.getId()==i){
                    isChange="0";
                } if (radio_reduce.getId()==i){
                    isChange="1";
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
            {
                //提交调整金额
                initData();
            }
            break;
        }
    }
    //调整金额
    private void initData(){
        if (editTxt_money.getText().toString().equals("")){
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isChange.equals("0")){
            if ((Double.parseDouble(Utils.getMyString(editTxt_money.getText().toString(),"0"))+Double.parseDouble(ExpectAmount))>max){
                Toast.makeText(this,"输入金额不合法，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Double.parseDouble(Utils.getMyString(editTxt_money.getText().toString(),"0"))<=0){
                Toast.makeText(this,"输入金额不合法，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Double.parseDouble(Utils.getMyString(editTxt_money.getText().toString(),"0"))>daiyusuan){
                Toast.makeText(this,"增加的金额不大于待分配预算",Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if(isChange.equals("1")){

            if (Double.parseDouble(not_money)<Double.parseDouble(editTxt_money.getText().toString())){
                Toast.makeText(this,"调整金额不能大于未使用金额",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Double.parseDouble(Utils.getMyString(editTxt_money.getText().toString(),"0"))<=0){
                Toast.makeText(this,"输入金额不合法，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Change",isChange);
        map.put("Amount",editTxt_money.getText().toString());
        map.put("DirectorGroupId",DirectorGroupId);
        XUtil.Post(UrlUtils.URL_setAppNowDateAmount,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")){
                        Toast.makeText(AdjustmentActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
//                        Toast.makeText(AdjustmentActivity.this,myjson.getCode(),Toast.LENGTH_SHORT).show();
                    }
                }
                mDialog.dismiss();
            }
        });
    }
}
