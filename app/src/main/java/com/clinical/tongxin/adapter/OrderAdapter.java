package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.OrderEntity;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class OrderAdapter extends BaseAdapter {


    private List<OrderEntity> list;
    private LayoutInflater inflater;
    private OnOrderClickListener listener;
    public OrderAdapter(Context context, List<OrderEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public void setOnOrderClickListener(OnOrderClickListener listener)
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
            convertView=inflater.inflate(R.layout.item_order, null);
            viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.img_url=(ImageView)convertView.findViewById(R.id.img_url);
            viewHolder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
            viewHolder.ll_gorder=(LinearLayout)convertView.findViewById(R.id.ll_gorder);
            viewHolder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            viewHolder.tv_statename=(TextView)convertView.findViewById(R.id.tv_statename);
            viewHolder.tv_advanced=(TextView)convertView.findViewById(R.id.tv_advanced);
            viewHolder.tv_priceTitle=(TextView)convertView.findViewById(R.id.tv_priceTitle);
            viewHolder.btn_cancel=(Button)convertView.findViewById(R.id.btn_cancel);
            viewHolder.btn_pay=(Button)convertView.findViewById(R.id.btn_pay);
            viewHolder.btn_applyRefunds=(Button)convertView.findViewById(R.id.btn_applyRefunds);
            viewHolder.btn_applyResolve=(Button)convertView.findViewById(R.id.btn_applyResolve);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getDoctor());
        viewHolder.tv_project.setText(list.get(position).getProject());
        viewHolder.tv_date.setText(list.get(position).getoDate());
        viewHolder.tv_price.setText(list.get(position).getPrices());
        viewHolder.tv_statename.setText(list.get(position).getStateName());
        viewHolder.tv_advanced.setText("预付款已支付：" + Utils.getMyString(list.get(position).getAdvancedCharge(),"0"));
        viewHolder.tv_statename.setVisibility(View.GONE);
        viewHolder.img_url.setImageBitmap(null);
        ImageLoader.getInstance().displayImage(list.get(position).getPath(), viewHolder.img_url, MyApplication.normalOption);
        viewHolder.tv_priceTitle.setText("总价:");
        if(list.get(position).getState().equals("0"))
        {
            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.btn_applyRefunds.setVisibility(View.GONE);
            viewHolder.btn_applyResolve.setVisibility(View.GONE);
        }
        else if(list.get(position).getState().equals("1"))
        {
            viewHolder.tv_priceTitle.setText("还需支付:");
            float prices=Float.valueOf(Utils.getMyString(list.get(position).getPrices(), "0"));
            float AdvancedCharge=Float.valueOf(Utils.getMyString(list.get(position).getAdvancedCharge(), "0"));
            String shengyu=Utils.getFloatTo2(prices-AdvancedCharge);
            viewHolder.tv_price.setText(shengyu);
            viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            viewHolder.btn_pay.setVisibility(View.VISIBLE);
            viewHolder.btn_applyRefunds.setVisibility(View.GONE);
            viewHolder.btn_applyResolve.setVisibility(View.GONE);
        }
        else if(list.get(position).getState().equals("2"))
        {
            viewHolder.btn_cancel.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.btn_applyRefunds.setVisibility(View.VISIBLE);
            viewHolder.btn_applyResolve.setVisibility(View.VISIBLE);
        }
        else if(list.get(position).getState().equals("3"))
        {
            viewHolder.btn_cancel.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.btn_applyRefunds.setVisibility(View.VISIBLE);
            viewHolder.btn_applyResolve.setVisibility(View.VISIBLE);
        }
        else if(list.get(position).getState().equals("5")|list.get(position).getState().equals("6"))
        {
            viewHolder.btn_cancel.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.btn_applyRefunds.setVisibility(View.GONE);
            viewHolder.btn_applyResolve.setVisibility(View.GONE);
            viewHolder.tv_statename.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.btn_cancel.setVisibility(View.GONE);
            viewHolder.btn_pay.setVisibility(View.GONE);
            viewHolder.btn_applyRefunds.setVisibility(View.GONE);
            viewHolder.btn_applyResolve.setVisibility(View.GONE);
        }
        viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancel(list.get(position).getoId(), position);
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
        viewHolder.btn_applyRefunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onApplyRefunds(list.get(position).getoId(), position);
            }
        });
        viewHolder.btn_applyResolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onApplyResolve(list.get(position).getoId(), position);
            }
        });
        return convertView;
    }
    class ViewHolder
    {
        ImageView img_url;
        TextView tv_name;
        TextView tv_project;
        TextView tv_date;
        TextView tv_price;
        TextView tv_statename;
        TextView tv_advanced;
        TextView tv_priceTitle;
        LinearLayout ll_gorder;
        Button btn_cancel;
        Button btn_pay;
        Button btn_applyRefunds;
        Button btn_applyResolve;
    }
    public interface OnOrderClickListener
    {
        public void onCancel(String id,int position);
        public void onPay(OrderEntity model,int position);
        public void onDetail(OrderEntity model);
        public void onApplyRefunds(String id,int position);
        public void onApplyResolve(String id,int position);
    }
}
