package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.AuctionInfoEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.util.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by linchao on 2016/12/9.
 */

public class AuctionInfoAdapter extends BaseAdapter {
    private Context mcontext;
    private List<AuctionInfoEntity> list;
    private LayoutInflater inflater;
    public AuctionInfoAdapter(Context mcontext, List<AuctionInfoEntity> list){
        this.mcontext=mcontext;
        this.list=list;
        inflater=LayoutInflater.from(mcontext);
    }

    public void setList(List<AuctionInfoEntity> list){
        this.list = list;
        notifyDataSetChanged();
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
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.auction_info_item,null);
            viewHolder.tv_score= (TextView) view.findViewById(R.id.tv_score);
            viewHolder.tv_count= (TextView) view.findViewById(R.id.tv_count);

            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        AuctionInfoEntity entity=list.get(i);
        viewHolder.tv_score.setText(entity.getServerScore());
        viewHolder.tv_count.setText(entity.getCompleteCnt());
        return view;
    }
    static class  ViewHolder{
        TextView tv_score;
        TextView tv_count;
    }
}
