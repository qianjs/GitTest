package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.VideoEntity;

import java.util.List;

/**
 * Created by lzj667 on 2016/9/12.
 */
public class VideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoEntity> list;
    private LayoutInflater inflater;
    private OnVideoClickListener mListener;
    public VideoAdapter(Context context,List<VideoEntity> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);

    }
    public void setOnVideoClickListener(OnVideoClickListener mListener)
    {
        this.mListener=mListener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view=inflater.inflate(R.layout.item_video_list,null);
            viewHolder=new ViewHolder();
           // viewHolder.txt_videoName= (TextView) view.findViewById(R.id.txt_videoName);
           // viewHolder.txt_details= (TextView) view.findViewById(R.id.txt_details);
            viewHolder.img_video= (ImageView) view.findViewById(R.id.img_video);
            viewHolder.img_play= (ImageView) view.findViewById(R.id.img_play);
            viewHolder.ll_info= (LinearLayout) view.findViewById(R.id.ll_info);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        final VideoEntity entity=list.get(i);
        //viewHolder.txt_videoName.setText("视频名称："+entity.getName());
        //viewHolder.txt_details.setText("视频简介：  " + entity.getDetails());
        //viewHolder.img_video.setImageBitmap(Utils.createVideoThumbnail(entity.getUrl(), 80, 80));
        viewHolder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPlayClick(entity.getUrl(),entity.getvId());
            }
        });
        viewHolder.ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onShowInfoClick(entity.getdId());
            }
        });

        return view;
    }
    private class ViewHolder{
        ImageView img_video;
        ImageView img_play;
        LinearLayout ll_info;
    }
    public interface OnVideoClickListener
    {
        public void onPlayClick(String url,String vid);
        public void onShowInfoClick(String id);
    }
}
