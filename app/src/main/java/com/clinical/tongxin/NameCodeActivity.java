package com.clinical.tongxin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入备注码
 * Created by Administrator on 2016/11/28 0028.
 */
public class NameCodeActivity extends BaseActivity implements View.OnClickListener{
    private TextView txt_name,txt_bankName,txt_bankcard;
    private EditText editTxt_code;
    private Button btn_ok;
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
        setContentView(R.layout.activity_name_code);
        initView();
        initListener();
    }

    private void initView() {
        TextView title= (TextView) findViewById(R.id.title);
        title.setText("实名认证验证码");
        txt_name= (TextView) findViewById(R.id.txt_name);
        txt_name.setText("平台向您"+getLoginUserSharedPre().getString("RealName","") + "(***************" + getLoginUserSharedPre().getString("CardNumber","")+")汇入一笔1分钱的确认金额");
        txt_bankName= (TextView) findViewById(R.id.txt_bankName);
        txt_bankName.setText(getLoginUserSharedPre().getString("BankName",""));
        txt_bankcard= (TextView) findViewById(R.id.txt_bankcard);
        txt_bankcard.setText("****************"+getLoginUserSharedPre().getString("CardNumber",""));
        editTxt_code= (EditText) findViewById(R.id.editTxt_code);
        btn_ok= (Button) findViewById(R.id.btn_ok);

    }

    private void initListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
            {
                finddate();
            }break;
        }
    }
    private void finddate(){
        dialog.show();
        if (editTxt_code.getText().toString().equals("")){
            Toast.makeText(context, "请输入备注码", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map=new HashMap<>();
        map.put("ValidateCode",editTxt_code.getText().toString());
        XUtil.Post(UrlUtils.URL_FillAuthenticationCode, map, new MyCallBack<String>() {


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
                    JSONObject obj=new JSONObject(json);
                    if (obj.getString("code").equals("200")){
                        JSONObject obj2=obj.getJSONObject("result");
                        boolean s3=obj2.getBoolean("check");
                        if (s3){
                            preferences.edit().putString("Status", "7").commit();
                            finish();
                        }else{
                            Toast.makeText(context, "备注码不正确", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(context, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });
    }
}
