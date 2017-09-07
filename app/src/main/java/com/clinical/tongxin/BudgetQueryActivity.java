package com.clinical.tongxin;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.clinical.tongxin.adapter.BudgetQueryAdapter;
import com.clinical.tongxin.entity.BudgetQueryEntitity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by Administrator on 2017/1/5 0005.
 */

public class BudgetQueryActivity extends BaseActivity {
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<BudgetQueryEntitity> mList;
    private BudgetQueryAdapter mAdapter;

    private String date1;
    private String sortId="0";
    private TextView txt_date;
    TimePickerView pvTime;
    View vMasker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_budget);
        initView();
        initListener();
        initPickerView();
        initData("0");
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("预算查询");
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        date1= Utils.GetDateNow("yyyy-MM");
        mListView = (ListView) findViewById(R.id.load_more_listview);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 20)));
        mList = new ArrayList<BudgetQueryEntitity>();
        mAdapter = new BudgetQueryAdapter(this,mList );
        mListView.setAdapter(mAdapter);
        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
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
                String sortId="<"+mList.get(mList.size()-1).getSortId();
                initData(sortId);
            }

        });
        txt_date= (TextView) findViewById(R.id.txt_date);
        txt_date.setText(date1);
    }




    private void initListener() {
        //时间点击事件
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

    }




    private void initData(String sortId) {
        if (date1.equals("")){
            Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Date",date1);
        map.put("sort","");
        map.put("order","");
        map.put("searchPMList","");
        map.put("SortId",sortId);
        XUtil.Post(UrlUtils.URl_ShowAppBudgetList,map,new MyCallBack<String>(){
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
//                     是否还有数据
                    boolean ishave = false;
                    // 数据是否为空
                    boolean isnull = false;
                    List<BudgetQueryEntitity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<BudgetQueryEntitity>>() {
                    }.getType());
                    if (null != list) {
                        if (list.size() > 0) {
                            ishave = true;
                        }
                        for (BudgetQueryEntitity model : list) {
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
                }
                mDialog.dismiss();
            }
        });
    }

    //显示时间表
    private void initPickerView() {
        vMasker = findViewById(R.id.vMasker);
        // 控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
        pvTime.setRange(calendar.get(Calendar.YEAR)-20, calendar.get(Calendar.YEAR) + 20);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String date2 = Utils.getYearMonthDay(date);
                String str[] = date2.split("");
                date1=str[0]+str[1]+str[2]+str[3]+str[4]+str[5]+str[6]+str[7];
                txt_date.setText(date1);
                mList.clear();
                initData("0");
            }
        });
    }
}
