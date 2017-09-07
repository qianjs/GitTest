package com.clinical.tongxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ItemQualificationsEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class QualificationGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<ItemQualificationsEntity> list;
    private LayoutInflater inflater;

    public QualificationGridViewAdapter(Context context, List<ItemQualificationsEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        if (view == null) {
            view = inflater.inflate(R.layout.item_qualification_gridview, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.txtContent.setText(list.get(i).getTaskTypenName());
        if (list.get(i).getType().equals("1")){
            viewHolder.txtContent.setBackgroundResource(R.drawable.btn_red);
            viewHolder.txtContent.setTextColor(Color.WHITE);
        }else{
            viewHolder.txtContent.setBackgroundResource(R.drawable.btn_white1);
            viewHolder.txtContent.setTextColor(context.getResources().getColor(R.color.alpha_gray));
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.txt_content)
        TextView txtContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
