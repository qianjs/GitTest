package com.clinical.tongxin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
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
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_ENTITY;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_CODE;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_OPTION_ITEM_RESULT_NAME;
import static com.clinical.tongxin.OptionFirstLevelActivity.KEY_TITLE;
import static com.clinical.tongxin.ResumeDescriptionActivity.KEY_DESCRIPTION_TEXT;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;
import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_MOBILE;
import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_WEB;
import static com.clinical.tongxin.ResumeWorkExperienceListActivity.KEY_RESUME_WORK_EXPERIENCE;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeWorkExperienceInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.enterprise_edt)
    EditText mEnterpriseEdt;
    @BindView(R.id.industry_tv)
    TextView mIndustryTv;
    @BindView(R.id.job_type_tv)
    TextView mJobTypeTv;
    @BindView(R.id.job_name_edt)
    EditText mJobNameEdt;
    @BindView(R.id.work_start_time_tv)
    TextView mWorkStartTimeTv;
    @BindView(R.id.work_end_time_tv)
    TextView mWorkEndTimeTv;
    @BindView(R.id.salary_tv)
    TextView mSalaryTv;
    @BindView(R.id.description_tv)
    TextView mDescriptionTv;

    private Unbinder mUnbinder;
    private ResumeEntity.WorkExperience mWorkExperience;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_work_experience_info);
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
            mTitleTv.setText("修改工作经历");
        }else {
            mTitleTv.setText("添加工作经历");
        }

        mEnterpriseEdt.setText(mWorkExperience == null ? "" : mWorkExperience.mEnterprise);
        mIndustryTv.setText(mWorkExperience == null ? "" : mWorkExperience.mIndustry);
        mJobTypeTv.setText(mWorkExperience == null ? "" : mWorkExperience.mJobType);
        mJobNameEdt.setText(mWorkExperience == null ? "" : mWorkExperience.mJobName);
        mWorkStartTimeTv.setText(mWorkExperience == null ? "" : mWorkExperience.mWorkStartTime);
        mWorkEndTimeTv.setText(mWorkExperience == null ? "" : mWorkExperience.mWorkEndTime);
        mSalaryTv.setText(mWorkExperience == null ? "" : mWorkExperience.mSalary);
        mDescriptionTv.setText(mWorkExperience == null ? "" : mWorkExperience.mWorkDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
    }

    private void loadData(){
        if(getIntent() != null) {
            mWorkExperience = getIntent().getParcelableExtra(KEY_RESUME_WORK_EXPERIENCE);
        }

        if(mWorkExperience == null) {
            mWorkExperience = new ResumeEntity.WorkExperience();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.enterprise_layout)
    void onClickEnterpriseLayout(View view){
        mEnterpriseEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.industry_layout)
    void onClickIndustryLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("industry.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "行业名称");
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.job_type_layout)
    void onClickJobTypeLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("funtype.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "职位类别");
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.job_name_layout)
    void onClickJobNameLayout(View view){
        mJobNameEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.work_start_time_layout)
    void onClickWorkStartTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mWorkStartTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mWorkStartTimeTv.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            dp.updateDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(v != null){
                            DatePicker datePicker = (DatePicker) v.findViewById(R.id.date_picker);
                            mWorkExperience.mWorkStartTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mWorkStartTimeTv.setText(mWorkExperience.mWorkStartTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.work_end_time_layout)
    void onClickWorkEndTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mWorkEndTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mWorkEndTimeTv.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            dp.updateDate(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(v != null){
                            DatePicker datePicker = (DatePicker) v.findViewById(R.id.date_picker);
                            mWorkExperience.mWorkEndTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mWorkEndTimeTv.setText(mWorkExperience.mWorkEndTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.salary_layout)
    void onClickSalaryLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("salary.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "职位月薪");
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.description_layout)
    void onClickDescriptionLayout(View view){
        Intent intent = new Intent(this, ResumeDescriptionActivity.class);
        intent.putExtra(KEY_DESCRIPTION_TEXT, mWorkExperience.mWorkDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        intent.putExtra(ResumeDescriptionActivity.KEY_TITLE, "工作描述");
        startActivityForResult(intent, 3);
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTv(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存企业名称、职位名称信息
        mWorkExperience.mEnterprise = mEnterpriseEdt.getText().toString().trim();
        mWorkExperience.mJobName = mJobNameEdt.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("work", getEntityJson().toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_WORK_EXPERIENCE, map, new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                LoadingDialogManager.getInstance().dismiss();
                Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException arg0) {
                super.onCancelled(arg0);
                LoadingDialogManager.getInstance().dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                LoadingDialogManager.getInstance().dismiss();
                try {
                    ResultEntity resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        mWorkExperience.mId = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_WORK_EXPERIENCE, mWorkExperience);
                        setResult(0, intent);
                        ResumeWorkExperienceInfoActivity.this.finish();
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
            result = new JSONObject(gson.toJson(mWorkExperience));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mEnterpriseEdt.getText().toString())){
            Toast.makeText(this, "请输入企业名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mIndustryTv.getText().toString())){
            Toast.makeText(this, "请选择行业名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mJobTypeTv.getText().toString())){
            Toast.makeText(this, "请选择职位类别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mJobNameEdt.getText().toString())){
            Toast.makeText(this, "请输入职位名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mWorkStartTimeTv.getText().toString())){
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mWorkEndTimeTv.getText().toString())){
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(sdf.parse(mWorkEndTimeTv.getText().toString()).before(sdf.parse(mWorkStartTimeTv.getText().toString()))){
                Toast.makeText(this, "结束时间不能大于开始时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "时间格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mSalaryTv.getText().toString().trim())){
            Toast.makeText(this, "请选择职位月薪", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mDescriptionTv.getText().toString())){
            Toast.makeText(this, "请输入工作描述", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //行业类别
            case 0:
                if (data != null) {
                    mWorkExperience.mIndustryId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mWorkExperience.mIndustry = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mIndustryTv.setText(mWorkExperience.mIndustry);
                }
                break;
            //职位类别
            case 1:
                if (data != null) {
                    mWorkExperience.mJobTypeId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mWorkExperience.mJobType = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mJobTypeTv.setText(mWorkExperience.mJobType);
                }
                break;
            //薪资
            case 2:
                if (data != null) {
                    mWorkExperience.mSalaryId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mWorkExperience.mSalary = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mSalaryTv.setText(mWorkExperience.mSalary);
                }
                break;
            //工作描述
            case 3:
                if (data != null) {
                    String description = data.getStringExtra(KEY_DESCRIPTION_TEXT);
                    mWorkExperience.mWorkDescription = description.replace(RETURN_KEY_SYMBOL_MOBILE, RETURN_KEY_SYMBOL_WEB);
                    mDescriptionTv.setText(description);
                }
                break;
        }
    }
}
