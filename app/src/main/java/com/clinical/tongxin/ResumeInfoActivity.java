package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.LoadingDialogManager;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/17.
 */

public class ResumeInfoActivity extends BaseActivity {
    public final static String KEY_RESUME_INFO = "key_resume_info";
    public final static String KEY_RESUME_BASIC_INFO = "key_resume_basic_info";
    public final static String KEY_RESUME_ADD_OR_UPDATE = "key_resume_add_or_update";
    public final static String KEY_RESUME_HEAD_PHOTO_URL = "key_resume_head_photo_url";
    public final static String KEY_RESUME_WORK_EXPERIENCE_LIST = "key_resume_work_experience_list";
    public final static String KEY_RESUME_PROJECT_EXPERIENCE_LIST = "key_resume_project_experience_list";
    public final static String KEY_RESUME_EDUCATION_EXPERIENCE_LIST = "key_resume_education_experience_list";
    public final static String KEY_RESUME_CERTIFICATE_LIST = "key_resume_certificate_list";
    public final static String KEY_RESUME_SKILL_LIST = "key_resume_skill_list";
    public final static String KEY_RESUME_CERTIFICATE_ATTACHMENT_LIST = "key_resume_certificate_attachment_list";
    public final static String RETURN_KEY_SYMBOL_WEB = "●▽●";
    public final static String RETURN_KEY_SYMBOL_MOBILE = "\n";
    public final static String FLAG_ADD = "1";
    public final static String FLAG_UPDATE = "2";

    private Unbinder mUnbinder;

    private ResumeEntity mResumeEntity;

    private boolean mIsNullBasicInfo = false;

    private boolean mIsNullPhoto = true;

