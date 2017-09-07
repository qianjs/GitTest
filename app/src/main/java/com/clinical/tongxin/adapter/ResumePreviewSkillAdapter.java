package com.clinical.tongxin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ResumeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qjs on 2017/9/1.
 */

public class ResumePreviewSkillAdapter extends RecyclerView.Adapter<ResumePreviewSkillAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.Skill> mSkills = new ArrayList<>();

    public ResumePreviewSkillAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_skill, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSkillValueTv.setText(mSkills.get(position).mSkillName);
        holder.mSkillTypeValueTv.setText(mSkills.get(position).mSkillType);
        holder.mUseTimeValueTv.setText(mSkills.get(position).mUseTime);
        holder.mMasteryValueTv.setText(mSkills.get(position).mMastery);
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mSkills.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mSkills.size();
    }

    public void setData(List<ResumeEntity.Skill> skills){
        if(skills != null){
            mSkills.clear();
            mSkills.addAll(skills);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.skill_value_tv)
        TextView mSkillValueTv;
        @BindView(R.id.skill_type_value_tv)
        TextView mSkillTypeValueTv;
        @BindView(R.id.use_time_value_tv)
        TextView mUseTimeValueTv;
        @BindView(R.id.mastery_value_tv)
        TextView mMasteryValueTv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
