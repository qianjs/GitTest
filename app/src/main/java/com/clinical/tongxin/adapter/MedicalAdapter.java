package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MedicalRecordEntity;

import java.util.List;

/**
 * Created by lzj667 on 2016/9/1.
 */
public class MedicalAdapter extends BaseAdapter {
    private Context mcontext;
    private List<MedicalRecordEntity> list;
    private LayoutInflater inflater;
    public MedicalAdapter(Context mcontext,List<MedicalRecordEntity> list){
        this.mcontext=mcontext;
        this.list=list;
        inflater=LayoutInflater.from(mcontext);
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
            view=inflater.inflate(R.layout.item_medicalrecord,null);
            viewHolder.txt_doctor= (TextView) view.findViewById(R.id.txt_doctor);
            viewHolder.txt_agency= (TextView) view.findViewById(R.id.txt_agency);
            viewHolder.txt_project= (TextView) view.findViewById(R.id.txt_project);
            viewHolder.txt_time= (TextView) view.findViewById(R.id.txt_time);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        MedicalRecordEntity entity=list.get(i);
        viewHolder.txt_time.setText("手术时间:"+entity.getDate());
        viewHolder.txt_project.setText(entity.getProject());
        viewHolder.txt_agency.setText("医疗机构："+entity.getAgency());
        viewHolder.txt_doctor.setText(entity.getPerson());
        return view;
    }
    private  class  ViewHolder{
        TextView txt_doctor;
        TextView txt_project;
        TextView txt_agency;
        TextView txt_time;

    }
}
