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

public class ResumePreviewCertificateExperienceAdapter extends RecyclerView.Adapter<ResumePreviewCertificateExperienceAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.Certificate> mCertificates = new ArrayList<>();

    public ResumePreviewCertificateExperienceAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_certificate, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCertificateValueTv.setText(mCertificates.get(position).mCertificateName);
        holder.mCertificateStartTimeValueTv.setText(mCertificates.get(position).mGetTime);
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mCertificates.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mCertificates.size();
    }

    public void setData(List<ResumeEntity.Certificate> certificates){
        if(certificates != null){
            mCertificates.clear();
            mCertificates.addAll(certificates);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.certificate_value_tv)
        TextView mCertificateValueTv;
        @BindView(R.id.certificate_start_time_value_tv)
        TextView mCertificateStartTimeValueTv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
