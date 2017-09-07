package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户设置
 * Created by Administrator on 2017/1/15 0015.
 */

public class UserSetUpActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private Button btn_ok;//退出账号
    private LinearLayout ll_user,ll_password;
    private ImageView img_head;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);
        initView();
        initListener();
    }

    private void initView() {
        dialog=new MyProgressDialog(this,"正在上传...");
        title= (TextView) findViewById(R.id.title);
        title.setText("账户设置");
        title.setVisibility(View.GONE);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        ll_user= (LinearLayout) findViewById(R.id.ll_user);
        ll_password= (LinearLayout) findViewById(R.id.ll_password);
        img_head= (ImageView) findViewById(R.id.img_head);
        String url=getLoginUserSharedPre().getString("PhotoUrl", "");
        ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL1+url, img_head, MyApplication.roundedOption);
    }
    private void initListener(){
        btn_ok.setOnClickListener(this);
        ll_user.setOnClickListener(this);
        ll_password.setOnClickListener(this);
        img_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
            {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("linchao","环信账号退出登录成功");
                        getLoginUserSharedPre().edit().clear().commit();
                        MyApplication.exitapp();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("linchao","环信账号退出失败");
                        getLoginUserSharedPre().edit().clear().commit();
                        MyApplication.exitapp();
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
            break;
            //修改用户
            case R.id.ll_user:
            {
                Intent intent=new Intent(this,UserMobileActivity.class);
                startActivity(intent);
            }
            break;
            //修改密码
            case R.id.ll_password:
            {
                Intent intent=new Intent(this,PasswordReciveActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_head:
            {
                setuserheadpic();
            }
        }
    }
    public void setuserheadpic(){
        Intent intent = new Intent(UserSetUpActivity.this, SelectCameraDialog.class);
        Bundle bundleObject = new Bundle();
        GoClass myclass = new GoClass();
        myclass.setGoToClass(UserSetUpActivity.class);
        bundleObject.putSerializable("myclass", myclass);
        intent.putExtras(bundleObject);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.e("linchao","========onNewIntent=====");
        String strpath = "";
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            strpath= Utils.getMyString(intent.getExtras().getString("path"),"");
            ImageLoader.getInstance().displayImage("file://"+strpath, img_head, MyApplication.roundedOption);
            setImageHead(strpath);
        }




        }

//    @Override
//    protected void onResume() {
//        if (getIntent().getStringExtra("path")!= null){
//            Log.e("linchao","========onResume=====");
//            String  strpath = Utils.getMyString(getIntent().getStringExtra("path"),"");
//            if (TextUtils.isEmpty(strpath)){
//                Toast.makeText(context,"获取图片路径失败",Toast.LENGTH_SHORT).show();
//            }else {
//                ImageLoader.getInstance().displayImage("file://"+strpath, img_head, MyApplication.roundedOption);
//                setImageHead(strpath);
//            }
//
//
//        }
//        super.onResume();
//    }

    private void setImageHead(String path){
        dialog.show();
        Map<String,Object> map=new HashMap<>();
        map.put("CustomerId",getLoginConfig().getUserId());
        map.put("Ukey",getLoginConfig().getUkey());
        map.put("photoUrl",new File(path));
        XUtil.UpLoadFileAndText(UrlUtils.URL_updatePhotourlApp,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if(myjson.getCode().equals("200")){
                    preferences.edit().putString("PhotoUrl",myjson.getResult()).commit();
                    Toast.makeText(context, myjson.getMsg(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

           }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                dialog.dismiss();
                super.onError(arg0, arg1);
            }
        });
    }
}
