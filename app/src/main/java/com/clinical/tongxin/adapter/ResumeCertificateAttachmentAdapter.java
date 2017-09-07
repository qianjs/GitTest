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
import com.clinical.tongxin.inteface.IOnClickItemListener;
import com.clinical.tongxin.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeCertificateAttachmentAdapter extends RecyclerView.Adapter<ResumeCertificateAttachmentAdapter.ViewHolder> {
    private Context mContext;
    private List<ResumeEntity.CertificateAttachment> mCertificateAttachments = new ArrayList<>();
    private IOnClickItemListener mOnClickItemListener;

    public ResumeCertificateAttachmentAdapter(Context context, IOnClickItemListener onClickItemListener){
        mContext = context;
        mOnClickItemListener = onClickItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_resume_certificate_attachment_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String url = mCertificateAttachments.get(position).mUrl;
        if(!url.contains("storage")){
            //网络地址
            url = UrlUtils.BASE_URL + url;
        }
        Glide.with(mContext).load(url)
                .placeholder(R.mipmap.fail)
                .into(holder.mImageIv);
        holder.mContentTv.setText(mCertificateAttachments.get(position).mDescription);

//        ((View)holder.mContentTv.getParent()).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnClickItemListener.onClickItem(position);
//            }
//        });
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
        return mCertificateAttachments.size();
    }

    public void setData(List<ResumeEntity.CertificateAttachment> certificateAttachments){
        if(certificateAttachments != null){
            mCertificateAttachments.clear();
            mCertificateAttachments.addAll(certificateAttachments);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageIv;
        TextView mContentTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageIv = (ImageView) itemView.findViewById(R.id.img_iv);
            mContentTv = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }
}
