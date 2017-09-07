package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_INFO;


/**
 * Created by 马骥 on 2016/7/27 0027.
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.ll_task)
    LinearLayout llTask;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    Unbinder unbinder;
    @BindView(R.id.tv_amout)
    TextView tvAmout;
    @BindView(R.id.ll_query)
    LinearLayout llQuery;
    @BindView(R.id.ll_distribution)
    LinearLayout llDistribution;
    @BindView(R.id.ll_my_budget)
    LinearLayout llMyBudget;
    @BindView(R.id.iv_unread)
    ImageView ivUnread;
    @BindView(R.id.img_information)
    ImageView imgInformation;
    private LinearLayout ll_Queries, ll_Phone, ll_Name_confirm, ll_mycase, ll_setting, ll_task_subscibe,ll_my_resume;
    private ImageView img_head;
    private TextView tv_name, txt_phone;
    private String headUrl = "";
    private TextView txt_queryCnt, txt_cmCnt, txt_dmandCnt, txt_auctionCnt, txt_policyCnt, txt_orderCnt, txt_disputeCnt, txt_name_confirm;
    private MyProgressDialog mDialog;
    private Button btn_back_app;
    private ImageView img_setup;
    private RatingBar rb_score;
    private TextView tv_score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }


    private void initView() {
        mDialog = new MyProgressDialog(getActivity(), "请稍等...");
        ll_Queries = (LinearLayout) getActivity().findViewById(R.id.ll_Queries);
        ll_mycase = (LinearLayout) getActivity().findViewById(R.id.ll_mycase);
        ll_setting = (LinearLayout) getActivity().findViewById(R.id.ll_setting);
        ll_task_subscibe = (LinearLayout) getActivity().findViewById(R.id.ll_task_subscibe);
        ll_my_resume = (LinearLayout) getActivity().findViewById(R.id.ll_my_resume);
        if ("接包方".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
            ll_task_subscibe.setVisibility(View.VISIBLE);
            llMyBudget.setVisibility(View.GONE);
            llQuery.setVisibility(View.GONE);
            llDistribution.setVisibility(View.GONE);
        } else {
            ll_task_subscibe.setVisibility(View.GONE);
        }
        if ("总监".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
            ll_Queries.setVisibility(View.GONE);
            llMyBudget.setVisibility(View.GONE);
        }
        if ("项目经理".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
            llDistribution.setVisibility(View.GONE);
            llQuery.setVisibility(View.GONE);
        }
        if ("发包方".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
            llMyBudget.setVisibility(View.GONE);
            llDistribution.setVisibility(View.GONE);
            llQuery.setVisibility(View.GONE);
        }
        ll_Name_confirm = (LinearLayout) getActivity().findViewById(R.id.ll_Name_confirm);
        txt_phone = (TextView) getActivity().findViewById(R.id.txt_phone);
        txt_phone.setText(((BaseActivity) getActivity()).getLoginUserSharedPre().getString("Mobile", ""));
        txt_name_confirm = (TextView) getActivity().findViewById(R.id.txt_name_confirm);
        ll_Name_confirm.setEnabled(true);
        ll_Phone = (LinearLayout) getActivity().findViewById(R.id.ll_Phone);
        btn_back_app = (Button) getActivity().findViewById(R.id.btn_back_app);
        img_setup = (ImageView) getActivity().findViewById(R.id.img_setup);
        img_head = (ImageView) getActivity().findViewById(R.id.img_head);


        float score = 5 * Float.valueOf(((MainActivity) getActivity()).getLoginConfig().getRating()) / Float.valueOf(((MainActivity) getActivity()).getLoginConfig().getRatingType());
        rb_score = (RatingBar) getActivity().findViewById(R.id.rb_score);
        rb_score.setRating(score);
        tv_score = (TextView) getActivity().findViewById(R.id.tv_score);
        tv_score.setText(score + "");
//        ll_Phone.setVisibility(View.GONE);
    }

    private void initListener() {
        ll_Queries.setOnClickListener(this);
        ll_mycase.setOnClickListener(this);
        ll_Phone.setOnClickListener(this);
        ll_Name_confirm.setOnClickListener(this);
        btn_back_app.setOnClickListener(this);
        img_setup.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_task_subscibe.setOnClickListener(this);
        llQuery.setOnClickListener(this);
        llMyBudget.setOnClickListener(this);
        llDistribution.setOnClickListener(this);
        ll_my_resume.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mycase: {
                getActivity().startActivity(new Intent(getActivity(), OrganizationActivity.class));
            }
            break;
            case R.id.btn_back_app: {

                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("linchao", "环信账号退出登录成功");
                        ((BaseActivity) getActivity()).getLoginUserSharedPre().edit().clear().commit();
                        MyApplication.exitapp();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("linchao", "环信账号退出失败");
                        ((BaseActivity) getActivity()).getLoginUserSharedPre().edit().clear().commit();
                        MyApplication.exitapp();
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

            }
            break;
            case R.id.ll_setting: {
                Intent intent = new Intent(getActivity(), UserSetUpActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.ll_Name_confirm: {
                String status = ((BaseActivity) getActivity()).getLoginUserSharedPre().getString("Status", "");
                if (status.equals("0")) { // 申请实名
                    Intent intent = new Intent(getActivity(), NameAuthenticationActivity.class);
                    startActivity(intent);
                } else if (status.equals("1")) { // 设置支付信息
                    Intent intent = new Intent(getActivity(), PaySetUpPwdActivity.class);
                    startActivity(intent);
                } else if (status.equals("2")) {
                    // 等待审核
                } else if (status.equals("3")) {
                    // 初审通过
                } else if (status.equals("4")) { // 初审失败
                    Intent intent = new Intent(getActivity(), ReasonActivity.class);
                    startActivity(intent);
                } else if (status.equals("5")) { // 财务审核通过
                    //Intent intent = new Intent(getActivity(), AuthAptitudeActivity.class);
                    //startActivity(intent);
                } else if (status.equals("6")) { // 财务审核不通过
                    Intent intent = new Intent(getActivity(), ReasonActivity.class);
                    startActivity(intent);
                } else if (status.equals("7")) {
                    Intent intent = new Intent(getActivity(), RealInformationActivity.class);
                    startActivity(intent);
                }
            }
            break;
            case R.id.ll_Queries: {
                //工程资质
                Intent intent = new Intent(getActivity(), QualificationsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.ll_Phone: {
                PopWindowAlert popwindow = new PopWindowAlert(getActivity());
                popwindow.setOnAlertClickListener(new PopWindowAlert.OnAlertClickListener() {
                    @Override
                    public void onClickConfirm(String paramString) {
                        Utils.callPhone(paramString);
                    }

                    @Override
                    public void onClickOK(String paramString) {

                    }
                });
                popwindow.showAtLocation(ll_Phone, Gravity.CENTER, 0, 0);
            }
            break;
            case R.id.ll_task_subscibe:
                startActivity(new Intent(getActivity(), SubscribeActivity.class));
                break;

            case R.id.ll_my_budget: // 我的预算
                getActivity().startActivity(new Intent(getActivity(), BudgetActivity.class));
                break;
            case R.id.ll_query: // 预算查询
                if ("总监".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
                    getActivity().startActivity(new Intent(getActivity(), BudgetQueryActivity.class));
                } else if ("项目经理".equals(((MainActivity) getActivity()).getLoginConfig().getRole())) {
                    getActivity().startActivity(new Intent(getActivity(), ManagerActivity.class));
                }
                break;
            case R.id.ll_distribution: // 预算分配
                getActivity().startActivity(new Intent(getActivity(), DistributionBudgetActivity.class));
                break;
            case R.id.ll_my_resume:
//                getActivity().startActivity(new Intent(getActivity(),ResumeActivity.class));
                mDialog.show();
                Map<String, String> map = new HashMap<>();
                map.put("flag", "android");
                map.put("Uid", ((MainActivity) getActivity()).getLoginConfig().getUserId());
                XUtil.Get(UrlUtils.URL_RESUME, map, new MyCallBack<String>(){
                    @Override
                    public void onSuccess(String arg0) {
                        super.onSuccess(arg0);
                        mDialog.dismiss();

                        ResultEntity<ResumeEntity> resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<ResumeEntity>>(){}.getType());
                        if(resultEntity.getCode() == 200){
                            ResumeEntity resumeEntity = resultEntity.getResult();
                            Intent intent = new Intent(getActivity(), ResumeInfoActivity.class);
                            intent.putExtra(KEY_RESUME_INFO, resumeEntity);
                            getActivity().startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(CancelledException arg0) {
                        super.onCancelled(arg0);
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable arg0, boolean arg1) {
                        super.onError(arg0, arg1);
                        Toast.makeText(getActivity(), arg0.toString(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                        mDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAmount();
        String url = ((BaseActivity) getActivity()).getLoginUserSharedPre().getString("PhotoUrl", "");
//        String name=((BaseActivity) getActivity()).getLoginUserSharedPre().getString("userName", "");
//        if(name.equals(""))
//        {
//            name="未登录";
//        }
//        if(tv_name!=null) {
//            tv_name.setText(name);
//        }
        if (!url.equals(headUrl)) {
            headUrl = url;
            ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL1 + headUrl, img_head, MyApplication.roundedOption);
        }

//        String status = ((BaseActivity) getActivity()).getLoginUserSharedPre().getString("Status", "");
//        if (status.equals("0")) {
//            txt_name_confirm.setText("未认证");
//        } else if (status.equals("1")) {
//            txt_name_confirm.setText("设置支付信息");
//        } else if (status.equals("7")) {
//            txt_name_confirm.setText("已认证");
//        } else if (status.equals("5")) {
//            txt_name_confirm.setText("认证资质");
//        } else if (status.equals("4") || status.equals("6")) {
//            txt_name_confirm.setText("认证失败,点击查看原因");
//        } else {
//            txt_name_confirm.setText("认证中");
//        }
    }


    public void getStatus(String status) {

        if (status.equals("0")) {
            txt_name_confirm.setText("未认证");
        } else if (status.equals("1")) {
            txt_name_confirm.setText("设置支付信息");
        } else if (status.equals("7")) {
            txt_name_confirm.setText("已认证");
        } else if (status.equals("5")) {
            txt_name_confirm.setText("认证资质");
        } else if (status.equals("4") || status.equals("6")) {
            txt_name_confirm.setText("认证失败,点击查看原因");
        } else {
            txt_name_confirm.setText("认证中");
        }
    }

    private void shareApp() {
        UMImage image = new UMImage(getActivity(), "http://www.umeng.com/images/pic/social/integrated_3.png");
        String url = "http://www.umeng.com";
        new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withText("医美镜APP，让美丽如此简单，快来下载吧")
                .withMedia(image)
                .withTargetUrl(url)
                .setCallback(umShareListener)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_group, R.id.ll_task, R.id.ll_data, R.id.tv_amout, R.id.img_information})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_group:
                getActivity().startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            case R.id.ll_task:
                getActivity().startActivity(new Intent(getActivity(), ConversationListActivity.class));
                break;
            case R.id.ll_data:

                break;
            case R.id.tv_amout:
//                Intent intent = new Intent(getActivity(), PropertyActivity.class);
//                startActivity(intent);
                break;
            case R.id.img_information:
                getActivity().startActivity(new Intent(getActivity(), ConversationListActivity.class));
                break;
        }
    }

    private void getAmount() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", ((MainActivity) getActivity()).getLoginConfig().getUserId());
        map.put("Ukey", ((MainActivity) getActivity()).getLoginConfig().getUkey());
        XUtil.Post(UrlUtils.URL_queryCustomer, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(), arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    JSONObject obj = new JSONObject(arg0);
                    if (obj.getString("code").equals("200")) {
                        JSONObject result = new JSONObject(obj.getString("result"));
                        tvAmout.setText(result.getString("amount") + "元 >");
                        ((MainActivity) getActivity()).preferences.edit().putString("Status", result.getString("status")).apply();
                        getStatus(result.getString("status"));

                    } else {
                        Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
//                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
//                    }.getType());
//                    if (resultEntity != null && resultEntity.getCode() == 200) {
//                        Map mapAmout = (Map) resultEntity.getResult();
//                        tvAmout.setText(mapAmout.get("amount") + "元 >");
//                        ((MainActivity)getActivity()).preferences.edit().putString("Status",String.valueOf(mapAmout.get("status"))).apply();
//                        getStatus((String)mapAmout.get("Status"));
//                    } else {
//                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }
            }
        });
    }

    public void setIvUnread(boolean isShow) {
        ivUnread.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
