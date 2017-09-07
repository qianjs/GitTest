package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bigkoo.pickerview.TimePickerView;
import com.clinical.tongxin.adapter.ManagerMemberAdapter;
import com.clinical.tongxin.adapter.TaskAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.ManagerMemberEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonAptitude;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.GpsUtils;
import com.clinical.tongxin.util.StringUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.clinical.tongxin.R.color.gray_deep;


/**
 * 任务页面
 * @author LINCHAO
 * 2017/1/3
 */
@ContentView(R.layout.activity_task)
public class TaskActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 任务列表
    @ViewInject(R.id.lv_task)
    ListView lv_task;
    // 搜索框
    @ViewInject(R.id.et_search)
    EditText et_search;
    // 清除按钮
    @ViewInject(R.id.iv_search_clear)
    ImageView iv_search_clear;
    // 空结果
    @ViewInject(R.id.empty_view)
    View empty_view;
    // 定位按钮
    @ViewInject(R.id.iv_more)
    ImageView iv_more;
    // 当前位置
    @ViewInject(R.id.tv_my_location)
    TextView tv_my_location;
    // 位置
    @ViewInject(R.id.ll_location)
    View ll_location;
    // loading
    @ViewInject(R.id.pb_refresh_location)
    ProgressBar pb_refresh_location;

    // 刷新按钮
    @ViewInject(R.id.iv_refresh)
    ImageView iv_refresh;

    @ViewInject(R.id.load_more_list_view_ptr_frame)
    PtrFrameLayout mPtrFrameLayout;

    @ViewInject(R.id.load_more_list_view_container)
    LoadMoreListViewContainer mLoadMoreListViewContainer;
    // 全部分类
    @ViewInject(R.id.ll_all_classification)
    View ll_all_classification;
    // 全部分类图标
    @ViewInject(R.id.iv_all_classification)
    ImageView iv_all_classification;
    // 全部分类标题
    @ViewInject(R.id.tv_all_classification)
    TextView tv_all_classification;

    // 智能排序
    @ViewInject(R.id.ll_all_auto)
    View ll_all_auto;
    // 智能图标
    @ViewInject(R.id.iv_all_auto)
    ImageView iv_all_auto;
    // 智能标题
    @ViewInject(R.id.tv_all_auto)
    TextView tv_all_auto;

    // 全部状态
    @ViewInject(R.id.ll_all_status)
    View ll_all_status;
    // 全部图标
    @ViewInject(R.id.iv_all_status)
    ImageView iv_all_status;
    // 全部标题
    @ViewInject(R.id.tv_all_status)
    TextView tv_all_status;

    @ViewInject(R.id.ll_task_classification)
    View ll_task_classification;

    private List<AptitudeEntity> aptitudeEntities;
    private Context context;
    private MyProgressDialog dialog;
    private List<TaskEntity> taskEntities;
    private GpsUtils gpsUtils;
    private UserEntity userEntity;
    private List<String> taskClassificationList;
    private TaskAdapter adapter;
    private ManagerMemberEntity memberManager;
    private PopWindowClassification popWindowClassification;
    private static final int CHOOSE_MEMBER = 2001;
    private AMapLocation location;
    private String keyWord;
    private Map<Integer,KeywordEntity> keyMap;
    private String auto;
    private String sort;
    private int taskStatus; // 0 已接任务 1指定任务 2公共任务
    private String status;
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    private Animation myAlphaAnimation;
    private String strtype="";//待接收；竞加中
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        initData();
        initView();
        initLister();
        gpsUtils.startLocation();
        requestManagerData("0");
    }


    private void initView() {

        tv_title.setText("任务列表");
        et_search.setHint("搜索任务名称");
        ll_all_status.setVisibility(taskStatus == 0 ? View.VISIBLE:View.GONE);
        //pb_refresh_location.setVisibility(View.VISIBLE);
        taskEntities = new ArrayList<>();
        adapter = new TaskAdapter(this, taskEntities);
        lv_task.setAdapter(adapter);
        dialog=new MyProgressDialog(this,"请稍等...");
        dialog.show();
        userEntity = getLoginConfig();

        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_task, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                requestManagerData("0");
            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {

                if (taskEntities.size() > 0){
                    String maxid = taskEntities.get(taskEntities.size() - 1).getSortId();
                    requestManagerData("<"+maxid);
                }else {
                    requestManagerData("0");
                }
            }
        });
        popWindowClassification = new PopWindowClassification(context);
        myAlphaAnimation= AnimationUtils.loadAnimation(this, R.anim.refresh_loading);
        myAlphaAnimation.setInterpolator(new LinearInterpolator());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        context = this;
        taskStatus = getIntent().getIntExtra("taskStatus",0);
        userEntity = getLoginConfig();
        gpsUtils = new GpsUtils(context, new GpsUtils.LocationListener() {
            @Override
            public void retBackLocation(AMapLocation loc) {
                //pb_refresh_location.setVisibility(View.GONE);
//                StringBuffer sb = new StringBuffer("当前:");
//                sb.append(loc.getCity()).append(loc.getAddress())
                iv_refresh.setAnimation(null);
                location = loc;
                tv_my_location.setText(loc.getAddress());

            }

            @Override
            public void fail() {
                //pb_refresh_location.setVisibility(View.GONE);
                //iv_refresh.setAnimation(null);
                tv_my_location.setText("当前位置获取失败,点击刷新");
                //requestManagerData("0");
            }
        });
        keyMap = new HashMap<>();
    }

    /**
     * 初始化监听
     */
    private void initLister() {
        ll_location.setOnClickListener(this);
        ll_all_classification.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        ll_all_auto.setOnClickListener(this);
        ll_all_status.setOnClickListener(this);
        popWindowClassification.setOnSelectItemChangeListener(new PopWindowClassification.OnSelectItemChangeListener() {

            @Override
            public void onSelectChange(String paramString, int position, int type) {
                if (type == 3){
                    switch (position){
                        case 0:
                            auto = "Scale";
                            sort = "desc";
                            break;
                        case 1:
                            auto = "Scale";
                            sort = "asc";
                            break;
                        case 2:
                            auto = "Amount";
                            sort = "desc";
                            break;
                        case 3:
                            auto = "Amount";
                            sort = "asc";
                            break;

                    }
                    tv_all_auto.setText(paramString);
                }else {
                    if (type == 1) {
                        strtype=paramString;
                        tv_all_status.setText(paramString);
                        //addKeylist(type,getField(type),"equal","全部".equals(paramString)?"": Utils.statusSwtichToInt(paramString)+"");
                        status = "全部".equals(paramString)?null:String.valueOf(Utils.statusSwtichToInt(paramString));
                    }
                    if (type == 2){
                        tv_all_classification.setText(paramString);
                        addKeylist(type,getField(type),"contains","全部".equals(paramString)?"":paramString);
                    }

                }
                requestManagerData("0");
            }
        });
        popWindowClassification.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showPop(false,popWindowClassification.getType());
            }
        });
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String taskId = ((TaskEntity)adapterView.getAdapter().getItem(i)).getTaskId();
                Intent intent = new Intent(context,TaskDetailsActivity.class);
                intent.putExtra("taskId",taskId);
                intent.putExtra("taskStatus",taskStatus);
                intent.putExtra("isAcceptScore",((TaskEntity)adapterView.getAdapter().getItem(i)).getIsAcceptScore());
                intent.putExtra("isSendScore",((TaskEntity)adapterView.getAdapter().getItem(i)).getIsSendScore());

                if (location != null){
                    intent.putExtra("longitude",location.getLongitude());
                    intent.putExtra("latitude",location.getLatitude());
                }

                startActivity(intent);
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    hideSoftKeyboard();
                    addKeylist(0,"ProjectName","contains",keyWord);
                    requestManagerData("0");
                    return true;
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = et_search.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())){
                    iv_search_clear.setVisibility(View.GONE);
                }else {
                    iv_search_clear.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private String getField(int type) {
        String field = null;
        switch (type){
            case 1:
                field = "Status";
                break;
            case 2:
                field = "TaskTypeName";
                break;
            default:
                field = "";
                break;
        }
        return field;
    }
    /**
     * 发包方任务状态转换
     * @param status 任务状态
     * @return  状态码
     */
    public static String managerMemberStatus(String status){
        String memberStatus = "";
        switch (status){
            case "待提交":
                memberStatus = "-2";
                break;

            case "待审批":
                memberStatus = "0";
                break;
            case "待接受":
                memberStatus = "accepted";
                break;
            case "竞价中":
                memberStatus = "1";
                break;
            case "待验收":
                memberStatus = "2";
                break;
            case "待支付":
                memberStatus = "3";
                break;
            case "待评价":
                memberStatus = "4";
                break;
            case "已完成":
                memberStatus = "5";
                break;
            case "仲裁":
                memberStatus = "6,7";
                break;
            case "已下架":
                memberStatus = "-1";
                break;
            case "全部":
                memberStatus="false";
                break;
        }
        return memberStatus;
    }
    private Map<String ,String>  jiebaofang(String strstatus){
        Map<String,String> map=new HashMap<>();
        map.clear();
        switch (strstatus) {
            case "待接受":
                map.put("contionType", "toReceive");
                map.put("status","1");
                break;

            case "待分配":
                map.put("contionType", "ToBeDistributed");
                break;
            case "待验收":
                map.put("status", "2");
                break;
            case "待评价":
                map.put("contionType", "ToEvaluate");
                break;
            case "待支付":
                map.put("status", "3");
                break;
            case "已完成":
                map.put("contionType", "Done");
                break;
            case "仲裁":
                map.put("status", "6,7");
                break;
            case "终止":
                map.put("status", "10");
                break;
            case "全部":

                break;
        }
            return map;
    }

    /*
    * 经理和总监
    * */
    private String directororstatus(String statusStr){
        switch (statusStr){
            case "待提交":
                return "-2";

            case "待审批":
                return "0";
            case "竞价中":
                return "1";
            case "待接受":
                return "accepted";
            case "待验收":
                return "2";
            case "待评价":
                return "4";
            case "待支付":
                return "3";
            case "已完成":
                return "5";
            case "仲裁":
                return "7";
            case "全部":
                return "false";

        }
        return "false";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_location:
                gpsUtils.startLocation();
                iv_refresh.startAnimation(myAlphaAnimation);

                break;
            case R.id.ll_all_auto:
                popWindowClassification.setType(3);
                showPop(!popWindowClassification.isShowing(),3);
                break;
            case R.id.ll_all_status:
                popWindowClassification.setType(1);
                showPop(!popWindowClassification.isShowing(),1);
                break;

            case R.id.ll_all_classification:
                popWindowClassification.setType(2);
                if (taskClassificationList == null){
                    requestTaskData();
                    return;
                }
                showPop(!popWindowClassification.isShowing(),2);

                break;
            case R.id.iv_search_clear:
                et_search.setText("");
                iv_search_clear.setVisibility(View.GONE);
                break;

        }
    }

    private void showPop(boolean show, int type) {
        switch (type){
            case 1:
                if (show){
                    tv_all_status.setTextColor(Color.BLACK);
                    iv_all_status.setImageResource(R.mipmap.icon_scale_hideall);
                    popWindowClassification.setList(allStatus());
                    popWindowClassification.showAsDropDown(ll_task_classification);
                }else {
                    tv_all_status.setTextColor(Color.parseColor("#85000000"));
                    iv_all_status.setImageResource(R.mipmap.icon_sacle_showall);
                    popWindowClassification.dismiss();
                }
                break;
            case 2:
                if (show){
                    tv_all_classification.setTextColor(Color.BLACK);
                    iv_all_classification.setImageResource(R.mipmap.icon_scale_hideall);
                    popWindowClassification.setList(taskClassificationList);
                    popWindowClassification.showAsDropDown(ll_task_classification);
                }else {
                    tv_all_classification.setTextColor(Color.parseColor("#85000000"));
                    iv_all_classification.setImageResource(R.mipmap.icon_sacle_showall);
                    popWindowClassification.dismiss();
                }
                break;
            case 3:
                if (show){
                    tv_all_auto.setTextColor(Color.BLACK);
                    iv_all_auto.setImageResource(R.mipmap.icon_scale_hideall);
                    popWindowClassification.setList(auto());
                    popWindowClassification.showAsDropDown(ll_task_classification);
                }else {
                    tv_all_auto.setTextColor(Color.parseColor("#85000000"));
                    iv_all_auto.setImageResource(R.mipmap.icon_sacle_showall);
                    popWindowClassification.dismiss();
                }
                break;
        }


    }


    private void requestManagerData(final String sortId){

        dialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());

