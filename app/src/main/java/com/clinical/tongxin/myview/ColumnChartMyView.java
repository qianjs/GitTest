package com.clinical.tongxin.myview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29 0029.
 * 柱形图
 */

public class ColumnChartMyView extends View {
    private int leftX;
    private int leftY;
    private int with;
    private Paint mainPaint,textPaint;
    private List<Integer>  number;//进行中，待验收， 待支付，已完成，仲裁的数目
    private float maxvalues;
    public ColumnChartMyView(Context context) {
        super(context);
    }

    public ColumnChartMyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnChartMyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnChartMyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.with=w;
        // TODO Auto-generated method stub
        leftX=50;
        leftY=h-70;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    public void initData(List<Integer> Number,float maxvalues){
        this.number=Number;
        this.maxvalues=maxvalues;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        onDrawLine(canvas);
        super.onDraw(canvas);
    }

    private void onDrawLine(Canvas canvas){
        Path path = new Path();
        path.reset();
        for (int i = 0; i < 2; i++) {
            path.moveTo(leftX, leftY);
            if (i==0) {

                path.lineTo(leftX, 0);

            }else{
                path.lineTo(leftX+with, leftY);
            }
            canvas.drawPath(path, mainPaint);
        }
        Path path1 = new Path();
        path1.reset();
        int jj=leftY/10;
        int jw=with/15;
//	mainPaint.setColor(Color.parseColor("#000000"));
//	for (int i = 1; i <15; i++) {
//		path1.moveTo(leftX+jw*i, leftY);
//		path1.lineTo(leftX+jw*i, 0);
//		canvas.drawPath(path1, mainPaint);
//	}
        mainPaint.setStrokeWidth(1);
        for (int i = 1; i < 10; i++) {
            if (i==1) {
                canvas.drawText("0", leftX-40,leftY,textPaint);
            }
            path1.moveTo(leftX, leftY-jj*i);
            path1.lineTo(leftX+with, leftY-jj*i);
            canvas.drawPath(path1, mainPaint);
            if (maxvalues<=10){
                canvas.drawText(i+"", leftX-40,leftY-jj*i,textPaint);
            }else {
                canvas.drawText((int)maxvalues/10*i+"", leftX-40,leftY-jj*i,textPaint);
            }
//            canvas.drawText(i*10+"", leftX-40,leftY-jj*i,textPaint);
        }
        int jw1=with/10;
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mainPaint.setColor(Color.parseColor("#41b6fb"));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        String aa[]={"进行中","待验收","待支付","已完成","仲裁"};
        for (int j = 0; j <5; j++) {
            float numbertop;
            if (maxvalues<=10){
                numbertop=leftY*number.get(j)/10;
            }else {
                numbertop=leftY*number.get(j)/maxvalues;
            }

            canvas.drawRect(leftX + jw1*(j+1)+50*j, leftY-numbertop, leftX +jw1*(j+1)+50*(j+1), leftY, mainPaint);//左上角x,y右下角x,y，画笔
            canvas.drawText(aa[j]+"", leftX+jw1*(j+1)+50*j,leftY+20,textPaint);
        }
        textPaint.setTextSize(26);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("数目", with/2,leftY+50,textPaint);
    }
}
