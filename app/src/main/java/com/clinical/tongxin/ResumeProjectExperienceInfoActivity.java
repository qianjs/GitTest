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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.ResumeDescriptionActivity.KEY_DESCRIPTION_TEXT;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;
import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_MOBILE;
import static com.clinical.tongxin.ResumeInfoActivity.RETURN_KEY_SYMBOL_WEB;
import static com.clinical.tongxin.ResumeProjectExperienceListActivity.KEY_RESUME_PROJECT_EXPERIENCE;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeProjectExperienceInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.project_name_edt)
    EditText mProjectNameEdt;
    @BindView(R.id.project_start_time_tv)
    TextView mProjectStartTimeTv;
    @BindView(R.id.project_end_time_tv)
    TextView mProjectEndTimeTv;
    @BindView(R.id.project_duty_tv)
    TextView mProjectDutyTv;
    @BindView(R.id.description_tv)
    TextView mDescriptionTv;

    private Unbinder mUnbinder;
    private ResumeEntity.ProjectExperience mProjectExperience;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_project_experience_info);
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
            mTitleTv.setText("修改项目经验");
        }else {
            mTitleTv.setText("添加项目经验");
        }

        mProjectNameEdt.setText(mProjectExperience.mProjectName);
        mProjectStartTimeTv.setText(mProjectExperience.mProjectStartTime);
        mProjectEndTimeTv.setText(mProjectExperience.mProjectEndTime);
        mProjectDutyTv.setText(mProjectExperience.mProjectDuty.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        mDescriptionTv.setText(mProjectExperience.mProjectDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
    }

    private void loadData(){
        if(getIntent() != null) {
            mProjectExperience = getIntent().getParcelableExtra(KEY_RESUME_PROJECT_EXPERIENCE);
        }

        if(mProjectExperience == null) {
            mProjectExperience = new ResumeEntity.ProjectExperience();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.project_name_layout)
    void onClickProjectNameLayout(View view){
        mProjectNameEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.project_start_time_layout)
    void onClickProjectStartTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mProjectStartTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mProjectStartTimeTv.getText().toString().trim());
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
                            mProjectExperience.mProjectStartTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mProjectStartTimeTv.setText(mProjectExperience.mProjectStartTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.project_end_time_layout)
    void onClickProjectEndTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mProjectEndTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mProjectEndTimeTv.getText().toString().trim());
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
                            mProjectExperience.mProjectEndTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mProjectEndTimeTv.setText(mProjectExperience.mProjectEndTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.project_duty_layout)
    void onClickProjectDutyLayout(View view){
        Intent intent = new Intent(this, ResumeDescriptionActivity.class);
        intent.putExtra(KEY_DESCRIPTION_TEXT, mProjectExperience.mProjectDuty.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        intent.putExtra(ResumeDescriptionActivity.KEY_TITLE, "项目职责");
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.description_layout)
    void onClickDescriptionLayout(View view){
        Intent intent = new Intent(this, ResumeDescriptionActivity.class);
        intent.putExtra(KEY_DESCRIPTION_TEXT, mProjectExperience.mProjectDescription.replace(RETURN_KEY_SYMBOL_WEB, RETURN_KEY_SYMBOL_MOBILE));
        intent.putExtra(ResumeDescriptionActivity.KEY_TITLE, "项目描述");
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTv(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存企业名称、职位名称信息
        mProjectExperience.mProjectName = mProjectNameEdt.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("project", getEntityJson().toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_PROJECT_EXPERIENCE, map, new MyCallBack<String>(){
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
            public void onFinished() {
                super.onFinished();
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
                        mProjectExperience.mId = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_PROJECT_EXPERIENCE, mProjectExperience);
                        setResult(0, intent);
                        ResumeProjectExperienceInfoActivity.this.finish();
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
            result = new JSONObject(gson.toJson(mProjectExperience));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mProjectNameEdt.getText().toString())){
            Toast.makeText(this, "请输入项目名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mProjectStartTimeTv.getText().toString())){
            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mProjectEndTimeTv.getText().toString())){
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(sdf.parse(mProjectEndTimeTv.getText().toString()).before(sdf.parse(mProjectStartTimeTv.getText().toString()))){
                Toast.makeText(this, "结束时间不能大于开始时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "时间格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mProjectDutyTv.getText().toString().trim())){
            Toast.makeText(this, "请输入项目职责", Toast.LENGTH_SHORT).show();
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
            //项目职责
            case 0:
                if (data != null) {
                    String description = data.getStringExtra(KEY_DESCRIPTION_TEXT);
                    mProjectExperience.mProjectDuty = description.replace(RETURN_KEY_SYMBOL_MOBILE, RETURN_KEY_SYMBOL_WEB);
                    mProjectDutyTv.setText(description);
                }
                break;
            //项目描述
            case 1:
                if (data != null) {
                    String description = data.getStringExtra(KEY_DESCRIPTION_TEXT);
                    mProjectExperience.mProjectDescription = description.replace(RETURN_KEY_SYMBOL_MOBILE, RETURN_KEY_SYMBOL_WEB);
                    mDescriptionTv.setText(description);
                }
                break;
        }
    }
}
