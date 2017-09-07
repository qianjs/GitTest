package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.CityEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author linchao on 2017/6/21.
 */
public class ChooseCityGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<CityEntity> mCities;


    public void setmCities(List<CityEntity> mCities) {
        this.mCities = mCities;
        notifyDataSetChanged();
    }
    private onDeleteListener onDeleteListener;
    public ChooseCityGridAdapter(Context context,List<CityEntity> mCities,onDeleteListener onDeleteListener) {
        this.mContext = context;
        this.mCities = mCities;
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public CityEntity getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_city_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_city_name);
            holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            view.setTag(holder);
        }else{
            holder = (HotCityViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getCityName());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.remove(mCities.get(position));
            }
        });
        return view;
    }

    public static class HotCityViewHolder{
        TextView name;
        ImageView iv_delete;
    }

    public interface onDeleteListener{
        void remove(CityEntity cityEntity);
    }
}
