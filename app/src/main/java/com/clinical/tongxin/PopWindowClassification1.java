package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.clinical.tongxin.adapter.PopWindowClassificationAdapter;
import com.clinical.tongxin.adapter.PopWindowTaskAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务分类
 * Created by linchao on 2017/6/15
 */
public class PopWindowClassification1 extends PopupWindow {
    private View conentView;
    private OnSelectItemChangeListener listener;
    private LayoutInflater inflater;
    private Context context;
    private List<AptitudeEntity> mlist;
    private PopWindowTaskAdapter mAdapter;
    private ListView listview;
    private View view_hint;


    public PopWindowClassification1(Context paramContext,int type) {
        this.context=paramContext;
        inflater = LayoutInflater.from(paramContext);
        this.conentView = inflater.inflate(R.layout.popwindow_classification, null);
        // 加载中
        mlist = new ArrayList<>();
        mAdapter=new PopWindowTaskAdapter(paramContext,mlist,type);
        listview=(ListView)conentView.findViewById(R.id.mlistview);
        listview.setAdapter(mAdapter);
        view_hint = conentView.findViewById(R.id.view_hint);
        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initListener();
    }

    public void setList(List<AptitudeEntity> list){
        mAdapter.setList(list);
    }

    private void initListener()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AptitudeEntity aptitudeEntity = (AptitudeEntity) mAdapter.getItem(i);
                listener.onSelectChange(aptitudeEntity,i);
                dismiss();
            }
        });
        view_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    public void setOnSelectItemChangeListener(OnSelectItemChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectItemChangeListener {
        public void onSelectChange(AptitudeEntity aptitudeEntity, int position);
    }

}
