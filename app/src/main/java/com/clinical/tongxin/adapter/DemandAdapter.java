package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.DemandEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class DemandAdapter extends BaseAdapter {
    private List<DemandEntity> list;
    private LayoutInflater inflater;
    private DemandDeleteListener mlistener;
    public DemandAdapter(Context context, List<DemandEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public void setOnDemandDeleteListener(DemandDeleteListener mlistener)
    {
        this.mlistener=mlistener;
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
            convertView=inflater.inflate(R.layout.item_demand, null);
            viewHolder.tv_project=(TextView)convertView.findViewById(R.id.tv_project);
            viewHolder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tv_endDate=(TextView)convertView.findViewById(R.id.tv_endDate);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            viewHolder.tv_stateName=(TextView)convertView.findViewById(R.id.tv_stateName);
            viewHolder.btn_delete=(Button)convertView.findViewById(R.id.btn_delete);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_project.setText(list.get(position).getProject());
        viewHolder.tv_date.setText(list.get(position).getDate());
        viewHolder.tv_endDate.setText(list.get(position).getEndDate());
        viewHolder.tv_content.setText(list.get(position).getContent());
        viewHolder.tv_stateName.setText(list.get(position).getStateName());
        if(list.get(position).getDemandState().equals("0"))
        {
            viewHolder.btn_delete.setVisibility(View.VISIBLE);
            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onDelete(list.get(position).getdId());
                }
            });
        }
        else
        {
            viewHolder.btn_delete.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_project;
        TextView tv_date;
        TextView tv_endDate;
        TextView tv_content;
        TextView tv_stateName;
        Button btn_delete;
    }
    public interface DemandDeleteListener
    {
        public void onDelete(String id);
    }
}
