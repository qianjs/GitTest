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

public class ResumePreviewEducationExperienceAdapter extends RecyclerView.Adapter<ResumePreviewEducationExperienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.EducationExperience> mEducationExperiences = new ArrayList<>();

    public ResumePreviewEducationExperienceAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_education_experience, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mUniversityValueTv.setText(mEducationExperiences.get(position).mUniversity);
        holder.mEducationStartTimeValueTv.setText(mEducationExperiences.get(position).mEducationStartTime);
        holder.mEducationEndTimeValueTv.setText(mEducationExperiences.get(position).mEducationEndTime);
        holder.mRecruitValueTv.setText(mEducationExperiences.get(position).mIsRecruit);
        holder.mMajorValueTv.setText(mEducationExperiences.get(position).mMajor);
        holder.mDegreeValueTv.setText(mEducationExperiences.get(position).mDegree);
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mEducationExperiences.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mEducationExperiences.size();
    }

    public void setData(List<ResumeEntity.EducationExperience> educationExperiences){
        if(educationExperiences != null){
            mEducationExperiences.clear();
            mEducationExperiences.addAll(educationExperiences);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.university_value_tv)
        TextView mUniversityValueTv;
        @BindView(R.id.education_start_time_value_tv)
        TextView mEducationStartTimeValueTv;
        @BindView(R.id.education_end_time_value_tv)
        TextView mEducationEndTimeValueTv;
        @BindView(R.id.recruit_value_tv)
        TextView mRecruitValueTv;
        @BindView(R.id.major_value_tv)
        TextView mMajorValueTv;
        @BindView(R.id.degree_value_tv)
        TextView mDegreeValueTv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
