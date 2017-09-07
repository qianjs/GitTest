package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ManagerEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManagerAdapter extends BaseAdapter {
    private List<ManagerEntity> list;
    private Context context;
    private LayoutInflater inflater ;

    public ManagerAdapter(Context context,List<ManagerEntity> list){
        this.list=list;
        this.context=context;
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
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.item_manager_layout,null);
            viewHolder.txt_ExpectAmount= (TextView) view.findViewById(R.id.txt_ExpectAmount);
            viewHolder.txt_date= (TextView) view.findViewById(R.id.txt_date);
            viewHolder.txt_Status= (TextView) view.findViewById(R.id.txt_Status);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        ManagerEntity model=list.get(i);
        viewHolder.txt_ExpectAmount.setText(Float.parseFloat(Utils.getMyString(model.getExpectAmount(),"0.00"))+"");
        viewHolder.txt_date.setText(model.getAllocationsDate());
        viewHolder.txt_Status.setText(model.getStatus());
        return view;
    }

    class ViewHolder{
        TextView txt_ExpectAmount,txt_date,txt_Status;
    }
}
