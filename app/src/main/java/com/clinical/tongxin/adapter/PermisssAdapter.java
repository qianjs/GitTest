package com.clinical.tongxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.TeamManagementActivity;
import com.clinical.tongxin.entity.PermissionEntity;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static android.R.attr.start;

/**
 * Created by apple on 2016/11/22.
 */

public class PermisssAdapter extends BaseAdapter {
    private List<PermissionEntity> list;
    private LayoutInflater inflater;
    private PermisssAdapter.OnClickImageListener listener;
    private Context context;
    public PermisssAdapter(Context context, List<PermissionEntity> list)
    {
        this.list=list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }
    public void setOnClickClearImageListener(PermisssAdapter.OnClickImageListener listener)
    {
        this.listener=listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        PermisssAdapter.ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new PermisssAdapter.ViewHolder();
            convertView=inflater.inflate(R.layout.item_permiss, null);
            viewHolder.img_pic=(ImageView)convertView.findViewById(R.id.img_pic);
            viewHolder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(PermisssAdapter.ViewHolder) convertView.getTag();
        }
//        if(list.get(position).getName().equals("已经任务"))
//        {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon01);
//        }
//        else if(list.get(position).getName().equals("指定任务"))
//        {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon02);
//        }
//        else
        if(list.get(position).getName().equals("我的任务"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon03);
        }
        else if(list.get(position).getName().equals("我的资产"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon04);
        }
        else if(list.get(position).getName().equals("分配预算"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon05);
        }
        else if(list.get(position).getName().equals("预算查询"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon06);
        }
        else if(list.get(position).getName().equals("项目经理"))
        {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon07);
        }
        else if(list.get(position).getName().equals("我的团队"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon08);
        }
        else if(list.get(position).getName().equals("统计分析"))
        {
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon09);
        }
        else if(list.get(position).getName().equals("抢单"))
        {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon010);
        }else if (list.get(position).getName().equals("我的预算")){
            viewHolder.img_pic.setImageResource(R.mipmap.index_icon09);
        }
        viewHolder.txt_name.setText(list.get(position).getName());


        return convertView;
    }
    class ViewHolder
    {
        ImageView img_pic;
        TextView txt_name;
    }
    public interface OnClickImageListener
    {
        public void OnClickImage(int position);
    }
}
