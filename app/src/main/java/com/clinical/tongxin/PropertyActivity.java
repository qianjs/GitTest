package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.clinical.tongxin.adapter.PropertyAdapter;
import com.clinical.tongxin.entity.PropertyEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

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
 * 资产
 * Created by Administrator on 2016/12/30 0030.
 */

public class PropertyActivity extends BaseActivity implements View.OnClickListener {
    // 进度
    private MyProgressDialog mDialog;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<PropertyEntity> mList;
    private PropertyAdapter mAdapter;
    //余额;立即充值
    private TextView txt_money,txt_PayMoney;
    //时间筛选，订单状态筛选;交易类型
    private LinearLayout ll_date, ll_status, ll_Transaction_type;
    private TextView txt_year, txt_mouth, txt_status, txt_Transaction_type;
    TimePickerView pvTime;
    View vMasker;
    private String date1;
    private PopwindowProperty popwindow, popwindow1;
    private ImageView img_head;
    private String tradeTypeId="",State="0";
    private String sumMoney="0.0";
    private LinearLayout ll_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        initView();
        initListener();
        initData("0");
        initPickerView();

    }

    @Override
    protected void onResume() {
        if (!"项目经理".equals(getLoginConfig().getRole())){
            getMoney();
        }
        super.onResume();
    }

    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title= (TextView) findViewById(R.id.title);
        title.setText("我的资产");
        mList = new ArrayList<PropertyEntity>();
        date1= Utils.GetDateNow("yyyy-MM");
        mListView = (ListView) findViewById(R.id.load_more_listview);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 2)));
        mListView.addHeaderView(headerMarginView);
        mAdapter = new PropertyAdapter(this, mList);
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
        ll_date = (LinearLayout) findViewById(R.id.ll_date);
        ll_status = (LinearLayout) findViewById(R.id.ll_status);
        ll_Transaction_type = (LinearLayout) findViewById(R.id.ll_Transaction_type);

        txt_year = (TextView) findViewById(R.id.txt_year);
        txt_mouth = (TextView) findViewById(R.id.txt_mouth);
        String str1[] = date1.split("");
        txt_year.setText(str1[1] + str1[2] + str1[3] + str1[4]);
        txt_mouth.setText(str1[6] + str1[7] + "月");
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_Transaction_type = (TextView) findViewById(R.id.txt_Transaction_type);
        txt_money = (TextView) findViewById(R.id.txt_money);
        txt_PayMoney = (TextView) findViewById(R.id.txt_PayMoney);
        img_head= (ImageView) findViewById(R.id.img_head);
        String url=getLoginUserSharedPre().getString("url", "");
        if (!url.equals(""))
        ImageLoader.getInstance().displayImage(url, img_head, MyApplication.roundedOption);
        ll_money= (LinearLayout) findViewById(R.id.ll_money);
        if ("项目经理".equals(getLoginConfig().getRole())){
            ll_money.setVisibility(View.GONE);
        }else{
            ll_money.setVisibility(View.VISIBLE);
        }

    }

    private void initListener() {
        ll_date.setOnClickListener(this);
        ll_status.setOnClickListener(this);
        txt_PayMoney.setOnClickListener(this);
        ll_Transaction_type.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PropertyEntity model= (PropertyEntity) adapterView.getItemAtPosition(i);
                if (model!=null){
                    Intent intent=new Intent(PropertyActivity.this,PropertyDetailsActivity.class);
                    intent.putExtra("BalanceDetailsId",model.getBalanceDetailsId());
                    intent.putExtra("TradeType",model.getTradeType());
                    startActivity(intent);
                }
            }
        });
    }

    //获取余额
    private void getMoney() {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Get(UrlUtils.URl_ShowAppDirectororg, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1=Utils.wsJsonToModel1(arg0);
                if (resultJsonP1.getCode().equals("200")){
                    try {
                        JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());
                        sumMoney=jsonObject.getString("Amount");
                        txt_money.setText(sumMoney);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(PropertyActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });

    }

    //获取资产列表数据
    private void initData(final String sortId) {
        if (date1.equals("")){
            Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("Time",date1);
        map.put("TradeType",tradeTypeId);
//        map.put("State",State);
        map.put("sortId",sortId);
        XUtil.Post(UrlUtils.URl_getMyBalanceDetails_app,map,new MyCallBack<String>(){
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
                    List<PropertyEntity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<PropertyEntity>>() {
                    }.getType());
                    if (null != list) {
                        if (list.size() > 0) {
                            ishave = true;
                        }
                        if(sortId.equals("0"))
                        {
                            mList.clear();
                        }
                        for (PropertyEntity model : list) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //选择时间
            case R.id.ll_date: {
                pvTime.show();


            }
            break;
            //订单状态
            case R.id.ll_status: {
                if (popwindow == null) {
                    popwindow = new PopwindowProperty(this);
                    popwindow.showList(PopwindowEnum.SetUpProperty.OrderStatus);
                    popwindow.setOnOKClickListener(new PopwindowProperty.OnOKClickListener() {
                        @Override
                        public void OnOKClick(String Name, String Id) {
                            txt_status.setText(Name);
                            State=Id;
                            initData("0");
                        }
                    });
                    popwindow.showAsDropDown(ll_status);
                } else {
                    popwindow.showAsDropDown(ll_status);
                }

            }
            break;
            //交易类型
            case R.id.ll_Transaction_type: {
                if (popwindow1 == null) {
                    popwindow1 = new PopwindowProperty(this);
                    popwindow1.showList(PopwindowEnum.SetUpProperty.Transaction_type);
                    popwindow1.setOnOKClickListener(new PopwindowProperty.OnOKClickListener() {
                        @Override
                        public void OnOKClick(String Name, String Id) {
                            txt_Transaction_type.setText(Name);
                            tradeTypeId=Id;
                            initData("0");
                        }
                    });
                    popwindow1.showAsDropDown(ll_status);
                } else {
                    popwindow1.showAsDropDown(ll_status);
                }
            }
            break;
            //立即支付
            case R.id.txt_PayMoney:
            {
                Intent intent=new Intent(this,RechargeActivity.class);
                intent.putExtra("property",sumMoney);
                startActivity(intent);
            }
            break;
        }

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
//                if (date.getTime() < new Date().getTime()) {
//                    Toast.makeText(PropertyActivity.this,
//                            "选择的有效期不能小于当前时间", Toast.LENGTH_SHORT).show();
//                } else {
                String date2 = Utils.getYearMonthDay(date);
                String str[] = date2.split("");
                date1=str[0]+str[1]+str[2]+str[3]+str[4]+str[5]+str[6]+str[7];
                txt_year.setText(str[1] + str[2] + str[3] + str[4]);
                txt_mouth.setText(str[6] + str[7] + "月");
                initData("0");
//                }
            }
        });
    }

}
