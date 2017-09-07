package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.AptitudeEntity;

import java.util.List;

/**
 * 任务分类查询
 * Created by lincha0 on 2017/6/15 0017.
 */
public class PopWindowTaskAdapter extends BaseAdapter {
    private List<AptitudeEntity> list;
    private LayoutInflater inflater;
    private int type; // 0 项目 1任务

    public PopWindowTaskAdapter(Context context, List<AptitudeEntity> list,int type)
    {
        this.list=list;
        this.type = type;
        inflater=LayoutInflater.from(context);
    }
    public void setList(List<AptitudeEntity> list){
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
        viewHolder.tvtext.setText(type==0?list.get(position).getProjectTypeName():list.get(position).getTaskTypeName());

        return convertView;
    }
    class ViewHolder
    {
        TextView tvtext;
    }
}
