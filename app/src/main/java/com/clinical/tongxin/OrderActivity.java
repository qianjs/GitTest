package com.clinical.tongxin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.OrderAdapter;
import com.clinical.tongxin.entity.OrderEntity;
import com.clinical.tongxin.entity.ResultJsonP;
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
 * Created by Administrator on 2016/7/30 0030.
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener{
    // 进度
    private MyProgressDialog mDialog;
    private BroadcastReceiver receiver = new GOrderReceiver();
    // private PullToRefreshListView listview;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<OrderEntity> mList;
    private OrderAdapter mAdapter;
    private String state="0";//订单状态
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        initView();
        initListener();
        findOrder("0");
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("我的订单");
        mList=new ArrayList<OrderEntity>();
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        tv7=(TextView)findViewById(R.id.tv7);
        tv1.setSelected(true);
        //定义广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("order_pay_success");
        registerReceiver(receiver, intentFilter);
        mListView = (ListView) findViewById(R.id.load_more_listview);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 20)));
        //mListView.addHeaderView(headerMarginView);

        mAdapter = new OrderAdapter(this, mList);
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
                findOrder("0");
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
                // String maxid = mList.get(mList.size() - 1).getSortid();
                // findAppraise(maxid,"less","false");
                String maxid = mList.get(mList.size() - 1).getSort_id();
                findOrder("-"+maxid);
            }
        });
    }
    private void initListener()
    {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
//                ExpertEntity model = (ExpertEntity) arg0.getItemAtPosition(arg2);
//                showToast(model.getName());
//                if (model != null) {
//                    Intent intent = new Intent(ActivityPublicWorkOrder.this, ActivityWorkOrderDetail.class);
//                    intent.putExtra("id", model.getSortid());
//                    intent.putExtra("ButtonSelect", ButtonSelect.Orders);
//                    startActivity(intent);
//                }
            }
        });
        mAdapter.setOnOrderClickListener(new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onCancel(String id, int position) {
                OrderCancel(id,position);
            }

            @Override
            public void onPay(OrderEntity model, int position) {
                OrderPay(model,position);
            }

            @Override
            public void onDetail(OrderEntity model) {

            }

            @Override
            public void onApplyRefunds(String id, int position) {
                OrderApplyRefunds(id,position);
            }

            @Override
            public void onApplyResolve(String id, int position) {
                OrderApplyResolve(id,position);
            }
        });
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
    }
    private void findOrder(String sortid)
    {
        mDialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey",getLoginUserSharedPre().getString("ukey", ""));
        map.put("state",state);
        map.put("sort_id",sortid);
        map.put("newset","true");

        XUtil.Get(UrlUtils.URL_FindOrder, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP myjson = Utils.wsJsonToModel(json);
                if (myjson != null) {
                    // 是否还有数据
                    boolean ishave = false;
                    // 数据是否为空
                    boolean isnull = false;
                    List<OrderEntity> list = new Gson().fromJson(myjson.getI().getList(), new TypeToken<List<OrderEntity>>() {
                    }.getType());
                    //List<GuaranteeOrderEntity> list= (List<GuaranteeOrderEntity>) new Gson().fromJson(str,GuaranteeOrderEntity.class);
                    if (null != list) {
                        if (list.size() > 0) {
                            ishave = true;
                        }

                        for (OrderEntity model : list) {
                            model.setState(state);
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
                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
//        OrderEntity model=new OrderEntity();
//        model.setSort_id("1");
//        model.setDoctor("张爱萍");
//        model.setoDate("2015-03-24");
//        model.setoId("1");
//        model.setPrices("768.00");
//        model.setProject("隆胸、双眼皮");
//        mList.add(model);
//        mPtrFrameLayout.refreshComplete();
//        mLoadMoreListViewContainer.loadMoreFinish(false, true);
//        mAdapter.notifyDataSetChanged();
    }
    private void OrderCancel(String id, final int position)
    {
        mDialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey",getLoginUserSharedPre().getString("ukey", ""));
        map.put("oId",id);

        XUtil.Post(UrlUtils.URL_CancelOrder, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP myjson = Utils.wsJsonToModel(json);
                if(myjson!=null) {
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }
    private void OrderPay(OrderEntity model, final int position)
    {
        Intent intent=new Intent(OrderActivity.this,PayTypeActivity.class);
        intent.putExtra("sumprice", Double.valueOf(model.getPrices()));
        intent.putExtra("position", position);
        intent.putExtra("pid", model.getoId());
        intent.putExtra("orderType", "1");
        startActivity(intent);
    }
    private void OrderApplyRefunds(String id, int position)
    {
        Intent intent=new Intent(OrderActivity.this,OrderOperateActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("oid", id);
        startActivity(intent);
    }
    private void OrderApplyResolve(String id, int position)
    {
        Intent intent=new Intent(OrderActivity.this,OrderOperateActivity.class);
        intent.putExtra("type", "1");
        intent.putExtra("oid", id);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv1:
            {
                if(!state.equals("0")) {
                    tv1.setSelected(true);
                    tv2.setSelected(false);
                    tv3.setSelected(false);
                    tv4.setSelected(false);
                    tv5.setSelected(false);
                    tv6.setSelected(false);
                    tv7.setSelected(false);
                    state = "0";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv2:
            {
                if(!state.equals("1")) {
                    tv1.setSelected(false);
                    tv2.setSelected(true);
                    tv3.setSelected(false);
                    tv4.setSelected(false);
                    tv5.setSelected(false);
                    tv6.setSelected(false);
                    tv7.setSelected(false);
                    state = "1";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv3:
            {
                if(!state.equals("2")) {
                    tv1.setSelected(false);
                    tv2.setSelected(false);
                    tv3.setSelected(true);
                    tv4.setSelected(false);
                    tv5.setSelected(false);
                    tv6.setSelected(false);
                    tv7.setSelected(false);
                    state = "2";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv4:
            {
                if(!state.equals("3")) {
                    tv1.setSelected(false);
                    tv2.setSelected(false);
                    tv3.setSelected(false);
                    tv4.setSelected(true);
                    tv5.setSelected(false);
                    tv6.setSelected(false);
                    tv7.setSelected(false);
                    state = "3";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv5:
            {
                if(!state.equals("4")) {
                    tv1.setSelected(false);
                    tv2.setSelected(false);
                    tv3.setSelected(false);
                    tv4.setSelected(false);
                    tv5.setSelected(true);
                    tv6.setSelected(false);
                    tv7.setSelected(false);
                    state = "4";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv6:
            {
                if(!state.equals("5")) {
                    tv1.setSelected(false);
                    tv2.setSelected(false);
                    tv3.setSelected(false);
                    tv4.setSelected(false);
                    tv5.setSelected(false);
                    tv6.setSelected(true);
                    tv7.setSelected(false);
                    state = "5";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
            case R.id.tv7:
            {
                if(!state.equals("6")) {
                    tv1.setSelected(false);
                    tv2.setSelected(false);
                    tv3.setSelected(false);
                    tv4.setSelected(false);
                    tv5.setSelected(false);
                    tv6.setSelected(false);
                    tv7.setSelected(true);
                    state = "6";
                    mList.clear();
                    findOrder("0");
                }
            }
            break;
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(this.receiver);
        super.onDestroy();
    }

    public class GOrderReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action="";
            try
            {
                action=intent.getAction();
                if(action.equals("order_pay_success"))
                {
                    int position=intent.getIntExtra("position",-1);
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
            catch (Exception ex)
            {

            }
        }
    }
}
