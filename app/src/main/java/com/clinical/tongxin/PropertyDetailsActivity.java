package com.clinical.tongxin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clinical.tongxin.adapter.PropertyDetailsAdapter;
import com.clinical.tongxin.entity.PropertyRechageDetailsEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TaskPropertyEntity;
import com.clinical.tongxin.entity.TaskPropertyListEntity;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资产详情
 * Created by Administrator on 2017/1/3 0003.
 */

public class PropertyDetailsActivity extends BaseActivity {
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.ll_property_details)
    LinearLayout llPropertyDetails;
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
    @BindView(R.id.txt_TradeType)
    TextView txtTradeType;
    @BindView(R.id.txt_DealNumber)
    TextView txtDealNumber;
    @BindView(R.id.txt_PayPrice)
    TextView txtPayPrice;
    @BindView(R.id.txt_Charge)
    TextView txtCharge;
    @BindView(R.id.txt_SumPrice)
    TextView txtSumPrice;
    @BindView(R.id.txt_TradeTime)
    TextView txtTradeTime;
    @BindView(R.id.ll_recharge)
    LinearLayout ll_Recharge;

    private TextView txtProjectName;
    private TextView txtNickName;
    private TextView txtOrderTotal;
    private TextView txt_PayPrice;
    private TextView txtOrderNumber;

    private String BalanceDetailsId = "";
    private String TradeType = "";
    // 进度
    private MyProgressDialog mDialog;
    private PropertyDetailsAdapter adapter;
    private List<TaskPropertyListEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        ButterKnife.bind(this);
        initView();
        initListener();
        initData();

    }


    private void initView() {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        title.setText("交易详情");
        BalanceDetailsId = getIntent().getExtras().getString("BalanceDetailsId");
        TradeType = getIntent().getExtras().getString("TradeType");
        View view = LayoutInflater.from(this).inflate(R.layout.listview_property_head_details, null);
        txtProjectName = (TextView) view.findViewById(R.id.txt_ProjectName);
        txtNickName = (TextView) view.findViewById(R.id.txt_NickName);
        txt_PayPrice = (TextView) view.findViewById(R.id.txt_PayPrice);
        txtOrderNumber = (TextView) view.findViewById(R.id.txt_OrderNumber);
        listView.addHeaderView(view);
        list = new ArrayList<>();
        adapter = new PropertyDetailsAdapter(this, list);
        listView.setAdapter(adapter);
    }


    private void initListener() {

    }

    //资产详情
    private void initData() {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        String url = "";
        if (TradeType.equals("0") || TradeType.equals("5")) {
            map.put("BalanceDetailsId", BalanceDetailsId);
            url = UrlUtils.URL_getBalanceDetailApp;
        } else {
            map.put("OrderId", BalanceDetailsId);
            url = UrlUtils.URL_queryOrdersInfoApp;
        }

        XUtil.Get(url, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1 = Utils.wsJsonToModel1(arg0);
                if (TradeType.equals("0") || TradeType.equals("5")) {
                    PropertyRechageDetailsEntity model=new Gson().fromJson(resultJsonP1.getResult(),PropertyRechageDetailsEntity.class);
                    txtTradeType.setText(model.getTradeType());
                    txtDealNumber.setText(model.getNumber());
                    txtPayPrice.setText(model.getPayPrice());
                    txtCharge.setText(model.getCharge());
                    txtSumPrice.setText(model.getBalance());
                    txtTradeTime.setText(model.getTradeTime());
                    ll_Recharge.setVisibility(View.VISIBLE);
                    llPropertyDetails.setVisibility(View.GONE);
                } else {
                    TaskPropertyEntity model = new Gson().fromJson(resultJsonP1.getResult(), new TypeToken<TaskPropertyEntity>() {
                    }.getType());
                    ll_Recharge.setVisibility(View.GONE);
                    llPropertyDetails.setVisibility(View.VISIBLE);
                    txtProjectName.setText(model.getProjectName());
                    txtOrderNumber.setText("订单编号：" + model.getOrderNumber());
                    txtOrderNumber.setVisibility(View.GONE);
                    txtNickName.setText(model.getRealName() + "(" + model.getMobile() + ")");
                    txt_PayPrice.setText(model.getOrderTotal());
                    list.addAll(model.getProperty());
                    adapter.notifyDataSetChanged();
                }
//                if (resultJsonP1.getCode().equals("200")){
//                    try {
//                        JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());
//
//                        //状态
//                        int status=jsonObject.getInt("state");
//                        switch (status){
//                            case 0:
//                            {
//                                txt_state.setText("交易成功");
//                                ll_bankName.setVisibility(View.VISIBLE);
//                                ll_bankNumber.setVisibility(View.VISIBLE);
//                            }
//                            break;
//                            case 1:
//                            {
//                                txt_state.setText("交易成功");
//                                ll_bankName.setVisibility(View.GONE);
//                                ll_bankNumber.setVisibility(View.GONE);
//                            }
//                            break;
//                            case 2:
//                            {
//                                txt_state.setText("交易失败");
//                                ll_bankName.setVisibility(View.GONE);
//                                ll_bankNumber.setVisibility(View.GONE);
//
//                            }
//                            break;
//                        }
//                        //交易类型
//                        txt_TradeType.setText(Utils.getMyString(jsonObject.getString("TradeType"),"--"));
//                        //银行卡号
//                        txt_BankNumber.setText(Utils.getMyString(jsonObject.getString("Number"),"--"));
//                        //银行卡名称
//                        txt_BankName.setText(Utils.getMyString(jsonObject.getString("BankBranchName"),"--"));
//                        //交易号
//                        txt_DealNumber.setText(jsonObject.getString("DealNumber"));
//                        //实付金额
//                        txt_PayPrice.setText(Utils.getMyString(jsonObject.getString("PayPrice"),"0.0"));
//                        //服务费
//                        txt_Charge.setText(Utils.getMyString(jsonObject.getString("Charge"),"0.0"));
//                        //总额
//                        txt_SumPrice.setText(Utils.getMyString(jsonObject.getString("SumPrice"),"0.0"));
//                        //对方昵称
//                        txt_NickName.setText(jsonObject.getString("NickName")+"("+jsonObject.getString("Mobile")+")");
//                        //交易时间
//                        txt_TradeTime.setText(jsonObject.getString("TradeTime"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    Toast.makeText(PropertyDetailsActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
//                }
                mDialog.dismiss();
            }
        });
    }
}
