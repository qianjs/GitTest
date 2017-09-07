package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.AuctionProjectEntity;

import java.util.List;

/**
 * Created by lzj667 on 2016/8/29.
 */
public class AuctionProjectAdapter extends BaseAdapter {
    private Context mcontext;
    private List<AuctionProjectEntity> mlist;
    private LayoutInflater minflater;
    public AuctionProjectAdapter(Context context,List<AuctionProjectEntity> list){
        this.mcontext=context;
        this.mlist=list;
        minflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view=minflater.inflate(R.layout.item_projectadapter,null);
            viewHolder.txt_proName= (TextView) view.findViewById(R.id.txt_proName);
            viewHolder.txt_fristprice= (TextView) view.findViewById(R.id.txt_first_price);
            viewHolder.txt_oneprice= (TextView) view.findViewById(R.id.txt_one_price);
            viewHolder.txt_aType= (TextView) view.findViewById(R.id.txt_aType);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        AuctionProjectEntity entity = mlist.get(i);
        viewHolder.txt_proName.setText(entity.getName());
        viewHolder.txt_fristprice.setText(entity.getPrice());
        viewHolder.txt_oneprice.setText(entity.getfPrice());
        viewHolder.txt_aType.setText(entity.getaType()+"|"+entity.getaName());
        return view;
    }
    private class ViewHolder{
        TextView txt_proName;
        TextView txt_fristprice;
        TextView txt_oneprice;
        TextView txt_aType;

    }
}
