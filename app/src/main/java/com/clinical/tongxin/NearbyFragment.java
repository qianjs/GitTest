package com.clinical.tongxin;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.baidu.mapapi.map.Text;
import com.clinical.tongxin.adapter.TaskAdapter;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.GpsUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
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

import static com.clinical.tongxin.R.id.empty_view;
import static com.clinical.tongxin.R.id.iv_search_clear;
import static com.clinical.tongxin.R.id.lv_task;
import static com.clinical.tongxin.R.id.pb_refresh_location;
import static com.clinical.tongxin.R.id.tv_location;
import static com.clinical.tongxin.R.id.tv_my_location;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by apple on 2016/11/22.
 */

public class NearbyFragment extends Fragment implements View.OnClickListener{

    // 任务列表
    private ListView lv_task;
    // 搜索框
    private EditText et_search;
    // 定位
    private View ll_location;
    // 定位位置
    private TextView tv_address;
    // 已接任务
    private TextView tv_have_task;
    // 指定任务
    private TextView tv_assign_task;
    // 公共任务
    private TextView tv_public_task;
    private LinearLayout ll_cursor;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ProgressBar pb_refresh_location;
    private View empty_view;
    private ImageView cursor;
    private List<TaskEntity> taskEntities;
    private TaskAdapter adapter;
    private MyProgressDialog dialog;
    private UserEntity userEntity;
    private Context context;
    private PopWindowClassification popWindowClassification;
    private GpsUtils gpsUtils;
    private String keyWord;
    private int taskStatus; // 0 已接任务 1指定任务 2公共任务
    private Map<Integer,KeywordEntity> keyMap;
    private AMapLocation location;
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    private int width;
    private int lastPosition = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nearbyfragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        initListener();
        //findNumber();
        addCursor();
        gpsUtils.startLocation();
    }

    private void addCursor() {
        cursor = new ImageView(context);
        Gallery.LayoutParams params = new Gallery.LayoutParams(width/3, Gallery.LayoutParams.MATCH_PARENT);
        cursor.setLayoutParams(params);
        cursor.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_corner));
        ll_cursor.addView(cursor);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        taskStatus = 0;
        context = getActivity();
        userEntity = ((MainActivity)getActivity()).getLoginConfig();
        gpsUtils = new GpsUtils(context, new GpsUtils.LocationListener() {
            @Override
            public void retBackLocation(AMapLocation loc) {
                pb_refresh_location.setVisibility(View.GONE);
                location = loc;
                tv_address.setText(location.getStreet());
                requestManagerData("0");
            }

            @Override
            public void fail() {
                pb_refresh_location.setVisibility(View.GONE);
                tv_address.setText("获取失败，请刷新位置");
                //requestManagerData("0");
            }
        });
        keyMap = new HashMap<>();
        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    }

    private void initListener() {
        ll_location.setOnClickListener(this);
        tv_have_task.setOnClickListener(this);
        tv_assign_task.setOnClickListener(this);
        tv_public_task.setOnClickListener(this);

        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String taskId = ((TaskEntity)adapterView.getAdapter().getItem(i)).getTaskId();
                Intent intent = new Intent(context,TaskDetailsActivity.class);
                intent.putExtra("taskId",taskId);
                intent.putExtra("taskStatus",taskStatus);
                intent.putExtra("isPublicTask",true);
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
            }
        });
    }

    private void initView() {
        lv_task = (ListView) getActivity().findViewById(R.id.lv_task);
        et_search = (EditText) getActivity().findViewById(R.id.et_search);
        ll_location = getActivity().findViewById(R.id.ll_location);
        tv_address = (TextView) getActivity().findViewById(R.id.tv_address);
        tv_have_task = (TextView) getActivity().findViewById(R.id.tv_have_task);
        tv_assign_task = (TextView) getActivity().findViewById(R.id.tv_assign_task);
        tv_public_task = (TextView) getActivity().findViewById(R.id.tv_public_task);
        pb_refresh_location = (ProgressBar) getActivity().findViewById(R.id.pb_refresh_location);
        ll_cursor = (LinearLayout) getActivity().findViewById(R.id.ll_cursor);
        pb_refresh_location.setVisibility(View.VISIBLE);
        taskEntities = new ArrayList<>();
        adapter = new TaskAdapter(getActivity(), taskEntities);
        lv_task.setAdapter(adapter);
        dialog=new MyProgressDialog(getActivity(),"请稍等...");
        //dialog.show();
        empty_view = getActivity().findViewById(R.id.empty_view1);
        mPtrFrameLayout = (PtrFrameLayout) getActivity().findViewById(R.id.load_more_list_view_ptr_frame);

        // .设置下拉刷新组件和事件监听
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
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) getActivity().findViewById(R.id.load_more_list_view_container1);
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


    }

    private void requestManagerData(final String sortId) {
        dialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("Status","public");
        if (location != null){
            map.put("Longitude",location.getLongitude()+"");
            map.put("Latitude",location.getLatitude()+"");
        }
        Gson gson = new Gson();
        List<KeywordEntity> keyList = new ArrayList<>();
        for (Integer i : keyMap.keySet()){
            keyList.add(keyMap.get(i));
        }
        String keyJson = gson.toJson(keyList,new TypeToken<List<KeywordEntity>>(){}.getType());
        map.put("filterRules",keyJson);
//        map.put("sort","");
//        map.put("order","");
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
                            Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_location:
                // 点击点位
//                startActivity(new Intent(context,MarkerActivity.class).putExtra("TaskEntity", (Serializable) taskEntities));

                pb_refresh_location.setVisibility(View.VISIBLE);
                gpsUtils.startLocation();
                break;
            case R.id.tv_have_task:
                // 已接任务
                taskStatus = 0;
                startAnimation(lastPosition,0);
                requestManagerData("0");
                lastPosition = 0;
                break;
            case R.id.tv_assign_task:
                // 指定任务
                taskStatus = 1;
                startAnimation(lastPosition,1);
                requestManagerData("0");
                lastPosition = 1;
                break;
            case R.id.tv_public_task:
                // 公共任务
                taskStatus = 2;
                startAnimation(lastPosition,2);
                requestManagerData("0");
                lastPosition = 2;
                break;

        }
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void addKeylist(int type ,String field,String op, String key){
        KeywordEntity keyword = new KeywordEntity();
        keyword.setField(field);
        keyword.setOp(op);
        keyword.setValue(key);
        keyMap.put(type,keyword);
    }

    /**
     * 初始化动画
     */
    private void startAnimation(int start,int end) {
        Animation animation = null;
        if (start== 0){
            if (end == 1) animation = new TranslateAnimation(0, width/3, 0, 0);
            if (end == 2) animation = new TranslateAnimation(0, width*2/3, 0, 0);
        }else if (start == 1){
            if (end == 0) animation = new TranslateAnimation(width/3, 0, 0, 0);
            if (end == 2) animation = new TranslateAnimation(width/3, width*2/3, 0, 0);
        }else if (start == 2){
            if (end == 0) animation = new TranslateAnimation(width*2/3, 0, 0, 0);
            if (end == 1) animation = new TranslateAnimation(width*2/3, width/3, 0, 0);
        }
        if (animation != null){
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }
//    private void InitImageView() {
//
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        int offset = (screenW / 3 - Utils.dip2px(context,100)) / 2;// 计算偏移量
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        cursor.setImageMatrix(matrix);// 设置动画初始位置
//    }
}
