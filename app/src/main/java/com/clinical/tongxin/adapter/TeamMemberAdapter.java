package com.clinical.tongxin.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MemberEntity;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 团队人员适配器
 * @author LINCHAO
 * 2016/12/26
 */
public class TeamMemberAdapter extends BaseAdapter {

    private List<MemberEntity> list;
    private Context context;
    private LayoutInflater mInflater;
    private int type; // 1单选 2多选

    public TeamMemberAdapter(Context context, List<MemberEntity> list, int type) {
        this.list = list;
        this.context = context;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<MemberEntity> memberEntityList){
        this.list = memberEntityList;
        notifyDataSetChanged();
    }

//    public MemberEntity setCheck(int position){
//        setListCheck(list,false);
//        getItem(position).setCheck(true);
//        notifyDataSetChanged();
//        return getItem(position);
//    }

    private void setListCheck(List<MemberEntity> memberEntities, boolean isCheck){
        for (MemberEntity memberEntity:memberEntities){
            memberEntity.setCheck(isCheck);
        }
    }

    public List<MemberEntity> getCheckMember(){
        List<MemberEntity> memberEntities = new ArrayList<>();
        for (MemberEntity memberEntity:list){
            if (memberEntity.isCheck()){
                memberEntities.add(memberEntity);
            }
        }
        return memberEntities;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MemberEntity getItem(int i) {
        return (MemberEntity)list.get(i);
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
        MemberEntity memberEntity = list.get(i);
        holder.tv_member_name.setText(memberEntity.getNickName());
        holder.cb_choose.setChecked(memberEntity.isCheck());
        holder.ll_choose_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    setListCheck(list,false);
                    list.get(i).setCheck(true);
                }else if (type == 2){
                    list.get(i).setCheck(!list.get(i).isCheck());
                }
                notifyDataSetChanged();

            }
        });
//        x.image().bind(holder.iv_team_head,teamEntity.getTeamHeaderUri(),options);
//        ImageLoader.getInstance().displayImage(memberEntity.getMemberHeaderUri(),holder.iv_member_head, MyApplication.normalOption);

        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.cb_choose)
        CheckBox cb_choose;
        @ViewInject(R.id.iv_member_head)
        ImageView iv_member_head;
        @ViewInject(R.id.tv_member_name)
        TextView tv_member_name;
        @ViewInject(R.id.ll_choose_item)
        View ll_choose_item;
    }
}
