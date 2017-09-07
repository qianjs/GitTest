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

import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.location.d.j.C;
import static com.clinical.tongxin.R.id.rl_team_aptitude;
import static com.clinical.tongxin.R.id.rl_team_manager;
import static com.clinical.tongxin.R.id.tv_team_create;
import static com.clinical.tongxin.R.id.tv_team_edit;
import static com.clinical.tongxin.R.id.tv_team_remove;


/**
 * 创建团队
 * @author LINCHAO
 * 2016/12/27
 */
@ContentView(R.layout.activity_team_create)
public class TeamCreateActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 更多
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;

    // 团队名称
    @ViewInject(R.id.et_name)
    EditText et_name;

    // 项目经理名称
    @ViewInject(R.id.tv_manager)
    TextView tv_manager;
    @ViewInject(R.id.rl_team_manager)
    View rl_team_manager;


    // 资质
    @ViewInject(R.id.tv_aptitude)
    TextView tv_aptitude;
    @ViewInject(R.id.rl_team_aptitude)
    View rl_team_aptitude;

    // 创建团队
    @ViewInject(R.id.tv_team_create)
    TextView tv_team_create;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;

    private static final int CHOOSE_MANAGER = 1;
    private static final int CHOOSE_APTITUDE = 2;

    private String managerId;
    private String jsonAptitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initView();
        initLister();
    }


    private void initView() {
        mDialog = new MyProgressDialog(this,"请稍后...");
        userEntity = getLoginConfig();
        tv_title.setText("创建团队");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 初始化监听
     */
    private void initLister() {
        tv_team_create.setOnClickListener(this);
        rl_team_aptitude.setOnClickListener(this);
        rl_team_manager.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_team_create:
                // 创建团队
                createTeam();
                break;
            case R.id.rl_team_aptitude:
                // 资质选择
                Intent it = new Intent(TeamCreateActivity.this,ChooseAptitudeActivity.class);
               startActivityForResult(it,CHOOSE_APTITUDE);
                break;
            case R.id.rl_team_manager:
                // 项目经理选择
                Intent intent = new Intent(TeamCreateActivity.this,ChooseMemberActivity.class);
                intent.putExtra("type",1);
                startActivityForResult(intent,CHOOSE_MANAGER);
                break;
        }
    }

    private void createTeam() {
        if (TextUtils.isEmpty(et_name.getText())){
            Toast.makeText(TeamCreateActivity.this, "团队名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_manager.getText())){
            Toast.makeText(TeamCreateActivity.this, "项目经理不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_aptitude.getText())){
            Toast.makeText(TeamCreateActivity.this, "资质不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();

        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("Name",et_name.getText().toString());
        map.put("ProjectManagerCustomerId",managerId);
        map.put("AptitudeList",jsonAptitude);
        XUtil.Post(UrlUtils.URL_TEAM_CREATE, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(TeamCreateActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj=new JSONObject(arg0);
                    if (obj.getString("code").equals("200")){
                        Toast.makeText(TeamCreateActivity.this, "团队已创建，等待项目经理加入", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        setResult(Activity.RESULT_OK,new Intent());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },2000);
                    }else {
                        mDialog.dismiss();
                        Toast.makeText(TeamCreateActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(TeamCreateActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CHOOSE_MANAGER && data != null){
                Bundle bundle = data.getExtras();
                List<MemberEntity> memberEntities = (List<MemberEntity>) bundle.getSerializable("member");
                String name = memberEntities.get(0).getNickName();
                managerId = memberEntities.get(0).getCustomerId();
                tv_manager.setText(name);
            }else if (requestCode == CHOOSE_APTITUDE && data != null){
                String text = data.getStringExtra("text");
                tv_aptitude.setText(text);
                jsonAptitude = data.getStringExtra("json");
            }
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


}
