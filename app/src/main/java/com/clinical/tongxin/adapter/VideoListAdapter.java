package com.clinical.tongxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.VideoActivity;
import com.clinical.tongxin.VideoViewActivity;
import com.clinical.tongxin.entity.TrainVideoTypeInfoEntity;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by QJS on 2017-07-25.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private Context mContext;
    private List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> mData = new ArrayList<>();

    public VideoListAdapter(Context context){
        mContext = context;
    }

    public void setData(List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> data){
        if(data != null) {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_info, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mVideoNameTv.setText(mData.get(position).getVideoName());
        holder.mClickTimesTv.setText(Integer.toString(mData.get(position).getClickTimes()));
        //加载缩略图
        Utils.showVideoThumbView(mContext, holder.mThumbnailIv, UrlUtils.BASE_URL + mData.get(position).getThumbnailUrl());
        //播放视频
        holder.mPlayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至视频播放页面
                String url = mData.get(position).getUrl();
                Intent intent = new Intent(mContext, VideoViewActivity.class);
                intent.putExtra(VideoViewActivity.KEY_VIDEO_URL, url);
                mContext.startActivity(intent);

                //发送增加视频点击量请求
                Map<String, String> map = new HashMap<>();
                map.put("videoId", mData.get(position).getVideoId());
                XUtil.Get(UrlUtils.URL_TRAIN_VIDEO_CLICK_TIMES_ADD, map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("jinshan", "点击视频请求成功！");
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("jinshan", "点击视频请求失败！");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.video_name_tv)
        TextView mVideoNameTv;
        @BindView(R.id.click_times_tv)
        TextView mClickTimesTv;
        @BindView(R.id.thumbnail_iv)
        ImageView mThumbnailIv;
        @BindView(R.id.play_iv)
        ImageView mPlayIv;
        public ViewHolder(View itemView) {
            super(itemView);
//            mVideoNameTv = (TextView) itemView.findViewById(R.id.video_name_tv);
//            mClickTimesTv = (TextView) itemView.findViewById(R.id.click_times_tv);
//            mThumbnailIv = (ImageView) itemView.findViewById(R.id.thumbnail_iv);
//            mPlayIv = (ImageView) itemView.findViewById(R.id.play_iv);
            ButterKnife.bind(this, itemView);
        }
    }
}
