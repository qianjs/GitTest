package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ServiceFragmentAdapter;
import com.clinical.tongxin.entity.FragmentEntity;
import com.clinical.tongxin.entity.ItemFragmententity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TrainVideoTypeInfoEntity;
import com.clinical.tongxin.inteface.MyCallBack;
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
 * Created by Administrator on 2017/7/11 0011.
 */

public class SerciveFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listView)
    ListView listView;
    private View view;
    // 进度
    private MyProgressDialog mDialog;
    private ServiceFragmentAdapter adapter;
    private List<FragmentEntity> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    private void initView() {
        title.setText("服务");
        list = new ArrayList<>();
        adapter = new ServiceFragmentAdapter(list, getActivity());
        listView.setAdapter(adapter);
        adapter.setItemGridViewAndListViewListener(new ServiceFragmentAdapter.ItemGridViewAndListViewListener() {
            @Override
            public void itemclick(ItemFragmententity itemFragmententity) {
                if("培训".equals(itemFragmententity.getContentName())){
                    //获取视频列表
                    getVideoList();
                }else {
                    if ("text".equals(itemFragmententity.getLinkType())){
                        Intent intent = new Intent(getActivity(), ServiceTextActivity.class);
                        intent.putExtra("content", itemFragmententity.getContent());
                        intent.putExtra("contentName", itemFragmententity.getContentName());
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), WebServiceActivity.class);
                        intent.putExtra("url", itemFragmententity.getLinkAddress());
                        intent.putExtra("ContentName", itemFragmententity.getContentName());
                        startActivity(intent);
                    }

                }

            }
        });


    }


    private void initData() {
        mDialog = new MyProgressDialog(getActivity(), "请稍后...");
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", ((BaseActivity) getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity) getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Post(UrlUtils.URL_queryServiceNavigationList, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);

                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {

                    List<FragmentEntity> list1 = new Gson().fromJson(myjson.getResult(), new TypeToken<List<FragmentEntity>>() {
                    }.getType());
                    list.clear();
                    list.addAll(list1);
                    adapter.notifyDataSetChanged();
                }
                mDialog.dismiss();
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取视频列表
     */
    private void getVideoList(){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("count", "6");
        XUtil.Get(UrlUtils.URL_TRAIN_VIDEO_ALL, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultJsonP1 myjson = Utils.wsJsonToModel1(result);
                if (myjson.getCode().equals("200")) {
                    Gson gson = new Gson();
                    List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> list = gson.fromJson(myjson.getResult(), new TypeToken<List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity>>() {
                    }.getType());
                    Intent intent = new Intent(SerciveFragment.this.getContext(), AllVideoListActivity.class);
                    intent.putParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST, (ArrayList<? extends Parcelable>) list);
                    startActivity(intent);
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SerciveFragment.this.getContext(), "获取视频失败，请重试。", Toast.LENGTH_SHORT).show();
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
