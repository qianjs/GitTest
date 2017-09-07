package com.clinical.tongxin.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.QueriesEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class QueriesAdapter extends BaseAdapter {
    private List<QueriesEntity> list;
    private LayoutInflater inflater;
    public QueriesAdapter(Context context, List<QueriesEntity> list)
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
            convertView=inflater.inflate(R.layout.item_queries, null);
            viewHolder.tv_title=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            viewHolder.tv_doctorName=(TextView)convertView.findViewById(R.id.tv_doctorName);
            viewHolder.tv_length=(TextView)convertView.findViewById(R.id.tv_length);
            viewHolder.iv_userhead=(ImageView)convertView.findViewById(R.id.iv_userhead);
            //viewHolder.tv_isAnswer=(TextView)convertView.findViewById(R.id.tv_isAnswer);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(list.get(position).getTitle());
        viewHolder.tv_content.setText(list.get(position).getDoctorDetails());
        viewHolder.tv_doctorName.setText(list.get(position).getDoctorName());
        //viewHolder.tv_length.setText(list.get(position).getVoiceSecond()+"&apos;&apos;");
        CharSequence styledText1 = Html.fromHtml(list.get(position).getVoiceSecond()+"&apos;&apos;");
        viewHolder.tv_length.setText(styledText1);
        viewHolder.iv_userhead.setImageBitmap(null);
        ImageLoader.getInstance().displayImage(list.get(position).getDoctorUrl(), viewHolder.iv_userhead, MyApplication.roundedOption);
        //viewHolder.tv_isAnswer.setText(list.get(position).getIsAnswer()=="true"?"已回答":"未回答");
        return convertView;
    }
    class ViewHolder
    {
        TextView tv_title;
        TextView tv_content;
        ImageView iv_userhead;
        TextView tv_doctorName;
        TextView tv_length;

    }
}
