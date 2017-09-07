package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 选择支付方式
 * Created by 马骥 on 2016/8/11 0011.
 */
public class PayTypeActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rl_zhifubao, rl_weixin;
    private ImageView img_selectzfb, img_selectwx;
    private TextView tv_prices;
    private Button btn_pay;
    private double sumprice;//支付金额
    private int select = 1; // 1支付宝支付 0微信支付
    private int position;//此支付保单在待支付保单List中的position
    private String pid;//保单编号
    private String orderType="0";//订单类型 0保单 1订单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytype);
        initView();
        initListener();
    }
    private void initView()
    {
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("请选择支付方式");
        rl_zhifubao = (RelativeLayout) findViewById(R.id.rl_zhifubao);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        img_selectzfb = (ImageView) findViewById(R.id.img_selectzfb);
        img_selectwx = (ImageView) findViewById(R.id.img_selectwx);
        tv_prices = (TextView) findViewById(R.id.tv_prices);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        sumprice = getIntent().getDoubleExtra("sumprice", 0.00);
        position = getIntent().getIntExtra("position", -1);
        pid = getIntent().getStringExtra("pid");
        orderType=getIntent().getStringExtra("orderType");
        tv_prices.setText("¥" + sumprice);
        btn_pay.setText("确认支付" + sumprice + "元");
    }
    private void initListener()
    {
        rl_zhifubao.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rl_zhifubao:
            {
                select = 1;
                selectType();
            }
            break;
            case R.id.rl_weixin:
            {
                select = 0;
                selectType();
            }
            break;
            case R.id.btn_pay:
            {
                pay();
            }
            break;
        }
    }
    private void selectType() {
        if (select == 1) {
            img_selectzfb
                    .setImageResource(R.mipmap.game_player_coupon_selected);
            img_selectwx
                    .setImageResource(R.mipmap.game_player_coupon_unselected);
        } else {
            img_selectwx
                    .setImageResource(R.mipmap.game_player_coupon_selected);
            img_selectzfb
                    .setImageResource(R.mipmap.game_player_coupon_unselected);
        }
    }
    private void pay()
    {
        if(orderType.equals("0")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", getLoginUserSharedPre().getString("userId", ""));
            map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
            map.put("pid", pid);
            map.put("operation", "1");
            map.put("payType", String.valueOf(select));

            XUtil.Post(UrlUtils.URL_PolicyEdit, map, new MyCallBack<String>() {

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    // TODO Auto-generated method stub
                    super.onError(arg0, arg1);
                }

                @Override
                public void onSuccess(String json) {

                    // TODO Auto-generated method stub
                    super.onSuccess(json);
                    ResultJsonP myjson = Utils.wsJsonToModel(json);
                    if (myjson != null) {
                        Intent intent = new Intent("guarantee_order_pay_success");
                        intent.putExtra("position", position);
                        sendBroadcast(intent);
                        finish();
                    }

                }
            });
        }
        else
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", getLoginUserSharedPre().getString("userId", ""));
            map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
            map.put("oId", pid);

            XUtil.Post(UrlUtils.URL_PayOrder, map, new MyCallBack<String>() {

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    // TODO Auto-generated method stub
                    super.onError(arg0, arg1);
                }

                @Override
                public void onSuccess(String json) {

                    // TODO Auto-generated method stub
                    super.onSuccess(json);
                    ResultJsonP myjson = Utils.wsJsonToModel(json);
                    if (myjson != null) {
                        Intent intent = new Intent("order_pay_success");
                        intent.putExtra("position", position);
                        sendBroadcast(intent);
                        finish();
                    }

                }
            });
        }
    }
}
