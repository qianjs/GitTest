package com.clinical.tongxin;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.clinical.tongxin.adapter.GridViewPicAdapter;
import com.clinical.tongxin.entity.BaseEntity;
import com.clinical.tongxin.entity.DisplayInfoEntity;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyGridView;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 添加需求
 * Created by 马骥 on 2016/8/18 0018.
 */
public class ReleaseDemandActivity extends BaseActivity{
    // 进度
    private MyProgressDialog mDialog;
    private GridViewPicAdapter mAdapter;
    private MyGridView myGridView;
    private EditText edtTxt_content;
    private ImageView img_add;
    private Button btn_ok;
    private List<String> listpic;
    private RelativeLayout rl_selectdate;
    private TextView tv_date;
    private String pid="";//分类编号
    private ImageView img_pic;
    private TextView tv_title, tv_subtitle, tv_content,tv_selectProject,tv_city;
    private FlowLayout flowLayout;
    //private PopWindowSingleListView popWindowSingleListView;
    //private int mPosition=0;//选择的position
    private List<BaseEntity> sProject;//选择的项目
    private String provinceId="";//省份编号
    private PopWindowSelectArea popWindowSelectArea;
    private PopWindowSelectProject popWindowSelectProject;
    TimePickerView pvTime;
    View vMasker;
    private LayoutInflater inflater;
    GradientDrawable gd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releasedemand);
        initView();
        findDisplayInfo("9");
        initListener();
        initPickerView();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("发布需求");
        inflater=LayoutInflater.from(this);
        gd=new GradientDrawable();
        listpic=new ArrayList<String>();
        sProject=new ArrayList<BaseEntity>();
        edtTxt_content=(EditText)findViewById(R.id.edtTxt_content);
        img_add=(ImageView)findViewById(R.id.img_add);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        rl_selectdate=(RelativeLayout)findViewById(R.id.rl_selectdate);
        tv_date=(TextView)findViewById(R.id.tv_date);
        myGridView=(MyGridView)findViewById(R.id.gv_pic);
        mAdapter=new GridViewPicAdapter(this,listpic);
        myGridView.setAdapter(mAdapter);

        img_pic = (ImageView) findViewById(R.id.img_pic);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_subtitle = (TextView)findViewById(R.id.tv_subtitle);
        tv_content = (TextView) findViewById(R.id.tv_content);


        tv_selectProject = (TextView) findViewById(R.id.tv_selectProject);
        tv_city = (TextView) findViewById(R.id.tv_city);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        //pid=getIntent().getStringExtra("typeId");
    }
    private void initListener()
    {
        mAdapter.setOnClickClearImageListener(new GridViewPicAdapter.OnClickClearImageListener() {
            @Override
            public void OnClearImage(int position) {
                listpic.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReleaseDemandActivity.this, SelectCameraDialog.class);
                Bundle bundleObject = new Bundle();
                GoClass myclass = new GoClass();
                myclass.setGoToClass(ReleaseDemandActivity.class);
                bundleObject.putSerializable("myclass", myclass);
                intent.putExtras(bundleObject);
                startActivity(intent);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTxt_content.getText().toString().equals("")) {
                    Toast.makeText(ReleaseDemandActivity.this, "需求内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                releaseDemand();
            }
        });
        rl_selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
//                if(popWindowSingleListView==null)
//                {
//                    popWindowSingleListView=new PopWindowSingleListView(ReleaseDemandActivity.this);
//                    popWindowSingleListView.setOnSelectItemChangeListener(new PopWindowSingleListView.OnSelectItemChangeListener() {
//                        @Override
//                        public void onSelectChange(String paramString, int position, String id) {
//                            tv_date.setText(paramString);
//                            mPosition=position;
//                        }
//                    });
//                    popWindowSingleListView.ShowData("demand");
//                    popWindowSingleListView.showAsDropDown(rl_selectdate);
//                }
//                else
//                {
//                    popWindowSingleListView.setSelected(mPosition);
//                    popWindowSingleListView.showAsDropDown(rl_selectdate);
//                }
            }
        });
        tv_selectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindowSelectProject == null) {
                    popWindowSelectProject = new PopWindowSelectProject(ReleaseDemandActivity.this);
                    popWindowSelectProject.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                    popWindowSelectProject.setOnSelectItemChangeListener(new PopWindowSelectProject.OnSelectItemChangeListener() {
                        @Override
                        public void onSelectChange(String paramString, String id) {

                            int i = 0;
                            for (BaseEntity model0 : sProject) {
                                if (model0.getId().equals(id)) {
                                    i++;
                                }
                            }
                            if (i > 0) {
                                Toast.makeText(ReleaseDemandActivity.this, paramString + "已经选择不能重复选择", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            BaseEntity model = new BaseEntity();
                            model.setId(id);
                            model.setText(paramString);
                            sProject.add(model);
                            refreshProject();
                        }
                    });

                    popWindowSelectProject.ShowData();
                    popWindowSelectProject.showAsDropDown(findViewById(R.id.in_top));
                } else {
                    popWindowSelectProject.showAsDropDown(findViewById(R.id.in_top));
                }
            }
        });
        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindowSelectArea == null) {
                    popWindowSelectArea = new PopWindowSelectArea(ReleaseDemandActivity.this);
                    popWindowSelectArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                    popWindowSelectArea.setOnSelectItemChangeListener(new PopWindowSelectArea.OnSelectItemChangeListener() {
                        @Override
                        public void onSelectChange(String paramString, String id, String pid) {
                            //countryId=pid;
                            provinceId = id;
                            //Toast.makeText(MyBeautyActivity.this, paramString, Toast.LENGTH_SHORT).show();
                            tv_city.setText(paramString);

                        }
                    });
                    popWindowSelectArea.ShowData();
                    popWindowSelectArea.showAsDropDown(findViewById(R.id.in_top));
                } else {
                    popWindowSelectArea.showAsDropDown(findViewById(R.id.in_top));
                }
            }
        });
    }
    private void initPickerView() {
        vMasker = findViewById(R.id.vMasker);
        // 控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 5);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() < new Date().getTime()) {
                    Toast.makeText(ReleaseDemandActivity.this,
                            "选择的有效期不能小于当前时间", Toast.LENGTH_SHORT).show();
                } else {
                    tv_date.setText(Utils.getYearMonthDay(date));
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            ArrayList<String> list = (ArrayList<String>)bundle.getSerializable("list");
            listpic.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void releaseDemand()
    {
        mDialog.show();
        String projectId="";
        for(BaseEntity model:sProject)
        {
            projectId+=model.getId()+",";
        }
        if(!projectId.equals(""))
        {
            projectId=projectId.substring(0,projectId.length()-1);
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
        map.put("pListId",projectId);
        map.put("provinceId",pid);
        map.put("date",tv_date.getText().toString());
        map.put("text",edtTxt_content.getText().toString());
        for (int i=0;i<listpic.size();i++)
        {
            map.put("file"+i, new File(listpic.get(i)));
        }

        XUtil.UpLoadFileAndText(UrlUtils.URL_ReleaseDemand, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP myjson = Utils.wsJsonToModel(json);

                mDialog.dismiss();
                finish();
            }

        });
    }
    private void findDisplayInfo(String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        XUtil.Get(UrlUtils.URL_FindDisplayInfo, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(String json) {
                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP myjson = Utils.wsJsonToModel(json);
                if (myjson != null) {
                    DisplayInfoEntity entity = new Gson().fromJson(myjson.getI().getData().toString(), DisplayInfoEntity.class);
                    ImageLoader.getInstance().displayImage(entity.getUrl(), img_pic, MyApplication.normalOption);
                    tv_title.setText(entity.getTitle());
                    tv_subtitle.setText(entity.getSubTitle());
                    tv_content.setText(entity.getDetails());
                }

            }
        });
    }
    private void refreshProject()
    {
        flowLayout.removeAllViews();
        for(BaseEntity model:sProject)
        {
            final View viewtext = inflater.inflate(R.layout.textview2, null);
            //viewtext.setBackgroundColor(Color.parseColor("#e8f2d9"));
             TextView tv = (TextView) viewtext.findViewById(R.id.tv_mytext);
            tv.setText(model.getText()+" X ");
            viewtext.setTag(model.getId());
            //gd.setColor(Color.parseColor(colors[Utils.getRandomInt(4)]));
            //gd.setColor(Color.parseColor("#e8f2d9"));
            //viewtext.setBackgroundResource(R.drawable.textview_corner2);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            params.bottomMargin = 10;
            viewtext.setLayoutParams(params);
            flowLayout.addView(viewtext);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(Iterator<BaseEntity> it=sProject.iterator();it.hasNext();)
                    {
                        String s=it.next().getId();
                        if(s.equals(viewtext.getTag().toString()))
                        {
                            it.remove();
                            flowLayout.removeView(viewtext);
                        }
                    }

                }
            });
        }
    }
}
