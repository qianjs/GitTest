package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.DisputeEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class DisputeAdapter extends BaseAdapter {
    private List<DisputeEntity> list;
    private LayoutInflater inflater;
    public DisputeAdapter(Context context, List<DisputeEntity> list)
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
            convertView=inflater.inflate(R.layout.item_dispute, null);
            viewHolder.tv_did=(TextView)convertView.findViewById(R.id.tv_did);
            viewHolder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_did.setText(list.get(position).getdId());
        viewHolder.tv_date.setText(list.get(position).getDate());
        viewHolder.tv_content.setText(list.get(position).getContent());
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_did;
        TextView tv_date;
        TextView tv_content;
    }
}
