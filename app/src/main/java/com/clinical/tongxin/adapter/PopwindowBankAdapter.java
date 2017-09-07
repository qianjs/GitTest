package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.BankEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class PopwindowBankAdapter extends BaseAdapter {
    private List<BankEntity> list;
    private Context context;
    private LayoutInflater inflater;
    public PopwindowBankAdapter(Context context,List<BankEntity> list){
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
        BankEntity model=list.get(position);
        viewholder.txt_name.setText(model.getName());
        return convertView;
    }
    class  Viewholder{
        TextView txt_name;
    }
}
