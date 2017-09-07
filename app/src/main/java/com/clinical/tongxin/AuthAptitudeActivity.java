package com.clinical.tongxin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.SubscribeAptitudeAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.RowsEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 资质认证
 * @author LINCHAO
 * 2017/6/22
 */
@ContentView(R.layout.activity_choose_aptitude)
public class AuthAptitudeActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;
    // 完成
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;
    // 更多
    @ViewInject(R.id.elv_choose_aptitude)
    ExpandableListView elv_choose_aptitude;

    private SubscribeAptitudeAdapter adapter;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;

    private List<AptitudeEntity> projectEntities;
    private List<AptitudeEntity> chooseProjects;
    private String projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        chooseProjects = new ArrayList<>();
        initView();
        initLister();
        requestProjectData();
    }


    private void initView() {
        mDialog = new MyProgressDialog(this,"请稍后...");
        mDialog.show();

        userEntity = getLoginConfig();
        tv_complete.setVisibility(View.VISIBLE);
        tv_title.setText("我能提供的服务");
        adapter = new SubscribeAptitudeAdapter(this);
        elv_choose_aptitude.setAdapter(adapter);
        elv_choose_aptitude.setGroupIndicator(null);

        elv_choose_aptitude.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void requestTaskData() {
        XUtil.Post(UrlUtils.URL_queryAllTaskType, null, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<List<AptitudeEntity>> resultEntity = gson.fromJson(json,new TypeToken<ResultEntity<List<AptitudeEntity>>>(){}.getType());

                    if (resultEntity.getCode() == 200){
                        List<AptitudeEntity> taskEntities = resultEntity.getResult();
                        adapter.setList(projectEntities,taskEntities,chooseProjects);
                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                            elv_choose_aptitude.expandGroup(i);
                        }
                    }else {
                        Toast.makeText(AuthAptitudeActivity.this,resultEntity.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AuthAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                }

                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }
    private void requestProjectData() {

        XUtil.Post(UrlUtils.URL_queryAllProjecttype, null, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<List<AptitudeEntity>> resultEntity = gson.fromJson(json,new TypeToken<ResultEntity<List<AptitudeEntity>>>(){}.getType());

                    if (resultEntity.getCode() == 200){
                        projectEntities = resultEntity.getResult();
                        requestTaskData();
                    }else {
                        Toast.makeText(AuthAptitudeActivity.this,resultEntity.getMsg(),Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AuthAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });


    }

    /**
     * 初始化监听
     */
    private void initLister() {
        tv_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_complete:
                // 完成
                projects = adapter.getJsonData();
                if (TextUtils.isEmpty(projects)){
                    Toast.makeText(context,"请选择你能提供的资质",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context,RegisterActivity.class);
                intent.putExtra("AptitudeList",projects);
                startActivity(intent);
                finish();
                //subscribeProjects();
                break;

        }
    }

    /**
     * 订阅项目
     */
    private void subscribeProjects() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("AptitudeList",projects);

        XUtil.Get(UrlUtils.URL_saveQualificationByCustomerApp, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<Object>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        Toast.makeText(context, "提交资质信息成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "提交资质信息失败", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }


}
