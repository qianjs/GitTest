package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.PolicyEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by lzj667 on 2016/9/8.
 */
public class Doctor9Adapter extends BaseAdapter{
    private Context context;
    private List<PolicyEntity> list;
    private LayoutInflater inflater;
    private OnlistButtonListener listener;

    public void setMyListener(OnlistButtonListener listener) {
        this.listener = listener;
    }

    public Doctor9Adapter(Context context,List<PolicyEntity> list){
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
            view=inflater.inflate(R.layout.item_doctor9,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_doctor= (TextView) view.findViewById(R.id.txt_doctor);
            viewHolder.txt_down_date= (TextView) view.findViewById(R.id.txt_down_date);
            viewHolder.txt_price= (TextView) view.findViewById(R.id.txt_proName);
            viewHolder.txt_price= (TextView) view.findViewById(R.id.txt_price);
            viewHolder.txt_date= (TextView) view.findViewById(R.id.txt_date);
            viewHolder.txt_proName= (TextView) view.findViewById(R.id.txt_proName);
            viewHolder.ll_tellphone= (LinearLayout) view.findViewById(R.id.ll_tellphone);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
            final PolicyEntity entity=list.get(i);
            viewHolder.txt_doctor.setText(entity.getDoctor());
            String  string=Utils.dateToString(Utils.strToDateLog(entity.getDate()));
        System.out.println(string);
            viewHolder.txt_down_date.setText(string);
            viewHolder.txt_proName.setText(entity.getProject());
            viewHolder.txt_price.setText(entity.getPrice());
            viewHolder.txt_date.setText("");
            viewHolder.ll_tellphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onclick(entity.getDoctorPhone());
                }
            });

        return view;
    }
    class ViewHolder{
        TextView txt_doctor;
        TextView txt_down_date;
        TextView txt_proName;
        TextView txt_price;
        TextView txt_date;
        LinearLayout ll_tellphone;
    }
    public interface OnlistButtonListener{
        public void onclick(String str);
    }
}
