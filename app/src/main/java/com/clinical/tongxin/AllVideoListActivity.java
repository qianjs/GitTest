package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.VideoListAdapter;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TrainVideoTypeInfoEntity;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/3.
 */

public class AllVideoListActivity extends BaseActivity {
    public final static String KEY_VIDEO_LIST = "key_video_list";

    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.title)
    TextView mTitleTv;

    private Unbinder mUnbinder;
    private VideoListAdapter mAdapter;
    private List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> mData = new ArrayList<>();
    // 进度
    private MyProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        mAdapter = new VideoListAdapter(this);
    }

    private void initData(){
        //加载数据
        loadData();
        //顶部标题
        mTitleTv.setText("热门培训视频");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mListView.setLayoutManager(gridLayoutManager);
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void loadData(){
        if(getIntent() != null){
            mData = getIntent().getParcelableArrayListExtra(KEY_VIDEO_LIST);
        }
    }

    @OnClick(R.id.more_tv)
    void onClickMoreTextView(View view){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("count", "6");
        XUtil.Get(UrlUtils.URL_TRAIN_VIDEO_TYPE, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultJsonP1 myjson = Utils.wsJsonToModel1(result);
                if (myjson.getCode().equals("200")) {
                    Gson gson = new Gson();
                    List<TrainVideoTypeInfoEntity> list = gson.fromJson(myjson.getResult(), new TypeToken<List<TrainVideoTypeInfoEntity>>() {
                    }.getType());
                    Intent intent = new Intent(AllVideoListActivity.this, VideoTypeListActivity.class);
                    intent.putParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST, (ArrayList<? extends Parcelable>) list);
                    startActivity(intent);
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AllVideoListActivity.this, "获取视频失败，请重试。", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mDialog.dismiss();
            }

            @Override
            public void onFinished() {
                mDialog.dismiss();
            }
        });
    }
}
