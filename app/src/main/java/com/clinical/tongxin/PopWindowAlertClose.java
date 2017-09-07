package com.clinical.tongxin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/29 0029.
 */
public class PopWindowAlertClose extends PopupWindow {
    private LayoutInflater inflater;
    private TextView tvconfirm,tvtext,tvok;
    private View conentView;
    private OnAlertClickListener listener;
    public PopWindowAlertClose(Context context){
        inflater=LayoutInflater.from(context);
        this.conentView=inflater.inflate(R.layout.popwindowalertclose,null);
        tvconfirm=(TextView)conentView.findViewById(R.id.tvconfirm);
        tvtext=(TextView)conentView.findViewById(R.id.tvtext);
        float radius=14;
        int fillColor = Color.parseColor("#0079ff");//内部填充颜色
//        GradientDrawable gd2=new GradientDrawable();
//        gd2.setColor(fillColor);
//        gd2.setCornerRadius(radius);
//        gd2.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, 0, 0});
//        tvconfirm.setBackgroundDrawable(gd2);
        tvconfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onClickConfirm(tvtext.getText().toString());
                dismiss();
            }

        });

        setContentView(conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);

    }
    public void setText(String str)
    {
        tvtext.setText(str);
    }
    public void setButtonText(String str)
    {
        tvconfirm.setText(str);
    }
    public void setOnAlertClickListener(OnAlertClickListener listener) {
        this.listener = listener;
    }
    public interface OnAlertClickListener{
        void onClickConfirm(String paramString);
    }
}
