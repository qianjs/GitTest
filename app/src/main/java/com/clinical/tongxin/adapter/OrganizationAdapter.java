package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ManagerMemberEntity;
import com.clinical.tongxin.entity.OrganizationManagerEntity;
import com.clinical.tongxin.entity.OrganizationMemberEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.d.j.n;

/**
 * 组织机构适配器
 * Created by linchao on 2017/3/26.
 */
public class OrganizationAdapter extends BaseAdapter {
    private List<OrganizationManagerEntity> list;
    private LayoutInflater inflater;
    private Context context;
    private OnMemberClickListener listener;

    public OrganizationAdapter(Context context, List<OrganizationManagerEntity> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public List<OrganizationManagerEntity> getList() {
        return list;
    }

    public void setList(List<OrganizationManagerEntity> list) {
        if (list == null){
            this.list = new ArrayList<>();
        }else {
            this.list = list;
        }

        notifyDataSetChanged();
    }

    public OnMemberClickListener getListener() {
        return listener;
    }

    public void setListener(OnMemberClickListener listener) {
        this.listener = listener;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_org_member, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(list.get(position).getManagerName());
        viewHolder.llManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onManagerClick(list.get(position));
            }
        });
        viewHolder.tvPosition.setText(list.get(position).getManagerPosition());
        viewHolder.llMember.removeAllViews();
        for (final OrganizationMemberEntity entity : list.get(position).getManagerMembers()) {
            View memberView = inflater.inflate(R.layout.item_member_layout, null);
            TextView textView = (TextView) memberView.findViewById(R.id.tv_member_name);
            String name = entity.getName();
            if (name.length() > 2){
                textView.setText(name.substring(name.length() - 2,name.length()));
            }else {
                textView.setText(name);
            }
            viewHolder.llMember.addView(memberView);
            memberView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMemberClick(entity);
                }
            });
        }
        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.view_blank)
        View viewBlank;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.ll_member)
        LinearLayout llMember;
        @BindView(R.id.ll_manager)
        LinearLayout llManager;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnMemberClickListener {
        void onManagerClick(OrganizationManagerEntity entity);

        void onMemberClick(OrganizationMemberEntity entity);
    }

}
