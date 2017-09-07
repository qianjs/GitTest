package com.clinical.tongxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeDescriptionActivity extends BaseActivity {
    public final static String KEY_DESCRIPTION_TEXT = "key_description_text";
    public final static String KEY_TITLE = "key_title";

    @BindView(R.id.menu_tv)
    TextView mMenuTv;
    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.description_edt)
    TextView mDescriptionEdt;

    private Unbinder mUnbinder;
    private String mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_description);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initData(){
        loadData();
        mMenuTv.setText("完成");
        mTitleTv.setText(mTitle);
    }

    private void loadData(){
        if(getIntent() != null){
            mDescriptionEdt.setText(getIntent().getStringExtra(KEY_DESCRIPTION_TEXT));
            mTitle = getIntent().getStringExtra(KEY_TITLE);
        }
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTextView(View view){
        Intent intent = new Intent();
        intent.putExtra(KEY_DESCRIPTION_TEXT, mDescriptionEdt.getText().toString());
        setResult(0, intent);

        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        finish();
    }
}
