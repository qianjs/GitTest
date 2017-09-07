package com.clinical.tongxin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qjs on 2017/9/1.
 */

public class ResumePreviewCertificateAttachmentAdapter extends RecyclerView.Adapter<ResumePreviewCertificateAttachmentAdapter.ViewHolder> {

    private Context mContext;
    private List<ResumeEntity.CertificateAttachment> mCertificateAttachments = new ArrayList<>();

    public ResumePreviewCertificateAttachmentAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_preview_certificate_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCertificateAttachmentValueTv.setText(mCertificateAttachments.get(position).mDescription);
        Glide.with(mContext).load(UrlUtils.BASE_URL + mCertificateAttachments.get(position).mUrl)
//                .placeholder(R.mipmap.fail)
                .into(holder.mImageValueIv);
        //最后一行，去掉分隔线
        holder.mDividerLineView.setVisibility(position == mCertificateAttachments.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mCertificateAttachments.size();
    }

    public void setData(List<ResumeEntity.CertificateAttachment> certificateAttachments){
        if(certificateAttachments != null){
            mCertificateAttachments.clear();
            mCertificateAttachments.addAll(certificateAttachments);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.certificate_attachment_value_tv)
        TextView mCertificateAttachmentValueTv;
        @BindView(R.id.image_value_iv)
        ImageView mImageValueIv;
        @BindView(R.id.divider_line_view)
        View mDividerLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
