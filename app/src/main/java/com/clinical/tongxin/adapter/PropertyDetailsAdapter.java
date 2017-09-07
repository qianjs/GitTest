package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.TaskPropertyListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class PropertyDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<TaskPropertyListEntity> list;
    private LayoutInflater inflater;

    public PropertyDetailsAdapter(Context context, List<TaskPropertyListEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        if (view == null) {
            view = inflater.inflate(R.layout.item_property_details, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        TaskPropertyListEntity model=list.get(i);
        viewHolder.txtOrderNumber.setText("交易号："+model.getNumber());
        viewHolder.txtOrderType.setText("交易类型："+model.getStatus());
        viewHolder.txtOrderTime.setText("交易时间："+model.getTradeTime());
        viewHolder.txtInMoney.setText("收入："+model.getIncome());
        viewHolder.txtPayMoney.setText("支出："+model.getExpenses());
        viewHolder.txtSum.setText("总额："+model.getBalance());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.txt_OrderNumber)
        TextView txtOrderNumber;
        @BindView(R.id.txt_orderType)
        TextView txtOrderType;
        @BindView(R.id.txt_OrderTime)
        TextView txtOrderTime;
        @BindView(R.id.txt_in_money)
        TextView txtInMoney;
        @BindView(R.id.txt_payMoney)
        TextView txtPayMoney;
        @BindView(R.id.txt_sum)
        TextView txtSum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
