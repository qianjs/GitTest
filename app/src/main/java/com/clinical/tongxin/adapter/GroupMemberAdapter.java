package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.GroupMemberEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class GroupMemberAdapter extends BaseAdapter {
    private Context context;
    private List<GroupMemberEntity> list;
    private LayoutInflater inflater;
    public GroupMemberAdapter(Context context,List<GroupMemberEntity> list ){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    public void setList(List<GroupMemberEntity> list) {
        this.list = list;
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
            //viewHolder.iv_unread= (ImageView) view.findViewById(R.id.iv_unread);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        GroupMemberEntity model=list.get(i);
        viewHolder.txt_Name.setText(model.getMemberName());
        //viewHolder.iv_unread.setVisibility(model.isUnRead()?View.VISIBLE:View.GONE);
        return view;
    }

    class ViewHolder{
        TextView txt_Name;
        //ImageView iv_unread;
    }
}
