package com.clinical.tongxin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.XYDataEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.ColumnChartMyView;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.RadarMapView;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class CountActivity extends  BaseActivity implements View.OnClickListener{
    private TextView title;
    private TextView txt_budget,txt_spend,txt_tasktype,txt_taskcount;
    //雷达图
    private RadarMapView radarmap;

    private List<PointValue> listPointValue;//折线图的数据
    private List<SubcolumnValue> listSubcolumnValue;//柱形图单个数据
    private List<Column> listColumn;//柱形图总数据
    private List<AxisValue> xlist,xlist1;//x轴的数据  预算
    private ComboLineColumnChartView comchartView;
//折线图
    String[] date = { "","7月-16","8月-16","9月-16","10月-16","11月-16","12月-16" };// X轴的标注
    String[] date1 = { "0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100" };
    int[] score =  {45,22,18,60,32,67};//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<PointValue> mPoint = new ArrayList<PointValue>();
    private LineChartView lineChart;
    private ColumnChartMyView columnchart;
    private MyProgressDialog mDialog;
    private ImageView cursor;
    private LinearLayout ll_cursor;
    private int width;
    private int lastPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        mDialog=new MyProgressDialog(this,"请稍等...");
        initView();
        initListener();
        findDatabudget();
        comchartView.setVisibility(View.VISIBLE);
        width = getWindowManager().getDefaultDisplay().getWidth();
        addCursor();
    }
    private void addCursor() {
        cursor = new ImageView(context);
        Gallery.LayoutParams params = new Gallery.LayoutParams(width/4, Gallery.LayoutParams.MATCH_PARENT);
        cursor.setLayoutParams(params);
        cursor.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_corner));
        ll_cursor.addView(cursor);
    }


    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setText("统计分析");
        txt_budget= (TextView) findViewById(R.id.txt_budget);
        txt_spend= (TextView) findViewById(R.id.txt_spend);
        txt_tasktype= (TextView) findViewById(R.id.txt_tasktype);
        txt_taskcount= (TextView) findViewById(R.id.txt_taskcount);
        radarmap= (RadarMapView) findViewById(R.id.radarMap_View);
        listPointValue=new ArrayList<PointValue>();
        listColumn=new ArrayList<Column>();
        xlist=new ArrayList<AxisValue>();
        comchartView= (ComboLineColumnChartView) findViewById(R.id.comchartView);
        lineChart= (LineChartView) findViewById(R.id.linechart);
        columnchart= (ColumnChartMyView) findViewById(R.id.columnchart);
        ll_cursor = (LinearLayout)findViewById(R.id.ll_cursor);
    }

    private void initListener() {
        txt_budget.setOnClickListener(this);
        txt_spend.setOnClickListener(this);
        txt_tasktype.setOnClickListener(this);
        txt_taskcount.setOnClickListener(this);
    }
    /**
     * 初始化动画
     */
    private void startAnimation(int start,int end) {
        Animation animation = null;
        if (start== 0){
            if (end == 1) animation = new TranslateAnimation(0, width/4, 0, 0);
            if (end == 2) animation = new TranslateAnimation(0, width*2/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(0, width*3/4, 0, 0);
        }else if (start == 1){
            if (end == 0) animation = new TranslateAnimation(width/4, 0, 0, 0);
            if (end == 2) animation = new TranslateAnimation(width/4, width*2/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(width/4, width*3/4, 0, 0);
        }else if (start == 2){
            if (end == 0) animation = new TranslateAnimation(width*2/4, 0, 0, 0);
            if (end == 1) animation = new TranslateAnimation(width*2/4, width*1/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(width*2/4, width*3/4, 0, 0);
        }else if (start == 3){
            if (end == 0) animation = new TranslateAnimation(width*3/4, 0, 0, 0);
            if (end == 1) animation = new TranslateAnimation(width*3/4, width*1/4, 0, 0);
            if (end == 2) animation = new TranslateAnimation(width*3/4, width*2/4, 0, 0);
        }
        if (animation != null){
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //预算情况
            case R.id.txt_budget:
            {
                radarmap.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                columnchart.setVisibility(View.GONE);
                comchartView.setVisibility(View.VISIBLE);
                startAnimation(lastPosition,0);

                findDatabudget();
                lastPosition = 0;
            }
            break;
            //花费金额
            case R.id.txt_spend:
            {
                int[] score =  {45,22,18,60,32,67};
//                getAxisXLables();
//                getAxisPoints(score);
//                initLineChart();
                startAnimation(lastPosition,1);
                getlinechartDate();
                lastPosition = 1;
                radarmap.setVisibility(View.GONE);
                comchartView.setVisibility(View.GONE);
                columnchart.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
            }
            break;
            //任务类型
            case R.id.txt_tasktype:
            {
                radarmap.setVisibility(View.VISIBLE);
                comchartView.setVisibility(View.GONE);
                columnchart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                startAnimation(lastPosition,2);
                getTaskTypeNumber();
                lastPosition = 2;
//                勘察；预算；说明；绘图
                String XyName[]={"","","","","",""};
                float aa[]={0,0,0,0,0,0};
                List<Float> list=new ArrayList<>();
                List<String> list1=new ArrayList<>();
                for(int i=0;i<aa.length;i++){
                    list.add(aa[i]);
                    list1.add(XyName[i]);
                }
                radarmap.InitDate(list,5,list1);
            }
            break;
            //任务统计
            case R.id.txt_taskcount:
            {
//                getColumnChartView();
                startAnimation(lastPosition,3);
                getTaskNumber();
                lastPosition = 3;
                float number[]={0,0,0,0,0};
                List<Integer> list11=new ArrayList<>();
                for (int i=1;i<6;i++){
                    list11.add((int)number[i-1]);
                }
                columnchart.initData(list11,1000);
                radarmap.setVisibility(View.GONE);
                comchartView.setVisibility(View.GONE);
                columnchart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
            }
            break;
        }
    }
    //任务类型
    private void getTaskTypeNumber() {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Get(UrlUtils.URL_ShowTaskType, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1= Utils.wsJsonToModel1(arg0);
                if (resultJsonP1.getCode().equals("200")){
                    try {
                        XYDataEntity model=new Gson().fromJson(resultJsonP1.getResult(),XYDataEntity.class);
                        float MaxValuess;
                        if (getMaxValue1(model.getSeriesData())<=10){
                            MaxValuess=10;
                        }else {
                            MaxValuess= getMaxValue1(model.getSeriesData());
                        }
                        radarmap.InitDate(model.getSeriesData(),MaxValuess,model.getCategories());
                        radarmap.invalidate();
                        JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CountActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });

    }
    //任务统计
    private void getTaskNumber() {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Get(UrlUtils.URl_ShowCountTask, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1= Utils.wsJsonToModel1(arg0);
                if (resultJsonP1.getCode().equals("200")){
                    try {
                        JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());
                        List<Integer> list = new Gson().fromJson(jsonObject.getString("seriesData"), new TypeToken<List<Integer>>() {
                        }.getType());
                        columnchart.invalidate();
                        float maxValuesList;
                        if (getMaxValue(list)<=10){
                            maxValuesList=10;
                        }else {
                            maxValuesList=getMaxValue(list);
                        }
                        columnchart.initData(list,maxValuesList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CountActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });

    }
    // 获取最大值 int型
    private int getMaxValue(List<Integer> list) {
        int rel = 0;
        for (int i = 0; i < list.size(); i++) {
            if (rel < list.get(i)) {
                rel = list.get(i);
            }
        }
        return rel;
    }
    // 获取最大值 double型
    private float getMaxValue1(List<Float> list) {
        float rel = 0;
        for (int i = 0; i < list.size(); i++) {
            if (rel < list.get(i)) {
                rel = list.get(i);
            }
        }
        return rel;
    }
    //预算情况
    private void findDatabudget(){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Post(UrlUtils.URL_ShowAppBudget, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultJsonP1= Utils.wsJsonToModel1(arg0);
                if (resultJsonP1.getCode().equals("200")){
                    try {
                        JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());
                        List<Float> list = new Gson().fromJson(jsonObject.getString("seriesUse"), new TypeToken<List<Float>>() {
                        }.getType());
                        List<Float> list1 = new Gson().fromJson(jsonObject.getString("seriesUnuse"), new TypeToken<List<Float>>() {
                        }.getType());
                        List<String> list2 = new Gson().fromJson(jsonObject.getString("categories"), new TypeToken<List<String>>() {
                        }.getType());
                        comchartView.invalidate();
                        budget(list2,list1,list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CountActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    /**
     * 预算情况
     */

    private void budget(List<String> xStrList,List<Float> linelist,List<Float> comlist){
        String ad[]={"项目经理1","项目经理2","项目经理3","项目经理4","项目经理5"};
        listPointValue.clear();
//        listSubcolumnValue.clear();
        listColumn.clear();
        xlist.clear();
        for (int i = 0; i < xStrList.size(); i++) {
            listPointValue.add(new PointValue(i,90));
            listSubcolumnValue = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < 1; ++j) {
                listSubcolumnValue.add(new SubcolumnValue((comlist.get(i)), Color.parseColor("#41b6fb")));
            }
            listColumn.add(new Column(listSubcolumnValue));
            xlist.add(new AxisValue(i).setLabel(xStrList
                    .get(i)));
        }
//        getAxisXLables(7);
        Line line=new Line(listPointValue).setColor(Color.parseColor("#fba461"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);// 折线图上每个数据点的形状 这里是圆形 （有三种
        // ：ValueShape.SQUARE
        // ValueShape.CIRCLE
        // ValueShape.SQUARE）
        line.setCubic(false);// 曲线是否平滑
        line.setStrokeWidth(1);// 线条的粗细，默认是3
//		line.setFilled(true);// 是否填充曲线的面积(阴影部分的面积)
//		line.setHasLabels(true);// 曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);// 是否用直线显示。如果为false 则没有曲线只有点显示
        line.setPointRadius(3);// 设置节点半径 s
        line.setHasPoints(true);// 是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);

        ColumnChartData data1=new ColumnChartData();
        data1.setColumns(listColumn);
        ColumnChartData data12=new ColumnChartData();
        data12.setColumns(listColumn);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        ComboLineColumnChartData cdata=new ComboLineColumnChartData();
        cdata.setLineChartData(data);
        cdata.setColumnChartData(data1);
        cdata.setColumnChartData(data12);
        Axis axisX = new Axis(); // X轴
        axisX.setHasTiltedLabels(false); // X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        // axisX.setTextColor(Color.WHITE); //设置字体颜色
        axisX.setTextColor(Color.parseColor("#d5dee7"));// 灰色

        // axisX.setName("未来几天的天气"); //表格名称
        axisX.setTextSize(11);// 设置字体大小
        axisX.setMaxLabelChars(1); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(xlist); // 填充X轴的坐标名称
        cdata.setAxisXBottom(axisX); // x 轴在底部
        // data.setAxisXTop(axisX); //x 轴在顶部
        axisX.setHasLines(false); // x 轴分割线

        // Axis axisY = new Axis(); //Y轴
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("");// y轴标注
        axisY.setTextSize(11);// 设置字体大小
        axisY.setHasTiltedLabels(false);// 字体倾斜true时
        // data.setAxisYLeft(axisY); //Y轴设置在左边
        axisY.setMaxLabelChars((getMaxValue1(comlist)+"").length()>(getMaxValue1(linelist)+"").length()?(getMaxValue1(comlist)+"").length():(getMaxValue1(linelist)+"").length());
        axisY.setHasLines(true); // x 轴分割线
        mAxisYValues.clear();
        for (int i = 0; i < 700; i += 100) {
            AxisValue value = new AxisValue(i);
            String label = i+"";
            value.setLabel(label);
            mAxisYValues.add(value);
        }
//        axisY.setValues(mAxisYValues);
        cdata.setAxisYLeft(axisY);
        comchartView.setZoomEnabled(false);//设置是否支持缩放
//		myview.setOnValueTouchListener(ColumnChartOnValueSelectListener touchListener);//为图表设置值得触摸事件
        comchartView.setInteractive(false);//设置图表是否可以与用户互动
        comchartView.setValueSelectionEnabled(false);//设置图表数据是否选中进行显示
        comchartView.setComboLineColumnChartData(cdata);//为图表设置数据，数据类型为ComboLineColumnChartData
        Viewport v = new Viewport(comchartView.getMaximumViewport());

        if (linelist.size()<7){
            v.right=6;
        }else {
            v.right=linelist.size();
        }
        v.top=(getMaxValue1(comlist)>getMaxValue1(linelist)?getMaxValue1(comlist):getMaxValue1(linelist))+10;
        comchartView.setMaximumViewport(v);
        comchartView.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(int count) {
        for (int j = 0; j < count; j++) {
            xlist.add(new AxisValue(j).setLabel(""));
        }

    }
    private void getlinechartDate(){
            mDialog.show();
            Map<String, String> map = new HashMap<>();
            map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
            map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
            XUtil.Get(UrlUtils.ShowAppMonthCost, map, new MyCallBack<String>() {
                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    super.onError(arg0, arg1);
                    mDialog.dismiss();
                }

                @Override
                public void onSuccess(String arg0) {
                    super.onSuccess(arg0);
                    ResultJsonP1 resultJsonP1= Utils.wsJsonToModel1(arg0);
                    if (resultJsonP1.getCode().equals("200")){
                        try {
                            JSONObject jsonObject=new JSONObject(resultJsonP1.getResult());
                            List<Integer> list = new Gson().fromJson(jsonObject.getString("seriesData"), new TypeToken<List<Integer>>() {
                            }.getType());
                            List<String> list1 = new Gson().fromJson(jsonObject.getString("categories"), new TypeToken<List<String>>() {
                            }.getType());
                            lineChart.invalidate();
                            float maxValuesList;
                            if (getMaxValue(list)<=10){
                                maxValuesList=10;
                            }else {
                                maxValuesList=getMaxValue(list);
                            }
                            getAxisXLables(list1);
                            getAxisPoints(list);
                            initLineChart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(CountActivity.this,resultJsonP1.getMsg().toString(),Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                }
            });


    }


    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#41b6fb")); // 折线的颜色
        final List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);// 折线图上每个数据点的形状 这里是圆形 （有三种
        // ：ValueShape.SQUARE
        // ValueShape.CIRCLE
        // ValueShape.SQUARE）
        line.setCubic(false);// 曲线是否平滑
        line.setStrokeWidth(1);// 线条的粗细，默认是3
        line.setFilled(false);// 是否填充曲线的面积(阴影部分的面积)
        line.setHasLabels(true);// 曲线的数据坐标是否加上备注[PointValue [x=1.0, y=72.0], PointValue [x=2.0, y=54.0], PointValue [x=3.0, y=54.0], PointValue [x=4.0, y=78.0], PointValue [x=5.0, y=66.0], PointValue [x=6.0, y=84.0], PointValue [x=7.0, y=60.0], PointValue [x=8.0, y=78.0], PointValue [x=9.0, y=54.0], PointValue [x=10.0, y=60.0], PointValue [x=11.0, y=84.0], PointValue [x=12.0, y=54.0], PointValue [x=13.0, y=66.0], PointValue [x=14.0, y=54.0], PointValue [x=15.0, y=78.0], PointValue [x=16.0, y=72.0], PointValue [x=17.0, y=54.0], PointValue [x=18.0, y=54.0], PointValue [x=19.0, y=78.0], PointValue [x=20.0, y=66.0], PointValue [x=21.0, y=84.0], PointValue [x=22.0, y=60.0], PointValue [x=23.0, y=78.0], PointValue [x=24.0, y=54.0], PointValue [x=25.0, y=60.0], PointValue [x=26.0, y=84.0], PointValue [x=27.0, y=54.0], PointValue [x=28.0, y=66.0], PointValue [x=29.0, y=54.0], PointValue [x=30.0, y=0.0], PointValue [x=31.0, y=0.0]]
        line.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）

        line.setHasLines(true);// 是否用直线显示。如果为false 则没有曲线只有点显示
        line.setPointRadius(3);// 设置节点半径 s
        line.setHasPoints(true);// 是否显示圆点 如果为false 则没有原点只有点显示
        // line.
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        Line line2 = new Line(mPoint).setColor(Color.parseColor("#0490e5")); // 点的颜色
        line2.setHasLabels(true);// 曲线的数据坐标是否加上备注
        lines.add(line2);

        // 坐标轴
        Axis axisX = new Axis(); // X轴
        axisX.setHasTiltedLabels(false); // X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        // axisX.setTextColor(Color.WHITE); //设置字体颜色
        axisX.setTextColor(Color.parseColor("#d5dee7"));// 灰色

        // axisX.setName("未来几天的天气"); //表格名称
        axisX.setTextSize(11);// 设置字体大小
        axisX.setMaxLabelChars(1); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues); // 填充X轴的坐标名称
        axisX.setHasLines(false); // x 轴分割线
        axisX.setHasSeparationLine(true);
        data.setAxisXBottom(axisX); // x 轴在底部
        // data.setAxisXTop(axisX); //x 轴在顶部

        // Axis axisY = new Axis(); //Y轴
        axisX.setName("金额");
//        axisX.setTextSize(15);
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("");// y轴标注
        axisY.setTextSize(11);// 设置字体大小
        axisY.setHasTiltedLabels(false);// 字体倾斜true时
        // data.setAxisYLeft(axisY); //Y轴设置在左边
        axisY.setMaxLabelChars(5);
        axisY.setHasLines(true); // x 轴分割线
        mAxisYValues.clear();
        for (int i = 0; i < 80000; i += 10000) {
            AxisValue value = new AxisValue(i);
            String label = ""+i;
            value.setLabel(label);
            mAxisYValues.add(value);
        }
//        axisY.setValues(mAxisYValues);
        data.setAxisYLeft(axisY);
        lineChart.setInteractive(false);
        lineChart.setZoomType(ZoomType.HORIZONTAL); // 缩放类型，水平
        lineChart.setMaxZoom(3);// 缩放比例
        lineChart.setInteractive(false);// 设置图表是否可以与用户互动
        lineChart.setLineChartData(data);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
       v.right=mPointValues.size()+1;
        v.bottom=0;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);

    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(List<String> list) {
        mAxisXValues.clear();
         for (int i = 1; i <=list.size(); i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(list.get(i-1)));
            }

    }

    /**
     * Y轴的显示
     */
    // private void getAxitYLables(){
    // for (int i = 0; i < 100; i++) {
    // mAxisYValues.add(new AxisValue(i).setLabel(i+""));
    // }
    // }
    //

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(List<Integer> list) {
        mPointValues.clear();
        mPoint.clear();
            for (int i = 1; i <=list.size(); i++) {

                mPointValues.add(new PointValue(i, list.get(i-1)));

            }
        }


    /**
     * 柱形图
     */

//    private void getColumnChartView(){
//        String aa[]={"进行中","待验收","待支付","已完成","仲裁"};
//        listPointValue.clear();
////        listSubcolumnValue.clear();
//        listColumn.clear();
//        xlist.clear();
//        for (int i = 0; i < 5; i++) {
//            listPointValue.add(new PointValue(i, (i+1)*9));
//            listSubcolumnValue = new ArrayList<SubcolumnValue>();
//            for (int j = 0; j < 1; ++j) {
//                listSubcolumnValue.add(new SubcolumnValue((i+1)*9, Color.parseColor("#41b6fb")));
//            }
//            listColumn.add(new Column(listSubcolumnValue));
//            xlist.add(new AxisValue(i).setLabel(aa[i]));
//        }
//        ColumnChartData data1=new ColumnChartData(listColumn);
//        Axis axisX = new Axis(); // X轴
//        axisX.setHasTiltedLabels(false); // X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//        // axisX.setTextColor(Color.WHITE); //设置字体颜色
//        axisX.setTextColor(Color.parseColor("#d5dee7"));// 灰色
//
////         axisX.setName("未来几天的天气"); //表格名称
//        axisX.setTextSize(11);// 设置字体大小
//        axisX.setMaxLabelChars(1); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
//        axisX.setValues(xlist); // 填充X轴的坐标名称
//        axisX.setHasLines(false); // x 轴分割线
//        axisX.setHasSeparationLine(true);
//        data1.setAxisXBottom(axisX); // x 轴在底部
//        // data.setAxisXTop(axisX); //x 轴在顶部
//
//        // Axis axisY = new Axis(); //Y轴
//        axisX.setName("数量");
////        axisX.setTextSize(15);
//        Axis axisY = new Axis().setHasLines(true);
//        axisY.setName("");// y轴标注
//        axisY.setTextSize(11);// 设置字体大小
//        axisY.setHasTiltedLabels(false);// 字体倾斜true时
//        // data.setAxisYLeft(axisY); //Y轴设置在左边
//        axisY.setMaxLabelChars(3);
//        axisY.setHasLines(true); // x 轴分割线
//        for (int i = 0; i < 100; i += 10) {
//            AxisValue value = new AxisValue(i);
//            String label = "";
//            value.setLabel(label);
//            mAxisYValues.add(value);
//        }
//        data1.setAxisYLeft(axisY);
//        columnchart.setInteractive(false);
//        columnchart.setZoomType(ZoomType.HORIZONTAL); // 缩放类型，水平
//        columnchart.setMaxZoom(3);// 缩放比例
//        columnchart.setInteractive(true);// 设置图表是否可以与用户互动
//        columnchart.setColumnChartData(data1);
////        Viewport v = new Viewport(columnchart.getMaximumViewport());
////        v.left = 0;
////        v.right=xlist.size();
//////        v.bottom = 0;
//////        v.top = 105;
////        columnchart.setMaximumViewport(v);
////        columnchart.setCurrentViewport(v);
//    }
}
