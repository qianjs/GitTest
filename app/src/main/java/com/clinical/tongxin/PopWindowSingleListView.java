package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.clinical.tongxin.adapter.PopWindowRecommendAdapter;
import com.clinical.tongxin.entity.MyBaseEntity;
import com.clinical.tongxin.myview.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能推荐
 * Created by 马骥 on 2016/8/17 0017.
 */
public class PopWindowSingleListView extends PopupWindow {
    private View conentView;
    private OnSelectItemChangeListener listener;
    private LayoutInflater inflater;
    private MyProgressDialog mDialog;
    private Context context;
    private List<MyBaseEntity> mlist;
    private PopWindowRecommendAdapter mAdapter;
    private ListView listview;
    public PopWindowSingleListView(Context paramContext) {
        this.context=paramContext;
        inflater = LayoutInflater.from(paramContext);
        this.conentView = inflater.inflate(R.layout.popwindow_recommend, null);
        // 加载中
        mDialog = new MyProgressDialog(context, "请稍后...");
        mlist=new ArrayList<MyBaseEntity>();
        mAdapter=new PopWindowRecommendAdapter(paramContext,mlist);
        listview=(ListView)conentView.findViewById(R.id.mlistview);
        listview.setAdapter(mAdapter);

        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
        initListener();
    }
    private void initView()
    {

    }
    public void ShowData(String type)
    {
        if(type.equals("demand"))
        {
            //发布需求 选择日期
            MyBaseEntity model = new MyBaseEntity();
            model.setId("1");
            model.setText("1天");
            model.setIsSelected("true");
            mlist.add(model);
            MyBaseEntity model2 = new MyBaseEntity();
            model2.setId("2");
            model2.setText("15天");
            model2.setIsSelected("false");
            mlist.add(model2);
            MyBaseEntity model3 = new MyBaseEntity();
            model3.setId("2");
            model3.setText("30天");
            model3.setIsSelected("false");
            mlist.add(model3);
        }
        else if(type.equals("recommend")) {
            //智能推荐
            MyBaseEntity model = new MyBaseEntity();
            model.setId("0");
            model.setText("智能推荐");
            model.setIsSelected("true");
            mlist.add(model);
            MyBaseEntity model2 = new MyBaseEntity();
            model2.setId("1");
            model2.setText("好评专家");
            model2.setIsSelected("false");
            mlist.add(model2);
            MyBaseEntity model3 = new MyBaseEntity();
            model3.setId("2");
            model3.setText("在线咨询");
            model3.setIsSelected("false");
            mlist.add(model3);
        }
        mAdapter.notifyDataSetChanged();
    }
    public void setSelected(int position)
    {
        if(position==-1)
        {
            return;
        }
        for(MyBaseEntity model : mlist)
        {
            model.setIsSelected("false");
        }
        mlist.get(position).setIsSelected("true");
        mAdapter.notifyDataSetChanged();
    }
    private void initListener()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                MyBaseEntity model = (MyBaseEntity) arg0.getItemAtPosition(arg2);
                listener.onSelectChange(model.getText(), arg2, model.getId());
                dismiss();
            }

        });
    }
    public void setOnSelectItemChangeListener(OnSelectItemChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectItemChangeListener {
        public void onSelectChange(String paramString,int position,String id);
    }

}
