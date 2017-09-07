package com.clinical.tongxin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.OptionItemEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.LoadingDialogManager;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_ENTITY;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_CODE;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_NAME;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_TITLE;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;
import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_SKILL_LIST;
import static com.clinical.tongxin.ResumeSkillListActivity.KEY_RESUME_SKILL;
import static com.clinical.tongxin.ResumeWorkExperienceListActivity.KEY_RESUME_WORK_EXPERIENCE;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeSkillInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.skill_name_edt)
    EditText mSkillNameEdt;
    @BindView(R.id.skill_type_tv)
    TextView mSkillTypeTv;
    @BindView(R.id.use_time_edt)
    EditText mUseTimeEdt;
    @BindView(R.id.mastery_tv)
    TextView mMasteryTv;

    private Unbinder mUnbinder;
    private ResumeEntity.Skill mSkill;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_skill_info);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initData(){
        loadData();
        if(TextUtils.equals(mAddOrUpdateFlag, FLAG_UPDATE)){
            mTitleTv.setText("修改专业技能");
        }else {
            mTitleTv.setText("添加专业技能");
        }

        mSkillNameEdt.setText(mSkill.mSkillName);
        mSkillTypeTv.setText(mSkill.mSkillType);
        mUseTimeEdt.setText(mSkill.mUseTime);
        mMasteryTv.setText(mSkill.mMastery);
    }

    private void loadData(){
        if(getIntent() != null) {
            mSkill = getIntent().getParcelableExtra(KEY_RESUME_SKILL);
        }

        if(mSkill == null) {
            mSkill = new ResumeEntity.Skill();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.skill_name_layout)
    void onClickSkillNameLayout(View view){
        mSkillNameEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.skill_type_layout)
    void onClickSkillTypeLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("skill.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "技能类别");
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.use_time_layout)
    void onClickUseTimeLayout(View view){
        mUseTimeEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.mastery_layout)
    void onClickMasteryLayout(View view){
        Utils.closeInputMethod(this);
        final String[] items = new String[]{"一般", "良好", "熟练", "精通"};
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSkill.mMasteryId = Integer.toString(which + 1);
                        mSkill.mMastery = items[which];
                        mMasteryTv.setText(mSkill.mMastery);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTv(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存技能名称、使用时长信息
        mSkill.mSkillName = mSkillNameEdt.getText().toString().trim();
        mSkill.mUseTime = mUseTimeEdt.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("skills", getEntityJson().toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_SKILL, map, new MyCallBack<String>(){
            @Override
            public void onCancelled(CancelledException arg0) {
                super.onCancelled(arg0);
                LoadingDialogManager.getInstance().dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                LoadingDialogManager.getInstance().dismiss();
                Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0); try {
                    LoadingDialogManager.getInstance().dismiss();
                    ResultEntity resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        mSkill.mId = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_SKILL, mSkill);
                        setResult(0, intent);
                        ResumeSkillInfoActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "返回数据格式错误。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject getEntityJson(){
        JSONObject result = new JSONObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            result = new JSONObject(gson.toJson(mSkill));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mSkillNameEdt.getText().toString())){
            Toast.makeText(this, "请输入技能名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mSkillTypeTv.getText().toString())){
            Toast.makeText(this, "请选择技能类别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mUseTimeEdt.getText().toString())){
            Toast.makeText(this, "请输入使用时长", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mMasteryTv.getText().toString().trim())){
            Toast.makeText(this, "请选择熟练程度", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //技能类别
            case 0:
                if (data != null) {
                    mSkill.mSkillTypeId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mSkill.mSkillType = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mSkillTypeTv.setText(mSkill.mSkillType);
                }
                break;
        }
    }
}
