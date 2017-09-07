package com.clinical.tongxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ChooseAptitudeAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonAptitude;
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
import java.util.List;
import java.util.Map;


/**
 * 选择资质
 * @author LINCHAO
 * 2016/12/28
 */
@ContentView(R.layout.activity_choose_aptitude)
public class ChooseAptitudeActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;
    // 完成
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;
    // 更多
    @ViewInject(R.id.elv_choose_aptitude)
    ExpandableListView elv_choose_aptitude;

    private ChooseAptitudeAdapter adapter;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;

    private List<AptitudeEntity> projectEntities;
    private List<AptitudeEntity> chooseAptitudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initView();
        initLister();
        requestProjectData();
    }


    private void initView() {
        chooseAptitudes = (List<AptitudeEntity>) getIntent().getSerializableExtra("chooseAptitudes");
        mDialog = new MyProgressDialog(this,"请稍后...");
        mDialog.show();

        userEntity = getLoginConfig();
        tv_complete.setVisibility(View.VISIBLE);
        tv_title.setText("选择资质");
        if(chooseAptitudes == null){
            chooseAptitudes = new ArrayList<>();
        }
        adapter = new ChooseAptitudeAdapter(this,chooseAptitudes);
        elv_choose_aptitude.setAdapter(adapter);
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
                        adapter.setList(projectEntities,taskEntities);
                    }else {
                        Toast.makeText(ChooseAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChooseAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChooseAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChooseAptitudeActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
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
                Map<String,String> mapAptitude = adapter.getData();
                if (TextUtils.isEmpty(mapAptitude.get("text"))){
                    Toast.makeText(context,"请选择资质",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("text",mapAptitude.get("text"));
                intent.putExtra("json",mapAptitude.get("json"));
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;

        }
    }




}
