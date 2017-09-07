package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.PopwindowPropertyEntitity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

public class PopPropertyAdapter extends BaseAdapter{
    private List<PopwindowPropertyEntitity> list;
    private Context context;
    private LayoutInflater inflater;
    public PopPropertyAdapter(Context context,List<PopwindowPropertyEntitity> list){
        this.list=list;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_textview,null);
            viewholder=new Viewholder();
            viewholder.txt_name= (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(viewholder);
        }else {
            viewholder= (Viewholder) convertView.getTag();
        }
        PopwindowPropertyEntitity model=list.get(position);
        viewholder.txt_name.setText(model.getName());
        return convertView;
    }
    class  Viewholder{
        TextView txt_name;
    }
}
