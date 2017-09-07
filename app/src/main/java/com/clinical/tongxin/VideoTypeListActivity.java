package com.clinical.tongxin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.clinical.tongxin.adapter.VideoListAdapter;
import com.clinical.tongxin.adapter.VideoTypeAdapter;
import com.clinical.tongxin.entity.TrainVideoTypeInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by QJS on 2017-07-25.
 */

public class VideoTypeListActivity extends BaseActivity {
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.more_tv)
    TextView mMoreTv;

    Unbinder mUnbinder;

    private List<TrainVideoTypeInfoEntity> mData = new ArrayList<>();
    private VideoTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView(){
        mAdapter = new VideoTypeAdapter(this);
    }

    private void initData(){
        //隐藏“更多”
        mMoreTv.setVisibility(View.GONE);
        //顶部标题
        mTitleTv.setText("培训视频分类");
        //加载数据
        loadData();

        mAdapter.setData(mData);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);
    }

    private void loadData(){
        if(getIntent() != null){
            mData = getIntent().getParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
