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
 * Created by Administrator on 2016/12/28 0028.
 */

public class RadarMapView extends View{
    private int count=5;
    private double maxValue ;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔
    private int centerX,centerY;
    private float radius;                   //网格最大半径
    private float angle = (float) (Math.PI*2/count);
//    private double[] data ; //各维度分值
    private List<Float> data;
    private String scale[];
//    private String XYName[];//坐标名称
    private List<String> XYName;

    public RadarMapView(Context context) {
        super(context);
        angle = (float) (Math.PI*2/count);
    }

    public RadarMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        angle = (float) (Math.PI*2/count);
    }

    public RadarMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        angle = (float) (Math.PI*2/count);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadarMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        angle = (float) (Math.PI*2/count);
    }

    /**
     * 设置数据
     */

    public  void InitDate(List<Float> data,double maxValues,List<String> xyName){
        this.data=data;
        this.maxValue=maxValues;
        scale =new String[]{"0",(int)maxValue/4+"",(int)maxValue/4*2+"",(int)maxValue/4*3+"",(int)maxValue/4*4+""};
        this.XYName=xyName;
        this.count=data.size();
        angle = (float) (Math.PI*2/count);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        radius = Math.min(h, w)/2*0.7f;
        centerX = w/2;
        centerY = h/2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawScale(canvas);
        drawRegion(canvas);
        super.onDraw(canvas);
    }

    /**
     * 绘制正多边形
     */
    private void drawPolygon(Canvas canvas){
        Path path = new Path();
        float dis = textPaint.measureText("长度");//文本长度
        float r = radius/4;
        for(int i=0;i<5;i++){
            float curR = r*i;
            path.reset();
            for(int j=0;j<count;j++){
                if(j==0){
                    path.moveTo(centerX,centerY-curR);
                }else{
                    float x = (float) (centerX-curR*Math.sin(angle*j));
                    float y = (float) (centerY-curR*Math.cos(angle*j));
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制刻度
     */

    private void drawScale(Canvas canvas){
        float r = radius/4;

        for (int i = 0; i < 5; i++) {
            float curR = r*i;
            canvas.drawText(scale[i], centerX,centerY-curR,textPaint);

        }
    }
    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas){
        Path path = new Path();
        for(int i=0;i<6;i++){
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX-radius*Math.sin(angle*i));
            float y = (float) (centerY-radius*Math.cos(angle*i));
            path.lineTo(x, y);
            mainPaint.setStrokeWidth(2);
            canvas.drawPath(path, mainPaint);
        }
    }



    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for(int i=0;i<count;i++){
            float x = (float) (centerX-(radius+fontHeight/2)*Math.sin(angle*i));
            float y = (float) (centerY-(radius+fontHeight/2)*Math.cos(angle*i));

            if(angle*i>=0&&angle*i<=Math.PI/2){//第4象限
                float dis = textPaint.measureText("长度");//文本长度
                canvas.drawText(XYName.get(i), x-dis/2,y,textPaint);
            }else if(angle*i>=3*Math.PI/2&&angle*i<=Math.PI*2){//第3象限
                float dis = textPaint.measureText("长度");//文本长度
                canvas.drawText(XYName.get(i), x-dis/4,y,textPaint);
            }else if(angle*i>Math.PI/2&&angle*i<=Math.PI){//第2象限
                float dis = textPaint.measureText("长度");//文本长度
                canvas.drawText(XYName.get(i), x-dis,y,textPaint);
            }else if(angle*i>=Math.PI&&angle*i<3*Math.PI/2){//第1象限
                float dis = textPaint.measureText("长度");//文本长度
                canvas.drawText(XYName.get(i), x+dis/4,y,textPaint);
            }
        }
    }

    /**
     * 绘制区域
     * @param canvas
     */
    private void drawRegion(Canvas canvas){
        Path path = new Path();
        valuePaint.setAlpha(255);
        valuePaint.setColor(Color.parseColor("#41b6fb"));
        valuePaint.setStrokeWidth(2);
        for(int i=0;i<=count;i++){
            double percent =0;
            if (i==count) {
                percent = data.get(0)/maxValue;
            }else{
                percent = data.get(i)/maxValue;
            }
            float x = (float) (centerX-radius*Math.sin(angle*i)*percent);
            float y = (float) (centerY-radius*Math.cos(angle*i)*percent);
            if(i==0){
                path.moveTo(centerX, y);
            }else{
                path.lineTo(x,y);
            }
            valuePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, valuePaint);
            //绘制小圆点
//            canvas.drawCircle(x,y,0,valuePaint);
        }
    }
}
