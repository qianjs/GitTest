package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.BannerEntity;
import com.clinical.tongxin.entity.ImageURLEntity;
import com.clinical.tongxin.entity.ProjectDetailEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.SlideShowView;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class ProjectDetailActivity extends BaseActivity implements View.OnClickListener{
    // 进度
    private MyProgressDialog mDialog;
    private TextView tv_submit;
    private SlideShowView ssv;
    private List<BannerEntity> navlist;
    private TextView tv_name,tv_subTitle,tv_price,tv_unit,tv_city,tv_feeDetails;
    private String pId;//项目编号
    private ProjectDetailEntity pModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectdetail);
        initView();
        initListener();

        getProjectDetail();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("项目详情");
        pId=getIntent().getStringExtra("pid");
        ssv = (SlideShowView)findViewById(R.id.slideshowView);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_subTitle=(TextView)findViewById(R.id.tv_subTitle);
        tv_price=(TextView)findViewById(R.id.tv_price);
        tv_unit=(TextView)findViewById(R.id.tv_unit);
        tv_city=(TextView)findViewById(R.id.tv_city);
        tv_feeDetails=(TextView)findViewById(R.id.tv_feeDetails);
        navlist=new ArrayList<BannerEntity>();
        tv_submit=(TextView)findViewById(R.id.tv_submit);
    }
    private void initListener()
    {
        tv_submit.setOnClickListener(this);
    }

    private void showInfo(ProjectDetailEntity model)
    {
        tv_name.setText(model.getName());
        tv_subTitle.setText(model.getSubTitle());
        tv_price.setText(model.getPrice());
        tv_unit.setText(model.getUnit());
        tv_city.setText(model.getCity());
        tv_feeDetails.setText(model.getFeeDetails());
        pModel=model;
        navlist.clear();
        List<BannerEntity> list =new ArrayList<BannerEntity>();// new Gson().fromJson(json, new TypeToken<List<BannerEntity>>() {
        //}.getType());
        for(ImageURLEntity entity:model.getUrlList())
        {
            BannerEntity be=new BannerEntity();
            be.setName("");
            be.setContent("");
            be.setId("");
            be.setPicUrl(entity.getUrl());
            be.setType("");
            be.setWebUrl("");
            list.add(be);
        }
        navlist.addAll(list);
        ssv.setData(navlist);
    }
    private void getProjectDetail()
    {
        mDialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("dId", pId);

        XUtil.Get(UrlUtils.URL_GetProjectDetails, map, new MyCallBack<String>() {


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
                ResultJsonP myjson = Utils.wsJsonToModel(json);
                if (myjson != null) {
                    ProjectDetailEntity model=new Gson().fromJson(myjson.getI().getData(),ProjectDetailEntity.class);
                    showInfo(model);
                }
                mDialog.dismiss();
            }

        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_submit:
            {
                Intent intent=new Intent(ProjectDetailActivity.this,SubmitOrderActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("pmodel",pModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
        }
    }
}
