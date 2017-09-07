package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
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
 * Created by Administrator on 2016/11/30 0030.
 */
public class RegisterXYActivity  extends BaseActivity{
    private TextView title,txt_content;
    private Button btn_ok;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_xy);
        dialog=new MyProgressDialog(this,"请稍等...");
        initView();
        initListener();
        findData();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("用户协议");
        txt_content= (TextView) findViewById(R.id.txt_content);
        btn_ok= (Button) findViewById(R.id.btn_ok);
    }
    private void initListener(){
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterXYActivity.this,AuthAptitudeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void findData(){
        dialog.show();
        XUtil.Get(UrlUtils.URl_GetAgreement,null,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(RegisterXYActivity.this,arg0.toString(),Toast.LENGTH_SHORT);
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson!=null){
                    if (myjson.getCode().equals("200")){
                        try {
                            JSONObject object=new JSONObject(myjson.getResult());
                            btn_ok.setEnabled(true);
                            txt_content.setText(Html.fromHtml(Utils.getMyString(object.getString("Content"),"")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
                dialog.dismiss();
            }
        });
    }
}
