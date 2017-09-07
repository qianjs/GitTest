package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public class ReasonActivity extends BaseActivity {
    private Button btn_ok;
    private TextView title,txt_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_xy);
        initView();
        findData();
    }



    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("失败原因");
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_ok.setText("重新认证");
        txt_content= (TextView) findViewById(R.id.txt_content);
        btn_ok.setEnabled(true);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReasonActivity.this,NameAuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void findData() {
        Map<String,String> map=new HashMap<>();
        map.put("CustomerId",getLoginUserSharedPre().getString("userId",""));
        map.put("Ukey",getLoginUserSharedPre().getString("Ukey",""));
        XUtil.Get(UrlUtils.URL_GetNotAuthenticationReason, map, new MyCallBack<String>() {


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
                try {
                    JSONObject obj=new JSONObject(json);
                    if (obj.getString("code").equals("200")){
                        JSONObject jsonObject=new JSONObject(obj.getString("result"));
                        txt_content.setText(jsonObject.getString("NotAuthenticationReason").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
