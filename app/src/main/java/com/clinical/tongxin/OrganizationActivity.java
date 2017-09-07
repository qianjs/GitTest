package com.clinical.tongxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.OrganizationAdapter;
import com.clinical.tongxin.entity.ManagerMemberEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.OrganizationManagerEntity;
import com.clinical.tongxin.entity.OrganizationDirectorEntity;
import com.clinical.tongxin.entity.OrganizationMemberEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
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

import static android.R.attr.start;

/**
 * 组织架构
 * LINCHAO
 * 2017年3月26日10:23:13
 */
public class OrganizationActivity extends BaseActivity {


    @BindView(R.id.lv_member)
    ListView lvMember;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;

    private TextView tvHead;
    private TextView tvInfoName;
    private TextView tvPhone;
    private LinearLayout llCall;
    private TextView tvDelete;
    private AlertDialog dialog;
    private View viewBlank;

    private TextView tvName;
    private TextView tvPosition;
    private LinearLayout llMember;
    private OrganizationAdapter adapter;
    private List<OrganizationManagerEntity> list;
    private OrganizationDirectorEntity organizationDirectorEntity;
    private boolean hasTeam;
    private final static int CHOOSE_MEMBER = 1001;
    private final static int DETAILS_INFO = 1002;

    private MyProgressDialog mDialog;
    private View view_director;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        ButterKnife.bind(this);


