package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.SearchRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class SearchRecordAdapter extends BaseAdapter {


    private List<SearchRecord> list;
    private LayoutInflater inflater;
    public SearchRecordAdapter(Context context, List<SearchRecord> list)
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
            convertView=inflater.inflate(R.layout.item_searchrecord, null);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_content.setText(list.get(position).getScontent());
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_content;
    }

}
