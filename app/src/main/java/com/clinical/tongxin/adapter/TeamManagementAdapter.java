package com.clinical.tongxin.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.TeamEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 团队管理适配器
 * @author LINCHAO
 * 2016/12/26
 */
public class TeamManagementAdapter extends BaseAdapter {

    private List<TeamEntity> list;
    private Context context;
    private LayoutInflater mInflater;

    public TeamManagementAdapter(Context context , List<TeamEntity> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<TeamEntity> teamEntityList){
        this.list = teamEntityList;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.team_managemet_item,null);
            holder = new ViewHolder();
            x.view().inject(holder,view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        TeamEntity teamEntity = list.get(i);
        holder.tv_team_name.setText(teamEntity.getName());
//        x.image().bind(holder.iv_team_head,teamEntity.getTeamHeaderUri(),options);
        holder.iv_team_head.setImageResource(R.mipmap.wo07);

        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.iv_team_head)
        ImageView iv_team_head;
        @ViewInject(R.id.tv_team_name)
        TextView tv_team_name;
    }
}
