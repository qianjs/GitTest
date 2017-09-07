package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import static com.xiaomi.push.service.y.u;


/**
 * 团队详情
 *
 * @author LINCHAO
 *         2016/12/27
 */
@ContentView(R.layout.activity_team_details)
public class TeamDetailsActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 更多
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;

    // 团队编号
    @ViewInject(R.id.tv_num)
    TextView tv_num;

    // 团队名称
    @ViewInject(R.id.et_name)
    EditText et_name;

    // 项目经理名称
    @ViewInject(R.id.tv_manager)
    TextView tv_manager;

    // 已完成数量
    @ViewInject(R.id.tv_finish)
    TextView tv_finish;

    // 投诉数
    @ViewInject(R.id.tv_complaint)
    TextView tv_complaint;

    // 资质
    @ViewInject(R.id.tv_aptitude)
    TextView tv_aptitude;
    @ViewInject(R.id.rl_team_aptitude)
    View rl_team_aptitude;
    // 备注
    @ViewInject(R.id.tv_remark)
    TextView tv_remark;

    // 删除团队
    @ViewInject(R.id.tv_team_remove)
    TextView tv_team_remove;

    // 编辑团队
    @ViewInject(R.id.tv_team_edit)
    TextView tv_team_edit;

    // 经理电话
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;

    private View ll_call;
    private String phone;
    private TeamEntity teamEntity;
    private UserEntity userEntity;
    private String jsonAptitude;
    private MyProgressDialog mDialog;
    private static final int CHOOSE_APTITUDE = 1003;
    private String directorGroupId;
    private String managerId;
    private final static int CHOOSE_MANAGER = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initData();
        initView();
        initLister();
        getTeamInfo();
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        directorGroupId = getIntent().getStringExtra("directorGroupId");
    }

    private void initView() {
        ll_call = findViewById(R.id.ll_call);
        mDialog = new MyProgressDialog(this, "请稍后...");
        //setData(teamEntity);
        userEntity = getLoginConfig();
        et_name.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 初始化监听
     */
    private void initLister() {
        tv_team_remove.setOnClickListener(this);
        tv_team_edit.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        ll_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_team_remove:
                // 删除团队
                removeTeam();
                break;
            case R.id.tv_team_edit:
                isEdit(true);
                break;
            case R.id.tv_complete:
                // 修改团队信息
                editTeamInfo();
                break;
            case R.id.ll_call:
                if (!TextUtils.isEmpty(phone)) {
                    Utils.callPhone(phone);
                }
                break;
            case R.id.tv_manager:
                // 项目经理选择
                Intent intent = new Intent(context, ChooseMemberActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, CHOOSE_MANAGER);
                break;
        }
    }

    private void getTeamInfo() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("DirectorGroupId", directorGroupId);

        XUtil.Post(UrlUtils.URL_queryDirectorGroupByManagerCustomerId, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(TeamDetailsActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    ResultEntity<TeamEntity> resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<TeamEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        teamEntity = resultEntity.getResult();
                        setData(teamEntity);
                    } else {
                        Toast.makeText(TeamDetailsActivity.this, "获取团队信息失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(TeamDetailsActivity.this, "获取团队信息失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    private void editTeamInfo() {
        if (TextUtils.isEmpty(et_name.getText())) {
            Toast.makeText(TeamDetailsActivity.this, "团队名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_aptitude.getText())) {
            Toast.makeText(TeamDetailsActivity.this, "资质不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();

        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("DirectorGroupId", teamEntity.getDirectorGroupId());
        map.put("Name", et_name.getText().toString());
        if (!TextUtils.isEmpty(managerId)) {
            map.put("ProjectManagerCustomerId", managerId);
        }
        map.put("AptitudeList", jsonAptitude);
        XUtil.Post(UrlUtils.URL_TEAM_UPDATE, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(TeamDetailsActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                isEdit(false);
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj = new JSONObject(arg0);
                    if (obj.getString("code").equals("200")) {
                        // 修改成功
                        isEdit(false);
                        setResult(Activity.RESULT_OK, new Intent());
                        teamEntity.setName(et_name.getText().toString());
//                        teamEntity.setAptitude(tv_aptitude.getText().toString());
                        Toast.makeText(TeamDetailsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TeamDetailsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(TeamDetailsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    private void removeTeam() {
        mDialog.show();

        Map map = new HashMap<>();
        map.put("CustomerId", userEntity.getUserId());
        map.put("Ukey", userEntity.getUkey());
        map.put("DirectorGroupId", teamEntity.getDirectorGroupId());

        XUtil.Post(UrlUtils.URL_TEAM_DEL, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(TeamDetailsActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj = new JSONObject(arg0);
                    if (obj.getString("code").equals("200")) {
                        // 删除成功
                        Toast.makeText(TeamDetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, new Intent());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {
                        Toast.makeText(TeamDetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TeamDetailsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });
    }


    private void isEdit(boolean editing) {
        if (editing) {
            tv_complete.setVisibility(View.VISIBLE);
            tv_team_edit.setVisibility(View.GONE);
            tv_team_remove.setVisibility(View.GONE);
            et_name.setEnabled(true);
            et_name.setBackgroundResource(R.drawable.btn_red1);
            tv_manager.setBackgroundResource(R.drawable.btn_red1);
            rl_team_aptitude.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转选择资质
                    Intent it = new Intent(TeamDetailsActivity.this, ChooseAptitudeActivity.class);
                    it.putExtra("chooseAptitudes", (Serializable) teamEntity.getAptitudes());
                    startActivityForResult(it, CHOOSE_APTITUDE);
                }
            });
            tv_manager.setOnClickListener(this);
            tv_aptitude.setBackgroundResource(R.drawable.btn_red1);
        } else {
            tv_complete.setVisibility(View.GONE);
            tv_team_edit.setVisibility(View.VISIBLE);
//            tv_team_remove.setVisibility(View.VISIBLE);
            et_name.setEnabled(false);
            et_name.setBackgroundResource(R.drawable.btn_white);
            rl_team_aptitude.setOnClickListener(null);
            tv_manager.setOnClickListener(null);
            tv_aptitude.setBackgroundResource(R.drawable.btn_white);
            tv_manager.setBackgroundResource(R.drawable.btn_white);
        }
    }

    private void setData(TeamEntity teamEntity) {
        tv_title.setText(teamEntity.getName());
        tv_num.setText(teamEntity.getDirectorGroupId());
        et_name.setText(teamEntity.getName());
        et_name.setEnabled(false);
        tv_manager.setText(teamEntity.getProjectManagerCustomerName());
        tv_finish.setText(teamEntity.getFinish());
        tv_complaint.setText(teamEntity.getComplaitCnt());
        tv_aptitude.setText(getData());
        tv_remark.setText(Utils.getMyString(teamEntity.getRemark(), ""));
        tv_phone.setText(TextUtils.isEmpty(phone) ? "" : phone);
        if ("总监".equals(getLoginConfig().getRole())) {
//            tv_team_remove.setVisibility(View.VISIBLE);
            tv_team_edit.setVisibility(View.VISIBLE);
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
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            if (requestCode == CHOOSE_MANAGER && data != null) {
                Bundle bundle = data.getExtras();
                List<MemberEntity> memberEntities = (List<MemberEntity>) bundle.getSerializable("member");
                String name = memberEntities.get(0).getNickName();
                managerId = memberEntities.get(0).getCustomerId();
                tv_manager.setText(name);
            } else if (requestCode == CHOOSE_APTITUDE) {
                String text = data.getStringExtra("text");
                jsonAptitude = data.getStringExtra("json");
                tv_aptitude.setText(text);
                List<AptitudeEntity> chooseAptitudes = new Gson().fromJson(jsonAptitude, new TypeToken<List<AptitudeEntity>>() {
                }.getType());
                teamEntity.setAptitudes(chooseAptitudes);
//                teamEntity.setAptitude(text);
            }
        }
    }

    public String getData() {
        StringBuffer sb = new StringBuffer();
        List<AptitudeEntity> aptitudes = teamEntity.getAptitudes();
        if (aptitudes == null || aptitudes.size() == 0) {
            return "";
        }
        List<String> projects = new ArrayList<>();
        for (AptitudeEntity aptitudeEntity : aptitudes) {
            String project = aptitudeEntity.getProjectTypeName();
            if (!projects.contains(project)) {
                projects.add(project);
            }
        }
        for (String project : projects) {
            sb.append(project).append(":");
            for (AptitudeEntity aptitudeEntity : aptitudes) {
                if (project.equals(aptitudeEntity.getProjectTypeName())) {
                    sb.append(aptitudeEntity.getTaskTypeName()).append(" ");
                }
            }
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf("\n") != -1) {
            String text = sb.toString().substring(0, sb.toString().lastIndexOf("\n") - 1);
            return text;
        }
        return sb.toString();
    }

}
