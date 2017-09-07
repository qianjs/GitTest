package com.clinical.tongxin.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clinical.tongxin.ManagerMemberActivity;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ManagerMemberEntity;


import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 经理团队人员适配器
 * @author LINCHAO
 * 2016/12/26
 */
public class ManagerMemberAdapter extends BaseAdapter {

    private List<ManagerMemberEntity> list;
    private Context context;
    private LayoutInflater mInflater;
    private boolean isEdit;
//    private Handler handler;
//    public static final int MANAGER_DATA = 1001;
    public ManagerMemberAdapter(Context context, List<ManagerMemberEntity> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<ManagerMemberEntity> memberEntityList){
        this.list = memberEntityList;
        notifyDataSetChanged();
    }

    public void setEdit(boolean isEdit){
       for (ManagerMemberEntity managerMemberEntity: list){
           managerMemberEntity.setEdit(isEdit);
       }
        this.isEdit = isEdit;
    }

    public List<ManagerMemberEntity> getList(){
        List<ManagerMemberEntity> managerMemberEntities = new ArrayList<>();
        for (ManagerMemberEntity managerMemberEntity: list){
            if (managerMemberEntity.isCheck()){
                managerMemberEntities.add(managerMemberEntity);
            }
        }
        return managerMemberEntities;
    }
    private void setListCheck(List<ManagerMemberEntity> memberEntities, boolean isCheck){
        for (ManagerMemberEntity memberEntity:memberEntities){
            memberEntity.setCheck(isCheck);
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ManagerMemberEntity getItem(int i) {
        return (ManagerMemberEntity)list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.team_member_item,null);
            holder = new ViewHolder();
            x.view().inject(holder,view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        ManagerMemberEntity mangerMemberEntity = list.get(i);
        holder.tv_member_tel.setVisibility(View.VISIBLE);
        holder.tv_member_tel.setText(mangerMemberEntity.getMobile());
        holder.cb_choose.setChecked(mangerMemberEntity.isCheck());
        holder.cb_choose.setVisibility(mangerMemberEntity.isEdit() == true ? View.VISIBLE:View.GONE);
        holder.tv_member_name.setText(mangerMemberEntity.getNickName());
        holder.ll_choose_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    list.get(i).setCheck(!list.get(i).isCheck());
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.cb_choose)
        CheckBox cb_choose;
        @ViewInject(R.id.tv_member_tel)
        TextView tv_member_tel;
        @ViewInject(R.id.tv_member_name)
        TextView tv_member_name;
        @ViewInject(R.id.ll_choose_item)
        View ll_choose_item;
    }
}
