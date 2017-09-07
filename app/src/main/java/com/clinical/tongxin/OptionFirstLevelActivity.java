package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.clinical.tongxin.adapter.OptionFirstLevelAdapter;
import com.clinical.tongxin.entity.OptionItemEntity;
import com.clinical.tongxin.inteface.IOnClickItemListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/21.
 */

public class OptionFirstLevelActivity extends BaseActivity {
    public final static String KEY_OPTION_ITEM_ENTITY = "key_option_item_entity";
    public final static String KEY_OPTION_ITEM_RESULT_CODE = "key_option_item_result_code";
    public final static String KEY_OPTION_ITEM_RESULT_NAME = "key_option_item_result_name";
    public final static String KEY_TITLE = "key_title";

    private Unbinder mUnbinder;
    @BindView(R.id.list_view)
    RecyclerView mListView;
    @BindView(R.id.title)
    TextView mTitleTv;

    private ArrayList<OptionItemEntity> mOptionItemEntity = new ArrayList<>();
    private OptionFirstLevelAdapter mAdapter;

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
                Intent intent;
                //跳转至子级列表
                if(mOptionItemEntity.get(position).mSubs.size() > 0) {
                    intent = new Intent(OptionFirstLevelActivity.this, OptionSecondLevelActivity.class);
                    intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, mOptionItemEntity.get(position).mSubs);
                    intent.putExtra(KEY_TITLE, mTitle);
                    startActivityForResult(intent, 0);
                }else{
                    intent = new Intent();
                    intent.putExtra(KEY_OPTION_ITEM_RESULT_CODE, mOptionItemEntity.get(position).mCode);
                    intent.putExtra(KEY_OPTION_ITEM_RESULT_NAME, mOptionItemEntity.get(position).mName);
                    setResult(0, intent);
                    OptionFirstLevelActivity.this.finish();
                }
            }

            @Override
            public void onLongClickItem(int position) {

            }
        };
        mAdapter = new OptionFirstLevelAdapter(this, mOnClickItemListener);
        mAdapter.setData(mOptionItemEntity);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);
    }

    private void loadData(){
        if(getIntent() != null && getIntent().getParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY) != null){
            mOptionItemEntity = getIntent().getParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY);
            mTitle = getIntent().getStringExtra(KEY_TITLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            setResult(0, data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
