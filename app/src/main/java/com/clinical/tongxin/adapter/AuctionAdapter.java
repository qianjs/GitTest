package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.AuctionEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class AuctionAdapter extends BaseAdapter {
    private List<AuctionEntity> list;
    private LayoutInflater inflater;
    public AuctionAdapter(Context context, List<AuctionEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_auction, null);
            viewHolder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
            viewHolder.tv_myPrice=(TextView)convertView.findViewById(R.id.tv_myPrice);
            viewHolder.tv_residualTime=(TextView)convertView.findViewById(R.id.tv_residualTime);
            viewHolder.tv_doctor=(TextView)convertView.findViewById(R.id.tv_doctor);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_project.setText(list.get(position).getProject());
        viewHolder.tv_myPrice.setText(list.get(position).getPrice());
        viewHolder.tv_residualTime.setText(list.get(position).getResidualTime());
        viewHolder.tv_doctor.setText(list.get(position).getReleaseDoctor());
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_project;
        TextView tv_myPrice;
        TextView tv_residualTime;
        TextView tv_doctor;
    }
}
