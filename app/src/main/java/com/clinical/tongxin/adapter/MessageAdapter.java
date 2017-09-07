package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MessageEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class MessageAdapter extends BaseAdapter {
    private List<MessageEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private OnOkorNoClickListener onOkorNoClickListener;

    public void setList(List<MessageEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnOkorNoClickListener(OnOkorNoClickListener onOkorNoClickListener) {
        this.onOkorNoClickListener = onOkorNoClickListener;
    }

    public MessageAdapter(Context context, List<MessageEntity> list){
        this.list=list;
        this.context=context;
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
            view=inflater.inflate(R.layout.item_message_layout,null);
            viewHolder.txt_Name= (TextView) view.findViewById(R.id.txt_name);
            viewHolder.txt_content= (TextView) view.findViewById(R.id.txt_QName);
            viewHolder.txt_typeName= (TextView) view.findViewById(R.id.txt_typeName);
            viewHolder.btn_ok= (Button) view.findViewById(R.id.btn_ok);
            viewHolder.btn_no= (Button) view.findViewById(R.id.btn_no);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        final MessageEntity model=list.get(i);
        viewHolder.txt_Name.setText(Utils.decodeUnicode(model.getFromName()));

//        unicodeToChina(bb)
        if (model.getGroupName().equals("")){
            viewHolder.txt_content.setText("邀请您加入他的团队");
        }else{
            if (Utils.decodeUnicode(model.getGroupName()).equals("")){
                viewHolder.txt_content.setText("邀请您加入他的团队");
            }else{
                viewHolder.txt_content.setText("邀请您加入"+Utils.decodeUnicode(model.getGroupName())+"该团队");
            }
        }
        if (model.getType().equals("1")){
            viewHolder.btn_ok.setEnabled(false);
            viewHolder.btn_no.setEnabled(false);
            viewHolder.btn_ok.setVisibility(View.GONE);
            viewHolder.btn_no.setVisibility(View.GONE);
            viewHolder.txt_typeName.setVisibility(View.VISIBLE);
            viewHolder.txt_typeName.setText("已加入");
        }else if (model.getType().equals("0")){
            viewHolder.btn_ok.setEnabled(false);
            viewHolder.btn_no.setEnabled(false);
            viewHolder.btn_ok.setVisibility(View.GONE);
            viewHolder.btn_no.setVisibility(View.GONE);
            viewHolder.txt_typeName.setVisibility(View.VISIBLE);
            viewHolder.txt_typeName.setText("已忽略");
        }else if (model.getType().equals("3")){
            viewHolder.btn_ok.setEnabled(false);
            viewHolder.btn_no.setEnabled(false);
            viewHolder.btn_ok.setVisibility(View.GONE);
            viewHolder.btn_no.setVisibility(View.GONE);
            viewHolder.txt_typeName.setVisibility(View.VISIBLE);
            viewHolder.txt_typeName.setText("已忽略");
        }else if (model.getType().equals("2")){
            viewHolder.btn_ok.setEnabled(true);
            viewHolder.btn_no.setEnabled(true);
            viewHolder.btn_ok.setVisibility(View.VISIBLE);
            viewHolder.btn_no.setVisibility(View.VISIBLE);
            viewHolder.txt_typeName.setVisibility(View.GONE);
        }
            viewHolder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkorNoClickListener.setOk(model);
            }
        });
        viewHolder.btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkorNoClickListener.setNo(model);
            }
        });
        return view;
    }
    private class ViewHolder{
        TextView txt_Name,txt_content,txt_typeName;
        Button btn_ok,btn_no;
    }
    public interface OnOkorNoClickListener{
        void setOk(MessageEntity messageEntity);
        void setNo(MessageEntity messageEntity);
    }
}
