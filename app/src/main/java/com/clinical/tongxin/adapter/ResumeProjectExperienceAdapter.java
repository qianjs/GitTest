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

public class ResumeProjectExperienceAdapter extends RecyclerView.Adapter<ResumeProjectExperienceAdapter.ViewHolder> {
    private Context mContext;
    private List<ResumeEntity.ProjectExperience> mProjectExperiences = new ArrayList<>();
    private IOnClickItemListener mOnClickItemListener;

    public ResumeProjectExperienceAdapter(Context context, IOnClickItemListener onClickItemListener){
        mContext = context;
        mOnClickItemListener = onClickItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_project_experience_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String projectStartTime = mProjectExperiences.get(position).mProjectStartTime;
        String projectEndTime = mProjectExperiences.get(position).mProjectEndTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            projectStartTime = sdf.format(sdf.parse(projectStartTime));
            projectEndTime = sdf.format(sdf.parse(projectEndTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mDateTv.setText(projectStartTime + " -- " + projectEndTime);
        holder.mContentTv.setText(mProjectExperiences.get(position).mProjectName);

        ((View)holder.mContentTv.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemListener.onClickItem(position);
            }
        });
        ((View)holder.mContentTv.getParent()).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnClickItemListener.onLongClickItem(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjectExperiences.size();
    }

    public void setData(List<ResumeEntity.ProjectExperience> projectExperiences){
        if(projectExperiences != null){
            mProjectExperiences.clear();
            mProjectExperiences.addAll(projectExperiences);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mDateTv;
        TextView mContentTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mDateTv = (TextView) itemView.findViewById(R.id.date_tv);
            mContentTv = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }
}
