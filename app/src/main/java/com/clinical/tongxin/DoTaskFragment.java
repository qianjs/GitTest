package com.clinical.tongxin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.view.BasePickerView;
import com.clinical.tongxin.adapter.ArbitrationQuestionAdapter;
import com.clinical.tongxin.adapter.DoServiceAdapter;
import com.clinical.tongxin.adapter.DoTaskAdapter;
import com.clinical.tongxin.entity.DoTaskDetailsPublisherEntity;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.SignInEntity;
import com.clinical.tongxin.entity.TaskCountEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.TaskServiceEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.mydatepicker.DPCManager;
import com.clinical.tongxin.myview.mydatepicker.DPDecor;
import com.clinical.tongxin.myview.mydatepicker.DPMode;
import com.clinical.tongxin.myview.mydatepicker.DatePicker;
import com.clinical.tongxin.myview.mydatepicker.DatePicker2;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import static com.clinical.tongxin.R.id.lv_task;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * 做任务
 * Created by linchao on 2017/7/4.
 */

public class DoTaskFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_notify)
    ImageView ivNotify;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(lv_task)
    ListView lvTask;
    @BindView(R.id.load_more_list_view_container)
    LoadMoreListViewContainer mLoadMoreListViewContainer;
    @BindView(R.id.load_more_list_view_ptr_frame)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.txt_project_task)
    TextView txtProjectTask;
    @BindView(R.id.txt_service_task)
    TextView txtServiceTask;
    @BindView(R.id.ll_service_tab)
    LinearLayout llServiceTab;
    @BindView(R.id.lv_task_service)
    ListView lvTaskService;
    @BindView(R.id.load_more_list_view_container_service)
    LoadMoreListViewContainer loadMoreListViewContainerService;
    @BindView(R.id.load_more_list_view_ptr_frame_service)
    PtrClassicFrameLayout mPtrFrameLayoutService;
    @BindView(R.id.ll_service_task)
    LinearLayout llServiceTask;
    @BindView(R.id.ll_project_task)
    LinearLayout ll_project_task;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @BindView(R.id.empty_view1)
    LinearLayout emptyView1;

    //游标
    private ImageView cursor;
    private int width;
    private UserEntity userEntity;
    private List<String> statusList;

    private List<TextView> listTab;
    private List<TextView> listCount;
    private List<View> lines;
    private int position;
    private int position1;
    private MyProgressDialog dialog;
    private String strtype = "";//待接收；竞加中
    private Map<Integer, KeywordEntity> keyMap;
    private List<TaskEntity> taskEntities;
    private List<SignInEntity> signInEntities;
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    // 是否还有数据
    private boolean ishave1 = true;
    // 数据是否为空
    private boolean isnull1 = false;
    private DoTaskAdapter adapter;

    private TaskCountEntity taskCountEntity;
    private int type = 0;//0:工程任务；1：服务任务
    private View view;
    //服务任务
    private List<TextView> listTab1;
    private List<String> statusList1;
    private List<TaskServiceEntity> taskEntitiesS;
    private DoServiceAdapter adapters;
    private String statusStr; // 服务任务状态 0进行中  1已完成 2仲裁 3终止
    private AlertDialog arbitrationDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        view = inflater.inflate(R.layout.fragment_do_task, null);
        ButterKnife.bind(this, view);
        initView();
        initViewservice();
        addTab();