        initData();
        getData();
        initListener();
    }

    private void initListener() {

        adapter.setListener(new OrganizationAdapter.OnMemberClickListener() {
            @Override
            public void onManagerClick(OrganizationManagerEntity entity) {
                if (!"接包方".equals(getLoginConfig().getRole())) {
                    Intent intent = new Intent(context, TeamDetailsActivity.class);
                    intent.putExtra("phone", entity.getManagerPhone());
                    intent.putExtra("directorGroupId", entity.getDirectorGroupId());
                    startActivityForResult(intent, DETAILS_INFO);
                } else {
                    if (hasTeam) {
                        showInfo(entity.getManagerName(), entity.getManagerPhone(), false, null);
                    }
                }
            }

            @Override
            public void onMemberClick(OrganizationMemberEntity entity) {
                if ("项目经理".equals(getLoginConfig().getRole()) ||
                        ("接包方".equals(getLoginConfig().getRole()) && !hasTeam)) {
                    showInfo(entity.getName(), entity.getPhone(), true, entity);
                }

            }
        });
    }


    private void showInfo(String name, final String phone, boolean delete, final OrganizationMemberEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View infoView = LayoutInflater.from(context).inflate(R.layout.dialog_manager_info, null);
        tvInfoName = (TextView) infoView.findViewById(R.id.tv_name);
        tvPhone = (TextView) infoView.findViewById(R.id.tv_phone);
        llCall = (LinearLayout) infoView.findViewById(R.id.ll_call);
        tvDelete = (TextView) infoView.findViewById(R.id.tv_delete);
        if (delete) {
            tvDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.GONE);
        }
        tvInfoName.setText(name);
        tvPhone.setText(phone);
        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.callPhone(phone);
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delMember(entity.getId());
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setView(infoView);
        dialog = builder.create();
        dialog.show();
    }

    private void initData() {
        mDialog = new MyProgressDialog(this, "请稍后...");
        title.setText("组织架构");
        ivMore.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        adapter = new OrganizationAdapter(context, list);
        lvMember.setAdapter(adapter);
        View headView = LayoutInflater.from(context).inflate(R.layout.header_organization, null);
        view_director = headView.findViewById(R.id.view_director);
        ImageView imageView = (ImageView) headView.findViewById(R.id.iv_icon);
        imageView.setImageResource(R.mipmap.org_lv1);
        viewBlank = headView.findViewById(R.id.view_blank);
        viewBlank.setVisibility(View.GONE);
        tvName = (TextView) headView.findViewById(R.id.tv_name);
        tvPosition = (TextView) headView.findViewById(R.id.tv_position);
        llMember = (LinearLayout) headView.findViewById(R.id.ll_member);
        llMember.setVisibility(View.GONE);
        lvMember.addHeaderView(headView);
    }

    private void setData() {

//        String json = "{\"directorName\":\"李正军\",\"directorId\":\"123456\",\"directorPosition\":\"项目总监\",\"directorPhone\":\"13355888888\",\"managers\":[{\"managerName\":\"林超\",\"managerId\":\"123\",\"managerPosition\":\"项目经理\",\"managerPhone\":\"13355555555\",\"managerMembers\":[{\"name\":\"张三\",\"phone\":\"13813245678\",\"id\":\"123\"},{\"name\":\"张2\",\"phone\":\"13813245678\",\"id\":\"123\"},{\"name\":\"张5\",\"phone\":\"13813245678\",\"id\":\"123\"}]},{\"managerName\":\"张猛\",\"managerId\":\"123\",\"managerPhone\":\"13355555555\",\"managerMembers\":[{\"name\":\"李三\",\"phone\":\"13813245678\",\"id\":\"123\"},{\"name\":\"张2\",\"phone\":\"13813245678\",\"id\":\"123\"},{\"name\":\"张5\",\"phone\":\"13813245678\",\"id\":\"123\"}]}]}";
//        organizationDirectorEntity = new Gson().fromJson(json, OrganizationDirectorEntity.class);
        adapter.setList(organizationDirectorEntity.getManagers());
        if ("项目经理".equals(getLoginConfig().getRole())) {
            view_director.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInfo(organizationDirectorEntity.getDirectorName(), organizationDirectorEntity.getDirectorPhone(), false, null);
                }
            });
        }
        view_director.setVisibility("接包方".equals(getLoginConfig().getRole()) ? View.GONE : View.VISIBLE);
        tvName.setText(organizationDirectorEntity.getDirectorName());
        tvPosition.setText(organizationDirectorEntity.getDirectorPosition());

    }

    @OnClick({R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                if (hasTeam || "发包方".equals(getLoginConfig().getRole())|| "接包方".equals(getLoginConfig().getRole())) {
                    Toast.makeText(context, "无添加人员权限", Toast.LENGTH_SHORT).show();
                } else {
                    if ("总监".equals(getLoginConfig().getRole())) {
                        startActivity(new Intent(context, TeamCreateActivity.class));
                    } else {
                        Intent intent1 = new Intent(context, ChooseMemberActivity.class);
                        intent1.putExtra("type", 2);
                        startActivityForResult(intent1, CHOOSE_MEMBER);
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_MEMBER) {
                try {
                    List<MemberEntity> memberEntities = (List<MemberEntity>) data.getSerializableExtra("member");
                    addMember(memberEntities);
                } catch (Exception e) {
                    Toast.makeText(context, "添加人员失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else if (requestCode == DETAILS_INFO) {
                getData();
            }
        }
    }

    private void getData() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());

        XUtil.Post("接包方".equals(getLoginConfig().getRole()) ?
                UrlUtils.URL_queryCustomerGroup : UrlUtils.URL_queryDirectorGroup, map, new MyCallBack<String>() {
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
                    ResultEntity<Object> resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        if (TextUtils.isEmpty(resultEntity.getResult().toString())) {
                            Toast.makeText(context, "暂无成员列表", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            return;
                        }
                        if ("接包方".equals(getLoginConfig().getRole())) {
                            ResultEntity<OrganizationManagerEntity> managerEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<OrganizationManagerEntity>>() {
                            }.getType());
                            if (managerEntity.getResult() != null && !managerEntity.getResult().getManagerId().equals(getLoginConfig().getUserId())) {
                                hasTeam = true;
                            }
                            organizationDirectorEntity = new OrganizationDirectorEntity();
                            List<OrganizationManagerEntity> managers = new ArrayList<OrganizationManagerEntity>();
                            managers.add(managerEntity.getResult());
                            organizationDirectorEntity.setManagers(managers);
                        } else {
                            ResultEntity<OrganizationDirectorEntity> directorEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<OrganizationDirectorEntity>>() {
                            }.getType());
                            organizationDirectorEntity = directorEntity.getResult();
                        }
                        setData();
                    } else {
                        Toast.makeText(context, "获取组织机构列表失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(context, "获取组织机构列表失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 添加人员接口
     *
     * @param memberEntities
     */
    private void addMember(List<MemberEntity> memberEntities) {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("CustomerIdList", addMemberIdList(memberEntities));

        XUtil.Post("项目经理".equals(getLoginConfig().getRole()) ? UrlUtils.URL_MANAGER_ADD_MEMBER : UrlUtils.URL_ADD_CONTRACTOR_MEMBER, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultJsonP1 = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>(){}.getType());
                    if (resultJsonP1 != null && resultJsonP1.getCode() == 200) {
                        Toast.makeText(context, "已发送邀请，等待人员确认", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(context, resultJsonP1.getMsg(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "添加人员失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "添加人员失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    /**
     * 转化字符串
     *
     * @param memberEntities
     * @return id
     */
    private String addMemberIdList(List<MemberEntity> memberEntities) {
        StringBuffer sb = new StringBuffer();
        for (MemberEntity memberEntity : memberEntities) {
            sb.append(memberEntity.getCustomerId()).append(",");
        }
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf(",") != -1) {
            String text = sb.toString().substring(0, sb.toString().lastIndexOf(","));
            return text;
        }
        return sb.toString();
    }

    /**
     * 删除成员
     *
     * @param
     */
    private void delMember(String id) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("CustomerIdList", id);

        XUtil.Post("项目经理".equals(getLoginConfig().getRole()) ? UrlUtils.URL_MANAGER_DEL_MEMBER : UrlUtils.URL_DEL_CONTRACTOR_MEMBER,
                map, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String arg0) {
                        super.onSuccess(arg0);
                        try {
                            Gson gson = new Gson();
                           ResultEntity<Object> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<Object>>(){}.getType());
                            if (resultEntity != null && resultEntity.getCode() == 200) {
                                Toast.makeText(context, "删除人员成功", Toast.LENGTH_SHORT).show();
                                getData();
                            } else {
                                Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "删除人员失败", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            e.printStackTrace();
                        }
                        mDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable arg0, boolean arg1) {
                        super.onError(arg0, arg1);
                        Toast.makeText(context, "删除人员失败", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                });
    }


}
