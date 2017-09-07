package com.clinical.tongxin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clinical.tongxin.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class DoTaskActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.lv_task)
    ListView lvTask;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @BindView(R.id.load_more_list_view_container)
    LoadMoreListViewContainer loadMoreListViewContainer;
    @BindView(R.id.load_more_list_view_ptr_frame)
    PtrClassicFrameLayout loadMoreListViewPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_6, R.id.tv_7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                ToastUtils.showToast(context,"==========1=======");
                break;
            case R.id.tv_2:
                ToastUtils.showToast(context,"==========2=======");
                break;
            case R.id.tv_3:
                ToastUtils.showToast(context,"==========3=======");
                break;
            case R.id.tv_4:
                ToastUtils.showToast(context,"==========4=======");
                break;
            case R.id.tv_5:
                ToastUtils.showToast(context,"==========5=======");
                break;
            case R.id.tv_6:
                ToastUtils.showToast(context,"==========6=======");
                break;
            case R.id.tv_7:
                break;
        }
    }
}
