package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.clinical.tongxin.adapter.OptionFirstLevelAdapter;
import com.clinical.tongxin.adapter.OptionSecondLevelAdapter;
import com.clinical.tongxin.entity.OptionItemEntity;
import com.clinical.tongxin.inteface.IOnClickItemListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_ENTITY;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_CODE;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_NAME;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_TITLE;

/**
 * Created by qjs on 2017/8/21.
 */

public class OptionSecondLevelActivity extends BaseActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.title)
    TextView mTitleTv;

    private ArrayList<OptionItemEntity.Sub> mSubs = new ArrayList<>();
    private OptionSecondLevelAdapter mAdapter;

    private IOnClickItemListener mOnClickItemListener;

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_first_level);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        loadData();
        mTitleTv.setText(mTitle);
        mOnClickItemListener = new IOnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                Intent intent = new Intent();
                intent.putExtra(KEY_OPTION_ITEM_RESULT_CODE, mSubs.get(position).mCode);
                intent.putExtra(KEY_OPTION_ITEM_RESULT_NAME, mSubs.get(position).mName);
                setResult(0, intent);
                OptionSecondLevelActivity.this.finish();
            }

            @Override
            public void onLongClickItem(int position) {

            }

        };
        mAdapter = new OptionSecondLevelAdapter(this, mOnClickItemListener);
        mAdapter.setData(mSubs);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);
    }

    private void loadData(){
        if(getIntent() != null && getIntent().getParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY) != null){
            mSubs = getIntent().getParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY);
            mTitle = getIntent().getStringExtra(KEY_TITLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