//        if (location != null){
//            map.put("Longitude",location.getLongitude()+"");
//            map.put("Latitude",location.getLatitude()+"");
//        }
        if (getLoginConfig().getRole().equals("总监")||getLoginConfig().getRole().equals("项目经理"))
        {
            if (!directororstatus(strtype).equals("false")){
                map.put("status",directororstatus(strtype));
            }
        }else if (getLoginConfig().getRole().equals("发包方")){
            if (!managerMemberStatus(strtype).equals("false")){
                map.put("status",managerMemberStatus(strtype));
            }
        }else if (getLoginConfig().getRole().equals("接包方")){
            map.putAll(jiebaofang(strtype));
        }

//        if (strtype.equals("待接受")){
//            map.put("Status","accepted");
//        }else if (strtype.equals("竞价中")){
//            map.put("Status","1");
//        }else {
//            map.put("Status",status);
//        }
        Gson gson = new Gson();
        List<KeywordEntity> keyList = new ArrayList<>();
        for (Integer i : keyMap.keySet()){
            keyList.add(keyMap.get(i));
        }
        String keyJson = gson.toJson(keyList,new TypeToken<List<KeywordEntity>>(){}.getType());
        map.put("filterRules",keyJson);
        map.put("sort",auto);
        map.put("order",sort);
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_GET_TASK_LIST, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        taskEntities.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<TaskEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave = false;
                            }else {
                                ishave = true;
                            }
                            taskEntities.addAll(list);
                        }else {
                            ishave = false;
                            Toast.makeText(TaskActivity.this,"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (taskEntities.size() <= 0) {
                            isnull = true;
                            empty_view.setVisibility(View.VISIBLE);
                            lv_task.setVisibility(View.GONE);
                        }else {
                            isnull = false;
                            empty_view.setVisibility(View.GONE);
                            lv_task.setVisibility(View.VISIBLE);
                        }

                        adapter.setList(taskEntities);
                    }else {

                        Toast.makeText(TaskActivity.this,"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(TaskActivity.this,"获取列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)){
                    taskEntities.clear();
                }
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(TaskActivity.this,"获取列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        });
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private List<TaskEntity> testData(){
        List<TaskEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskEntity model=new TaskEntity();
            model.setAmount("12000");
            model.setBidCount("13");
            model.setProjectName("阿城200个站点勘测任务");
            model.setTaskId("1");
            model.setTypePicUrl("http://img4.duitang.com/uploads/item/201405/21/20140521183956_Ghafh.jpeg");
            list.add(model);
            TaskEntity model2=new TaskEntity();
            model2.setAmount("8000");
            model2.setBidCount("3");
            model2.setProjectName("哈尔滨铁塔图纸绘制");
            model2.setTaskId("2");
            model2.setTypePicUrl("http://res.co188.com/data/drawing/read/39/60308739/1398489193045549.dwg.2000.jpg.midscreen.jpg");
            list.add(model2);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null){
            if (requestCode == CHOOSE_MEMBER){
                // 回调刷新页面
            }
        }
    }

    private void requestTaskData() {
        XUtil.Get(UrlUtils.URL_TASK_TYPE, null, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultJsonAptitude resultJson = gson.fromJson(json,ResultJsonAptitude.class);
                    if (resultJson.getCode() == 200){
                        aptitudeEntities = resultJson.getResult();
                        taskClassificationList = getList(aptitudeEntities);
                        showPop(true,2);

                    }else {
                        Toast.makeText(TaskActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TaskActivity.this,"获取资质信息失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private List<String> getList(List<AptitudeEntity> aptitudeEntities) {
        List<String> list = new ArrayList<>();
        list.add("全部");
        for (AptitudeEntity aptitudeEntity:aptitudeEntities){
            list.add(aptitudeEntity.getTaskTypeName());
        }
        return list;
    }

    private List<String> allStatus(){
        List<String> list = new ArrayList<>();
        list.add("全部");
        if ("总监".equals(userEntity.getRole()) || "项目经理".equals(userEntity.getRole())){
            list.add("待提交");
            list.add("待审批");
            list.add("竞价中");
            list.add("待接受");
            list.add("待验收");
            list.add("待评价");
            list.add("待支付");
            list.add("已完成");
            list.add("仲裁");
        }else if ("发包方".equals(userEntity.getRole())){
            list.add("待提交");
            list.add("待审批");
            list.add("待接受");
            list.add("竞价中");
            list.add("待验收");
            list.add("待评价");
            list.add("待支付");
            list.add("已完成");
            list.add("仲裁");
            list.add("已下架");
        }else if ("接包方".equals(userEntity.getRole())){
            list.add("待接受");
            list.add("待分配");
            list.add("待验收");
            list.add("待评价");
            list.add("待支付");
            list.add("仲裁");
            list.add("终止");
            list.add("已完成");
        }
        return list;
    }

    private List<String> auto(){
        List<String> list = new ArrayList<>();
        list.add("规模由大到小");
        list.add("规模由小到大");
        list.add("总价由高到低");
        list.add("总价由底到高");
        return list;
    }

    private void addKeylist(int type ,String field,String op, String key){
        KeywordEntity keyword = new KeywordEntity();
        keyword.setField(field);
        keyword.setOp(op);
        keyword.setValue(key);
        keyMap.put(type,keyword);
    }

}
