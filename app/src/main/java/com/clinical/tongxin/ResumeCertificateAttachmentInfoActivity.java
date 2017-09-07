package com.clinical.tongxin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.OptionItemEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.LoadingDialogManager;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
import static com.clinical.tongxin.ResumeCertificateAttachmentListActivity.KEY_RESUME_CERTIFICATE_ATTACHMENT;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_ADD;
import static com.clinical.tongxin.ResumeInfoActivity.FLAG_UPDATE;
import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_CERTIFICATE_ATTACHMENT_LIST;
import static com.clinical.tongxin.ResumeSkillListActivity.KEY_RESUME_SKILL;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeCertificateAttachmentInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.description_edt)
    EditText mDescriptionEdt;
    @BindView(R.id.image_iv)
    ImageView mImageIv;

    private Unbinder mUnbinder;
    private ResumeEntity.CertificateAttachment mCertificateAttachment;
    private String mAddOrUpdateFlag = FLAG_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_certificate_attachment_info);
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
            mTitleTv.setText("修改证书附件");
        }else {
            mTitleTv.setText("添加证书附件");
        }

        mDescriptionEdt.setText(mCertificateAttachment.mDescription);
        Glide.with(this).load(mCertificateAttachment.mUrl)
                .placeholder(R.mipmap.em_smiley_add_btn_pressed)
                .into(mImageIv);
    }

    private void loadData(){
        if(getIntent() != null) {
            mCertificateAttachment = getIntent().getParcelableExtra(KEY_RESUME_CERTIFICATE_ATTACHMENT);
        }

        if(mCertificateAttachment == null) {
            mCertificateAttachment = new ResumeEntity.CertificateAttachment();
            mAddOrUpdateFlag = FLAG_ADD;
        }else{
            mAddOrUpdateFlag = FLAG_UPDATE;
        }
    }

    @OnClick(R.id.description_layout)
    void onClickDescriptionLayout(View view){
        mDescriptionEdt.requestFocus();
        Utils.showInputMethod(this);
    }

    @OnClick(R.id.image_iv)
    void onClickImageImageView(View view){
        //打开图片选择
        Intent intent = new Intent(this, SelectCameraDialog.class);
        Bundle bundleObject = new Bundle();
        GoClass myclass = new GoClass();
        myclass.setGoToClass(ResumeCertificateAttachmentInfoActivity.class);
        bundleObject.putSerializable("myclass", myclass);
        intent.putExtras(bundleObject);
        startActivity(intent);
    }

    @OnClick(R.id.menu_tv)
    void onClickMenuTv(View view){
        //校验输入数据是否完整
        if(!checkInputCompleted()){
            return;
        }

        LoadingDialogManager.getInstance().show(this);
        //保存附件描述信息
        mCertificateAttachment.mDescription = mDescriptionEdt.getText().toString().trim();

        Map<String,Object> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        map.put("flagAddOrUpAttachment", "1");
        String description = mCertificateAttachment.mDescription;
        try {
            description = new String(mCertificateAttachment.mDescription.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("describe", description);
        map.put("fileUrl",new File(mCertificateAttachment.mUrl));

        XUtil.UpLoadFileAndText(UrlUtils.URL_POST_RESUME_CERTIFICATE_ATTACHMENT,map,new MyCallBack<String>(){
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
                    Gson gson = new Gson();
                    ResultEntity resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        String[] strs = resultEntity.getMsg().split("_");
                        if(strs.length > 0){
                            mCertificateAttachment.mId = strs[0];
                        }
                        if(strs.length > 1){
                            mCertificateAttachment.mUrl = strs[1];
                        }
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                        //更新基本信息
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESUME_CERTIFICATE_ATTACHMENT, mCertificateAttachment);
                        setResult(0, intent);
                        ResumeCertificateAttachmentInfoActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "返回数据格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject getEntityJson(){
        JSONObject result = new JSONObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            result = new JSONObject(gson.toJson(mCertificateAttachment));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean checkInputCompleted(){
        if(TextUtils.isEmpty(mDescriptionEdt.getText().toString())){
            Toast.makeText(this, "请输入附件描述", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mCertificateAttachment.mUrl)){
            Toast.makeText(this, "请添加附件图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String strpath = "";
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            strpath= Utils.getMyString(intent.getExtras().getString("path"),"");
            ImageLoader.getInstance().displayImage("file://"+strpath, mImageIv, MyApplication.normalOption);
            mCertificateAttachment.mUrl = strpath;
        }
    }
}
