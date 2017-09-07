package com.clinical.tongxin;

import android.os.Bundle;
import android.widget.TextView;

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
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManagerDetailsActivity extends BaseActivity{
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private TextView txt_ExpectAmount,txt_in_money,txt_over_money,txt_not_money,txt_ManagerName,txt_date,txt_Status;
    private String BudgetId="";
    private String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_details);
        initView();
        initListener();
        findData();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("预算详情");
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        BudgetId=getIntent().getExtras().getString("BudgetId");
        Date=getIntent().getExtras().getString("Date");
        txt_ExpectAmount= (TextView) findViewById(R.id.txt_ExpectAmount);
        txt_in_money= (TextView) findViewById(R.id.txt_in_money);
        txt_over_money= (TextView) findViewById(R.id.txt_over_money);
        txt_not_money= (TextView) findViewById(R.id.txt_not_money);
        txt_ManagerName= (TextView) findViewById(R.id.txt_ManagerName);
        txt_date= (TextView) findViewById(R.id.txt_date);
        txt_Status= (TextView) findViewById(R.id.txt_Status);
    }

    private void initListener() {
        
    }
    private void findData(){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("BudgetId", BudgetId);
        map.put("Date", Date);
        XUtil.Post(UrlUtils.URl_ShowAppPMNowDateAmount,map,new MyCallBack<String >(){
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
                    if (myjson.getCode().equals("200")) {
                        try {
                            JSONObject json=new JSONObject(myjson.getResult());
                            txt_ExpectAmount.setText(Utils.getMyString(Double.parseDouble(json.getString("ExpectAmount"))+"","0.0"));
                            txt_in_money.setText(Utils.getMyString(Double.parseDouble(json.getString("ing"))+"","0.0"));
                            txt_Status.setText(Utils.getMyString(json.getString("Stutes"),"0.0"));
                            txt_over_money.setText(Utils.getMyString(Double.parseDouble(json.getString("unuse"))+"","0.0"));
                            txt_not_money.setText(Utils.getMyString(Double.parseDouble(json.getString("use"))+"","0.0"));
                            txt_ManagerName.setText(Utils.getMyString(json.getString("PMName"),"--"));
                            txt_date.setText(Utils.getMyString(json.getString("Date"),"--"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                mDialog.dismiss();

            }
        });
    }
}
