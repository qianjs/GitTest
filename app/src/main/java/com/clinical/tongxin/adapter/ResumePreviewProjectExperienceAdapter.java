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

import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_MOBILE;
import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_WEB;

/**
 * Created by qjs on 2017/9/1.
 */

public class ResumePreviewProjectExperienceAdapter extends RecyclerView.Adapter<ResumePreviewProjectExperienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.ProjectExperience> mProjectExperiences = new ArrayList<>();

    public ResumePreviewProjectExperienceAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_project_experience, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mProjectValueTv.setText(mProjectExperiences.get(position).mProjectName);
        holder.mProjectStartTimeValueTv.setText(mProjectExperiences.get(position).mProjectStartTime);
        holder.mProjectEndTimeValueTv.setText(mProjectExperiences.get(position).mProjectEndTime);
        holder.mProjectDutyValueTv.setText(mProjectExperiences.get(position).mProjectDuty.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        holder.mDescriptionValueTv.setText(mProjectExperiences.get(position).mProjectDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mProjectExperiences.size() - 1 ? View.GONE : View.VISIBLE);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.project_value_tv)
        TextView mProjectValueTv;
        @BindView(R.id.project_start_time_value_tv)
        TextView mProjectStartTimeValueTv;
        @BindView(R.id.project_end_time_value_tv)
        TextView mProjectEndTimeValueTv;
        @BindView(R.id.project_duty_value_tv)
        TextView mProjectDutyValueTv;
        @BindView(R.id.description_value_tv)
        TextView mDescriptionValueTv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
