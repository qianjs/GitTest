package com.clinical.tongxin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ChooseAptitudeAdapter;
import com.clinical.tongxin.adapter.SubscribeAptitudeAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.CityEntity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订阅项目
 * @author LINCHAO
 * 2017/6/22
 */
@ContentView(R.layout.activity_choose_aptitude)
public class SubscribeAptitudeActivity extends BaseActivity implements View.OnClickListener{

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
    private boolean isSubscribeSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        chooseProjects = (List<AptitudeEntity>) getIntent().getSerializableExtra("chooseProjects");
        initView();
        initLister();
        requestProjectData();
    }


    private void initView() {
        mDialog = new MyProgressDialog(this,"请稍后...");
        mDialog.show();

        userEntity = getLoginConfig();
        tv_complete.setVisibility(View.VISIBLE);
        tv_title.setText("选择资质");
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
        XUtil.Get(UrlUtils.URL_TASK_TYPE, null, new MyCallBack<String>() {

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
                    ResultEntity<RowsEntity> resultEntity = gson.fromJson(json,new TypeToken<ResultEntity<RowsEntity>>(){}.getType());

                    if (resultEntity.getCode() == 200){
                        List<AptitudeEntity> taskEntities = resultEntity.getResult().getRows();
                        adapter.setList(projectEntities,taskEntities,chooseProjects);
                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                            elv_choose_aptitude.expandGroup(i);
                        }
                    }else {
                        Toast.makeText(SubscribeAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SubscribeAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                }

                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }
    private void requestProjectData() {

        XUtil.Get(UrlUtils.URL_PROTECT_TYPE, null, new MyCallBack<String>() {

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
                    ResultEntity<RowsEntity> resultEntity = gson.fromJson(json,new TypeToken<ResultEntity<RowsEntity>>(){}.getType());

                    if (resultEntity.getCode() == 200){
                        projectEntities = resultEntity.getResult().getRows();
                        requestTaskData();
                    }else {
                        Toast.makeText(SubscribeAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SubscribeAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context,"请选择接收项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                subscribeProjects();
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
        map.put("projects",projects);

        XUtil.Get(UrlUtils.URL_saveAppTaskTypeSubscription, map, new MyCallBack<String>() {

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
                        Toast.makeText(context, "订阅成功", Toast.LENGTH_SHORT).show();
                        isSubscribeSuccess = true;
                        finish();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "订阅失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }
    @Override
    public void finish() {
        if (isSubscribeSuccess){
            super.finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("订阅城市");
            builder.setMessage("订阅的项目还未保存，是否退出？");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    projects = adapter.getJsonData();
                    subscribeProjects();
                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    SubscribeAptitudeActivity.super.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

}
