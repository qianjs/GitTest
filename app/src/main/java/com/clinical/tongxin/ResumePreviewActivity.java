package com.clinical.tongxin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinical.tongxin.adapter.ResumePreviewCertificateAttachmentAdapter;
import com.clinical.tongxin.adapter.ResumePreviewCertificateExperienceAdapter;
import com.clinical.tongxin.adapter.ResumePreviewEducationExperienceAdapter;
import com.clinical.tongxin.adapter.ResumePreviewPlatformExperienceAdapter;
import com.clinical.tongxin.adapter.ResumePreviewProjectExperienceAdapter;
import com.clinical.tongxin.adapter.ResumePreviewSkillAdapter;
import com.clinical.tongxin.adapter.ResumePreviewWorkExperienceAdapter;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.myview.RecyclerView;
import com.clinical.tongxin.util.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_INFO;

/**
 * Created by qjs on 2017/9/1.
 */

public class ResumePreviewActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitleTv;
    @BindView(R.id.head_photo_iv)
    ImageView mHeadPhotoIv;
    @BindView(R.id.name_value_tv)
    TextView mNameValueTv;
    @BindView(R.id.sex_value_tv)
    TextView mSexValueTv;
    @BindView(R.id.birthday_value_tv)
    TextView mBirthdayValueTv;
    @BindView(R.id.household_value_tv)
    TextView mHouseholdValueTv;
    @BindView(R.id.work_start_time_value_tv)
    TextView mWorkStartTimeValueTv;
    @BindView(R.id.live_city_value_tv)
    TextView mLiveCityValueTv;
    @BindView(R.id.mobile_value_tv)
    TextView mMobileValueTv;
    @BindView(R.id.email_value_tv)
    TextView mEmailValueTv;
    @BindView(R.id.marry_value_tv)
    TextView mMarryValueTv;
    @BindView(R.id.country_value_tv)
    TextView mCountryValueTv;
    @BindView(R.id.political_status_value_tv)
    TextView mPoliticalStatusValueTv;
    @BindView(R.id.work_experience_layout)
    RelativeLayout mWorkExperienceLayout;
    @BindView(R.id.work_experience_list_view)
    RecyclerView mWorkExperienceListView;
    @BindView(R.id.project_experience_layout)
    RelativeLayout mProjectExperienceLayout;
    @BindView(R.id.project_experience_list_view)
    RecyclerView mProjectExperienceListView;
    @BindView(R.id.education_experience_layout)
    RelativeLayout mEducationExperienceLayout;
    @BindView(R.id.education_experience_list_view)
    RecyclerView mEducationExperienceListView;
    @BindView(R.id.certificate_layout)
    RelativeLayout mCertificateLayout;
    @BindView(R.id.certificate_list_view)
    RecyclerView mCertificateListView;
    @BindView(R.id.skill_layout)
    RelativeLayout mSkillLayout;
    @BindView(R.id.skill_list_view)
    RecyclerView mSkillListView;
    @BindView(R.id.certificate_attachment_layout)
    RelativeLayout mCertificateAttachmentLayout;
    @BindView(R.id.certificate_attachment_list_view)
    RecyclerView mCertificateAttachmentListView;
    @BindView(R.id.platform_experience_layout)
    RelativeLayout mPlatformExperienceLayout;
    @BindView(R.id.platform_experience_list_view)
    RecyclerView mPlatformExperienceListView;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    private Unbinder mUnbinder;
    private ResumeEntity mResumeEntity;
    private ResumePreviewWorkExperienceAdapter mWorkExperienceAdapter;
    private ResumePreviewProjectExperienceAdapter mProjectExperienceAdapter;
    private ResumePreviewEducationExperienceAdapter mEducationExperienceAdapter;
    private ResumePreviewCertificateExperienceAdapter mCertificateAdapter;
    private ResumePreviewSkillAdapter mSkillAdapter;
    private ResumePreviewCertificateAttachmentAdapter mCertificateAttachmentAdapter;
    private ResumePreviewPlatformExperienceAdapter mPlatformExperienceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_preview);
        mUnbinder = ButterKnife.bind(this);
        initData();
        //设置焦点，使ScrollView滚动至顶部
        mScrollView.smoothScrollTo(0, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initData(){
        loadData();
        mTitleTv.setText("预览简历");

        initBasicInfo();
        initWorkExperienceList();
        initProjectExperienceList();
        initEducationExperienceList();
        initCertificateList();
        initSkillList();
        initCertificateAttachmentList();
        initPlatformExperienceList();
    }

    private void loadData(){
        if(getIntent() != null && getIntent().getParcelableExtra(KEY_RESUME_INFO) != null){
            mResumeEntity = getIntent().getParcelableExtra(KEY_RESUME_INFO);
        }
        if(mResumeEntity == null){
            mResumeEntity = new ResumeEntity();
        }
    }

    private void initBasicInfo(){
        Glide.with(this).load(UrlUtils.BASE_URL + mResumeEntity.mHeadPhotoUrl).placeholder(R.mipmap.head).into(mHeadPhotoIv);
        mNameValueTv.setText(mResumeEntity.mBasicInfo.mCustomerName);
        mSexValueTv.setText(mResumeEntity.mBasicInfo.mSex);
        mBirthdayValueTv.setText(mResumeEntity.mBasicInfo.mBirthday);
        mHouseholdValueTv.setText(mResumeEntity.mBasicInfo.mHousehold);
        mWorkStartTimeValueTv.setText(mResumeEntity.mBasicInfo.mWorkStartTime);
        mLiveCityValueTv.setText(mResumeEntity.mBasicInfo.mLiveCity);
        mMobileValueTv.setText(mResumeEntity.mBasicInfo.mMobile);
        mEmailValueTv.setText(mResumeEntity.mBasicInfo.mEmail);
        mMarryValueTv.setText(mResumeEntity.mBasicInfo.mMarry);
        mCountryValueTv.setText(mResumeEntity.mBasicInfo.mCountry);
        mPoliticalStatusValueTv.setText(mResumeEntity.mBasicInfo.mPoliticalStatus);
    }

    private void initWorkExperienceList(){
        if(mResumeEntity.mWorkExperiences == null || mResumeEntity.mWorkExperiences.size() == 0){
            mWorkExperienceLayout.setVisibility(View.GONE);
        }else {
            mWorkExperienceLayout.setVisibility(View.VISIBLE);
            mWorkExperienceAdapter = new ResumePreviewWorkExperienceAdapter(this);
            mWorkExperienceAdapter.setData(mResumeEntity.mWorkExperiences);
            mWorkExperienceListView.setLayoutManager(new LinearLayoutManager(this));
            mWorkExperienceListView.setAdapter(mWorkExperienceAdapter);
        }
    }

    private void initProjectExperienceList(){
        if(mResumeEntity.mProjectExperiences == null || mResumeEntity.mProjectExperiences.size() == 0){
            mProjectExperienceLayout.setVisibility(View.GONE);
        }else {
            mProjectExperienceLayout.setVisibility(View.VISIBLE);
            mProjectExperienceAdapter = new ResumePreviewProjectExperienceAdapter(this);
            mProjectExperienceAdapter.setData(mResumeEntity.mProjectExperiences);
            mProjectExperienceListView.setLayoutManager(new LinearLayoutManager(this));
            mProjectExperienceListView.setAdapter(mProjectExperienceAdapter);
        }
    }

    private void initEducationExperienceList(){
        if(mResumeEntity.mEducationExperiences == null || mResumeEntity.mEducationExperiences.size() == 0){
            mEducationExperienceLayout.setVisibility(View.GONE);
        }else {
            mEducationExperienceLayout.setVisibility(View.VISIBLE);
            mEducationExperienceAdapter = new ResumePreviewEducationExperienceAdapter(this);
            mEducationExperienceAdapter.setData(mResumeEntity.mEducationExperiences);
            mEducationExperienceListView.setLayoutManager(new LinearLayoutManager(this));
            mEducationExperienceListView.setAdapter(mEducationExperienceAdapter);
        }
    }

    private void initCertificateList(){
        if(mResumeEntity.mCertificates == null || mResumeEntity.mCertificates.size() == 0){
            mCertificateLayout.setVisibility(View.GONE);
        }else {
            mCertificateLayout.setVisibility(View.VISIBLE);
            mCertificateAdapter = new ResumePreviewCertificateExperienceAdapter(this);
            mCertificateAdapter.setData(mResumeEntity.mCertificates);
            mCertificateListView.setLayoutManager(new LinearLayoutManager(this));
            mCertificateListView.setAdapter(mCertificateAdapter);
        }
    }

    private void initSkillList(){
        if(mResumeEntity.mSkills == null || mResumeEntity.mSkills.size() == 0){
            mSkillLayout.setVisibility(View.GONE);
        }else {
            mSkillLayout.setVisibility(View.VISIBLE);
            mSkillAdapter = new ResumePreviewSkillAdapter(this);
            mSkillAdapter.setData(mResumeEntity.mSkills);
            mSkillListView.setLayoutManager(new LinearLayoutManager(this));
            mSkillListView.setAdapter(mSkillAdapter);
        }
    }

    private void initCertificateAttachmentList(){
        if(mResumeEntity.mCertificateAttachments == null || mResumeEntity.mCertificateAttachments.size() == 0){
            mCertificateAttachmentLayout.setVisibility(View.GONE);
        }else {
            mCertificateAttachmentLayout.setVisibility(View.VISIBLE);
            mCertificateAttachmentAdapter = new ResumePreviewCertificateAttachmentAdapter(this);
            mCertificateAttachmentAdapter.setData(mResumeEntity.mCertificateAttachments);
            mCertificateAttachmentListView.setLayoutManager(new LinearLayoutManager(this));
            mCertificateAttachmentListView.setAdapter(mCertificateAttachmentAdapter);
        }
    }

    private void initPlatformExperienceList(){
        if(mResumeEntity.mPlatformProjectExperiences == null || mResumeEntity.mPlatformProjectExperiences.size() == 0){
            mPlatformExperienceLayout.setVisibility(View.GONE);
        }else {
            mPlatformExperienceLayout.setVisibility(View.VISIBLE);
            mPlatformExperienceAdapter = new ResumePreviewPlatformExperienceAdapter(this);
            mPlatformExperienceAdapter.setData(mResumeEntity.mPlatformProjectExperiences);
            mPlatformExperienceListView.setLayoutManager(new LinearLayoutManager(this));
            mPlatformExperienceListView.setAdapter(mPlatformExperienceAdapter);
        }
    }
}
