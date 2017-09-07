package com.clinical.tongxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.AllVideoListActivity;
import com.clinical.tongxin.R;
import com.clinical.tongxin.TypeVideoListActivity;
import com.clinical.tongxin.VideoViewActivity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TrainVideoTypeInfoEntity;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clinical.tongxin.VideoViewActivity.KEY_VIDEO_URL;

/**
 * Created by qjs on 2017/8/3.
 */

public class VideoTypeAdapter extends RecyclerView.Adapter<VideoTypeAdapter.ViewHolder> {

    private Context mContext;
    private List<TrainVideoTypeInfoEntity> mData = new ArrayList<>();
    // 进度
    private MyProgressDialog mDialog;

    public VideoTypeAdapter(Context context){
        mContext = context;
        // 加载中
        mDialog = new MyProgressDialog(mContext, "请稍后...");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_video_type_info, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mVideoTypeNameTv.setText(mData.get(position).getVideoTypeName());
        holder.mMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("count", "20");
                map.put("videoTypeId", mData.get(position).getVideoTypeId());
                XUtil.Get(UrlUtils.URL_TRAIN_TYPE_VIDEO, map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResultJsonP1 myjson = Utils.wsJsonToModel1(result);
                        if (myjson.getCode().equals("200")) {
                            Gson gson = new Gson();
                            List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity> list = gson.fromJson(myjson.getResult(), new TypeToken<List<TrainVideoTypeInfoEntity.TrainVideoInfoEntity>>() {
                            }.getType());
                            Intent intent = new Intent(mContext, TypeVideoListActivity.class);
                            intent.putExtra(TypeVideoListActivity.KEY_TITLE, mData.get(position).getVideoTypeName());
                            intent.putParcelableArrayListExtra(AllVideoListActivity.KEY_VIDEO_LIST, (ArrayList<? extends Parcelable>) list);
                            mContext.startActivity(intent);
                        }
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(mContext, "获取视频失败，请重试。", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onFinished() {
                        mDialog.dismiss();
                    }
                });
            }
        });

        initView(holder.mVideoName1Tv, holder.mClickTimes1Tv, holder.mThumbnail1Iv, holder.mPlay1Iv, position, 0);
        initView(holder.mVideoName2Tv, holder.mClickTimes2Tv, holder.mThumbnail2Iv, holder.mPlay2Iv, position, 1);
        initView(holder.mVideoName3Tv, holder.mClickTimes3Tv, holder.mThumbnail3Iv, holder.mPlay3Iv, position, 2);
        initView(holder.mVideoName4Tv, holder.mClickTimes4Tv, holder.mThumbnail4Iv, holder.mPlay4Iv, position, 3);
    }


    private void initView(TextView nameTv, TextView clickTimesTv, ImageView thumbnailIv, ImageView playIv, final int position, final int index){
        //数据不足，隐藏layout
        ViewGroup viewGroup = getParentLayout(thumbnailIv);
        if(mData.get(position).getTrainVideoInfoEntitys().size() > index) {
            viewGroup.setVisibility(View.VISIBLE);

            nameTv.setText(mData.get(position).getTrainVideoInfoEntitys().get(index).getVideoName());
            //加载缩略图t
            Utils.showVideoThumbView(mContext, thumbnailIv, UrlUtils.BASE_URL + mData.get(position).getTrainVideoInfoEntitys().get(index).getThumbnailUrl());
            clickTimesTv.setText(Integer.toString(mData.get(position).getTrainVideoInfoEntitys().get(index).getClickTimes()));

            playIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo(mData.get(position).getTrainVideoInfoEntitys().get(index).getUrl(), mData.get(position).getTrainVideoInfoEntitys().get(index).getVideoId());

                }
            });
        }else{
            //隐藏对应的Layout
            int lastVisibleIndex = mData.get(position).getTrainVideoInfoEntitys().size() - 1;
            if((lastVisibleIndex == index - 1) && (index % 2 > 0)) {
                //如果前一个layout是最后一个可见的，后面的layout需要占位，防止布局错乱
                viewGroup.setVisibility(View.INVISIBLE);
            }else{
                viewGroup.setVisibility(View.GONE);
            }
        }
    }

    private LinearLayout getParentLayout(View playView){
        return (LinearLayout)playView.getParent().getParent();
    }

    private void playVideo(String url, String videoId){
        Intent intent = new Intent(mContext, VideoViewActivity.class);
        intent.putExtra(KEY_VIDEO_URL, url);
        mContext.startActivity(intent);
        //发送增加视频点击量请求
        Map<String, String> map = new HashMap<>();
        map.put("videoId", videoId);
        XUtil.Get(UrlUtils.URL_TRAIN_VIDEO_CLICK_TIMES_ADD, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void setData(List<TrainVideoTypeInfoEntity> data){
        if(data != null){
            mData.clear();
            mData.addAll(data);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mVideoTypeNameTv;
        TextView mMoreTv;
        TextView mVideoName1Tv;
        TextView mClickTimes1Tv;
        ImageView mThumbnail1Iv;
        ImageView mPlay1Iv;
        TextView mVideoName2Tv;
        TextView mClickTimes2Tv;
        ImageView mThumbnail2Iv;
        ImageView mPlay2Iv;
        TextView mVideoName3Tv;
        TextView mClickTimes3Tv;
        ImageView mThumbnail3Iv;
        ImageView mPlay3Iv;
        TextView mVideoName4Tv;
        TextView mClickTimes4Tv;
        ImageView mThumbnail4Iv;
        ImageView mPlay4Iv;

        public ViewHolder(View itemView) {
            super(itemView);
            mVideoTypeNameTv = (TextView)itemView.findViewById(R.id.video_type_name_tv);
            mMoreTv = (TextView)itemView.findViewById(R.id.more_tv);
            //视频1
            LinearLayout linearLayout1 = (LinearLayout)itemView.findViewById(R.id.video1_layout);
            mVideoName1Tv = (TextView)linearLayout1.findViewById(R.id.video_name_tv);
            mClickTimes1Tv = (TextView)linearLayout1.findViewById(R.id.click_times_tv);
            mThumbnail1Iv = (ImageView)linearLayout1.findViewById(R.id.thumbnail_iv);
            mPlay1Iv = (ImageView)linearLayout1.findViewById(R.id.play_iv);
            //视频2
            LinearLayout linearLayout2 = (LinearLayout)itemView.findViewById(R.id.video2_layout);
            mVideoName2Tv = (TextView)linearLayout2.findViewById(R.id.video_name_tv);
            mClickTimes2Tv = (TextView)linearLayout2.findViewById(R.id.click_times_tv);
            mThumbnail2Iv = (ImageView)linearLayout2.findViewById(R.id.thumbnail_iv);
            mPlay2Iv = (ImageView)linearLayout2.findViewById(R.id.play_iv);
            //视频3
            LinearLayout linearLayout3 = (LinearLayout)itemView.findViewById(R.id.video3_layout);
            mVideoName3Tv = (TextView)linearLayout3.findViewById(R.id.video_name_tv);
            mClickTimes3Tv = (TextView)linearLayout3.findViewById(R.id.click_times_tv);
            mThumbnail3Iv = (ImageView)linearLayout3.findViewById(R.id.thumbnail_iv);
            mPlay3Iv = (ImageView)linearLayout3.findViewById(R.id.play_iv);
            //视频4
            LinearLayout linearLayout4 = (LinearLayout)itemView.findViewById(R.id.video4_layout);
            mVideoName4Tv = (TextView)linearLayout4.findViewById(R.id.video_name_tv);
            mClickTimes4Tv = (TextView)linearLayout4.findViewById(R.id.click_times_tv);
            mThumbnail4Iv = (ImageView)linearLayout4.findViewById(R.id.thumbnail_iv);
            mPlay4Iv = (ImageView)linearLayout4.findViewById(R.id.play_iv);
        }
    }
}
