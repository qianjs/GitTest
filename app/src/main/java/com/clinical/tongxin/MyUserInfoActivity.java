package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.EnumsEntity;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.HXMyFriend;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzj667 on 2016/9/3.
 * 个人信息
 */
public class MyUserInfoActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private TextView txt_userName,txt_pwd;
    private Button btn_logoff;
    private LinearLayout ll_userName,ll_pwd;
    private ImageView img_head;
    private String headPath="";
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myuserinfo);
        dialog=new MyProgressDialog(this,"请稍等...");
        initView();
        initListener();
    }
    public void setuserheadpic(){
        Intent intent = new Intent(MyUserInfoActivity.this, SelectCameraDialog.class);
        Bundle bundleObject = new Bundle();
        GoClass myclass = new GoClass();
        myclass.setGoToClass(MyUserInfoActivity.class);
        bundleObject.putSerializable("myclass", myclass);
        intent.putExtra("headpic", EnumsEntity.CameraClass.ActivityUserInfo);
        intent.putExtras(bundleObject);
        startActivity(intent);
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("个人信息");
        txt_userName= (TextView) findViewById(R.id.txt_userName);
        txt_userName.setText(getLoginUserSharedPre().getString("userName",""));
        txt_pwd= (TextView) findViewById(R.id.txt_pwd);
        txt_pwd.setText("******");
        btn_logoff= (Button) findViewById(R.id.btn_Logoff);
        ll_userName= (LinearLayout) findViewById(R.id.ll_user);
        ll_pwd= (LinearLayout) findViewById(R.id.ll_pwd);
        img_head= (ImageView) findViewById(R.id.img_head);
        ImageLoader.getInstance().displayImage(getLoginUserSharedPre().getString("url", ""), img_head, MyApplication.roundedOption);

    }

    private void initListener() {
        btn_logoff.setOnClickListener(this);
        ll_userName.setOnClickListener(this);
        ll_pwd.setOnClickListener(this);
        img_head.setOnClickListener(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        String path = intent.getStringExtra("path");
        headPath = path;
        path = "file://" + path;
        ImageLoader.getInstance().displayImage(path, img_head, MyApplication.roundedOption);
        finduserifo();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_user:
                Intent intent=new Intent(this,UpdateUserNameActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_pwd:
                Intent intent1=new Intent(this,PWDReciveActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_Logoff:
                getLoginUserSharedPre().edit().putString("userId","").commit();
                getLoginUserSharedPre().edit().putString("ukey", "").commit();
                getLoginUserSharedPre().edit().putString("userName", "").commit();
                //getLoginUserSharedPre().edit().putString("hxname", "").commit();
                MyApplication.exitapp();
                break;
            case R.id.img_head:
                setuserheadpic();
                break;
        }
    }
    public void finduserifo(){

        dialog.show();
        Map<String,Object> map=new HashMap<>();
        map.put("uid",getLoginUserSharedPre().getString("userId",""));
        map.put("ukey",getLoginUserSharedPre().getString("ukey",""));
        map.put("oldPwd","");
        map.put("newPwd","");
        map.put("name","");
        map.put("fileList","");
        map.put("file", new File(headPath));
        XUtil.UpLoadFileAndText(UrlUtils.URL_InfoEdit, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(MyUserInfoActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP resultJsonP = Utils.wsJsonToModel(arg0);
                if (resultJsonP != null) {
                    Toast.makeText(MyUserInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    String ss=resultJsonP.getI().getData();
                    if (!(ss.equals("")||ss.equals("null"))){
                        try {
                            JSONObject jsonObject = new JSONObject(ss);
                            getLoginUserSharedPre().edit().putString("url", jsonObject.getString("url")).commit();
                            List<HXMyFriend> list=XUtil.searchSelector(HXMyFriend.class, "name", getLoginUserSharedPre().getString("hxname","").toLowerCase());
                            if(list!=null) {
                                if (list.size() > 0) {
                                    list.get(0).setHeadurl(jsonObject.getString("url"));
                                    XUtil.saveOrUpdate(list.get(0));
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    JSONObject
//
                } else {
                    Toast.makeText(MyUserInfoActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
}
