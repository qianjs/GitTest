package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.SimilarProblemsEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class SimilarProblemsAdapter extends BaseAdapter {
    private List<SimilarProblemsEntity> list;
    private LayoutInflater inflater;
    public SimilarProblemsAdapter(Context context, List<SimilarProblemsEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_similarproblems, null);
            viewHolder.tv_title=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            viewHolder.rl_voice=(RelativeLayout)convertView.findViewById(R.id.rl_voice);
            viewHolder.iv_userhead=(ImageView)convertView.findViewById(R.id.iv_userhead);
            viewHolder.tv_length=(TextView)convertView.findViewById(R.id.tv_length);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(list.get(position).getTitle());
        viewHolder.tv_content.setText(list.get(position).getReply());
        if(list.get(position).getIsAnswer().equals("true"))
        {
            viewHolder.rl_voice.setVisibility(View.VISIBLE);
            viewHolder.iv_userhead.setImageBitmap(null);
            ImageLoader.getInstance().displayImage(list.get(position).getUrl(), viewHolder.iv_userhead, MyApplication.roundedOption);
            viewHolder.tv_length.setText(list.get(position).getVoiceSecond() + "&apos;&apos;");
        }
        else
        {
            viewHolder.rl_voice.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder
    {
        TextView tv_title;
        TextView tv_content;
        RelativeLayout rl_voice;
        ImageView iv_userhead;
        TextView tv_length;
    }
}
