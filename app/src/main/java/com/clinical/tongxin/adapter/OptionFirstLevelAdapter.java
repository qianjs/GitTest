package com.clinical.tongxin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.OptionItemEntity;
import com.clinical.tongxin.inteface.IOnClickItemListener;

import java.util.ArrayList;

/**
 * Created by qjs on 2017/8/21.
 */

public class OptionFirstLevelAdapter extends RecyclerView.Adapter<OptionFirstLevelAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<OptionItemEntity> mOptionItemEntities = new ArrayList<>();
    private IOnClickItemListener mOnClickItemListener;

    public OptionFirstLevelAdapter(Context context, IOnClickItemListener onClickItemListener){
        mContext = context;
        mOnClickItemListener = onClickItemListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_option_first_level, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mNameTv.setText(mOptionItemEntities.get(position).mName);
        if(mOptionItemEntities.get(position).mSubs.size() > 0) {
            holder.mArrowIv.setVisibility(View.VISIBLE);
        }else{
            holder.mArrowIv.setVisibility(View.GONE);
        }

        ((View) holder.mNameTv.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemListener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptionItemEntities.size();
    }

    public void setData(ArrayList<OptionItemEntity> optionItemEntities){
        if(optionItemEntities != null){
            mOptionItemEntities.clear();
            mOptionItemEntities.addAll(optionItemEntities);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mNameTv;
        private ImageView mArrowIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.name_tv);
            mArrowIv = (ImageView) itemView.findViewById(R.id.arrow_iv);
        }
    }
}
