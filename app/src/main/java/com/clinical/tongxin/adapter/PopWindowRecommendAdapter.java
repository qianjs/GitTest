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
public class PopWindowRecommendAdapter extends BaseAdapter {
    private List<MyBaseEntity> list;
    private LayoutInflater inflater;
    public PopWindowRecommendAdapter(Context context, List<MyBaseEntity> list)
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
            convertView=inflater.inflate(R.layout.item_popwindow_recommend, null);
            viewHolder.tvtext=(TextView)convertView.findViewById(R.id.tvtext);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tvtext.setText(list.get(position).getText());
        if(list.get(position).getIsSelected().equals("true"))
        {
            viewHolder.tvtext.setTextColor(Color.parseColor("#3587ef"));
        }
        else
        {
            viewHolder.tvtext.setTextColor(Color.parseColor("#000000"));
        }
        return convertView;
    }
    class ViewHolder
    {
        TextView tvtext;
    }
}
