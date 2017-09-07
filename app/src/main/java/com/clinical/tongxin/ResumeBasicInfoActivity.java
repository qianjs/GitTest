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
import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_ADD_OR_UPDATE;
import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_BASIC_INFO;
import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_HEAD_PHOTO_URL;

/**
 * Created by qjs on 2017/8/17.
 */

public class ResumeBasicInfoActivity extends BaseActivity {
    private Unbinder mUnbinder;

    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.sex_tv)
    TextView mSexTv;
    @BindView(R.id.birthday_tv)
    TextView mBirthdayTv;
    @BindView(R.id.household_tv)
    TextView mHouseholdTv;
    @BindView(R.id.work_start_time_tv)
    TextView mWorkStartTimeTv;
    @BindView(R.id.live_city_tv)
    TextView mLiveCityTv;
    @BindView(R.id.mobile_edt)
    EditText mMobileEdt;
    @BindView(R.id.email_edt)
    EditText mEmailEdt;
    @BindView(R.id.marry_tv)
    TextView mMarryTv;
    @BindView(R.id.country_tv)
    TextView mCountryTv;
    @BindView(R.id.political_status_tv)
    TextView mPoliticalStatusTv;
    @BindView(R.id.title)
    TextView mTitleTv;

    private ResumeEntity.BasicInfo mBasicInfo;

    private String mAddOrUpdateFlag = "1";
    private String mHeadPhotoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_basic_info);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void loadData(){
        if(getIntent() != null) {
            mBasicInfo = getIntent().getParcelableExtra(KEY_RESUME_BASIC_INFO);
            mAddOrUpdateFlag = getIntent().getStringExtra(KEY_RESUME_ADD_OR_UPDATE);
            mHeadPhotoUrl = getIntent().getStringExtra(KEY_RESUME_HEAD_PHOTO_URL);
        }
    }
    private void initData(){
        loadData();
        mTitleTv.setText("基本信息");
        if(mBasicInfo != null){
            mNameTv.setText(mBasicInfo.mCustomerName);
            mSexTv.setText(mBasicInfo.mSex);
            mBirthdayTv.setText(mBasicInfo.mBirthday);
            mHouseholdTv.setText(mBasicInfo.mHousehold);
            mWorkStartTimeTv.setText(mBasicInfo.mWorkStartTime);
            mLiveCityTv.setText(mBasicInfo.mLiveCity);
            mMobileEdt.setText(mBasicInfo.mMobile);
            mEmailEdt.setText(mBasicInfo.mEmail);
            mMarryTv.setText(mBasicInfo.mMarry);
            mCountryTv.setText(mBasicInfo.mCountry);
            mPoliticalStatusTv.setText(mBasicInfo.mPoliticalStatus);
        }
    }

    @OnClick(R.id.sex_layout)
    void onClickSexLayout(View view){
        Utils.closeInputMethod(this);
        final String[] items = new String[]{"男","女"};
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBasicInfo.mSexId = Integer.toString(which + 1);
                        mBasicInfo.mSex = items[which];
                        mSexTv.setText(mBasicInfo.mSex);
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

    @OnClick(R.id.birthday_layout)
    void onClickBirthdayLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
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
                            mBasicInfo.mBirthday = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mBirthdayTv.setText(mBasicInfo.mBirthday);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.household_layout)
    void onClickHouseHoldLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("resume_city.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "户口所在地");
        startActivityForResult(intent, 0);
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
                            mBasicInfo.mWorkStartTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mWorkStartTimeTv.setText(mBasicInfo.mWorkStartTime);
                        }
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.live_city_layout)
    void onClickLiveCityLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("resume_city.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "居住城市");
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.mobile_layout)
    void onClickMobileLayout(View view){
        mMobileEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.email_layout)
    void onClickEmailLayout(View view){
        mEmailEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.marry_layout)
    void onClickMarryLayout(View view){
        Utils.closeInputMethod(this);
        final String[] items = new String[]{"未婚","已婚"};
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBasicInfo.mMarryId = Integer.toString(which + 1);
                        mBasicInfo.mMarry = items[which];
                        mMarryTv.setText(mBasicInfo.mMarry);
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

    @OnClick(R.id.country_layout)
    void onClickCountryLayout(View view){
        ArrayList<OptionItemEntity> optionItemEntities = new Gson().fromJson(Utils.getAssetsFileText("nationality.json"), new TypeToken<List<OptionItemEntity>>(){}.getType());
        Intent intent = new Intent(this, OptionFirstLevelActivity.class);
        intent.putParcelableArrayListExtra(KEY_OPTION_ITEM_ENTITY, optionItemEntities);
        intent.putExtra(KEY_TITLE, "国籍");
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.political_status_layout)
    void onClickPoliticalStatusLayout(View view){
        Utils.closeInputMethod(this);
        final String[] items = new String[]{"中共党员(含预备党员)","团员","群众","民族党派","无党派人士"};

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBasicInfo.mPoliticalStatusId = Integer.toString(which + 1);
                        mBasicInfo.mPoliticalStatus = items[which];
                        mPoliticalStatusTv.setText(mBasicInfo.mPoliticalStatus);
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
    void onClickSaveMenu(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存Mobile、Email信息
        mBasicInfo.mMobile = mMobileEdt.getText().toString().trim();
        mBasicInfo.mEmail = mEmailEdt.getText().toString().trim();

        JSONObject jsonObject = getBasicInfoJson();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", mBasicInfo.mCustomerId);
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("picUrl", mHeadPhotoUrl == null ? "" : mHeadPhotoUrl);
        map.put("basic", jsonObject.toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_BASIC_INFO, map, new MyCallBack<String>(){
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
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                //更新基本信息
                Intent intent = new Intent();
                intent.putExtra(KEY_RESUME_BASIC_INFO, mBasicInfo);
                setResult(0, intent);
                ResumeBasicInfoActivity.this.finish();
            }
        });
    }

    private JSONObject getBasicInfoJson(){
        JSONObject result = new JSONObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            result = new JSONObject(gson.toJson(mBasicInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mSexTv.getText().toString())){
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mBirthdayTv.getText().toString())){
            Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mHouseholdTv.getText().toString())){
            Toast.makeText(this, "请选择户口所在地", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mWorkStartTimeTv.getText().toString())){
            Toast.makeText(this, "请选择参加工作时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mLiveCityTv.getText().toString())){
            Toast.makeText(this, "请选择居住城市", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mMobileEdt.getText().toString())){
            Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Utils.checkMobileNumber(mMobileEdt.getText().toString().trim())){
            Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mEmailEdt.getText().toString())){
            Toast.makeText(this, "请输入电子邮箱", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Utils.checkEmail(mEmailEdt.getText().toString().trim())){
            Toast.makeText(this, "电子邮箱格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mMarryTv.getText().toString())){
            Toast.makeText(this, "请选择婚姻状况", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mCountryTv.getText().toString())){
            Toast.makeText(this, "请选择国籍", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mPoliticalStatusTv.getText().toString())){
            Toast.makeText(this, "请选择政治面貌", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data != null) {
                    mBasicInfo.mHouseholdId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mBasicInfo.mHousehold = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mHouseholdTv.setText(mBasicInfo.mHousehold);
                }
                break;
            case 1:
                if (data != null) {
                    mBasicInfo.mLiveCityId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mBasicInfo.mLiveCity = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mLiveCityTv.setText(mBasicInfo.mLiveCity);
                }
                break;
            case 2:
                if (data != null) {
                    mBasicInfo.mCountryId = data.getStringExtra(KEY_OPTION_ITEM_RESULT_CODE);
                    mBasicInfo.mCountry = data.getStringExtra(KEY_OPTION_ITEM_RESULT_NAME);
                    mCountryTv.setText(mBasicInfo.mCountry);
                }
                break;
        }
    }
}