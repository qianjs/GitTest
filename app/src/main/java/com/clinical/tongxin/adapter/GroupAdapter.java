package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.GroupEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class GroupAdapter extends BaseAdapter {
    private List<GroupEntity> list;
    private Context context;
    private LayoutInflater inflater;
    public GroupAdapter(Context context,List<GroupEntity> list ){
        this.context=context;
        this.list=list;
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
            view=inflater.inflate(R.layout.item_group_name,null);
            viewHolder.txt_Name= (TextView) view.findViewById(R.id.txt_Name);
//            viewHolder.iv_unread= (ImageView) view.findViewById(R.id.iv_unread);
            viewHolder.img_head= (ImageView) view.findViewById(R.id.img_head);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        GroupEntity model=list.get(i);
        viewHolder.txt_Name.setText(model.getGroupName());
//        viewHolder.iv_unread.setVisibility(model.isUnRead()?View.VISIBLE:View.GONE);
        viewHolder.img_head.setImageResource(R.mipmap.wo07);
        return view;
    }

    class ViewHolder{
        TextView txt_Name;
//        ImageView iv_unread;
        ImageView img_head;
    }
}
