package com.clinical.tongxin;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.clinical.tongxin.adapter.TaskDetailsPublisherAdapter;
import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.PublisherEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TaskDetailsPublisherEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class TaskDetailsPublisherActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.lv_task)
    ListView lvTask;

    @BindView(R.id.load_more_list_view_ptr_frame)
    PtrClassicFrameLayout loadMoreListViewPtrFrame;
    private TextView tv_project_type;
    private TextView tv_task_type;
    private TextView tv_task_amount;
    private TextView tv_task_scale;
    private TextView tv_publish_time;
    private TextView tv_notify_count;
    private TextView tv_open_count;
    private TextView tv_bidding_count;
    private View ll_contrator_info;
    private RatingBar rb_score;
    private TextView tv_score;
    private TextView tv_price;
    private TaskDetailsPublisherEntity entity;
    private List<PublisherEntity> publisherEntities;
    private MyProgressDialog mDialog;
    private String taskId;
    private String status;
    private TaskDetailsPublisherAdapter adapter;
    private AlertDialog dialogConfirm; //  确认中标dialog
    private boolean isConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_publisher);
        ButterKnife.bind(this);
        taskId = getIntent().getStringExtra("taskId");
        status = getIntent().getStringExtra("status");
        //addOpenCount();
        initView();
        requestDetails();
    }


    private void initView() {
        title.setText("任务详情");
        mDialog = new MyProgressDialog(context, "请稍后...");
        publisherEntities = new ArrayList<>();
        View viewHeader = LayoutInflater.from(context).inflate(R.layout.header_task_details_publisher, null);
        tv_project_type = (TextView) viewHeader.findViewById(R.id.tv_project_type);
        tv_task_type = (TextView) viewHeader.findViewById(R.id.tv_task_type);
        tv_task_amount = (TextView) viewHeader.findViewById(R.id.tv_task_amount);
        tv_task_scale = (TextView) viewHeader.findViewById(R.id.tv_task_scale);
        tv_publish_time = (TextView) viewHeader.findViewById(R.id.tv_publish_time);
        tv_notify_count = (TextView) viewHeader.findViewById(R.id.tv_notify_count);
        tv_open_count = (TextView) viewHeader.findViewById(R.id.tv_open_count);
        tv_bidding_count = (TextView) viewHeader.findViewById(R.id.tv_bidding_count);
        ll_contrator_info = viewHeader.findViewById(R.id.ll_contrator_info);
        tv_score = (TextView) viewHeader.findViewById(R.id.tv_score);
        tv_price = (TextView) viewHeader.findViewById(R.id.tv_price);
        rb_score = (RatingBar) viewHeader.findViewById(R.id.rb_score);
        lvTask.addHeaderView(viewHeader);
        adapter = new TaskDetailsPublisherAdapter(context, publisherEntities);
        adapter.setOnClickDetailsLister(new TaskDetailsPublisherAdapter.onClickDetailsLister() {
            @Override
            public void onConfirm(final PublisherEntity publisherEntity) {
                if (!"项目经理".equals(getLoginConfig().getRole())){
                    ToastUtils.showToast(context,"无操作权限");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_bidding,null);
                TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finishBidding(publisherEntity);
                    }
                });
                TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialogConfirm != null){
                            dialogConfirm.dismiss();
                        }
                    }
                });
                builder.setView(view);
                dialogConfirm = builder.create();
                dialogConfirm.show();
            }

            @Override
            public void onItemClick(int position) {
                showEvaluate(position);
            }
        });
        lvTask.setAdapter(adapter);
//        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

        // .设置下拉刷新组件和事件监听
        loadMoreListViewPtrFrame.setLoadingMinTime(1000);
        loadMoreListViewPtrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvTask, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                if (!isConfirm){
                    requestDetails();
                }else {
                    frame.refreshComplete();
                }

            }

        });
    }

    /**
     * 查询评价
     * @param position item位置
     */
    private void showEvaluate(final int position) {

        PublisherEntity publisherEntity = adapter.getItem(position);
        if (TextUtils.isEmpty(publisherEntity.getContractorId())) {
            ToastUtils.showToast(context, "获取竞价人失败");
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", publisherEntity.getContractorId());
        XUtil.Get(UrlUtils.URL_queryTaskAllReview, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                loadMoreListViewPtrFrame.refreshComplete();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<List<EvaluateListEntity>> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<List<EvaluateListEntity>>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        List<EvaluateListEntity> evaluateList = resultEntity.getResult();
                        if (evaluateList.size() == 0){
                            Toast.makeText(context, "暂无评价", Toast.LENGTH_SHORT).show();
                        }else {
                            adapter.setEvaluate(evaluateList, position);
                        }

                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取评价失败", Toast.LENGTH_SHORT).show();

                } finally {
                    mDialog.dismiss();
                }

            }
        });
    }

    private void setText() {
        try {
            tv_project_type.setText(entity.getProjectTypeName());
            tv_task_type.setText(entity.getTaskTypeName());
            tv_task_amount.setText(entity.getAmount());
            tv_publish_time.setText(entity.getPublishTime());
            tv_notify_count.setText(entity.getNotifyCount());
            tv_open_count.setText(entity.getOpenCount());
            tv_bidding_count.setText(entity.getBiddingCount());
            tv_task_scale.setText(entity.getScale());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取任务详情
     */
    private void requestDetails() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        map.put("status",status);
        XUtil.Get(UrlUtils.URL_queryCreateTaskInfo, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                loadMoreListViewPtrFrame.refreshComplete();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    Gson gson = new Gson();
                    ResultEntity<TaskDetailsPublisherEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<TaskDetailsPublisherEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        entity = resultEntity.getResult();
                        setText();
                        adapter.setList(entity.getContractors());
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取任务详情失败", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                    loadMoreListViewPtrFrame.refreshComplete();
                }

            }
        });
    }


    /**
     * 统计查看次数接口
     */
    private void addOpenCount() {

        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        XUtil.Get(UrlUtils.URL_recordAccessCount, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 结束竞价
     */
    private void finishBidding(final PublisherEntity publisherEntity) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("BidCustomerId", publisherEntity.getContractorId());
        map.put("TaskId", taskId);
        XUtil.Get(UrlUtils.URL_endTaskOfbidByPerson, map, new MyCallBack<String>() {

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
                    ResultEntity<Object> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<Object>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        if (dialogConfirm != null){
                            dialogConfirm.dismiss();
                        }
                        Toast.makeText(context, "结束竞价成功", Toast.LENGTH_SHORT).show();
                        adapter.setList(new ArrayList<PublisherEntity>());
                        ll_contrator_info.setVisibility(View.VISIBLE);
                        tv_price.setText(publisherEntity.getBiddingAmount());
                        tv_score.setText(publisherEntity.getScore());
                        rb_score.setRating(Float.valueOf(publisherEntity.getScore()));
                        isConfirm = true;
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "结束竞价失败", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }
}
