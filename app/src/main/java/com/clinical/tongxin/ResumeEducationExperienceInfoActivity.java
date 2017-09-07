package com.clinical.tongxin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import static com.clinical.tongxin.ResumeEducationExperienceListActivity.KEY_RESUME_EDUCATION_EXPERIENCE;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeEducationExperienceInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.university_name_edt)
    EditText mUniversityNameEdt;
    @BindView(R.id.education_start_time_tv)
    TextView mEducationStartTimeTv;
    @BindView(R.id.education_end_time_tv)
    TextView mEducationEndTimeTv;
    @BindView(R.id.recruit_tv)
    TextView mRecruitTv;
    @BindView(R.id.major_tv)
    TextView mMajorTv;
    @BindView(R.id.degree_tv)
    TextView mDegreeTv;

    private Unbinder mUnbinder;
    private ResumeEntity.EducationExperience mEducationExperience;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_education_experience_info);
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
            mTitleTv.setText("修改教育经历");
        }else {
            mTitleTv.setText("添加教育经历");
        }

        mUniversityNameEdt.setText(mEducationExperience.mUniversity);
        mEducationStartTimeTv.setText(mEducationExperience.mEducationStartTime);
        mEducationEndTimeTv.setText(mEducationExperience.mEducationEndTime);
        mRecruitTv.setText(mEducationExperience.mIsRecruit);
        mMajorTv.setText(mEducationExperience.mMajor);
        mDegreeTv.setText(mEducationExperience.mDegree);
    }

    private void loadData(){
        if(getIntent() != null) {
            mEducationExperience = getIntent().getParcelableExtra(KEY_RESUME_EDUCATION_EXPERIENCE);
        }

        if(mEducationExperience == null) {
            mEducationExperience = new ResumeEntity.EducationExperience();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.university_name_layout)
    void onClickUniversityNameLayout(View view){
        mUniversityNameEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.education_start_time_layout)
    void onClickEducationStartTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mEducationStartTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mEducationStartTimeTv.getText().toString().trim());
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
                            mEducationExperience.mEducationStartTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mEducationStartTimeTv.setText(mEducationExperience.mEducationStartTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.education_end_time_layout)
    void onClickEducationEndTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mEducationEndTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mEducationEndTimeTv.getText().toString().trim());
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
                            mEducationExperience.mEducationEndTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mEducationEndTimeTv.setText(mEducationExperience.mEducationEndTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.recruit_layout)
    void onClickRecruitLayout(View view){
        Utils.closeInputMethod(this);
        final String[] items = new String[]{"是","否"};
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEducationExperience.mIsRecruitId = Integer.toString(which + 1);
                        mEducationExperience.mIsRecruit = items[which];
                        mRecruitTv.setText(mEducationExperience.mIsRecruit);
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

    @OnClick(R.id.major_layout)
    void onClickMajorLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("major.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "专业名称");
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.degree_layout)
    void onClickDegreeLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("degree.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "学历/学位");
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTv(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存学校名称信息
        mEducationExperience.mUniversity = mUniversityNameEdt.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("education", getEntityJson().toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_EDUCATION_EXPERIENCE, map, new MyCallBack<String>(){
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
                super.onSuccess(arg0);
                LoadingDialogManager.getInstance().dismiss();
                try {
                    ResultEntity resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        mEducationExperience.mId = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_EDUCATION_EXPERIENCE, mEducationExperience);
                        setResult(0, intent);
                        ResumeEducationExperienceInfoActivity.this.finish();
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
            result = new JSONObject(gson.toJson(mEducationExperience));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mUniversityNameEdt.getText().toString())){
            Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mEducationStartTimeTv.getText().toString())){
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mEducationEndTimeTv.getText().toString())){
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(sdf.parse(mEducationEndTimeTv.getText().toString()).before(sdf.parse(mEducationStartTimeTv.getText().toString()))){
                Toast.makeText(this, "结束时间不能大于开始时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "时间格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mRecruitTv.getText().toString().trim())){
            Toast.makeText(this, "请选择统一招生", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mMajorTv.getText().toString().trim())){
            Toast.makeText(this, "请选择专业名称", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mDegreeTv.getText().toString())){
            Toast.makeText(this, "请选择学历/学位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //专业名称
            case 0:
                if (data != null) {
                    mEducationExperience.mMajorId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mEducationExperience.mMajor = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mMajorTv.setText(mEducationExperience.mMajor);
                }
                break;
            //学历/学位
            case 1:
                if (data != null) {
                    mEducationExperience.mDegreeId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mEducationExperience.mDegree = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mDegreeTv.setText(mEducationExperience.mDegree);
                }
                break;
        }
    }
}
