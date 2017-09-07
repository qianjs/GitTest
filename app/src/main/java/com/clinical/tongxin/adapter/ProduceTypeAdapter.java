package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ProduceTypeEntity;

import java.util.List;

/**
 * Created by lzj667 on 2016/8/23.
 */
public class ProduceTypeAdapter extends BaseAdapter{
    private List<ProduceTypeEntity> mlist;
    private Context mcontext;
    private LayoutInflater inflater;
    public ProduceTypeAdapter(Context context,List list){
        this.mlist=list;
        this.mcontext=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewholder;
        if(view==null){
            viewholder=new ViewHolder();
            view=inflater.inflate(R.layout.item_popwindow_recommend,null);
            viewholder.txt_listString= (TextView) view.findViewById(R.id.tvtext);
            view.setTag(viewholder);

        }else {
            viewholder= (ViewHolder) view.getTag();
        }
        ProduceTypeEntity proentity=mlist.get(i);
        viewholder.txt_listString.setText(proentity.getPtName());
        return view;
    }
    public class ViewHolder{
        TextView txt_listString;
    }
}
