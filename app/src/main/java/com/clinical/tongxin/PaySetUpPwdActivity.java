package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置银行卡
 * Created by Administrator on 2016/12/1 0001.
 */
public class PaySetUpPwdActivity extends BaseActivity implements View.OnClickListener {
    private Popwindowbank popwindow1, popwindow2, popwindow3, popwindow4;
    private TextView txt_bank, txt_bankadress, txt_bankadress1, edtTxt_ID,edtTxt_name;
    private LinearLayout ll_top;
    private EditText editTxt_bankid,txt_bankName;
    private MyProgressDialog dialog;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new MyProgressDialog(this, "请稍等...");
        setContentView(R.layout.activity_pay_setup_pwd);
        initView();
        initListener();

    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("设置支付方式");
//        txt_bankadress = (TextView) findViewById(R.id.txt_bank_address);
//        txt_bankadress1 = (TextView) findViewById(R.id.txt_bank_address1);
//        txt_bank = (TextView) findViewById(R.id.txt_bank);
        txt_bankName = (EditText) findViewById(R.id.txt_bankName);
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        editTxt_bankid = (EditText) findViewById(R.id.editTxt_bankid);
        edtTxt_name = (TextView) findViewById(R.id.edtTxt_name);
        edtTxt_name.setText(getLoginUserSharedPre().getString("RealName",""));
        edtTxt_name.setEnabled(false);
        edtTxt_ID = (TextView) findViewById(R.id.edtTxt_ID);
        edtTxt_ID.setText(getLoginUserSharedPre().getString("IDNumber",""));
        edtTxt_ID.setEnabled(false);
        btn_ok = (Button) findViewById(R.id.btn_ok);

    }

    private void initListener() {
//        txt_bank.setOnClickListener(this);
//        txt_bankadress.setOnClickListener(this);
//        txt_bankadress1.setOnClickListener(this);
        txt_bankName.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //开户银行
//            case R.id.txt_bank: {
//                if (popwindow1 == null) {
//                    popwindow1 = new Popwindowbank(this);
//                    popwindow1.showList(PopwindowEnum.SetupPayMode.Bank);
//                    popwindow1.setOnOKClickListener(new Popwindowbank.OnOKClickListener() {
//                        @Override
//                        public void OnOKClick(String Name) {
//                            txt_bank.setText(Name);
//                            txt_bankName.setHint("请选择银行支行");
//                            txt_bankName.setText("");
//                        }
//                    });
//                    popwindow1.showAsDropDown(ll_top);
//                } else {
//                    popwindow1.showAsDropDown(ll_top);
//                }
//            }
//            break;
//            //开户银行所在地
//            case R.id.txt_bank_address: {
//                if (popwindow2 == null) {
//                    popwindow2 = new Popwindowbank(this);
//                    popwindow2.showList(PopwindowEnum.SetupPayMode.Province);
//                    popwindow2.setOnOKClickListener(new Popwindowbank.OnOKClickListener() {
//                        @Override
//                        public void OnOKClick(String Name) {
//                            txt_bankadress.setText(Name);
//                            txt_bankadress1.setText("请选择城市");
//                            txt_bankName.setText("");
//                            txt_bankName.setHint("请选择支行");
//                        }
//                    });
//                    popwindow2.showAsDropDown(ll_top);
//                } else {
//                    popwindow2.showAsDropDown(ll_top);
//                }
//            }
//            break;
//            case R.id.txt_bank_address1: {
//                if (txt_bankadress.getText().toString().equals("") || txt_bankadress.getText().toString().equals("请选择省份")) {
//                    Toast.makeText(this, "未选省份", Toast.LENGTH_LONG).show();
//                    return;
//                } else {
//                    if (popwindow3 == null) {
//
//
//                        popwindow3 = new Popwindowbank(this);
//                        popwindow3.setProvince(txt_bankadress.getText().toString());
//                        popwindow3.showList(PopwindowEnum.SetupPayMode.City);
//                        popwindow3.setOnOKClickListener(new Popwindowbank.OnOKClickListener() {
//                            @Override
//                            public void OnOKClick(String Name) {
//                                txt_bankadress1.setText(Name);
//                                txt_bankName.setText("");
//                                txt_bankName.setHint("请选择支行");
//                            }
//                        });
//                        popwindow3.showAsDropDown(ll_top);
//                    } else {
//                        popwindow3.setProvince(txt_bankadress.getText().toString());
//                        popwindow3.showList(PopwindowEnum.SetupPayMode.City);
//                        popwindow3.showAsDropDown(ll_top);
//                    }
//                }
//
//            }
//            break;
//            //开户银行名称
//            case R.id.txt_bankName: {
//                if (txt_bank.getText().toString().equals("") || txt_bank.getText().toString().equals("请选择银行")) {
//                    Toast.makeText(this, "未选开户银行", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if (txt_bankadress1.getText().toString().equals("") || txt_bankadress1.getText().toString().equals("请选择城市")) {
//                    Toast.makeText(this, "未选城市", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if (popwindow4 == null) {
//                    popwindow4 = new Popwindowbank(this);
//                    popwindow4.setBankName(txt_bank.getText().toString());
//                    popwindow4.setCityName(txt_bankadress1.getText().toString());
//                    popwindow4.showList(PopwindowEnum.SetupPayMode.BankName);
//                    popwindow4.setOnOKClickListener(new Popwindowbank.OnOKClickListener() {
//                        @Override
//                        public void OnOKClick(String Name) {
//                            txt_bankName.setText(Name);
//                        }
//                    });
//                    popwindow4.showAsDropDown(ll_top);
//                } else {
//                    popwindow4.setBankName(txt_bank.getText().toString());
//                    popwindow4.setCityName(txt_bankadress1.getText().toString());
//                    popwindow4.showList(PopwindowEnum.SetupPayMode.BankName);
//                    popwindow4.showAsDropDown(ll_top);
//                }
//            }
//            break;
            case R.id.btn_ok: {
                bntOk();
            }
            break;
        }
    }

    private void bntOk() {
//        if (txt_bank.getText().toString().equals("")) {
//            Toast.makeText(context, "请选择开户银行", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (txt_bankadress.getText().toString().equals("") || txt_bankadress.getText().toString().equals("请选择省份")) {
//            Toast.makeText(this, "请选择省份", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (txt_bankadress1.getText().toString().equals("") || txt_bankadress1.getText().toString().equals("请选择城市")) {
//            Toast.makeText(this, "请选择城市", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (txt_bankName.getText().toString().equals("")) {
            Toast.makeText(context, "请输入开户银行支行", Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxt_bankid.getText().toString().equals("")) {
            Toast.makeText(context, "请输入银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (editTxt_bankid.getText().toString().length()<10){
            Toast.makeText(context, "银行卡号不能小于10位", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        Map<String, String> map = new HashMap<String, String>();
        //map.put("func","list");
//        map.put("BankName", txt_bank.getText().toString());
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("BankBranchName", txt_bankName.getText().toString());
        map.put("CardNumber", editTxt_bankid.getText().toString());
        XUtil.Post(UrlUtils.URL_PaySetting, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                 super.onSuccess(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.getString("code").equals("200")) {
                        preferences.edit().putString("BankName", txt_bankName.getText().toString()).commit();
                        preferences.edit().putString("CardNumber", editTxt_bankid.getText().toString()).commit();
                        Toast.makeText(context, "设置成功,等待审核", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }
}
