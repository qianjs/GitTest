package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.clinical.tongxin.adapter.PopWindowClassificationAdapter;
import com.clinical.tongxin.adapter.PopWindowRecommendAdapter;
import com.clinical.tongxin.entity.MyBaseEntity;
import com.clinical.tongxin.myview.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import static com.hyphenate.chat.a.c.a;

/**
 * 任务分类
 * Created by linchao on 2017/1/3
 */
public class PopWindowClassification extends PopupWindow {
    private View conentView;
    private OnSelectItemChangeListener listener;
    private LayoutInflater inflater;
    private Context context;
    private List<String> mlist;
    private PopWindowClassificationAdapter mAdapter;
    private ListView listview;
    private View view_hint;
    private int type; // 1 全部状态 2全部分类 3 智能排序

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PopWindowClassification(Context paramContext) {
        this.context=paramContext;
        inflater = LayoutInflater.from(paramContext);
        this.conentView = inflater.inflate(R.layout.popwindow_classification, null);
        // 加载中
        mlist = new ArrayList<>();
        mAdapter=new PopWindowClassificationAdapter(paramContext,mlist);
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

    public void setList(List<String> list){
        mAdapter.setList(list);
    }

    private void initListener()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String model =  (String)arg0.getItemAtPosition(arg2);
                listener.onSelectChange(model, arg2, type);
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
        public void onSelectChange(String paramString, int position, int type);
    }

}
