package com.clinical.tongxin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzj667 on 2016/9/3.
 * 更新用户ID
 */
public class UpdateUserNameActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private EditText edtTxt_userName;
    private Button btn_ok;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userid);
        dialog=new MyProgressDialog(this,"请稍后...");
        initView();
        initListener();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("用户ID");
        edtTxt_userName= (EditText) findViewById(R.id.edtTxt_userName);
        btn_ok= (Button) findViewById(R.id.btn_ok);
    }

    private void initListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                finduserifo();
                break;
        }
    }
    public void finduserifo(){
        String aa=edtTxt_userName.getText().toString();
        aa=aa.replaceAll(" ","");
        aa=aa.replaceAll("\n","");
        if (aa.equals("")){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        Map<String,String> map=new HashMap<>();
        map.put("uid",getLoginUserSharedPre().getString("userId",""));
        map.put("ukey",getLoginUserSharedPre().getString("ukey",""));
        map.put("oldPwd","");
        map.put("newPwd","");
        map.put("name",edtTxt_userName.getText().toString());
        map.put("fileList","");
        XUtil.Post(UrlUtils.URL_InfoEdit,map,new MyCallBack<String >(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(UpdateUserNameActivity.this,arg0.toString(),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP resultJsonP= Utils.wsJsonToModel(arg0);
                if (resultJsonP!=null){
                    Toast.makeText(UpdateUserNameActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(UpdateUserNameActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
