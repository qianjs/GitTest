package com.clinical.tongxin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.IOnClickItemListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeWorkExperienceAdapter extends RecyclerView.Adapter<ResumeWorkExperienceAdapter.ViewHolder> {
    private Context mContext;
    private List<ResumeEntity.WorkExperience> mWorkExperiences = new ArrayList<>();
    private IOnClickItemListener mOnClickItemListener;

    public ResumeWorkExperienceAdapter(Context context, IOnClickItemListener onClickItemListener){
        mContext = context;
        mOnClickItemListener = onClickItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_work_experience_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String workStartTime = mWorkExperiences.get(position).mWorkStartTime;
        String workEndTime = mWorkExperiences.get(position).mWorkEndTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            workStartTime = sdf.format(sdf.parse(workStartTime));
            workEndTime = sdf.format(sdf.parse(workEndTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mDateTv.setText(workStartTime + " -- " + workEndTime);
        holder.mContentTv.setText(mWorkExperiences.get(position).mEnterprise);
        holder.mSummaryTv.setText(mWorkExperiences.get(position).mJobType);

        ((View)holder.mSummaryTv.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemListener.onClickItem(position);
            }
        });

        ((View)holder.mSummaryTv.getParent()).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnClickItemListener.onLongClickItem(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkExperiences.size();
    }

    public void setData(List<ResumeEntity.WorkExperience> workExperiences){
        if(workExperiences != null){
            mWorkExperiences.clear();
            mWorkExperiences.addAll(workExperiences);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mDateTv;
        TextView mContentTv;
        TextView mSummaryTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mDateTv = (TextView) itemView.findViewById(R.id.date_tv);
            mContentTv = (TextView) itemView.findViewById(R.id.content_tv);
            mSummaryTv = (TextView) itemView.findViewById(R.id.summary_tv);
        }
    }
}
