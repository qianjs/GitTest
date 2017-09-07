package com.clinical.tongxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.City;
import com.clinical.tongxin.entity.CityEntity;
import com.clinical.tongxin.entity.LocateState;
import com.clinical.tongxin.myview.WrapHeightGridView;
import com.clinical.tongxin.util.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author linchao on 2017/6/21.
 */
public class SubscribeCityAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 2;

    private Context mContext;
    private LayoutInflater inflater;
    private List<CityEntity> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private int locateState = LocateState.LOCATING;
    private String locatedCity;
    private ChooseCityGridAdapter chooseCityGridAdapter;
    private List<CityEntity> chooseCities;
    public SubscribeCityAdapter(Context mContext, List<CityEntity> mCities,List<CityEntity> chooseCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.chooseCities = chooseCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null){
            mCities = new ArrayList<>();
        }
        mCities.add(0, new CityEntity("已选", "0"));
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++){
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getSubScribeFirstLetter(mCities.get(index).getPinYin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getSubScribeFirstLetter(mCities.get(index - 1).getPinYin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)){
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     * @param state
     */
    public void updateLocateState(int state, String city){
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0: mCities.size();
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
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType){
            case 0:    //已选
                view = inflater.inflate(R.layout.view_choose_city, parent, false);
                WrapHeightGridView gridView = (WrapHeightGridView) view.findViewById(R.id.gridview_hot_city);
                chooseCityGridAdapter = new ChooseCityGridAdapter(mContext, chooseCities, new ChooseCityGridAdapter.onDeleteListener() {
                    @Override
                    public void remove(CityEntity cityEntity) {
                        if (chooseCities.contains(cityEntity)){
                            chooseCities.remove(cityEntity);
                            refreshGridView(chooseCities);
                        }

                    }
                });
                gridView.setAdapter(chooseCityGridAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onCityClickListener != null){
                            onCityClickListener.onCityClick(chooseCityGridAdapter.getItem(position));
                        }
                    }
                });
                break;
            case 1:     //所有
                if (view == null){
                    view = inflater.inflate(R.layout.item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                }else{
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1){
                    final String city = mCities.get(position).getCityName();
                    holder.name.setText(city);
                    String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).getPinYin());
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).getPinYin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)){
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                    }else{
                        holder.letter.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onCityClickListener != null){
                                onCityClickListener.onCityClick(mCities.get(position));
                            }
                        }
                    });
                }
                break;
        }
        return view;
    }

    public static class CityViewHolder{
        TextView letter;
        TextView name;
    }

    public void setOnCityClickListener(OnCityClickListener listener){
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener{
        void onCityClick(CityEntity cityEntity);
        void onLocateClick();
    }

    public void refreshGridView(List<CityEntity> cityEntities){
        chooseCityGridAdapter.setmCities(cityEntities);
    }
}
