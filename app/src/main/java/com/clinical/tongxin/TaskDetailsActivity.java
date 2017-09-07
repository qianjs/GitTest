package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.TaskDetailsAdapter;
import com.clinical.tongxin.entity.MyBudgetEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TaskDetailsEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.TaskSiteItemEntity;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.GpsUtils;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.location.d.j.G;
import static com.clinical.tongxin.R.id.et_name;
import static com.clinical.tongxin.R.id.left;
import static com.clinical.tongxin.R.id.lv_task;
import static com.clinical.tongxin.R.id.rl_team_aptitude;
import static com.clinical.tongxin.R.id.tv_aptitude;
import static com.clinical.tongxin.R.id.tv_complaint;
import static com.clinical.tongxin.R.id.tv_complete;
import static com.clinical.tongxin.R.id.tv_finish;
import static com.clinical.tongxin.R.id.tv_manager;
import static com.clinical.tongxin.R.id.tv_project;
import static com.clinical.tongxin.R.id.tv_remark;
import static com.clinical.tongxin.R.id.tv_task_scale_type;
import static com.clinical.tongxin.R.id.tv_team_edit;
import static com.clinical.tongxin.R.id.tv_team_remove;
import static com.clinical.tongxin.R.id.tv_title;
import static com.clinical.tongxin.R.layout.top_t;
import static com.clinical.tongxin.R.mipmap.index_icon04;


/**
 * 任务详情
 *
 * @author LINCHAO
 *         2017/1/5
 */
@ContentView(R.layout.activity_task_details)
public class TaskDetailsActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;
    // 地图显示点
    @ViewInject(R.id.iv_more)
    ImageView iv_more;
    @ViewInject(R.id.ll_top)
    View ll_top;
    // 站点列表
    @ViewInject(R.id.lv_site)
    ListView lv_site;

    // 任务图片
//    @ViewInject(R.id.iv_icon)
    private ImageView iv_icon;

    // 当前状态
//    @ViewInject(R.id.tv_project_type_name)
    private TextView tv_project_type_name;

    // 当前状态
//    @ViewInject(R.id.tv_status)
    private TextView tv_status;

    // 接单时间
//    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    private View rl_time;

    // 订单编号
//    @ViewInject(R.id.tv_order_num)
    private TextView tv_order_num;
    private View rl_order_num;
    // 项目编号
//    @ViewInject(R.id.tv_project_num)
    private TextView tv_project_num;

    // 项目名称
//    @ViewInject(R.id.tv_project_name)
    private TextView tv_project_name;
    // 项目地址
//    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    // 项目类型
//    @ViewInject(R.id.tv_type)
    private TextView tv_type;

    // 工作类型
//    @ViewInject(R.id.tv_task_work_type)
//    private TextView tv_task_work_type;

    // 规模
