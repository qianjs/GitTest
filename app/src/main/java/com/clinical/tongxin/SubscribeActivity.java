package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.CityEntity;
import com.clinical.tongxin.entity.CityResponseEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.SubscribeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscribeActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_area_count)
    TextView tvAreaCount;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.tv_project_count)
    TextView tvProjectCount;
    @BindView(R.id.ll_project)
    LinearLayout llProject;
    private MyProgressDialog mDialog;
    private List<CityEntity> cityEntities;
    private List<AptitudeEntity> aptitudeEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    protected void onResume() {
        getSubscribeList();
        super.onResume();
    }

    private void initView() {
        title.setText("任务订阅");
        mDialog = new MyProgressDialog(context, "请稍后...");
    }

    @OnClick({R.id.ll_area, R.id.ll_project})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_area:
                Intent intent = new Intent(context,SubscribeCityActivity.class);
                intent.putExtra("chooseCities", (Serializable) cityEntities);
                startActivity(intent);

                break;
            case R.id.ll_project:
                Intent intent1 = new Intent(context,SubscribeAptitudeActivity.class);
                intent1.putExtra("chooseProjects", (Serializable) aptitudeEntities);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 获取订阅列表
     */
    private void getSubscribeList() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        XUtil.Get(UrlUtils.URL_queryAppSubscriptionCount, map, new MyCallBack<String>() {

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
                    ResultEntity<SubscribeEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<SubscribeEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        cityEntities = resultEntity.getResult().getAreaReceiver();
                        aptitudeEntities = resultEntity.getResult().getProjectReceiver();
                        if (cityEntities != null && cityEntities.size() != 0){
                            tvAreaCount.setText(cityEntities.size()+"");
                        }else {
                            tvAreaCount.setText("");
                        }
                        if (aptitudeEntities != null && aptitudeEntities.size() != 0){
                            tvProjectCount.setText(aptitudeEntities.size()+"");
                        }else {
                            tvProjectCount.setText("");
                        }
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取订阅信息失败", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }
}