    @BindView(R.id.head_photo_iv)
    ImageView mHeadPhotoIv;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.title)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        loadData();
        mTitleTv.setText("简历");
        if(mResumeEntity != null){
            //如果基本信息为空，将登录信息填写至基本信息中
            if(mResumeEntity.mBasicInfo == null){
                mIsNullBasicInfo = true;
                mResumeEntity.mBasicInfo = new ResumeEntity.BasicInfo();
                mResumeEntity.mBasicInfo.mCustomerName = getLoginConfig().getUserName();
//                mResumeEntity.mBasicInfo.mCustomerName = "测试";
                mResumeEntity.mBasicInfo.mCustomerId = getLoginConfig().getUserId();
            }
            mNameTv.setText(mResumeEntity.mBasicInfo.mCustomerName);
            //默认使用平台头像
            String url = getLoginConfig().getPhotoUrl();
            //使用简历头像
            if(!TextUtils.isEmpty(mResumeEntity.mHeadPhotoUrl)){
                url = UrlUtils.BASE_URL + mResumeEntity.mHeadPhotoUrl;
                mIsNullPhoto = false;
            }
            if(!TextUtils.isEmpty(url)) {
                Glide.with(this)
                        .load(url)
                        .placeholder(R.mipmap.head)
                        .into(mHeadPhotoIv);
            }
        }
    }

    private void loadData(){
        if(getIntent() != null) {
            mResumeEntity = getIntent().getParcelableExtra(KEY_RESUME_INFO);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.head_photo_iv)
    void onClickHeadPhotoImageView(View view){
        //打开图片选择
        Intent intent = new Intent(this, SelectCameraDialog.class);
        Bundle bundleObject = new Bundle();
        GoClass myclass = new GoClass();
        myclass.setGoToClass(ResumeInfoActivity.class);
        bundleObject.putSerializable("myclass", myclass);
        intent.putExtras(bundleObject);
        startActivity(intent);
    }

    @OnClick(R.id.preview_btn)
    void onClickPreviewButton(View view){
        Intent intent = new Intent(this, ResumePreviewActivity.class);
        intent.putExtra(KEY_RESUME_INFO, mResumeEntity);
        startActivity(intent);
    }

    @OnClick(R.id.basic_info_layout)
    void onClickBasicInfoLayout(View view){
//        LoadingDialogManager.getInstance().show(this);
//        Map<String, String> map = new HashMap<>();
//        map.put("flag", "android");
//        map.put("Uid", "8");
//        XUtil.Get(UrlUtils.URL_GET_RESUME_BASIC_INFO, map, new MyCallBack<String>(){
//            @Override
//            public void onError(Throwable arg0, boolean arg1) {
//                super.onError(arg0, arg1);
//                LoadingDialogManager.getInstance().dismiss();
//                Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(String arg0) {
//                super.onSuccess(arg0);
//                LoadingDialogManager.getInstance().dismiss();
//                try {
//                    ResultEntity<ResumeEntity.BasicInfo> resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity<ResumeEntity.BasicInfo>>() {
//                    }.getType());
//                    if(resultEntity.getCode() == 200) {
//                        mResumeEntity.mBasicInfo = resultEntity.getResult();
//                        Intent intent = new Intent(ResumeInfoActivity.this, ResumeBasicInfoActivity.class);
//                        intent.putExtra(KEY_RESUME_BASIC_INFO, mResumeEntity.mBasicInfo);
//                        intent.putExtra(KEY_RESUME_ADD_OR_UPDATE, mIsNullBasicInfo ? "1" : "2");
//                        intent.putExtra(KEY_RESUME_HEAD_PHOTO_URL, mResumeEntity.mHeadPhotoUrl);
//                        startActivity(intent);
////                        startActivityForResult(intent, 0);
//                    }else{
//                        Toast.makeText(getApplicationContext(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "返回数据格式错误", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        Intent intent = new Intent(ResumeInfoActivity.this, ResumeBasicInfoActivity.class);
        intent.putExtra(KEY_RESUME_BASIC_INFO, mResumeEntity.mBasicInfo);
        intent.putExtra(KEY_RESUME_ADD_OR_UPDATE, mIsNullBasicInfo ? "1" : "2");
        intent.putExtra(KEY_RESUME_HEAD_PHOTO_URL, mResumeEntity.mHeadPhotoUrl);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.work_experience_layout)
    void onClickWorkExperienceLayout(View view){
        Intent intent = new Intent(this, ResumeWorkExperienceListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_WORK_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mWorkExperiences);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.project_experience_layout)
    void onClickProjectExperienceLayout(View view){
        Intent intent = new Intent(this, ResumeProjectExperienceListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mProjectExperiences);
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.education_experience_layout)
    void onClickEducationExperienceLayout(View view){
        Intent intent = new Intent(this, ResumeEducationExperienceListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_EDUCATION_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mEducationExperiences);
        startActivityForResult(intent, 3);
    }

    @OnClick(R.id.certificate_layout)
    void onClickCertificateLayout(View view){
        Intent intent = new Intent(this, ResumeCertificateListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mCertificates);
        startActivityForResult(intent, 4);
    }

    @OnClick(R.id.professional_skill_layout)
    void onClickProfessionalSkillLayout(View view){
        Intent intent = new Intent(this, ResumeSkillListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_SKILL_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mSkills);
        startActivityForResult(intent, 5);
    }

    @OnClick(R.id.certificate_attachment_layout)
    void onClickCertificateAttachmentLayout(View view){
        Intent intent = new Intent(this, ResumeCertificateAttachmentListActivity.class);
        intent.putParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_ATTACHMENT_LIST, (ArrayList<? extends Parcelable>) mResumeEntity.mCertificateAttachments);
        startActivityForResult(intent, 6);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(data != null){
                    mResumeEntity.mBasicInfo = data.getParcelableExtra(KEY_RESUME_BASIC_INFO);
                }
                break;
            case 1:
                if(data != null){
                    mResumeEntity.mWorkExperiences = data.getParcelableArrayListExtra(KEY_RESUME_WORK_EXPERIENCE_LIST);
                }
                break;
            case 2:
                if(data != null){
                    mResumeEntity.mProjectExperiences = data.getParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST);
                }
                break;
            case 3:
                if(data != null){
                    mResumeEntity.mEducationExperiences = data.getParcelableArrayListExtra(KEY_RESUME_EDUCATION_EXPERIENCE_LIST);
                }
                break;
            case 4:
                if(data != null){
                    mResumeEntity.mCertificates = data.getParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST);
                }
                break;
            case 5:
                if(data != null){
                    mResumeEntity.mSkills = data.getParcelableArrayListExtra(KEY_RESUME_SKILL_LIST);
                }
                break;
            case 6:
                if(data != null){
                    mResumeEntity.mCertificateAttachments = data.getParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_ATTACHMENT_LIST);
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String strpath = "";
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            strpath= Utils.getMyString(intent.getExtras().getString("path"),"");
            ImageLoader.getInstance().displayImage("file://"+strpath, mHeadPhotoIv, MyApplication.normalOption);
            mResumeEntity.mHeadPhotoUrl = strpath;
            saveHeadPhoto();
        }
    }

    private void saveHeadPhoto(){

        LoadingDialogManager.getInstance().show(this);

        Map<String,Object> map = new HashMap<>();
        map.put("flag", "android");
        map.put("Uid", getLoginConfig().getUserId());
        String realName = mResumeEntity.mBasicInfo.mCustomerName;
        try {
            realName = new String(mResumeEntity.mBasicInfo.mCustomerName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("realName", realName);
        map.put("flagAddOrUpPhoto", mIsNullPhoto ? "1" : "2");
        map.put("fileUrl",new File(mResumeEntity.mHeadPhotoUrl));

        XUtil.UpLoadFileAndText(UrlUtils.URL_POST_RESUME_HEAD_PHOTO,map,new MyCallBack<String>(){
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
                        mResumeEntity.mHeadPhotoUrl = resultEntity.getMsg();
                        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
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
}
