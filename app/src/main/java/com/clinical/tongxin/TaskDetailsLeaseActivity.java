package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.Tag;
import com.clinical.tongxin.entity.TaskLeaseEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.TagListView;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 任务详情租赁-接包人
 *
 * @author linchao
 *         2017年8月14日15:38:29
 */
public class TaskDetailsLeaseActivity extends BaseActivity {

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
    @BindView(R.id.tv_task_name)
    TextView tvTaskName;
    @BindView(R.id.tv_project_type)
    TextView tvProjectType;
    @BindView(R.id.tv_task_type)
    TextView tvTaskType;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_workers)
    TextView tvWorkers;
    @BindView(R.id.tv_lease_price)
    TextView tvLeasePrice;
    @BindView(R.id.tv_deadline)
    TextView tvDeadline;
    @BindView(R.id.tagview)
    TagListView tagview;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_btn1)
    TextView tvBtn1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.iv_evaluate)
    ImageView ivEvaluate;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    private MyProgressDialog mDialog;
    private String taskId;
    private String status;
    private TaskLeaseEntity taskLeaseEntity;
    private List<EvaluateListEntity> evaluateList; // 评价信息
    private String publishId; // 发布人id
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_lease);
        ButterKnife.bind(this);
        initView();
        requestDetailsData();
        addOpenCount();
    }

    private void initView() {
        taskId = getIntent().getStringExtra("taskId");
        status = getIntent().getStringExtra("status");
        title.setText("租赁任务详情");
        mDialog = new MyProgressDialog(context, "请稍后...");
        tvBtn1.setText("我要竞价");
    }

    /**
     * 获取任务详情
     */
    private void requestDetailsData() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        XUtil.Get(UrlUtils.URL_FIND_TASK_LEASE_DEATAIL, map, new MyCallBack<String>() {

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
                    ResultEntity<TaskLeaseEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<TaskLeaseEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        taskLeaseEntity = resultEntity.getResult();
                        publishId = taskLeaseEntity.getPublisherId();
                        setText();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取任务详情失败", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
                }

            }
        });


    }


    private void requestEvaluateData() {
        if (TextUtils.isEmpty(publishId)) {
            ToastUtils.showToast(context, "获取发布人失败");
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", publishId);
        XUtil.Get(UrlUtils.URL_queryTaskAllReview, map, new MyCallBack<String>() {

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
                    ResultEntity<List<EvaluateListEntity>> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<List<EvaluateListEntity>>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        evaluateList = resultEntity.getResult();
                        setUpData(evaluateList);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取评价失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });


    }

    /**
     * 设置评价数据
     *
     * @param evaluateListEntities 评价数组
     */
    private void setUpData(List<EvaluateListEntity> evaluateListEntities) {
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < evaluateListEntities.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(evaluateListEntities.get(i).getComment() + " " + evaluateListEntities.get(i).getCount());
            tag.setIsterms(evaluateListEntities.get(i).getIsterms());
            mTags.add(tag);
        }
        tagview.setTags(mTags);
    }

    private void setText() {
        tvTaskName.setText(taskLeaseEntity.getTaskName());
        tvProjectType.setText(taskLeaseEntity.getProjectTypeName());
        tvTaskType.setText(taskLeaseEntity.getTaskTypeName());
        tvAddress.setText(taskLeaseEntity.getAddress());
        tvWorkers.setText(taskLeaseEntity.getWorkers() + "人");
        tvLeasePrice.setText(taskLeaseEntity.getPrice() + "元/人天");
        tvDeadline.setText(taskLeaseEntity.getDeadline());

    }




    @OnClick({R.id.ll_evaluate, R.id.tv_btn1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_evaluate:
                if (tagview.getVisibility() == View.GONE) {
                    tagview.setVisibility(View.VISIBLE);
                    if (evaluateList != null) {
                        if (evaluateList.size() == 0) {
                            ToastUtils.showToast(context, "暂无评价");
                            return;
                        }
                        setUpData(evaluateList);
                    } else {
                        requestEvaluateData();
                    }
                } else {
                    tagview.setVisibility(View.GONE);
                }
                ivEvaluate.setImageResource(tagview.getVisibility() == View.GONE ? R.mipmap.icon_sacle_showall : R.mipmap.icon_scale_hideall);

                break;
            case R.id.tv_btn1:
                if (getLoginConfig().getStatus().equals("0")) { // 申请实名
                    Intent intent = new Intent(context, NameAuthenticationActivity.class);
                    startActivity(intent);
                } else if (getLoginConfig().getStatus().equals("1")) { // 设置支付信息
                    Intent intent = new Intent(context, PaySetUpPwdActivity.class);
                    startActivity(intent);
                } else if (getLoginConfig().getStatus().equals("2") || getLoginConfig().getStatus().equals("3")) {
                    Toast.makeText(context, "认证审核中！", Toast.LENGTH_SHORT).show();
                } else if (getLoginConfig().getStatus().equals("4") || getLoginConfig().getStatus().equals("6")) { // 初审失败
                    Intent intent = new Intent(context, ReasonActivity.class);
                    startActivity(intent);
                } else if (getLoginConfig().getStatus().equals("7")) {
                    showPrice();
                }
                break;
        }
    }

    private void showPrice() {
        if (taskLeaseEntity == null) {
            ToastUtils.showToast(context, "网络异常，请稍后再试");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_price, null);
        TextView tv_amount_desc = (TextView)view.findViewById(R.id.tv_amount_desc);
        tv_amount_desc.setText("租赁金额：");
        TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        tv_amount.setText(taskLeaseEntity.getPrice()+"元/人天");
        TextView tv_project_type = (TextView) view.findViewById(R.id.tv_project_type);
        tv_project_type.setText(taskLeaseEntity.getProjectTypeName());
        TextView tv_task_type = (TextView) view.findViewById(R.id.tv_task_type);
        tv_task_type.setText(taskLeaseEntity.getTaskTypeName());
        final EditText et_price = (EditText) view.findViewById(R.id.et_price);
        TextView tv_OK = (TextView) view.findViewById(R.id.tv_OK);
        tv_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    // 竞价
                    try {
                        if (Double.valueOf(taskLeaseEntity.getPrice()) < Double.valueOf(et_price.getText().toString())) {
                            ToastUtils.showToast(context, "竞价价格不能大于租赁价格");
                            return;
                        }
                        if (Double.valueOf(et_price.getText().toString()) < Double.valueOf(taskLeaseEntity.getPrice()) * 0.7) {
                            ToastUtils.showToast(context, "竞价价格不能小于租赁价格的70%");
                            return;
                        }
                        auction(et_price.getText().toString());
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void auction(String price) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        map.put("bidPrice", price);

        XUtil.Post(UrlUtils.URL_ParticipateBidding, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    if (jsonObject.getInt("code") == 200) {
                        // 删除成功
                        Toast.makeText(context, "竞价成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "竞价失败，请重试", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
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
}
