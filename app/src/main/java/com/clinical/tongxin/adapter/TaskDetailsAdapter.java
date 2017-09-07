package com.clinical.tongxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.SingleRouteCalculateActivity;
import com.clinical.tongxin.entity.TaskSiteItemEntity;
import com.clinical.tongxin.util.GpsUtils;
import com.clinical.tongxin.util.Utils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static android.R.attr.start;

/**
 * 任务详情
 * @author LINCHAO
 * 2017/1/5
 */
public class TaskDetailsAdapter extends BaseAdapter {

    private List<TaskSiteItemEntity> list;
    private Context context;
    private LayoutInflater mInflater;
    private onClickAmapLister listener;
    public TaskDetailsAdapter(Context context, List<TaskSiteItemEntity> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<TaskSiteItemEntity> taskSiteItemEntities){
        this.list = taskSiteItemEntities;
        notifyDataSetChanged();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.team_details_item,null);
            holder = new ViewHolder();
            x.view().inject(holder,view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final TaskSiteItemEntity taskSiteItemEntity = list.get(i);

        try {
            holder.tv_name.setText(taskSiteItemEntity.getName());
            holder.tv_address.setText(taskSiteItemEntity.getAddress());
            holder.tv_num.setText(i+"");
            holder.tv_latitude.setText(taskSiteItemEntity.getLatitude());
            holder.tv_longitude.setText(taskSiteItemEntity.getLongitude());
            holder.tv_status.setText(Utils.itemStatusSwtichToStr(Integer.valueOf(taskSiteItemEntity.getItemStatus())));
            holder.tv_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(taskSiteItemEntity);
//                    GpsUtils.getInstance(context).openGaoDeMap(45.74598697,126.69013023,"",45.76393046,126.65056229,"");
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.tv_num)
        TextView tv_num;
        @ViewInject(R.id.tv_status)
        TextView tv_status;
        @ViewInject(R.id.tv_address)
        TextView tv_address;
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.tv_longitude)
        TextView tv_longitude;
        @ViewInject(R.id.tv_latitude)
        TextView tv_latitude;
        @ViewInject(R.id.tv_navi)
        TextView tv_navi;
    }
    public void setOnClickAmapListener(onClickAmapLister listener)
    {
        this.listener=listener;
    }
    public interface onClickAmapLister{
        void onClick(TaskSiteItemEntity taskSiteItemEntity);
    }
}
