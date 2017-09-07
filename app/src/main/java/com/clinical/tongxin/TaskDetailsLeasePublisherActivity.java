package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.TaskDetailsLeasePublisherAdapter;
import com.clinical.tongxin.adapter.TaskDetailsPublisherAdapter;
import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.PublisherEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TaskDetailsPublisherEntity;
import com.clinical.tongxin.entity.TaskLeaseEntity;
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

import static com.clinical.tongxin.R.id.ll_contrator_info;
import static com.clinical.tongxin.R.id.rb_score;
import static com.clinical.tongxin.R.id.tv_price;
import static com.clinical.tongxin.R.id.tv_score;


/**
 * 任务详情租赁-发包方
 *
 * @author linchao
 *         2017年8月14日15:38:29
 */
public class TaskDetailsLeasePublisherActivity extends BaseActivity {


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
    private MyProgressDialog mDialog;
    private String taskId;
    private String status;
    private TaskLeaseEntity taskLeaseEntity;
    private List<EvaluateListEntity> evaluateList; // 评价信息
    private String publishId; // 发布人id
    private AlertDialog dialog;
    private TaskDetailsPublisherEntity entity;
    private TaskDetailsLeasePublisherAdapter adapter;
    private List<PublisherEntity> publisherEntities;
    private TextView tv_project_type;
    private TextView tv_task_type;
    private TextView tv_address;
    private TextView tv_workers;
    private TextView tv_lease_price;
    private TextView tv_deadline;
    private TextView tv_task_name;
    private AlertDialog dialogConfirm; //  确认中标dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_lease_pulisher);
        ButterKnife.bind(this);
        initView();
        requestDetails();
    }

    private void initView() {
        taskId = getIntent().getStringExtra("taskId");
        title.setText("租赁任务详情");
        mDialog = new MyProgressDialog(context, "请稍后...");
        publisherEntities = new ArrayList<>();
        View viewHeader = LayoutInflater.from(context).inflate(R.layout.header_task_details_lease_pulisher, null);
        tv_task_name = (TextView) viewHeader.findViewById(R.id.tv_task_name);
        tv_project_type = (TextView) viewHeader.findViewById(R.id.tv_project_type);
        tv_task_type = (TextView) viewHeader.findViewById(R.id.tv_task_type);
        tv_address = (TextView) viewHeader.findViewById(R.id.tv_address);
        tv_workers = (TextView) viewHeader.findViewById(R.id.tv_workers);
        tv_lease_price = (TextView) viewHeader.findViewById(R.id.tv_lease_price);
        tv_deadline = (TextView) viewHeader.findViewById(R.id.tv_deadline);

        lvTask.addHeaderView(viewHeader);
        adapter = new TaskDetailsLeasePublisherAdapter(context, publisherEntities);
        adapter.setOnClickDetailsLister(new TaskDetailsLeasePublisherAdapter.onClickDetailsLister() {
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
                        confirmBidding(publisherEntity);
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

            @Override
            public void onClickResume(PublisherEntity publisherEntity) {
                Intent intent = new Intent(context,ResumeActivity.class);
                intent.putExtra("uid",publisherEntity.getContractorId());
                startActivity(intent);
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
                requestDetails();
            }

        });
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
        XUtil.Get(UrlUtils.URL_FIND_TASK_LEASE_PUBLISHER_DEATAIL, map, new MyCallBack<String>() {

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
                } finally {
                    mDialog.dismiss();
                    loadMoreListViewPtrFrame.refreshComplete();
                }

            }
        });
    }

    private void setText() {
        tv_task_name.setText(entity.getTaskName());
        tv_project_type.setText(entity.getProjectTypeName());
        tv_task_type.setText(entity.getTaskTypeName());
        tv_address.setText(entity.getAddress());
        tv_workers.setText(entity.getWorkers()+"人");
        tv_lease_price.setText(entity.getPrice()+"元/人天");
        tv_deadline.setText(entity.getDeadline());
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

    /**
     * 结束竞价
     */
    private void confirmBidding(PublisherEntity publisherEntity) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("bidCustomerId", publisherEntity.getContractorId());
        map.put("taskId", taskId);
        XUtil.Get(UrlUtils.URL_LEASE_PUBLISHER_CONFIRM, map, new MyCallBack<String>() {

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
                    if (dialogConfirm != null){
                        dialogConfirm.dismiss();
                    }
                    if (resultEntity.getCode() == 200) {
                        Toast.makeText(context, "确定中标人成功", Toast.LENGTH_SHORT).show();
                        requestDetails();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "确定中标人失败", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }
}
