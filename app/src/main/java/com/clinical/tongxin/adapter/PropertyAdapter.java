package com.clinical.tongxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.PropertyEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private List<PropertyEntity> list;
    private LayoutInflater inflater;
    public PropertyAdapter(Context context,List<PropertyEntity> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view=inflater.inflate(R.layout.item_property_adapter,null);
            viewHolder=new ViewHolder();
//            viewHolder.txt_date= (TextView) view.findViewById(R.id.txt_date);
//            viewHolder.txt_orderNumber= (TextView) view.findViewById(R.id.txt_orderNumber);
            viewHolder.txt_tradingNumber= (TextView) view.findViewById(R.id.txt_tradingNumber);
            viewHolder.txt_myName= (TextView) view.findViewById(R.id.txt_myName);
            viewHolder.txt_exchangeName= (TextView) view.findViewById(R.id.txt_exchangeName);
            viewHolder.txt_money= (TextView) view.findViewById(R.id.txt_money);
            viewHolder.txt_status= (TextView) view.findViewById(R.id.txt_status);
            viewHolder.txt_createDate= (TextView) view.findViewById(R.id.txt_createDate);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        PropertyEntity model=list.get(i);
//        viewHolder.txt_date.setText(model.getDay()+"日");
        if (TextUtils.isEmpty(model.getOrderNumber())) {
            viewHolder.txt_orderNumber.setVisibility(View.GONE);
        }
//        }else {
//            viewHolder.txt_orderNumber.setText("订单号："+model.getOrderNumber());
//        }
        viewHolder.txt_tradingNumber.setText(model.getOrderNumber());;
        viewHolder.txt_myName.setText("名称："+model.getMyName());
        if (Utils.getMyString(model.getMyName(),"").equals("")){
            viewHolder.txt_myName.setVisibility(View.GONE);
        }else{
            viewHolder.txt_myName.setVisibility(View.VISIBLE);
        }

        viewHolder.txt_exchangeName.setText("操作人："+model.getExchanageName());
        viewHolder.txt_money.setText("金额："+model.getMoney());
        viewHolder.txt_status.setText("状态："+model.getStatus());
        viewHolder.txt_createDate.setText(model.getCreateDate());

        return view;
    }

    class ViewHolder{
        TextView txt_date,txt_orderNumber,
                 txt_tradingNumber,txt_myName,txt_exchangeName,
                 txt_money,txt_status,txt_createDate;
    }
}
