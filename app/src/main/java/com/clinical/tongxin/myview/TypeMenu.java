package com.clinical.tongxin.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.adapter.TextAdapter;
import com.clinical.tongxin.entity.MyBaseEntity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class TypeMenu extends LinearLayout {



    private ListView regionListView;
    private ListView plateListView;
    private ArrayList<MyBaseEntity> groups = new ArrayList<MyBaseEntity>();
    private LinkedList<MyBaseEntity> childrenItem = new LinkedList<MyBaseEntity>();
    private SparseArray<LinkedList<MyBaseEntity>> children = new SparseArray<LinkedList<MyBaseEntity>>();
    private TextAdapter plateListViewAdapter;
    private TextAdapter earaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tEaraPosition = 0;
    private int tBlockPosition = 0;
    private String showString = "不限";
Context context;
    public TypeMenu(Context context) {
        super(context);
        init(context);
    }

    public TypeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void updateShowText(String showArea, String showBlock) {
        if (showArea == null || showBlock == null) {
            return;
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).equals(showArea)) {
                earaListViewAdapter.setSelectedPosition(i);
                childrenItem.clear();
                if (i < children.size()) {
                    childrenItem.addAll(children.get(i));
                }
                tEaraPosition = i;
                break;
            }
        }
        for (int j = 0; j < childrenItem.size(); j++) {
            if (childrenItem.get(j).getText().replace("不限", "").equals(showBlock.trim())) {
                plateListViewAdapter.setSelectedPosition(j);
                tBlockPosition = j;
                break;
            }
        }
        setDefaultSelect();
    }

    private void init(Context context) {
        this.context=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
//        setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.choosearea_bg_left));
//        groups.add("保养配件");
//        LinkedList<String> tItem = new LinkedList<String>();
//        tItem.add("机油滤清器");
//        tItem.add("燃气滤清器");
//        tItem.add("空气滤清器");
//        tItem.add("刹车片");
//        tItem.add("火花塞");
//        children.put(0, tItem);
//        groups.add("油品/化学品");
//        LinkedList<String> tItem2 = new LinkedList<String>();
//        tItem2.add("机油润滑油");
//        tItem2.add("刹车油/制动液");
//        tItem2.add("助力转向油");
//        tItem2.add("手动挡变速箱油");
//        tItem2.add("空调制冷剂/冷媒");
//        children.put(1, tItem2);


    }
    public void setDataSource(ArrayList<MyBaseEntity> mgroups,SparseArray<LinkedList<MyBaseEntity>> mchildren)
    {
        this.groups=mgroups;
        this.children=mchildren;
        earaListViewAdapter = new TextAdapter(context, groups,
                R.mipmap.choose_item_selected,
                R.drawable.choose_eara_item_selector);
        earaListViewAdapter.setTextSize(17);
        earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
        regionListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < children.size()) {
                            childrenItem.clear();
                            childrenItem.addAll(children.get(position));
                            plateListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter = new TextAdapter(context, childrenItem,
                R.drawable.choose_item_right,
                R.drawable.choose_plate_item_selector);
        plateListViewAdapter.setTextSize(15);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {

                        showString = childrenItem.get(position).getText();
                        if (mOnSelectListener != null) {

                            mOnSelectListener.getValue(childrenItem.get(position),earaListViewAdapter.getSelectedPosition(),plateListViewAdapter.getSelectedPosition());
                        }
//						Toast.makeText(MyApplication.getInstance().getApplicationContext(), showString, Toast.LENGTH_SHORT).show();
                    }
                });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition).getText();
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();
    }
    public void setDefaultSelect() {
        regionListView.setSelection(tEaraPosition);
        plateListView.setSelection(tBlockPosition);
    }
    public void setSelect(int pPosition,int cPosition) {
//        earaListViewAdapter.setSelectedPosition(pPosition);
//        plateListViewAdapter.setSelectedPosition(cPosition);
        tEaraPosition=pPosition;
        tBlockPosition=cPosition;
    }
    public String getShowText() {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(MyBaseEntity showModel,int pPosition,int cPosition);
    }



}
