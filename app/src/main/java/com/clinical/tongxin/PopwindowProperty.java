package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clinical.tongxin.adapter.PopPropertyAdapter;
import com.clinical.tongxin.entity.BankEntity;
import com.clinical.tongxin.entity.PopwindowPropertyEntitity;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.clinical.tongxin.PopwindowEnum.SetupPayMode.Bank;
import static com.clinical.tongxin.R.id.list;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

public class PopwindowProperty extends PopupWindow{
    private LayoutInflater layoutInflater;
    private View conentView;
    private EditText et_price;
    private TextView tv_OK;
    private ListView listView;
    private Context context;
    private Gson gson;
    private String province = "";
    private String BankName = "";
    private String cityName = "";
    private MyProgressDialog dialog;
    private List<PopwindowPropertyEntitity> list;
    private PopPropertyAdapter adapter;
    private OnOKClickListener listener;
    public PopwindowProperty(Context context){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        conentView=layoutInflater.inflate(R.layout.item_listview, null);
        gson = new Gson();
        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
    }

    private void initView() {
        list = new ArrayList<PopwindowPropertyEntitity>();
        listView = (ListView) conentView.findViewById(R.id.listview);
        adapter = new PopPropertyAdapter(context, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopwindowPropertyEntitity model = (PopwindowPropertyEntitity) parent.getItemAtPosition(position);
                if (model != null) {
                    listener.OnOKClick(model.getName(),model.getId());
                    dismiss();
                }
            }
        });
    }
    //初始化数据
    public void showList(PopwindowEnum.SetUpProperty popwindowEnum) {
        switch (popwindowEnum) {
            case OrderStatus:
            {
                getOrderStatus();
            }
            break;
            case Transaction_type:
            {
                getTransactionType();
            }
            break;
        }

    }
    //
    private void getTransactionType(){
        PopwindowPropertyEntitity model=new PopwindowPropertyEntitity();
        model.setName("充值");
        model.setId("0");
        list.add(model);
        PopwindowPropertyEntitity model1=new PopwindowPropertyEntitity();
        model1.setName("提现");
        model1.setId("1");
        list.add(model1);
        PopwindowPropertyEntitity model2=new PopwindowPropertyEntitity();
        model2.setName("发布任务");
        model2.setId("2");
        list.add(model2);
        PopwindowPropertyEntitity model3=new PopwindowPropertyEntitity();
        model3.setName("接收人");
        model3.setId("3");
        list.add(model3);
        adapter.notifyDataSetChanged();
    }
    //获取订单状态
    private void getOrderStatus(){
        PopwindowPropertyEntitity model0=new PopwindowPropertyEntitity();
        model0.setName("全部");
        model0.setId("0");
        list.add(model0);
        PopwindowPropertyEntitity model=new PopwindowPropertyEntitity();
        model.setName("进行中");
        model.setId("1");
        list.add(model);
        PopwindowPropertyEntitity model1=new PopwindowPropertyEntitity();
        model1.setName("待验收");
        model1.setId("2");
        list.add(model1);
        PopwindowPropertyEntitity model2=new PopwindowPropertyEntitity();
        model2.setName("待支付");
        model2.setId("3");
        list.add(model2);
        PopwindowPropertyEntitity modelT=new PopwindowPropertyEntitity();
        modelT.setName("退款");
        modelT.setId("4");
        list.add(modelT);
        PopwindowPropertyEntitity model3=new PopwindowPropertyEntitity();
        model3.setName("已完成");
        model3.setId("5");
        list.add(model3);
        PopwindowPropertyEntitity model4=new PopwindowPropertyEntitity();
        model4.setName("失败");
        model4.setId("6");
        list.add(model4);
        adapter.notifyDataSetChanged();

    }


    public void setOnOKClickListener(OnOKClickListener listener) {
        this.listener = listener;
    }

    public interface OnOKClickListener {
        public void OnOKClick(String price,String Id);
    }
}
