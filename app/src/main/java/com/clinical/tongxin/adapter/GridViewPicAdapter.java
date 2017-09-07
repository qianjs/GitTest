package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class GridViewPicAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;
    private OnClickClearImageListener listener;
    public GridViewPicAdapter(Context context, List<String> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public void setOnClickClearImageListener(OnClickClearImageListener listener)
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
            convertView=inflater.inflate(R.layout.item_gridview_pic, null);
            viewHolder.iv_item=(ImageView)convertView.findViewById(R.id.iv_item);
            viewHolder.ll_clear=(LinearLayout)convertView.findViewById(R.id.ll_clear);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage("file://"+list.get(position), viewHolder.iv_item, MyApplication.normalOption);
        viewHolder.ll_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClearImage(position);
            }
        });
        viewHolder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setimageview(list.toArray(new String[list.size()]),position);
            }
        });
        return convertView;
    }
    class ViewHolder
    {
        ImageView iv_item;
        LinearLayout ll_clear;
    }
    public interface OnClickClearImageListener
    {
        public void OnClearImage(int position);
    }
}
