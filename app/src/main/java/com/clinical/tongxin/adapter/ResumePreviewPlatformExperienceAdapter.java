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

public class ResumePreviewPlatformExperienceAdapter extends RecyclerView.Adapter<ResumePreviewPlatformExperienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.PlatformProjectExperience> mPlatformProjectExperiences = new ArrayList<>();

    public ResumePreviewPlatformExperienceAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_platform_experience, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mProjectValueTv.setText(mPlatformProjectExperiences.get(position).mProjectTypeName);
        String taskStr = "";
        if(mPlatformProjectExperiences.get(position).mTasks != null) {
            for (ResumeEntity.PlatformProjectExperience.Task task : mPlatformProjectExperiences.get(position).mTasks) {
                taskStr += task.mName + "（" + task.mCount + "）" + "\n";
            }
        }
        holder.mTaskValueTv.setText(taskStr.trim());
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mPlatformProjectExperiences.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mPlatformProjectExperiences.size();
    }

    public void setData(List<ResumeEntity.PlatformProjectExperience> platformProjectExperiences){
        if(platformProjectExperiences != null){
            mPlatformProjectExperiences.clear();
            mPlatformProjectExperiences.addAll(platformProjectExperiences);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.project_value_tv)
        TextView mProjectValueTv;
        @BindView(R.id.task_value_tv)
        TextView mTaskValueTv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