//        addTab1();
        //默认的工程任务
        txtProjectTask.setTextColor(Color.parseColor("#f74b4d"));//工程任务字体选中
        txtServiceTask.setTextColor(Color.parseColor("#000000"));//服务任务字体未选中
        taskEntities.clear();
        ll_project_task.setVisibility(View.VISIBLE);
        llServiceTask.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        if (type == 0) {
            //工程任务
            requestManagerData("0", true);
        } else if (type == 1) {
            //服务任务
            getServicedata("0");
        }

        super.onResume();
    }

    /**
     * @param type 根据type刷新页面数据
     */
    private void typeTask(int type) {
        if (type == 0) {
            taskEntities.clear();
            ll_project_task.setVisibility(View.VISIBLE);
            llServiceTask.setVisibility(View.GONE);
            addTab();
            requestManagerData("0", true);

        } else if (type == 1) {
            taskEntitiesS.clear();
            ll_project_task.setVisibility(View.GONE);
            llServiceTask.setVisibility(View.VISIBLE);
            addTab1();
            getServicedata("0");
        }
    }

    //工程任务初始化
    private void initView() {
        title.setText("做任务");
        dialog = new MyProgressDialog(getActivity(), "请稍等...");
        listTab = new ArrayList<>();
        lines = new ArrayList<>();
        listCount = new ArrayList<>();
        taskEntities = new ArrayList<>();
        userEntity = ((MainActivity) getActivity()).getLoginConfig();
        statusList = allStatus();
        statusList1 = getAllStatus();
        adapter = new DoTaskAdapter(getActivity(), taskEntities);
        lvTask.setAdapter(adapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!userEntity.getRole().equals("接包方") && "1".equals(((TaskEntity) adapterView.getAdapter().getItem(i)).getStatusName())) {
                    String taskId = ((TaskEntity) adapterView.getAdapter().getItem(i)).getTaskId();
                    String status = ((TaskEntity) adapterView.getAdapter().getItem(i)).getStatusName();
                    Intent intent = new Intent(getActivity(), TaskDetailsPublisherActivity.class);
                    intent.putExtra("taskId", taskId);
                    intent.putExtra("status", status);
                    startActivity(intent);
                } else {
                    String taskId = ((TaskEntity) adapterView.getAdapter().getItem(i)).getTaskId();
                    String status = ((TaskEntity) adapterView.getAdapter().getItem(i)).getStatusName();
                    Intent intent = new Intent(getActivity(), TaskDetailsMarkerActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                }


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

                requestManagerData("0", true);


            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {

                if (taskEntities.size() > 0) {
                    String maxid = taskEntities.get(taskEntities.size() - 1).getSortId();
                    requestManagerData("<" + maxid, false);
                } else {
                    requestManagerData("0", false);
                }


            }
        });
    }

    //服务任务初始化
    private void initViewservice() {
        listTab1 = new ArrayList<>();
        taskEntitiesS = new ArrayList<>();
        statusList1 = getAllStatus();
        adapters = new DoServiceAdapter(getActivity(), taskEntitiesS);
        adapters.setContractor(userEntity.getRole().equals("接包方"));
        adapters.setOnClickLeaseLister(new DoServiceAdapter.onClickLeaseLister() {
            @Override
            public void onClickBtnArbitration(TaskServiceEntity taskServiceEntity) {
                // 仲裁
                requestArbitrationList(taskServiceEntity.getTaskId(),taskServiceEntity.getPublisherId());
            }

            @Override
            public void onClickBtnSignIn(TaskServiceEntity taskServiceEntity) {
                getCalender(taskServiceEntity.getTaskId());
            }

            @Override
            public void onClickBtnEvaluate(TaskServiceEntity taskServiceEntity) {
                // 评价
                Intent intent = new Intent(getActivity(),EvaluateLeaseActivity.class);
                intent.putExtra("taskId",taskServiceEntity.getTaskId());
                intent.putExtra("signCustomerId",taskServiceEntity.getPublisherId());
                intent.putExtra("name",taskServiceEntity.getPublisher());
                startActivityForResult(intent, 1001);
            }

            @Override
            public void onClickBtnArbitrationInfo(TaskServiceEntity taskServiceEntity) {
                // 仲裁信息
                Intent intent = new Intent(getActivity(),ArbitrationInfoLeaseActivity.class);
                intent.putExtra("taskId",taskServiceEntity.getTaskId());
                intent.putExtra("signCustomerId",taskServiceEntity.getPublisherId());
                startActivity(intent);
            }

        });
        lvTaskService.setAdapter(adapters);
        lvTaskService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskServiceEntity taskServiceEntity = (TaskServiceEntity)adapters.getItem(i);
                if (userEntity.getRole().equals("接包方")){

                }else {
                    Intent intent = new Intent(getActivity(),DoTaskDetailsLeasePublisherActivity.class);
                    intent.putExtra("taskId",taskServiceEntity.getTaskId());
                    startActivity(intent);
                }
            }
        });
        // .设置下拉刷新组件和事件监听
        mPtrFrameLayoutService.setLoadingMinTime(1000);
        mPtrFrameLayoutService.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvTaskService, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                getServicedata("0");

            }

        });
        // 4.加载更多的组件
        loadMoreListViewContainerService.setAutoLoadMore(true);// 设置是否自动加载更多
        loadMoreListViewContainerService.useDefaultHeader();
        // 5.添加加载更多的事件监听
        loadMoreListViewContainerService.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                if (taskEntitiesS.size() > 0) {
                    String maxid = taskEntitiesS.get(taskEntitiesS.size() - 1).getSortId();
                    getServicedata("<" + maxid);
                } else {
                    getServicedata("0");
                }
            }
        });
    }

    //加载工程任务的tab按钮
    private void addTab() {
        llTab.removeAllViews();
        listTab.clear();
        lines.clear();
        listCount.clear();
        for (int i = 0; i < statusList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width / 4, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_tab, null);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            listCount.add(tv_num);
            final TextView tabName = (TextView) view.findViewById(R.id.tv_tab);
            tabName.setText(statusList.get(i));
            listTab.add(tabName);
            final View line = view.findViewById(R.id.line);
            lines.add(line);
            if (i == 0) {
                strtype = statusList.get(i);
                tabName.setTextColor(getResources().getColor(R.color.holo_red_light));
                line.setVisibility(View.VISIBLE);
            } else {
                tabName.setTextColor(getResources().getColor(R.color.txt_gray));
                line.setVisibility(View.GONE);
            }

            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = finalI;
                    strtype = statusList.get(position);
                    for (int j = 0; j < statusList.size(); j++) {
                        if (position == j) {
                            listTab.get(j).setTextColor(getResources().getColor(R.color.holo_red_light));
                            lines.get(j).setVisibility(View.VISIBLE);
                        } else {
                            listTab.get(j).setTextColor(getResources().getColor(R.color.txt_gray));
                            lines.get(j).setVisibility(View.INVISIBLE);
                        }
                    }
                    if (type == 0) {
                        requestManagerData("0", false);
                    } else if (type == 1) {

                    }

                }
            });
            view.setLayoutParams(layoutParams);
            linearLayout.addView(view);
            llTab.addView(linearLayout);
            llTab.addView(getLine());
        }
    }

    //加载服务任务的tab按钮
    private void addTab1() {
        listTab1.clear();
        llServiceTab.removeAllViews();
        lines.clear();
        for (int i = 0; i < statusList1.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width / 4, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_tab, null);
            TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_num.setVisibility(View.GONE);
//            listCount.add(tv_num);
            final TextView tabName = (TextView) view.findViewById(R.id.tv_tab);
            tabName.setText(statusList1.get(i));
            listTab1.add(tabName);
            final View line = view.findViewById(R.id.line);
            lines.add(line);
            if (i == 0) {
//                strtype = statusList1.get(i);
                tabName.setTextColor(getResources().getColor(R.color.holo_red_light));
                line.setVisibility(View.VISIBLE);
            } else {
                tabName.setTextColor(getResources().getColor(R.color.txt_gray));
                line.setVisibility(View.GONE);
            }

            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position1 = finalI;
                    //strtype = statusList1.get(position1);
                    for (int j = 0; j < statusList1.size(); j++) {
                        if (position1 == j) {
                            listTab1.get(j).setTextColor(getResources().getColor(R.color.holo_red_light));
                            lines.get(j).setVisibility(View.VISIBLE);
                        } else {
                            listTab1.get(j).setTextColor(getResources().getColor(R.color.txt_gray));
                            lines.get(j).setVisibility(View.INVISIBLE);
                        }
                    }
                    //调用服务接口

                    getServicedata("0");

                }
            });
            view.setLayoutParams(layoutParams);
            linearLayout.addView(view);
            llServiceTab.addView(linearLayout);
            llServiceTab.addView(getLine());
        }
    }

