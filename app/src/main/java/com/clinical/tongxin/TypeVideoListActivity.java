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
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/3.
 */

public class TypeVideoListActivity extends BaseActivity {
    public final static String KEY_TITLE = "key_title";

    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.more_tv)
    TextView mMoreTv;

    private Unbinder mUnbinder;
    private VideoListAdapter mAdapter;
    private List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> mData = new ArrayList<>();
    private String mTitle = "";
    private MyProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mUnbinder = ButterKnife.bind(this);
        initData();
//        getVideoList();
    }

    private void initData(){
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        //隐藏“更多”
        mMoreTv.setVisibility(View.GONE);
        //加载数据
        loadData();
        //设置标题
        mTitleTv.setText(mTitle);

        mAdapter = new VideoListAdapter(this);
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
        if(getIntent() != null) {
            mTitle = getIntent().getStringExtra(KEY_TITLE);
            mData = getIntent().getParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST);
        }
    }

//    private void getVideoList(){
//        mDialog.show();
//        Map<String, String> map = new HashMap<>();
//        map.put("count", "6");
//        XUtil.Get(UrlUtils.URL_TRAIN_VIDEO_ALL, map, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                ResultJsonP1 myjson = Utils.wsJsonToModel1(result);
//                if (myjson.getCode().equals("200")) {
//                    Gson gson = new Gson();
//                    List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> list = gson.fromJson(myjson.getResult(), new TypeToken<List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity>>() {
//                    }.getType());
//                    mAdapter.setData(list);
////                    Intent intent = new Intent(context, AllVideoListActivity.class);
////                    intent.putParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST, (ArrayList<? extends Parcelable>) list);
////                    startActivity(intent);
//                }
//                mDialog.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(context, "获取视频失败，请重试。", Toast.LENGTH_SHORT).show();
//                mDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                mDialog.dismiss();
//            }
//
//            @Override
//            public void onFinished() {
//                mDialog.dismiss();
//            }
//        });
//    }
}
