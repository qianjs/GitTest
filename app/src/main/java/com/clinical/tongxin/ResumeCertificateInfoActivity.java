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

import static com.clinical.tongxin.ResumeCertificateListActivity.KEY_RESUME_CERTIFICATE;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeCertificateInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.certificate_name_edt)
    EditText mCertificateNameEdt;
    @BindView(R.id.certificate_start_time_tv)
    TextView mCertificateStartTimeTv;

    private Unbinder mUnbinder;
    private ResumeEntity.Certificate mCertificate;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_certificate_info);
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
            mTitleTv.setText("修改获得证书");
        }else {
            mTitleTv.setText("添加获得证书");
        }

        mCertificateNameEdt.setText(mCertificate.mCertificateName);
        mCertificateStartTimeTv.setText(mCertificate.mGetTime);
    }

    private void loadData(){
        if(getIntent() != null) {
            mCertificate = getIntent().getParcelableExtra(KEY_RESUME_CERTIFICATE);
        }

        if(mCertificate == null) {
            mCertificate = new ResumeEntity.Certificate();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.certificate_name_layout)
    void onClickCertificateNameLayout(View view){
        mCertificateNameEdt.requestFocus();
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick(R.id.certificate_start_time_layout)
    void onClickCertificateStartTimeLayout(View view){
        final View v = LayoutInflater.from(this).inflate(R.layout.dialog_datepicker, null);
        if(!TextUtils.isEmpty(mCertificateStartTimeTv.getText().toString())){
            DatePicker dp = (DatePicker) v.findViewById(R.id.date_picker);
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mCertificateStartTimeTv.getText().toString().trim());
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
                            mCertificate.mGetTime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            mCertificateStartTimeTv.setText(mCertificate.mGetTime);
                        }
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
        //保存学校名称信息
        mCertificate.mCertificateName = mCertificateNameEdt.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUp", mAddOrUpdateFlag);
        map.put("certificate", getEntityJson().toString());

        XUtil.Post(UrlUtils.URL_POST_RESUME_CERTIFICATE, map, new MyCallBack<String>(){
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
                LoadingDialogManager.getInstance().dismiss(); try {
                    ResultEntity resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity>() {
                    }.getType());
                    if (resultEntity.getCode() == 200) {
                        mCertificate.mId = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_CERTIFICATE, mCertificate);
                        setResult(0, intent);
                        ResumeCertificateInfoActivity.this.finish();
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
            result = new JSONObject(gson.toJson(mCertificate));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mCertificateNameEdt.getText().toString())){
            Toast.makeText(this, "请输入证书名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mCertificateStartTimeTv.getText().toString())){
            Toast.makeText(this, "请选择获得时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
