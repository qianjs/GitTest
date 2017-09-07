package com.clinical.tongxin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.clinical.tongxin.adapter.TaskFragmentAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.CityEntity;
import com.clinical.tongxin.entity.ProvinceEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.RowsEntity;
import com.clinical.tongxin.entity.TaskListEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.clinical.tongxin.R.id.empty_view;
import static com.clinical.tongxin.R.id.lv_task;
import static com.clinical.tongxin.R.id.txt_city;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * 找任务
 * Created by linchao on 2017/6/15.
 */

public class TaskFragment extends Fragment {


    @BindView(txt_city)
    TextView txtCity;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_notify)
    ImageView ivNotify;
    @BindView(R.id.tv_project_type)
    TextView tvProjectType;
    @BindView(R.id.iv_project_type)
    ImageView ivProjectType;
    @BindView(R.id.ll_project_type)
    LinearLayout llProjectType;
    @BindView(R.id.tv_task_type)
    TextView tvTaskType;
    @BindView(R.id.iv_task_type)
    ImageView ivTaskType;
    @BindView(R.id.ll_task_type)
    LinearLayout llTaskType;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.ll_sort)
    LinearLayout llSort;
    @BindView(lv_task)
    ListView lvTask;
    @BindView(empty_view)
    LinearLayout emptyView;
    @BindView(R.id.load_more_list_view_container)
    LoadMoreListViewContainer mLoadMoreListViewContainer;
    @BindView(R.id.load_more_list_view_ptr_frame)
    PtrClassicFrameLayout mPtrFrameLayout;
    private PopWindowClassification1 popProject;
    private PopWindowClassification1 popTask;
    private PopWindowClassification popSort;
    private List<AptitudeEntity> projectEntities;
    private List<AptitudeEntity> taskEntities;
    private List<TaskListEntity> taskListEntities;
    private TaskFragmentAdapter adapter;
    private MyProgressDialog mDialog;
    private int position;
    private String taskTypeId= "";
    private String projectTypeId= "";
    private String localCode = "";
    private String longitude= "";
    private String latitude= "";
    private String sortType = "TIME";
    private String sortId = "0";
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    private boolean needRefresh = true; // 定位成功之后需要刷新一次列表
    private boolean isContractor;
    private String cityCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);
        isContractor = "接包方".equals(((MainActivity)getActivity()).getLoginConfig().getRole());
        initView();
        requestTask();
        return view;
    }

    @Override
    public void onResume() {
        requestTask();
        super.onResume();
    }

    private void initView() {
        title.setText("任务列表");
        mDialog = new MyProgressDialog(getActivity(), "请稍后...");
        taskListEntities = new ArrayList<>();
        adapter = new TaskFragmentAdapter(getActivity(),taskListEntities,isContractor);
        lvTask.setAdapter(adapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskListEntity entity = (TaskListEntity)adapter.getItem(i);
                if (isContractor){

                    if("1".equals(entity.getType())){
                        Intent intent = new Intent(getActivity(),TaskDetailsLeaseActivity.class);
                        intent.putExtra("taskId",(((TaskListEntity)adapter.getItem(i)).getTaskId()));
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(),TaskDetailsMarkerActivity.class);
                        intent.putExtra("taskId",(((TaskListEntity)adapter.getItem(i)).getTaskId()));
                        intent.putExtra("isBidding",true);
                        startActivity(intent);
                    }

                }else {
                    if ("1".equals(entity.getType())){
                        Intent intent = new Intent(getActivity(),TaskDetailsLeasePublisherActivity.class);
                        intent.putExtra("taskId",(((TaskListEntity)adapter.getItem(i)).getTaskId()));
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(),TaskDetailsPublisherActivity.class);
                        intent.putExtra("taskId",(((TaskListEntity)adapter.getItem(i)).getTaskId()));
                        startActivity(intent);
                    }

                }

            }
        });
        if (isContractor){
            lvTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("是否忽略此条消息");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ignoreTask((((TaskListEntity)adapter.getItem(position)).getTaskId()));
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }

        popProject = new PopWindowClassification1(getActivity(), 0);
        popProject.setOnSelectItemChangeListener(new PopWindowClassification1.OnSelectItemChangeListener() {
            @Override
            public void onSelectChange(AptitudeEntity aptitudeEntity, int position) {
                projectTypeId = aptitudeEntity.getProjectTypeId();
                tvProjectType.setText(aptitudeEntity.getProjectTypeName());
                taskTypeId = "";
                tvTaskType.setText("任务类型");
                sortId = "0";
                requestTask();
            }
        });
        popProject.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showPop(false,position);
            }
        });
        popTask = new PopWindowClassification1(getActivity(), 1);
        popTask.setOnSelectItemChangeListener(new PopWindowClassification1.OnSelectItemChangeListener() {
            @Override
            public void onSelectChange(AptitudeEntity aptitudeEntity, int position) {
                projectTypeId = aptitudeEntity.getProjectTypeId();
                taskTypeId = aptitudeEntity.getTaskTypeId();
                tvTaskType.setText(aptitudeEntity.getTaskTypeName());
                sortId = "0";
                requestTask();
            }
        });
        popTask.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showPop(false,position);
            }
        });
        popSort = new PopWindowClassification(getActivity());
        popSort.setList(getSortList());
        popSort.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showPop(false,position);
            }
        });
        popSort.setOnSelectItemChangeListener(new PopWindowClassification.OnSelectItemChangeListener() {
            @Override
            public void onSelectChange(String paramString, int position, int type) {
                switch (position){
                    case 0:
                        sortType = "TIME";
                        break;
                    case 1:
                        sortType = "DISTANCE";
                        break;
                    case 2:
                        sortType = "AMOUNT";
                        break;
                }
                if (position ==2 &&(TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude))){
                    ToastUtils.showToast(getActivity(),"获取当前位置失败，请稍后再试");
                    return;
                }
                tvSort.setText(paramString);
                sortId = "0";
                requestTask();
            }
        });
        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvTask, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                requestTask();
            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {

                if (taskListEntities.size() > 0){
                    sortId="<"+ taskListEntities.get(taskListEntities.size() - 1).getSortId();
                }else {
                   sortId = "0";
                }
                requestTask();
            }
        });

    }

    private List<String> getSortList() {
        List<String> list = new ArrayList<>();
        list.add("最新发布");
        list.add("离我最近");
        list.add("薪酬最高");
        return list;
    }

    @OnClick({txt_city, R.id.iv_notify, R.id.ll_project_type, R.id.ll_task_type, R.id.ll_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case txt_city:
                Intent intent=new Intent(getActivity(),CityPickerActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.iv_notify:
                break;
            case R.id.ll_project_type:
                position = 1;
                if (projectEntities == null) {
                    requestProjectData();
                } else {
                    popProject.setList(projectEntities);
                    showPop(!popProject.isShowing(),position);
                }

                break;
            case R.id.ll_task_type:
                if (TextUtils.isEmpty(projectTypeId)){
                    ToastUtils.showToast(getActivity(),"请先选择项目类型");
                    return;
                }
                position = 2;
                if (taskEntities == null) {
                    requestTaskData();
                } else {
                    popTask.setList(getTaskEntities());
                    showPop(!popTask.isShowing(),position);
                }
                break;
            case R.id.ll_sort:
                position = 3;
                showPop(!popSort.isShowing(),position);
                break;
        }
    }

    private void requestProjectData() {

        XUtil.Get(UrlUtils.URL_PROTECT_TYPE, null, new MyCallBack<String>() {

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
                try {
                    Gson gson = new Gson();
                    ResultEntity<RowsEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<RowsEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        projectEntities = resultEntity.getResult().getRows();
                        AptitudeEntity aptitudeEntity = new AptitudeEntity();
                        aptitudeEntity.setProjectTypeName("全部项目");
                        aptitudeEntity.setProjectTypeId("");
                        projectEntities.add(0,aptitudeEntity);
                        popProject.setList(projectEntities);
                        showPop(true,position);
                    } else {
                        Toast.makeText(getActivity(), "获取项目类型失败", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "获取项目类型失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });


    }


    private void requestTaskData() {
        XUtil.Get(UrlUtils.URL_TASK_TYPE, null, new MyCallBack<String>() {

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
                try {
                    Gson gson = new Gson();
                    ResultEntity<RowsEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<RowsEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        if (taskEntities != null){
                            taskEntities.clear();
                        }else {
                            taskEntities =new ArrayList<AptitudeEntity>();
                        }
                        taskEntities.addAll(resultEntity.getResult().getRows());
                        popTask.setList(getTaskEntities());
                        showPop(true,position);
                    } else {
                        Toast.makeText(getActivity(), "获取任务类型失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "获取任务类型失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();

                }

                mDialog.dismiss();
            }
        });
    }

    private List<AptitudeEntity> getTaskEntities() {
        List<AptitudeEntity> list = new ArrayList<>();
        AptitudeEntity aptitudeEntity = new AptitudeEntity();
        aptitudeEntity.setProjectTypeId(projectTypeId);
        aptitudeEntity.setTaskTypeName("全部任务");
        aptitudeEntity.setTaskTypeId("");
        list.add(0,aptitudeEntity);
        for (AptitudeEntity entity:taskEntities){
            if (entity.getProjectTypeId().equals(projectTypeId)){
                list.add(entity);
            }
        }
        return list;
    }

    private void showPop(boolean show, int position) {
        switch (position) {
            case 1:
                if (show) {
                    tvProjectType.setTextColor(Color.BLACK);
                    ivProjectType.setImageResource(R.mipmap.icon_scale_hideall);
                    popProject.showAsDropDown(llProjectType);
                } else {
                    tvProjectType.setTextColor(Color.parseColor("#85000000"));
                    ivProjectType.setImageResource(R.mipmap.icon_sacle_showall);
                    popProject.dismiss();
                }
                break;
            case 2:
                if (show) {
                    tvTaskType.setTextColor(Color.BLACK);
                    ivTaskType.setImageResource(R.mipmap.icon_scale_hideall);
                    popTask.showAsDropDown(llTaskType);
                } else {
                    tvTaskType.setTextColor(Color.parseColor("#85000000"));
                    ivTaskType.setImageResource(R.mipmap.icon_sacle_showall);
                    popTask.dismiss();
                }
                break;
            case 3:
                if (show) {
                    tvSort.setTextColor(Color.BLACK);
                    ivSort.setImageResource(R.mipmap.icon_scale_hideall);
                    popSort.showAsDropDown(llSort);
                } else {
                    tvSort.setTextColor(Color.parseColor("#85000000"));
                    ivSort.setImageResource(R.mipmap.icon_sacle_showall);
                    popSort.dismiss();
                }
                break;
        }

    }

    private void requestTask(){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        map.put("taskTypeId", taskTypeId);
        map.put("projectTypeId", projectTypeId);
        if (isContractor){
            map.put("cityCode", cityCode);
            map.put("localCode",localCode);
            map.put("longitude", longitude);
            map.put("latitude", latitude);
        }

        map.put("sortType", sortType); //排序方式 AMOUNT 总价最高, DISTANCE 距离最近， TIME 发布最新
        map.put("sortId", sortId);

        XUtil.Post(isContractor?UrlUtils.URL_queryReceiverSubscribeTaskList:UrlUtils.URL_queryCreateTaskList, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    if (("0").equals(sortId)){
                        taskListEntities.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskListEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskListEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        List<TaskListEntity> list = resultEntity.getResult();
                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave = false;
                            }else {
                                ishave = true;
                            }
                            taskListEntities.addAll(list);
                        }else {
                            ishave = false;
                            //Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (taskListEntities.size() <= 0) {
                            isnull = true;
                            emptyView.setVisibility(View.VISIBLE);
                            lvTask.setVisibility(View.GONE);
                        }else {
                            isnull = false;
                            emptyView.setVisibility(View.GONE);
                            lvTask.setVisibility(View.VISIBLE);
                        }

                        adapter.setList(taskListEntities);
                    }else {

                        Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                        adapter.setList(taskListEntities);
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }

                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)){
                    taskListEntities.clear();
                }
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

        });
    }

    public void setLocation(AMapLocation location){
        if (location != null && needRefresh){
            txtCity.setText(location.getCity().replace("市",""));
            localCode = location.getAdCode().substring(0,4);
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            requestTask();
            needRefresh =false;
        }
    }

    public List<TaskListEntity> getTestData(){
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"msg\":\"查询成功\",\n" +
                "    \"result\": [{\n" +
                "      \"taskId\": \"2\",\n" +
                "      \"taskName\": \"阿城200个勘测任务\",\n" +
                "      \"cityName\":\"哈尔滨\",\n" +
                "      \"totalAmount\": \"2000\",\n" +
                " \"projectType\": \"传输设备\",\n" +
                " \"time\": \"2017年6月15日16:44:34\",\n" +
                "      \"typePicUrl\": \"http://www.dadfadf.jpg\" ,\n" +
                "      \"sortId\": \"50\"\n" +
                "    },{\n" +
                "      \"taskId\": \"3\",\n" +
                "      \"taskName\": \"呼兰200个勘测任务\",\n" +
                "      \"cityName\":\"哈尔滨\",\n" +
                "      \"totalAmount\": \"2000\",\n" +
                " \"projectType\": \"光缆线路\",\n" +
                " \"time\": \"2017年6月15日16:44:34\",\n" +
                "      \"typePicUrl\": \"http://www.dadfadf.jpg\" ,\n" +
                "      \"sortId\": \"49\"\n" +
                "    },{\n" +
                "      \"taskId\": \"3\",\n" +
                "      \"taskName\": \"呼兰200个勘测任务\",\n" +
                "      \"cityName\":\"哈尔滨\",\n" +
                "      \"totalAmount\": \"2000\",\n" +
                " \"projectType\": \"光缆线路\",\n" +
                " \"time\": \"2017年6月15日16:44:34\",\n" +
                "      \"typePicUrl\": \"http://www.dadfadf.jpg\" ,\n" +
                "      \"sortId\": \"49\"\n" +
                "    }]\n" +
                "  }";
        List<TaskListEntity> taskListEntities = new Gson().fromJson(json,new TypeToken<List<TaskListEntity>>(){}.getType());
        return taskListEntities;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(data!=null)
            {
                String city=data.getStringExtra("picked_city");
                txtCity.setText(city);
                cityCode = getCityCode(city);
                sortId = "0";
                requestTask();
            }
        }
    }

    private String getCityCode(String city) {
        String myjson= Utils.getAssetsFileText("city.json");
        List<ProvinceEntity> provinceEntities = new Gson().fromJson(myjson, new TypeToken<List<ProvinceEntity>>() {
        }.getType());
        for (ProvinceEntity provinceEntity:provinceEntities){
            for (CityEntity cityEntity:provinceEntity.getCitys()){
                if (cityEntity.getCityName().contains(city)){
                    return cityEntity.getCityCode().substring(0,4);
                }
            }
        }
        return "";
    }

    private void  ignoreTask(String taskId){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        map.put("TaskId", taskId);

        XUtil.Post(UrlUtils.URL_taskNeglect, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<Object>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        Toast.makeText(getActivity(),"操作成功",Toast.LENGTH_SHORT).show();
                        requestTask();
                    }else {
                        Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }finally {
                    mDialog.dismiss();
                }


            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

        });
    }
}
