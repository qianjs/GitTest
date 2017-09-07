package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.CaseListEntity;


import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public class CaseListAdapter extends BaseAdapter {


    private List<CaseListEntity> list;
    private LayoutInflater inflater;
    private Context context;
    public CaseListAdapter(Context context, List<CaseListEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
        this.context=context;
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
            convertView=inflater.inflate(R.layout.item_caselist, null);
            viewHolder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
            viewHolder.tv_doctor=(TextView)convertView.findViewById(R.id.tv_doctor);
            viewHolder.tv_agency=(TextView)convertView.findViewById(R.id.tv_agency);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.tv_time.setText(list.get(position).getTime());
        viewHolder.tv_project.setText(list.get(position).getProject());
        viewHolder.tv_doctor.setText(list.get(position).getDoctor());
        viewHolder.tv_agency.setText(list.get(position).getAgency());

        return convertView;
    }
    class ViewHolder
    {
        TextView tv_time;
        TextView tv_project;
        TextView tv_doctor;
        TextView tv_agency;
    }

}
