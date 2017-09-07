package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.FragmentEntity;
import com.clinical.tongxin.entity.ItemFragmententity;
import com.clinical.tongxin.myview.ListViewForScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ServiceFragmentAdapter extends BaseAdapter {
    private List<FragmentEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private ItemFragmentAdapter adapter;
    private ItemGridViewAndListViewListener itemGridViewAndListViewListener;

    public void setItemGridViewAndListViewListener(ItemGridViewAndListViewListener itemGridViewAndListViewListener) {
        this.itemGridViewAndListViewListener = itemGridViewAndListViewListener;
    }

    public ServiceFragmentAdapter(List<FragmentEntity> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = inflater.inflate(R.layout.item_service_adapter, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        FragmentEntity model=list.get(i);
        viewHolder.txtTitle.setText(model.getTitle());
        if (model.getImg_whole_type()==0){
            viewHolder.gridview.setVisibility(View.VISIBLE);
            viewHolder.listView.setVisibility(View.GONE);
            adapter=new ItemFragmentAdapter(model.getImgs(),context,model.getImg_whole_type());
            viewHolder.gridview.setAdapter(adapter);
        }else if (model.getImg_whole_type()==1){
            viewHolder.gridview.setVisibility(View.GONE);
            viewHolder.listView.setVisibility(View.VISIBLE);
            adapter=new ItemFragmentAdapter(model.getImgs(),context,model.getImg_whole_type());
            viewHolder.listView.setAdapter(adapter);
        }
        viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemFragmententity itemFragmententity= (ItemFragmententity) adapterView.getItemAtPosition(i);
                if (itemFragmententity!=null){
                    itemGridViewAndListViewListener.itemclick(itemFragmententity);
                }
            }
        });
        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemFragmententity itemFragmententity= (ItemFragmententity) adapterView.getItemAtPosition(i);
                if (itemFragmententity!=null){
                    itemGridViewAndListViewListener.itemclick(itemFragmententity);
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.gridview)
        GridView gridview;
        @BindView(R.id.listView)
        ListViewForScrollView listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface ItemGridViewAndListViewListener{
        void itemclick(ItemFragmententity itemFragmententity);
    }
}
