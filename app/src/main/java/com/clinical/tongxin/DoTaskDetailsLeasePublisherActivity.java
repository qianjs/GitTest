package com.clinical.tongxin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ArbitrationQuestionAdapter;
import com.clinical.tongxin.adapter.DoTaskDetailsLeasePublisherAdapter;
import com.clinical.tongxin.entity.DoTaskDetailsPublisherEntity;
import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.LeaseEntity;
import com.clinical.tongxin.entity.LeasePayInfoEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.SignInEntity;
import com.clinical.tongxin.entity.TaskLeaseEntity;
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
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * 做任务详情租赁-发包方
 *
 * @author linchao
 *         2017年8月14日15:38:29
 */
public class DoTaskDetailsLeasePublisherActivity extends BaseActivity {


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
    private DoTaskDetailsPublisherEntity entity;
    private DoTaskDetailsLeasePublisherAdapter adapter;
    private List<LeaseEntity> leaseEntities;
    private TextView tv_project_type;
    private TextView tv_task_type;
    private TextView tv_address;
    private TextView tv_workers;
    private TextView tv_lease_price;
    private TextView tv_deadline;
    private TextView tv_task_name;
    private TextView tv_wait_pay;
    private TextView tv_wait_confirm;
    private TextView tv_all;
    private AlertDialog dialogConfirm; //  确认中标dialog
    //游标
    private ImageView cursor;
    private LinearLayout ll_cursor;
    private int width;
    private int lastPosition = 0;
    private List<SignInEntity> signInEntities;
    private String signCustomerId;
    private AlertDialog payDialog; // 支付
    private AlertDialog dialogOver; // 终止
    private AlertDialog arbitrationDialog; // 仲裁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_lease_pulisher);
        ButterKnife.bind(this);
        initView();
        requestDetails();
    }

    private void addCursor(View headerView) {
        tv_all = (TextView) headerView.findViewById(R.id.tv_all);
        tv_wait_pay = (TextView) headerView.findViewById(R.id.tv_wait_pay);
        tv_wait_confirm = (TextView) headerView.findViewById(R.id.tv_wait_confirm);

        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(lastPosition,0);
                lastPosition = 0;
                tv_all.setTextColor(getResources().getColor(R.color.holo_red_light));
                tv_wait_pay.setTextColor(getResources().getColor(R.color.txt_gray));
                tv_wait_confirm.setTextColor(getResources().getColor(R.color.txt_gray));
                requestDetails();
            }
        });

        tv_wait_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(lastPosition,1);
                lastPosition = 1;
                tv_all.setTextColor(getResources().getColor(R.color.txt_gray));
                tv_wait_pay.setTextColor(getResources().getColor(R.color.holo_red_light));
                tv_wait_confirm.setTextColor(getResources().getColor(R.color.txt_gray));
                requestDetails();
            }
        });

        tv_wait_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(lastPosition,2);
                lastPosition = 2;
                tv_all.setTextColor(getResources().getColor(R.color.txt_gray));
                tv_wait_pay.setTextColor(getResources().getColor(R.color.txt_gray));
                tv_wait_confirm.setTextColor(getResources().getColor(R.color.holo_red_light));
                requestDetails();
            }
        });
        ll_cursor = (LinearLayout)headerView.findViewById(R.id.ll_cursor);
        View ll_type = headerView.findViewById(R.id.ll_type);
        ll_type.setVisibility(View.VISIBLE);
        cursor = new ImageView(context);
        width = getWindowManager().getDefaultDisplay().getWidth();
        Gallery.LayoutParams params = new Gallery.LayoutParams(width/3, Gallery.LayoutParams.MATCH_PARENT);
        cursor.setLayoutParams(params);
        cursor.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_corner_red));
        ll_cursor.addView(cursor);
        tv_all.setTextColor(getResources().getColor(R.color.holo_red_light));
    }

    private void initView() {
        taskId = getIntent().getStringExtra("taskId");
        title.setText("租赁任务详情");
        mDialog = new MyProgressDialog(context, "请稍后...");
        leaseEntities = new ArrayList<>();
        View viewHeader = LayoutInflater.from(context).inflate(R.layout.header_task_details_lease_pulisher, null);
        addCursor(viewHeader);
        tv_task_name = (TextView) viewHeader.findViewById(R.id.tv_task_name);
        tv_project_type = (TextView) viewHeader.findViewById(R.id.tv_project_type);
        tv_task_type = (TextView) viewHeader.findViewById(R.id.tv_task_type);
        tv_address = (TextView) viewHeader.findViewById(R.id.tv_address);
        tv_workers = (TextView) viewHeader.findViewById(R.id.tv_workers);
        tv_lease_price = (TextView) viewHeader.findViewById(R.id.tv_lease_price);
        tv_deadline = (TextView) viewHeader.findViewById(R.id.tv_deadline);

        lvTask.addHeaderView(viewHeader);
        adapter = new DoTaskDetailsLeasePublisherAdapter(context, leaseEntities,"发包方".equals(getLoginConfig().getRole()));
        adapter.setOnClickDetailsLister(new DoTaskDetailsLeasePublisherAdapter.onClickDetailsLister() {
            @Override
            public void onClickBtnOver(LeaseEntity leaseEntity) {
                signCustomerId = leaseEntity.getMemberId();
                // 终止
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View commitView = LayoutInflater.from(context).inflate(R.layout.dialog_over, null);
                TextView tv_confirm = (TextView) commitView.findViewById(R.id.tv_confirm);
                TextView tv_cancel = (TextView) commitView.findViewById(R.id.tv_cancel);
                builder.setView(commitView);


                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialogOver != null) {
                            dialogOver.dismiss();
                        }
                    }
                });
                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        overTask();
                    }
                });
                dialogOver = builder.create();
                dialogOver.show();
            }

            @Override
            public void onClickBtnArbitration(LeaseEntity leaseEntity) {
                // 仲裁
                signCustomerId = leaseEntity.getMemberId();
                requestArbitrationList();
            }

            @Override
            public void onClickBtnPay(LeaseEntity leaseEntity) {
                // 支付
                signCustomerId = leaseEntity.getMemberId();
                queryPayInfo();
            }

            @Override
            public void onClickBtnEvaluate(LeaseEntity leaseEntity) {
                // 评价
                Intent intent = new Intent(context,EvaluateLeaseActivity.class);
                intent.putExtra("taskId",taskId);
                intent.putExtra("signCustomerId",leaseEntity.getMemberId());
                intent.putExtra("name",leaseEntity.getName());
                startActivityForResult(intent, 1001);
            }

            @Override
            public void call(final LeaseEntity leaseEntity) {
                PopWindowAlert popwindow = new PopWindowAlert(context);
                popwindow.setText(leaseEntity.getPhone());
                popwindow.setOnAlertClickListener(new PopWindowAlert.OnAlertClickListener() {
                    @Override
                    public void onClickConfirm(String paramString) {
                        Utils.callPhone(paramString);
                    }

                    @Override
                    public void onClickOK(String paramString) {

                    }
                });
                popwindow.showAtLocation(lvTask, Gravity.CENTER, 0, 0);
            }

            @Override
            public void onClickCalendar(LeaseEntity leaseEntity) {
                // 日历
                // 确认
                signCustomerId = leaseEntity.getMemberId();
                getCalender(leaseEntity.getMemberId(),leaseEntity.getStatus());
            }

            @Override
            public void onClickBtnArbitrationInfo(LeaseEntity leaseEntity) {
                // 仲裁信息
                signCustomerId = leaseEntity.getMemberId();
                Intent intent = new Intent(context,ArbitrationInfoLeaseActivity.class);
                intent.putExtra("taskId",taskId);
                intent.putExtra("signCustomerId",signCustomerId);
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

    /**
     * 获取任务详情
     */
    private void requestDetails() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        map.put("status",lastPosition== 0?"":lastPosition+"");
        XUtil.Get(UrlUtils.URL_LEASE_DO_TASK_DETAIL_PUBLISHER, map, new MyCallBack<String>() {

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
                    ResultEntity<DoTaskDetailsPublisherEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<DoTaskDetailsPublisherEntity>>() {
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
     * 获取签到日期
     * @param signCustomerId 接包人id
     * @param status 接包人状态
     */
    private void getCalender(String signCustomerId, final String status) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId",taskId);
        map.put("signCustomerId",signCustomerId);
        XUtil.Get(UrlUtils.URL_queryTaskSignByCustomerId, map, new MyCallBack<String>() {

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
                    ResultEntity<List<SignInEntity>> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<List<SignInEntity>>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        signInEntities = new ArrayList<SignInEntity>();
                        for (SignInEntity signInEntity:resultEntity.getResult()){
                            signInEntities.add(signInEntity.clone());
                        }
                        showSignInDialog(resultEntity.getResult(),status);
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取日期列表失败", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
                }

            }
        });
    }

    private void showSignInDialog(final List<SignInEntity> tmp, final String status) {
        DPCManager.getInstance().clearnDATE_CACHE();
        DPCManager.getInstance().setSignInBG(tmp);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        DatePicker2 picker = new DatePicker2(context);
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
                if (!"0".equals(status)){
                    return;
                }
                for(SignInEntity signInEntity: signInEntities){
                    if (date.equals(signInEntity.getDate()) && "1".equals(signInEntity.getType())){
                        confirmSignIn(date,dialog);
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
    private void confirmSignIn(String date, final AlertDialog dialog) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("signTime",getFormateDate(date));
        map.put("signCustomerId",signCustomerId);
        map.put("taskId",taskId);
        XUtil.Get(UrlUtils.URL_updateTaskSignByConfirm, map, new MyCallBack<String>() {

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
                        if (dialog != null){
                            dialog.dismiss();
                        }
                        Toast.makeText(context, "确认签到成功！", Toast.LENGTH_SHORT).show();
                        requestDetails();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "确认签到失败！", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
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
     * 终止
     */
    private void overTask() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("signCustomerId",signCustomerId);
        map.put("taskId",taskId);
        XUtil.Get(UrlUtils.URL_endTaskLease, map, new MyCallBack<String>() {

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
                        if (dialogOver != null){
                            dialogOver.dismiss();
                        }
                        Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT).show();
                        requestDetails();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "操作失败！", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
                }

            }
        });
    }

    /**
     * 查询支付信息
     */
    private void queryPayInfo() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("signCustomerId",signCustomerId);
        map.put("taskId",taskId);
        XUtil.Get(UrlUtils.URL_queryTaskLeasePrice, map, new MyCallBack<String>() {

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
                    ResultEntity<LeasePayInfoEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<LeasePayInfoEntity>>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        if ("0".equals(resultEntity.getResult().getPayAmount())){
                            Toast.makeText(context, "无有效工日，无法支付！", Toast.LENGTH_SHORT).show();
                        }else {
                            showPayDialog(resultEntity.getResult());
                        }
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取支付信息失败！", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
                }

            }
        });
    }

    /**
     * 支付
     */
    private void leasePay(String pwd,String payAmount) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("signCustomerId",signCustomerId);
        map.put("taskId",taskId);
        map.put("tradePassword",pwd);
        map.put("payAmount",payAmount);

        XUtil.Get(UrlUtils.URL_payTaskLease, map, new MyCallBack<String>() {

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
                        Toast.makeText(context, "支付成功！", Toast.LENGTH_SHORT).show();
                        if (payDialog != null){
                            payDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "支付失败！", Toast.LENGTH_SHORT).show();
                } finally {
                    mDialog.dismiss();
                }

            }
        });
    }
    /**
     * 支付dialog
     * @param leasePayInfoEntity 支付信息
     */
    private void showPayDialog(final LeasePayInfoEntity leasePayInfoEntity) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lease_pay, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_sign_in_count = (TextView) view.findViewById(R.id.tv_sign_in_count);
        TextView tv_absent_count = (TextView) view.findViewById(R.id.tv_absent_count);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);

        tv_name.setText(leasePayInfoEntity.getName());
        tv_sign_in_count.setText(leasePayInfoEntity.getSignInCount()+"天");
        tv_absent_count.setText(leasePayInfoEntity.getConfirmCount()+"天");
        tv_price.setText(leasePayInfoEntity.getPrice()+"元/天");
        tv_total.setText(leasePayInfoEntity.getPayAmount()+"元");
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 确认支付接口
                leasePay(et_pwd.getText().toString(),leasePayInfoEntity.getPayAmount());
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payDialog != null)
                {
                    payDialog.dismiss();
                }
            }
        });
        payDialog = new AlertDialog.Builder(context).setView(view).create();
        payDialog.show();
    }

    /**
     * 查询仲裁问题列表
     */
    private void requestArbitrationList() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("SendOrReceive", "接包方".equals(getLoginConfig().getRole()) ? "1" : "0");
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
                        setArbitrationView(arbitrations);
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    /**
     * 显示仲裁dialog
     * @param arbitrations
     */
    private void setArbitrationView(List<String> arbitrations) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_arbitration, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_question);
        final ArbitrationQuestionAdapter arbitrationQuestionAdapter = new ArbitrationQuestionAdapter(context, arbitrations);
        listView.setAdapter(arbitrationQuestionAdapter);
        final EditText et_remark = (EditText) view.findViewById(R.id.et_remark);

        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arbitration(et_remark.getText().toString(), arbitrationQuestionAdapter.questionStr());
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
        arbitrationDialog = new AlertDialog.Builder(context).setView(view).create();
        arbitrationDialog.show();
    }

    /**
     * 仲裁接口
     * @param remark 备注
     * @param questions 问题列表
     */
    private void arbitration(String remark, String questions) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        map.put("remark", remark);
        map.put("taskArbitrateItemList", questions);
        map.put("signCustomerId",signCustomerId);
        XUtil.Post(UrlUtils.URL_arbitrationTaskLease, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "仲裁申请成功", Toast.LENGTH_SHORT).show();
                        if (arbitrationDialog != null) {
                            arbitrationDialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "仲裁申请失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "仲裁申请失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK){
            requestDetails();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
