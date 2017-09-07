package com.clinical.tongxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MyBaseEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class PopWindowClassificationAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;
    public PopWindowClassificationAdapter(Context context, List<String> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public void setList(List<String> list){
        this.list = list;
        notifyDataSetChanged();
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
            convertView=inflater.inflate(R.layout.item_popwindow_recommend, null);
            viewHolder.tvtext=(TextView)convertView.findViewById(R.id.tvtext);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tvtext.setText(list.get(position));

        return convertView;
    }
    class ViewHolder
    {
        TextView tvtext;
    }
}