//    @ViewInject(R.id.tv_task_scale_type)
    private TextView tv_task_scale_type;
    // 订单金额
    private TextView tv_amount;
    private View rl_amount;
    private LinearLayout container;

    private String taskId;
    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private Context context;
    private TaskDetailsEntity taskDetailsEntity;
    private List<TaskSiteItemEntity> taskSiteItemEntities;
    private TaskDetailsAdapter adapter;
    private PopWindowImportPrice popWindowImportPrice;
    //private int taskStatus;
    private double longitude;
    private double latitude;
    private AlertDialog approvalDialog;
    private TextView tv_notify;

    private boolean isPublicTask;
    private boolean isAllTask; // 是否为全部任务
    private String isAcceptScore; // 接包方是否已评价
    private String isSendScore; // 发包方是否已评价
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initData();
        initView();
        initLister();
        requestData();
        //setButton();
    }


    private void requestData() {
        mDialog.show();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDialog.dismiss();
//                        isEdit(false);
//                        teamEntity.setName(et_name.getText().toString());
//                        teamEntity.setAptitude(tv_aptitude.getText().toString());
//                    }
//                });
//            }
//        },2000);
        Map map = new HashMap<>();

        map.put("TaskId", taskId);
        map.put("CustomerId",getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        XUtil.Post(UrlUtils.URL_SHOW_TASK_INFO, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(TaskDetailsActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<TaskDetailsEntity> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<TaskDetailsEntity>>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        taskDetailsEntity = resultEntity.getResult();
                        setText();
                        if (!"7".equals(getLoginConfig().getStatus())) {
                            ToastUtils.showToast(context, "请实名认证");
                        } else {
                            showButton();
                        }

                    } else {
                        Toast.makeText(TaskDetailsActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(TaskDetailsActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }


    private void initData() {
        if (getIntent().getStringExtra("taskId") != null) {
            taskId = getIntent().getStringExtra("taskId");
        }
        isPublicTask = getIntent().getBooleanExtra("isPublicTask", false);
        isAllTask = getIntent().getBooleanExtra("isAllTask",false);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        isSendScore = getIntent().getStringExtra("isSendScore");
        isAcceptScore = getIntent().getStringExtra("isAcceptScore");
        context = this;
        taskSiteItemEntities = new ArrayList<>();
        userEntity = getLoginConfig();
    }

    private void initView() {
        mDialog = new MyProgressDialog(this, "请稍后...");
        userEntity = getLoginConfig();
        tv_title.setText("任务详情");
        iv_more.setImageResource(R.mipmap.map_location);
        iv_more.setVisibility(View.VISIBLE);
        adapter = new TaskDetailsAdapter(context, taskSiteItemEntities);
        View headerView = LayoutInflater.from(context).inflate(R.layout.team_details_header, null);
        tv_notify = (TextView) headerView.findViewById(R.id.tv_notify);
        iv_icon = (ImageView) headerView.findViewById(R.id.iv_icon);
        tv_project_type_name = (TextView) headerView.findViewById(R.id.tv_project_type_name);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        rl_time = headerView.findViewById(R.id.rl_time);
        tv_order_num = (TextView) headerView.findViewById(R.id.tv_order_num);
        rl_order_num = headerView.findViewById(R.id.rl_order_num);
        tv_project_num = (TextView) headerView.findViewById(R.id.tv_project_num);
        tv_project_name = (TextView) headerView.findViewById(R.id.tv_project_name);
        tv_address = (TextView) headerView.findViewById(R.id.tv_address);
        tv_type = (TextView) headerView.findViewById(R.id.tv_type);
        //tv_task_work_type = (TextView) headerView.findViewById(R.id.tv_task_work_type);
        tv_task_scale_type = (TextView) headerView.findViewById(R.id.tv_task_scale_type);
        tv_amount = (TextView) headerView.findViewById(R.id.tv_amount);
        rl_amount = headerView.findViewById(R.id.rl_amount);
        container = (LinearLayout) headerView.findViewById(R.id.container);

        lv_site.addHeaderView(headerView);
        lv_site.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 初始化监听
     */
    private void initLister() {
        iv_more.setOnClickListener(this);
        adapter.setOnClickAmapListener(new TaskDetailsAdapter.onClickAmapLister() {
            @Override
            public void onClick(TaskSiteItemEntity taskSiteItemEntity) {
                if (latitude == 0 || longitude == 0) {
                    Toast.makeText(context, "当前位置获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskSiteItemEntity.getLongitude()) || TextUtils.isEmpty(taskSiteItemEntity.getLatitude())) {
                    Toast.makeText(context, "站点位置获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    GpsUtils.getInstance(context).openGaoDeMap(latitude, longitude, "",
                            Double.valueOf(taskSiteItemEntity.getLatitude()), Double.valueOf(taskSiteItemEntity.getLongitude()), "");

//                    if (GpsUtils.getInstance(context).isGdMapInstalled()){
//                        GpsUtils.getInstance(context).openGaoDeMap(latitude,longitude,"",
//                                Double.valueOf(taskSiteItemEntity.getLatitude()),Double.valueOf(taskSiteItemEntity.getLongitude()),"");
//                    }else {
//                        Intent intent = new Intent(context,SingleRouteCalculateActivity.class);
//                        intent.putExtra("startLng",longitude);
//                        intent.putExtra("startLat",latitude);
//                        intent.putExtra("endLng",Double.valueOf(taskSiteItemEntity.getLongitude()));
//                        intent.putExtra("endLat",Double.valueOf(taskSiteItemEntity.getLatitude()));
//                        context.startActivity(intent);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                if (taskDetailsEntity.getTaskItem() != null && taskDetailsEntity.getTaskItem().size() > 0) {
                    Intent intent = new Intent(context, MarkerActivity.class);
                    intent.putExtra("TaskSiteItemEntity", (Serializable) taskDetailsEntity.getTaskItem());
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(context, "无站点信息");
                }
                break;
        }
    }


    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                finish();
            }
        }
    }

    private void setText() {
        try {
//            if (taskStatus == 0){
//                rl_order_num.setVisibility(View.VISIBLE);
//                rl_amount.setVisibility(View.VISIBLE);
//                rl_time.setVisibility(View.VISIBLE);
//            }
            if (TextUtils.isEmpty(taskDetailsEntity.getTypePicUrl())) {
                iv_icon.setVisibility(View.GONE);
            } else {
                ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL + taskDetailsEntity.getTypePicUrl(),
                        iv_icon, MyApplication.normalOption);
            }

            tv_status.setText(Utils.statusSwtichToStr(Integer.valueOf(taskDetailsEntity.getTaskStatusName()), getLoginConfig(), isPublicTask));
            tv_project_type_name.setText(taskDetailsEntity.getProjectName());
            tv_project_num.setText(taskDetailsEntity.getNumber());
            tv_project_name.setText(taskDetailsEntity.getProjectName());
            tv_type.setText(taskDetailsEntity.getProjectTypeName());
            tv_address.setText(taskDetailsEntity.getProvinceName() + taskDetailsEntity.getCityName() + taskDetailsEntity.getDistrictName());
            tv_task_scale_type.setText(taskDetailsEntity.getScale());
            tv_amount.setText(taskDetailsEntity.getAmount());
            tv_time.setText(taskDetailsEntity.getCreateOrderTime());
            tv_order_num.setText(taskDetailsEntity.getOrderId());
            adapter.setList(taskDetailsEntity.getTaskItem());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View setButton(int imageId, String text, final AddViewOnClickListener listener) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout ll_btn = new LinearLayout(context);

        // 为主布局container设置布局参数
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                width / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll_btn.setLayoutParams(llp);//设置container的布局
        ll_btn.setOrientation(LinearLayout.VERTICAL);// 设置主布局的orientation
        ll_btn.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageId);
        Gallery.LayoutParams params = new Gallery.LayoutParams(Utils.dip2px(context, 60), Utils.dip2px(context, 60));
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(Utils.dip2px(context, 5), Utils.dip2px(context, 5), Utils.dip2px(context, 5), Utils.dip2px(context, 5));

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        ll_btn.addView(imageView);
        ll_btn.addView(textView);

        ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });
        return ll_btn;
    }

    interface AddViewOnClickListener {
        void onClick();
    }


    private void auction(String price, int type) {
        mDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//              runOnUiThread(new Runnable() {
//                  @Override
//                  public void run() {
//                      mDialog.dismiss();
//                      finish();
//                  }
//              });
//            }
//        },2000);
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("TaskId", taskId);
        map.put("BidType", type + "");
        map.put("BidPrice", price);

        XUtil.Post(UrlUtils.URL_SHOW_AUCTION, map, new MyCallBack<String>() {
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
//                    ResultEntity<String> entity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<String>>() {
//                    }.getType());
                    JSONObject jsonObject=new JSONObject(arg0);
                    if (jsonObject.getInt("code") == 200) {
                        // 删除成功
                        Toast.makeText(context, "竞价成功", Toast.LENGTH_SHORT).show();
                        popWindowImportPrice.dismiss();
                    } else {
                        if (!TextUtils.isEmpty(jsonObject.getString("msg"))) {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "竞价失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                    popWindowImportPrice.dismiss();
                    mDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "竞价失败，请重试", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });
    }

    private void showButton() {
        if ("总监".equals(userEntity.getRole()) || isAllTask) {
            return;
        }
        if ("项目经理".equals(userEntity.getRole()) || "发包方".equals(userEntity.getRole())) {
            if (Integer.valueOf(taskDetailsEntity.getTaskStatusName()) > 1) {
                container.addView(setButton(R.mipmap.yjrw01, "交流互动", new AddViewOnClickListener() {
                    @Override
                    public void onClick() {
                        if (TextUtils.isEmpty(taskDetailsEntity.getContractorHxNum())) {
                            Toast.makeText(context, "接包人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(userEntity.getHxAccount())) {
                            Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsEntity.getContractorHxNum());
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                            startActivity(intent);
                        }
                    }
                }));
            }
            switch (taskDetailsEntity.getTaskStatusName()) {
                case "-2":
                    container.addView(setButton(R.mipmap.tijiao, "提交", new AddViewOnClickListener() {
                        @Override
                        public void onClick() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("提交任务").setMessage("是否确认提交").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 提交任务接口
                                    commitTask(dialogInterface);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }));
                    break;
                case "0":
                    if (!"发包方".equals(userEntity.getRole())) {
                        container.addView(setButton(R.mipmap.shenpi, "审批", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                showPayDialog(0);
                            }
                        }));
                    } else {
                        tv_notify.setVisibility(View.VISIBLE);
                    }

                    break;
                case "1":
                    if ("1".equals(taskDetailsEntity.getPublishedType())) {
                        container.addView(setButton(R.mipmap.jjxx, "竞价信息", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                // 跳转竞价信息
                                Intent intent = new Intent(context, AuctionInfoActivity.class);
                                intent.putExtra("isPublicTask", isPublicTask);
                                intent.putExtra("taskId", taskId);
                                startActivityForResult(intent, 1001);
                            }
                        }));
                    } else {
                        tv_notify.setVisibility(View.VISIBLE);
                    }

                    break;
                case "4":
                    if ("0".equals(isSendScore)){
                        container.addView(setButton(R.mipmap.evaluate, "评价", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                StringBuffer sb = new StringBuffer();
                                sb.append(taskDetailsEntity.getContractor())
                                        .append("(")
                                        .append(taskDetailsEntity.getContractorHxNum())
                                        .append(")");
                                Intent intent = new Intent(context, EvaluateActivity.class);
                                intent.putExtra("taskId", taskId);
                                intent.putExtra("person", sb.toString());
                                startActivityForResult(intent, 1001);
                            }
                        }));
                    }

                    break;
                case "3":

                    if (!"发包方".equals(userEntity.getRole())) {
                        container.addView(setButton(R.mipmap.zhifu, "支付", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                showPayDialog(3);
                            }
                        }));
                    } else {
                        tv_notify.setVisibility(View.VISIBLE);
                    }

                    break;
                case "6":
                    container.addView(setButton(R.mipmap.zcxx, "仲裁结果", new AddViewOnClickListener() {
                        @Override
                        public void onClick() {
                            // 仲裁
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));
                        }
                    }));
                    break;
                case "7":
                    container.addView(setButton(R.mipmap.zcxx, "仲裁结果", new AddViewOnClickListener() {
                        @Override
                        public void onClick() {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));
                        }
                    }));
                    break;
                default:
                    tv_notify.setVisibility(View.VISIBLE);
                    break;
            }
        } else if ("接包方".equals(userEntity.getRole())) {
            View view = setButton(R.mipmap.yjrw01, "交流互动", new AddViewOnClickListener() {
                @Override
                public void onClick() {
                    if (TextUtils.isEmpty(taskDetailsEntity.gethXNumber())) {
                        Toast.makeText(context, "发布人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(userEntity.getHxAccount())) {
                        Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsEntity.gethXNumber());
                        intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                        startActivity(intent);
                    }
                }
            });
            container.addView(view);
            switch (taskDetailsEntity.getTaskStatusName()) {
                case "1":
                    if ("1".equals(taskDetailsEntity.getPublishedType())) {
                        container.removeAllViews();
                        popWindowImportPrice = new PopWindowImportPrice(context);
                        popWindowImportPrice.setOnOKClickListener(new PopWindowImportPrice.OnOKClickListener() {
                            @Override
                            public void OnOKClick(String price, int type) {
                                if (Double.valueOf(taskDetailsEntity.getAmount()) < Double.valueOf(price)) {
                                    ToastUtils.showToast(context, "竞价价格不能大于总价");
                                    return;
                                }
                                if (Double.valueOf(price) < Double.valueOf(taskDetailsEntity.getAmount()) * 0.7) {
                                    ToastUtils.showToast(context, "竞价价格不能小于总价的70%");
                                    return;
                                }
                                auction(price, type);
                            }
                        });
                        container.addView(setButton(R.mipmap.auction, "我要竞价", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {

                                popWindowImportPrice.showAsDropDown(ll_top);
                            }
                        }));
                        container.addView(setButton(R.mipmap.jjxx, "竞价信息", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                // 跳转竞价信息
                                Intent intent = new Intent(context, AuctionInfoActivity.class);
                                intent.putExtra("isPublicTask", isPublicTask);
                                intent.putExtra("taskId", taskId);
                                startActivityForResult(intent, 1001);
                            }
                        }));
                    } else {
                        container.addView(setButton(R.mipmap.receive, "接受", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                taskConfirm("1");
                            }
                        }));
                        container.addView(setButton(R.mipmap.refuse, "拒接", new AddViewOnClickListener() {
                            @Override
                            public void onClick() {
                                taskConfirm("0");
                            }
                        }));
                    }

                    break;

                case "4":
                    if (taskDetailsEntity.getContractorHxNum().equals(getLoginConfig().getHxAccount())) {
                        if ("0".equals(isAcceptScore)){
                            container.addView(setButton(R.mipmap.evaluate, "评价", new AddViewOnClickListener() {
                                @Override
                                public void onClick() {
                                    StringBuffer sb = new StringBuffer();
                                    sb.append(taskDetailsEntity.getContractor())
                                            .append("(")
                                            .append(taskDetailsEntity.getContractorHxNum())
                                            .append(")");
                                    Intent intent = new Intent(context, EvaluateActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    intent.putExtra("person", sb.toString());
                                    startActivityForResult(intent, 1001);
                                }
                            }));
                        }

                    } else {
                        tv_notify.setVisibility(View.VISIBLE);
                    }

                    break;
                case "6":
                    container.addView(setButton(R.mipmap.zcxx, "仲裁结果", new AddViewOnClickListener() {
                        @Override
                        public void onClick() {
                            // 仲裁
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));
                        }
                    }));
                    break;
                case "7":
                    container.addView(setButton(R.mipmap.zcxx, "仲裁结果", new AddViewOnClickListener() {
                        @Override
                        public void onClick() {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));
                        }
                    }));
                    break;
                default:
                    tv_notify.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    private void taskConfirm(final String i) {// 1是接受 2是拒绝
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("TaskId", taskId);
        map.put("Type", i);
        XUtil.Post(UrlUtils.URL_TASK_RECEIVE, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, i.equals("1") ? "接受任务成功" : "拒绝任务成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, i.equals("1") ? "接受任务失败" : "拒绝任务失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void showPayDialog(final int type) {

        View view = LayoutInflater.from(context).inflate(R.layout.pay_confirm, null);
        final TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        tv_amount.setText(taskDetailsEntity.getPayAccount());
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 审批
                if (type == 0) {
                    // 审批
                    taskApproval(et_pwd, approvalDialog);
                } else if (type == 3) {
                    // 支付接包方
                    taskPay(et_pwd, approvalDialog);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (approvalDialog != null) {
                    approvalDialog.dismiss();
                }
            }
        });
        approvalDialog = new AlertDialog.Builder(context).setView(view).create();
        approvalDialog.show();
    }

    private void taskPay(EditText et_pwd, final AlertDialog dialog) {
        if (TextUtils.isEmpty(et_pwd.getText().toString())) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("TaskId", taskId);
        map.put("isPass", "1");
        map.put("TradePassword", et_pwd.getText().toString());
        XUtil.Post(UrlUtils.URL_PAY_TASK, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void commitTask(final DialogInterface dialogInterface) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("TaskId", taskId);
        XUtil.Post(UrlUtils.URL_COMMIT, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {

                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void taskApproval(EditText et_pwd, final AlertDialog dialog) {
        if (TextUtils.isEmpty(et_pwd.getText().toString())) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("TaskId", taskId);
        map.put("isPass", "1");
        map.put("TradePassword", et_pwd.getText().toString());
        XUtil.Post(UrlUtils.URL_TASK_APPROVAL, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }
}
