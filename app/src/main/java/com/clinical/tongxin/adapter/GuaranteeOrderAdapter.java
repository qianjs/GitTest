package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.GuaranteeOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class GuaranteeOrderAdapter extends BaseAdapter {
    private List<GuaranteeOrderEntity> list;
    private LayoutInflater inflater;
    private OnGuaranteeStateListener listener;
    public GuaranteeOrderAdapter(Context context, List<GuaranteeOrderEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public void setOnGuaranteeStateListener(OnGuaranteeStateListener listener)
    {
        this.listener=listener;
    }
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_guaranteeorder, null);
            viewHolder.ll_gorder=(LinearLayout)convertView.findViewById(R.id.ll_gorder);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
            viewHolder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            viewHolder.btn_cancel=(Button)convertView.findViewById(R.id.btn_cancel);
            viewHolder.btn_pay=(Button)convertView.findViewById(R.id.btn_pay);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getDoctor());
        viewHolder.tv_project.setText(list.get(position).getProject());
        viewHolder.tv_date.setText(list.get(position).getDate());
        viewHolder.tv_price.setText(list.get(position).getPrice());
        if(list.get(position).getState().equals("0"))
        {
            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.GONE);
        }
        else if(list.get(position).getState().equals("1"))
        {
            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.btn_cancel.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
        }
        viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancel(list.get(position).getgId(),position);
            }
        });
        viewHolder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPay(list.get(position), position);
            }
        });
        viewHolder.ll_gorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetail(list.get(position));
            }
        });
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_name;
        TextView tv_project;
        TextView tv_date;
        TextView tv_price;
        Button btn_cancel;
        Button btn_pay;
        LinearLayout ll_gorder;
    }
    public interface OnGuaranteeStateListener
    {
        public void onCancel(String id,int position);
        public void onPay(GuaranteeOrderEntity model,int position);
        public void onDetail(GuaranteeOrderEntity model);
    }
}
