package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class PopwindowAddGroup extends PopupWindow implements View.OnClickListener{
    private LayoutInflater inflater;
    private View conentView;
    private Context context;
    private EditText editTxt_Name;
    private TextView txt_confirm,txt_cancel;
    private OnConfirmClickListener onConfirmClickListener;

    public PopwindowAddGroup(Context context){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.conentView = inflater.inflate(R.layout.popwindowgroup, null);
        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
        initListener();
    }

    private void initView() {
        editTxt_Name= (EditText) conentView.findViewById(R.id.editTxt_Name);
        txt_confirm= (TextView) conentView.findViewById(R.id.txt_confirm);
        txt_cancel= (TextView) conentView.findViewById(R.id.txt_cancel);
    }


    private void initListener() {
        txt_confirm.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_confirm:
            {
                onConfirmClickListener.onconfirm(editTxt_Name.getText().toString());
            }
            break;
            case R.id.txt_cancel:
            {
                this.dismiss();
            }
            break;
        }
    }
    interface  OnConfirmClickListener{
        void onconfirm(String Name);
    }


    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }
}
