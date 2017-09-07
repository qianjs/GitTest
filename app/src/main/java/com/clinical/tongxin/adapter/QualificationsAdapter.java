package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.QualificationListEntity;
import com.clinical.tongxin.myview.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class QualificationsAdapter extends BaseAdapter {
    private Context context;
    private List<QualificationListEntity> list;
    private LayoutInflater inflater;
    private QualificationGridViewAdapter adapter;

    public QualificationsAdapter(Context context, List<QualificationListEntity> list) {
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
            view = inflater.inflate(R.layout.item_qualification, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.txtTitle.setText(list.get(i).getProjectTypeName());
        adapter=new QualificationGridViewAdapter(context,list.get(i).getList());
        viewHolder.gridview.setAdapter(adapter);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.gridview)
        MyGridView gridview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