//    private void addCursor() {
//        cursor = new ImageView(getActivity());
//        Gallery.LayoutParams params = new Gallery.LayoutParams(width / 4, Gallery.LayoutParams.MATCH_PARENT);
//        cursor.setLayoutParams(params);
//        cursor.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.btn_corner_red));
//        llCursor.addView(cursor);
////        txt_total.setTextColor(getResources().getColor(R.color.holo_red_light));
//    }

    private View getTabView(String name, boolean showline) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width / 4, Utils.dip2px(getActivity(), 43)));
        linearLayout.setGravity(Gravity.CENTER);
        TextView textView = new TextView(getActivity());
        textView.setText(name);
        textView.setTextSize(14);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 42));
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);
        linearLayout.addView(textView);
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 1)));
        imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.btn_corner_red));
        imageView.setVisibility(showline ? View.VISIBLE : View.GONE);
        linearLayout.addView(imageView);
        return linearLayout;
    }

    private View getLine() {
        View view = new View(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(1, Utils.dip2px(getActivity(), 24)));
        view.setBackgroundColor(getResources().getColor(R.color.line));
        return view;
    }

    //获取服务任务的数据
    private void getServicedata(final String sortId) {
        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("status", position1+"");
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_LEASE_DO_TASK_LIST, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)) {
                        taskEntitiesS.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskServiceEntity>> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<List<TaskServiceEntity>>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {

                        List<TaskServiceEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0) {
                            if (list.size() < Utils.PAGE_SIZE) {
                                ishave1 = false;
                            } else {
                                ishave1 = true;
                            }
                            taskEntitiesS.addAll(list);
                        } else {
                            ishave1 = false;
                            //Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (taskEntitiesS.size() <= 0) {
                            isnull1 = true;
                            emptyView1.setVisibility(View.VISIBLE);
                            lvTaskService.setVisibility(View.GONE);
                        } else {
                            isnull1 = false;
                            emptyView1.setVisibility(View.GONE);
                            lvTaskService.setVisibility(View.VISIBLE);
                        }
                        adapters.setStatus(position1+"");
                        adapters.setList(taskEntitiesS);
                    } else {

                        Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayoutService.refreshComplete();
                    loadMoreListViewContainerService.loadMoreFinish(isnull1, ishave1);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
                //if (refreshCount) requestTaskCount();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)) {
                    taskEntitiesS.clear();
                }
                mPtrFrameLayoutService.refreshComplete();
                loadMoreListViewContainerService.loadMoreFinish(isnull, ishave);
                Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        });
    }


    private void requestManagerData(final String sortId, final boolean refreshCount) {

        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());

