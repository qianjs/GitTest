package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ManagerAdapter;
import com.clinical.tongxin.entity.ManagerEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 经理预算查询
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManagerActivity extends BaseActivity {
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private ListView mListView;
    private List<ManagerEntity> mList;
    private ManagerAdapter mAdapter;
    private ImageView iv_more, img_back;
    private PtrFrameLayout mPtrFrameLayout;
    private String SortId="";
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        initView();
        initListener();
        initData("0");
    }


    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText("预算查询");
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");

        mListView = (ListView) this.findViewById(R.id.load_more_listview1);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 20)));
        mList = new ArrayList<ManagerEntity>();
        mAdapter = new ManagerAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) this.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                mList.clear();
                initData("0");
            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                SortId="<"+mList.get(mList.size()-1).getSortId();
                initData(SortId);
            }

        });
    }


    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ManagerEntity model= (ManagerEntity) adapterView.getItemAtPosition(i);
                if (model!=null){
                    Intent intent=new Intent(ManagerActivity.this,ManagerDetailsActivity.class);
                    intent.putExtra("BudgetId",model.getBudgetId());
                    intent.putExtra("Date",model.getAllocationsDate());
                    startActivity(intent);
                }
            }
        });
    }


    //获取群组成员列表数据
    private void initData(String sortId) {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("order", "");
        map.put("sort", "");
        map.put("SortId", sortId);
        XUtil.Post(UrlUtils.URL_ShowAppMyBudgetList,map,new MyCallBack<String>(){
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
                    if (myjson.getCode().equals("200")){
                        //                     是否还有数据
                        boolean ishave = false;
                        // 数据是否为空
                        boolean isnull = false;
                        List<ManagerEntity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<ManagerEntity>>() {
                        }.getType());
                        if (null != list) {
                            if (list.size() > 0) {
                                ishave = true;
                            }
                            for (ManagerEntity model : list) {
                                mList.add(model);
                            }
                        } else {
                            ishave = false;
                        }
                        mPtrFrameLayout.refreshComplete();
                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (mList.size() <= 0) {
                            isnull = true;
                        }
                        mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ManagerActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
                mDialog.dismiss();

            }
        });

    }
}
