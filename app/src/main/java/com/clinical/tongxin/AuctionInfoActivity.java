package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.A;
import com.clinical.tongxin.adapter.AuctionInfoAdapter;
import com.clinical.tongxin.entity.AuctionInfoEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clinical.tongxin.R.id.et_name;
import static com.clinical.tongxin.R.id.rl_team_manager;
import static com.clinical.tongxin.R.id.tv_aptitude;
import static com.clinical.tongxin.R.id.tv_manager;


/**
 * 竞价信息
 * @author LINCHAO
 * 2017/1/22
 */
@ContentView(R.layout.activity_auction_info)
public class AuctionInfoActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;


    // 信息列表
    @ViewInject(R.id.lv_auction)
    ListView lv_auction;


    // 结束竞价
    @ViewInject(R.id.tv_commit)
    TextView tv_commit;
    // 继续竞价
    @ViewInject(R.id.tv_cancel)
    TextView tv_cancel;

    @ViewInject(R.id.ll_button)
    View ll_button;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private String taskId;
    private AuctionInfoAdapter adapter;
    private List<AuctionInfoEntity> list;
    private boolean isPublicTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        if (getIntent().getStringExtra("taskId") != null){
            taskId = getIntent().getStringExtra("taskId");
        }
        initView();
        initLister();
        requstData();
    }

    private void requstData() {
        mDialog.show();

        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("TaskId",taskId);
        XUtil.Post(UrlUtils.URL_AUCTION_INFO, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(AuctionInfoActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    ResultEntity<List<AuctionInfoEntity>> resultEntity = new Gson().fromJson(arg0,new TypeToken<ResultEntity<List<AuctionInfoEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        mDialog.dismiss();
                        List<AuctionInfoEntity> auctions = resultEntity.getResult();
                        if (auctions == null || auctions.size() == 0){
                            Toast.makeText(AuctionInfoActivity.this, "暂无竞价信息", Toast.LENGTH_SHORT).show();
                        }else {
                            list.addAll(auctions);
                            adapter.setList(auctions);
                        }

                    }else {
                        mDialog.dismiss();
                        Toast.makeText(AuctionInfoActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AuctionInfoActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }


    private void initView() {
        isPublicTask = getIntent().getBooleanExtra("isPublicTask",false);
        if (isPublicTask){
            ll_button.setVisibility(View.GONE);
        }else {
            ll_button.setVisibility(View.VISIBLE);
        }
        mDialog = new MyProgressDialog(this,"请稍后...");
        userEntity = getLoginConfig();
        tv_title.setText("竞价信息");
        list = new ArrayList<>();
        adapter = new AuctionInfoAdapter(context,list);
        lv_auction.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 初始化监听
     */
    private void initLister() {
        tv_commit.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_commit:
                if(list == null || list.size() == 0){
                    Toast.makeText(context,"无竞价信息，不能结束竞价",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 结束竞价
                overAuction();
                break;
            case R.id.tv_cancel:
               finish();
                break;

        }
    }

    private void overAuction() {
        mDialog.show();

        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("TaskId",taskId);
        XUtil.Post(UrlUtils.URL_END_AUCTION, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(AuctionInfoActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    ResultEntity<Object> resultEntity = new Gson().fromJson(arg0,new TypeToken<ResultEntity<Object>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        Toast.makeText(AuctionInfoActivity.this, "结束竞价成功", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        setResult(RESULT_OK,new Intent(context,TaskDetailsActivity.class));
                        finish();
                    }else {
                        mDialog.dismiss();
                        Toast.makeText(AuctionInfoActivity.this, "结束竞价失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AuctionInfoActivity.this, "结束竞价失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }



}