//        if (location != null){
//            map.put("Longitude",location.getLongitude()+"");
//            map.put("Latitude",location.getLatitude()+"");
//        }
        if (userEntity.getRole().equals("总监") || userEntity.getRole().equals("项目经理")) {
            if (!directororstatus(strtype).equals("false")) {
                map.put("status", directororstatus(strtype));
            }
        } else if (userEntity.getRole().equals("发包方")) {
            if (!managerMemberStatus(strtype).equals("false")) {
                map.put("status", managerMemberStatus(strtype));
            }
        } else if (userEntity.getRole().equals("接包方")) {
            map.putAll(jiebaofang(strtype));
        }

//        Gson gson = new Gson();
//        List<KeywordEntity> keyList = new ArrayList<>();
//        for (Integer i : keyMap.keySet()) {
//            keyList.add(keyMap.get(i));
//        }
//        String keyJson = gson.toJson(keyList, new TypeToken<List<KeywordEntity>>() {
//        }.getType());
//        map.put("filterRules", keyJson);
//        map.put("sort", auto);
//        map.put("order", sort);
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_GET_TASK_LIST, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)) {
                        taskEntities.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<List<TaskEntity>>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {

                        List<TaskEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0) {
                            if (list.size() < Utils.PAGE_SIZE) {
                                ishave = false;
                            } else {
                                ishave = true;
                            }
                            taskEntities.addAll(list);
                        } else {
                            ishave = false;
                            //Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (taskEntities.size() <= 0) {
                            isnull = true;
                            emptyView.setVisibility(View.VISIBLE);
                            lvTask.setVisibility(View.GONE);
                        } else {
                            isnull = false;
                            emptyView.setVisibility(View.GONE);
                            lvTask.setVisibility(View.VISIBLE);
                        }

                        adapter.setList(taskEntities);
                    } else {

                        Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
                if (refreshCount) requestTaskCount();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)) {
                    taskEntities.clear();
                }
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(getActivity(), "获取列表失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        });
    }


    private void requestTaskCount() {

        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());

        XUtil.Post(UrlUtils.URL_ShowListTaskCount, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<TaskCountEntity> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<TaskCountEntity>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        taskCountEntity = resultEntity.getResult();
                        setTaskCount(taskCountEntity);
                    } else {
                        Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取任务数失败", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Toast.makeText(getActivity(), "获取任务数失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        });
    }

    private void setTaskCount(TaskCountEntity taskCountEntity) {
        for (int i = 0; i < listCount.size(); i++) {
            switch (statusList.get(i)) {
                case "待提交":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitcommit()) ? "0" : taskCountEntity.getWaitcommit());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitcommit(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "待审批":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitapproval()) ? "0" : taskCountEntity.getWaitapproval());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitapproval(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "竞价中":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getBidding()) ? "0" : taskCountEntity.getBidding());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getBidding(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "待接受":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitreceive()) ? "0" : taskCountEntity.getWaitreceive());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitreceive(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "待验收":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitcheck()) ? "0" : taskCountEntity.getWaitcheck());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitcheck(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "待评价":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitevaluate()) ? "0" : taskCountEntity.getWaitevaluate());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitevaluate(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "待支付":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getWaitpay()) ? "0" : taskCountEntity.getWaitpay());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getWaitpay(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "已完成":
                    if ("接包方".equals(userEntity.getRole())) {
                        listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getCompleted()) ? "0" : taskCountEntity.getCompleted());
                        if (Integer.parseInt(Utils.getMyString(taskCountEntity.getCompleted(), "0")) > 0) {
                            listCount.get(i).setVisibility(View.VISIBLE);
                        } else {
                            listCount.get(i).setVisibility(View.GONE);
                        }
                    } else {
                        int complete = 0;
                        int over = 0;
                        if (!TextUtils.isEmpty(taskCountEntity.getCompleted())) {
                            complete = Integer.valueOf(taskCountEntity.getCompleted());
                        }
                        if (!TextUtils.isEmpty(taskCountEntity.getOver())) {
                            over = Integer.valueOf(taskCountEntity.getOver());
                        }
                        listCount.get(i).setText((complete + over) + "");
                        if (Integer.parseInt(Utils.getMyString((complete + over) + "", "0")) > 0) {
                            listCount.get(i).setVisibility(View.VISIBLE);
                        } else {
                            listCount.get(i).setVisibility(View.GONE);
                        }

                    }
                    break;
                case "仲裁":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getArbitration()) ? "0" : taskCountEntity.getArbitration());

                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getArbitration(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "终止":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getOver()) ? "0" : taskCountEntity.getOver());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getOver(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
                case "已下架":
                    listCount.get(i).setText(TextUtils.isEmpty(taskCountEntity.getOffshelf()) ? "0" : taskCountEntity.getOffshelf());
                    if (Integer.parseInt(Utils.getMyString(taskCountEntity.getOffshelf(), "0")) > 0) {
                        listCount.get(i).setVisibility(View.VISIBLE);
                    } else {
                        listCount.get(i).setVisibility(View.GONE);
                    }
                    break;
            }


        }
    }

    private List<String> getAllStatus() {
        List<String> list = new ArrayList<>();
        list.add("进行中");
        list.add("已完成");
        list.add("仲裁");
        list.add("终止");
        return list;
    }

    private List<String> allStatus() {
        List<String> list = new ArrayList<>();
//        list.add("全部");
        if ("总监".equals(userEntity.getRole()) || "项目经理".equals(userEntity.getRole())) {
            list.add("待提交");
            list.add("待审批");
            list.add("竞价中");
            list.add("待接受");
            list.add("待验收");
            list.add("待评价");
            list.add("待支付");
            list.add("已完成");
            list.add("仲裁");
        } else if ("发包方".equals(userEntity.getRole())) {
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
        } else if ("接包方".equals(userEntity.getRole())) {
            list.add("待接受");
            list.add("待验收");
            list.add("待评价");
            list.add("待支付");
            list.add("仲裁");
            list.add("终止");
            list.add("已完成");
        }
        return list;
    }

    /*
    * 经理和总监
    * */
    private String directororstatus(String statusStr) {
        switch (statusStr) {
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

    /**
     * 发包方任务状态转换
     *
     * @param status 任务状态
     * @return 状态码
     */
    public static String managerMemberStatus(String status) {
        String memberStatus = "";
        switch (status) {
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
                memberStatus = "false";
                break;
        }
        return memberStatus;
    }

    private Map<String, String> jiebaofang(String strstatus) {
        Map<String, String> map = new HashMap<>();
        map.clear();
        switch (strstatus) {
            case "待接受":
                map.put("contionType", "toReceive");
                map.put("status", "1");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.txt_project_task, R.id.txt_service_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_project_task:
                txtProjectTask.setTextColor(Color.parseColor("#f74b4d"));
                txtServiceTask.setTextColor(Color.parseColor("#000000"));
                type = 0;
                typeTask(type);
                break;
            case R.id.txt_service_task: {
                txtProjectTask.setTextColor(Color.parseColor("#000000"));
                txtServiceTask.setTextColor(Color.parseColor("#f74b4d"));
                type = 1;
                typeTask(type);
            }
            break;
        }
    }

    /**
     * 获取签到日期
     */
    private void getCalender(final String taskId) {

        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("taskId",taskId);
        XUtil.Get(UrlUtils.URL_queryTaskSignByPerson, map, new MyCallBack<String>() {

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
                    ResultEntity<List<SignInEntity>> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<List<SignInEntity>>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        signInEntities = new ArrayList<SignInEntity>();
                        for (SignInEntity signInEntity:resultEntity.getResult()){
                            signInEntities.add(signInEntity.clone());
                        }
                        showSignInDialog(resultEntity.getResult(),taskId);


                    } else {
                        Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "获取日期列表失败", Toast.LENGTH_SHORT).show();
                } finally {
                    dialog.dismiss();
                }

            }
        });
    }

    private void showSignInDialog(final List<SignInEntity> tmp, final String taskId) {
        DPCManager.getInstance().clearnDATE_CACHE();
        DPCManager.getInstance().setSignInBG(tmp);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        DatePicker2 picker = new DatePicker2(getActivity());
        Calendar c = Calendar.getInstance();
        picker.setDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1);
        picker.setMode(DPMode.SINGLE);
        picker.setHolidayDisplay(false);
        picker.setFestivalDisplay(true);
        picker.setTodayDisplay(false);

        picker.setDPDecor(new DPDecor(){
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {

            }
        });
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                for(SignInEntity signInEntity: signInEntities){
                    if (date.equals(signInEntity.getDate()) && "0".equals(signInEntity.getType())){
                        signIn(date,dialog,taskId);
                        break;
                    }
                }

                //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }



    /**
     * 签到
     */
    private void signIn(String date, final AlertDialog dialogCalendar,String taskId) {

        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("signTime",getFormateDate(date));
        map.put("taskId",taskId);
        XUtil.Get(UrlUtils.URL_updateTaskSignBySign, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(), arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<Object>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        if (dialogCalendar != null){
                            dialogCalendar.dismiss();
                        }
                        Toast.makeText(getActivity(), "签到成功！", Toast.LENGTH_SHORT).show();
                        getServicedata("0");
                    } else {
                        Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "签到失败！", Toast.LENGTH_SHORT).show();
                } finally {
                    dialog.dismiss();
                }

            }
        });
    }

    private String getFormateDate(String date) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
            Date date1 = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = simpleDateFormat1.format(date1);
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 查询仲裁问题列表
     */
    private void requestArbitrationList(final String taskId, final String publisherId) {
        dialog.show();
        Map map = new HashMap<>();
        map.put("SendOrReceive", "接包方".equals(userEntity.getRole()) ? "1" : "0");
        XUtil.Post(UrlUtils.URL_queryTaskarbitrateitemApp, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<List<String>> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<List<String>>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        List<String> arbitrations = resultEntity.getResult();
                        setArbitrationView(arbitrations,taskId,publisherId);
                    } else {
                        Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(), "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    /**
     * 显示仲裁dialog
     * @param arbitrations
     */
    private void setArbitrationView(List<String> arbitrations, final String taskId, final String publisherid) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_arbitration, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_question);
        final ArbitrationQuestionAdapter arbitrationQuestionAdapter = new ArbitrationQuestionAdapter(getActivity(), arbitrations);
        listView.setAdapter(arbitrationQuestionAdapter);
        final EditText et_remark = (EditText) view.findViewById(R.id.et_remark);

        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arbitration(et_remark.getText().toString(), arbitrationQuestionAdapter.questionStr(),taskId,publisherid);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arbitrationDialog != null) {
                    arbitrationDialog.dismiss();
                }
            }
        });
        arbitrationDialog = builder.setView(view).create();
        arbitrationDialog.show();
    }

    /**
     * 仲裁接口
     * @param remark 备注
     * @param questions 问题列表
     */
    private void arbitration(String remark, String questions,String taskId,String publisherId) {
        dialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("taskId", taskId);
        map.put("remark", remark);
        map.put("taskArbitrateItemList", questions);
        map.put("signCustomerId",publisherId);
        XUtil.Post(UrlUtils.URL_arbitrationTaskLease, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(getActivity(), "仲裁申请成功", Toast.LENGTH_SHORT).show();
                        getServicedata("0");
                        if (arbitrationDialog != null) {
                            arbitrationDialog.dismiss();
                        }
                    } else {

                        Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "仲裁申请失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(), "仲裁申请失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK){
            getServicedata("0");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
