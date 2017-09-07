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

public class ResumePreviewWorkExperienceAdapter extends RecyclerView.Adapter<ResumePreviewWorkExperienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.WorkExperience> mWorkExperiences = new ArrayList<>();

    public ResumePreviewWorkExperienceAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_work_experience, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mEnterpriseValueTv.setText(mWorkExperiences.get(position).mEnterprise);
        holder.mIndustryValueTv.setText(mWorkExperiences.get(position).mIndustry);
        holder.mJobTypeValueTv.setText(mWorkExperiences.get(position).mJobType);
        holder.mJobNameValueTv.setText(mWorkExperiences.get(position).mJobName);
        holder.mWorkStartTimeValueTv.setText(mWorkExperiences.get(position).mWorkStartTime);
        holder.mWorkEndTimeValueTv.setText(mWorkExperiences.get(position).mWorkEndTime);
        holder.mSalaryValueTv.setText(mWorkExperiences.get(position).mSalary);
        holder.mDescriptionValueTv.setText(mWorkExperiences.get(position).mWorkDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mWorkExperiences.size() - 1 ? View.GONE : View.VISIBLE);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.enterprise_value_tv)
        TextView mEnterpriseValueTv;
        @BindView(R.id.industry_value_tv)
        TextView mIndustryValueTv;
        @BindView(R.id.job_type_value_tv)
        TextView mJobTypeValueTv;
        @BindView(R.id.job_name_value_tv)
        TextView mJobNameValueTv;
        @BindView(R.id.work_start_time_value_tv)
        TextView mWorkStartTimeValueTv;
        @BindView(R.id.work_end_time_value_tv)
        TextView mWorkEndTimeValueTv;
        @BindView(R.id.salary_value_tv)
        TextView mSalaryValueTv;
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
