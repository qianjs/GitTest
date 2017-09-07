package com.clinical.tongxin.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjs on 2017/8/17.
 */

public class ResumeEntity implements Parcelable{
    @SerializedName("basic")
    public BasicInfo mBasicInfo;
    @SerializedName("work")
    public List<WorkExperience> mWorkExperiences;
    @SerializedName("project")
    public List<ProjectExperience> mProjectExperiences;
    @SerializedName("education")
    public List<EducationExperience> mEducationExperiences;
    @SerializedName("certificate")
    public List<Certificate> mCertificates;
    @SerializedName("skills")
    public List<Skill> mSkills;
    @SerializedName("enclosure")
    public List<CertificateAttachment> mCertificateAttachments;
    @SerializedName("pingTai")
    public List<PlatformProjectExperience> mPlatformProjectExperiences;
    @SerializedName("headPic")
    public String mHeadPhotoUrl;

    public ResumeEntity(){
        mBasicInfo = new BasicInfo();
        mWorkExperiences = new ArrayList<>();
        mProjectExperiences = new ArrayList<>();
        mEducationExperiences = new ArrayList<>();
        mCertificates = new ArrayList<>();
        mSkills = new ArrayList<>();
        mCertificateAttachments = new ArrayList<>();
        mPlatformProjectExperiences = new ArrayList<>();
        mHeadPhotoUrl = "";
    }

    protected ResumeEntity(Parcel in) {
        mBasicInfo = in.readParcelable(BasicInfo.class.getClassLoader());
        mWorkExperiences = in.createTypedArrayList(WorkExperience.CREATOR);
        mProjectExperiences = in.createTypedArrayList(ProjectExperience.CREATOR);
        mEducationExperiences = in.createTypedArrayList(EducationExperience.CREATOR);
        mCertificates = in.createTypedArrayList(Certificate.CREATOR);
        mSkills = in.createTypedArrayList(Skill.CREATOR);
        mCertificateAttachments = in.createTypedArrayList(CertificateAttachment.CREATOR);
        mPlatformProjectExperiences = in.createTypedArrayList(PlatformProjectExperience.CREATOR);
        mHeadPhotoUrl = in.readString();
    }

    public static final Creator<ResumeEntity> CREATOR = new Creator<ResumeEntity>() {
        @Override
        public ResumeEntity createFromParcel(Parcel in) {
            return new ResumeEntity(in);
        }

        @Override
        public ResumeEntity[] newArray(int size) {
            return new ResumeEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mBasicInfo, flags);
        dest.writeTypedList(mWorkExperiences);
        dest.writeTypedList(mProjectExperiences);
        dest.writeTypedList(mEducationExperiences);
        dest.writeTypedList(mCertificates);
        dest.writeTypedList(mSkills);
        dest.writeTypedList(mCertificateAttachments);
        dest.writeTypedList(mPlatformProjectExperiences);
        dest.writeString(mHeadPhotoUrl);
    }

    //基本信息
    public static class BasicInfo implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        @SerializedName("customerName")
        public String mCustomerName;
        @SerializedName("sexId")
        public String mSexId;
        @SerializedName("sexText")
        public String mSex;
        @SerializedName("birth")
        public String mBirthday;
        @SerializedName("hukouRegionId")
        public String mHouseholdId;
        //户口所在地
        @SerializedName("hukouRegion")
        public String mHousehold;
        @SerializedName("workTime")
        public String mWorkStartTime;
        @SerializedName("cityId")
        public String mLiveCityId;
        @SerializedName("city")
        public String mLiveCity;
        @SerializedName("mobile")
        public String mMobile;
        @SerializedName("email")
        public String mEmail;
        //婚姻状况
        @SerializedName("marriageId")
        public String mMarryId;
        @SerializedName("marriage")
        public String mMarry;
        //国籍
        @SerializedName("nationalityId")
        public String mCountryId;
        @SerializedName("nationality")
        public String mCountry;
        //政治面貌
        @SerializedName("politicalOutlookId")
        public String mPoliticalStatusId;
        @SerializedName("politicalOutlook")
        public String mPoliticalStatus;

        public BasicInfo() {
            mId = "";
            mCustomerId = "";
            mCustomerName = "";
            mSexId = "";
            mSex = "";
            mBirthday = "";
            mHouseholdId = "";
            mHousehold = "";
            mWorkStartTime = "";
            mLiveCityId = "";
            mLiveCity = "";
            mMobile = "";
            mEmail = "";
            mMarryId = "";
            mMarry = "";
            mCountryId = "";
            mCountry = "";
            mPoliticalStatusId = "";
            mPoliticalStatus = "";
        }

        protected BasicInfo(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mCustomerName = in.readString();
            mSexId = in.readString();
            mSex = in.readString();
            mBirthday = in.readString();
            mHouseholdId = in.readString();
            mHousehold = in.readString();
            mWorkStartTime = in.readString();
            mLiveCityId = in.readString();
            mLiveCity = in.readString();
            mMobile = in.readString();
            mEmail = in.readString();
            mMarryId = in.readString();
            mMarry = in.readString();
            mCountryId = in.readString();
            mCountry = in.readString();
            mPoliticalStatusId = in.readString();
            mPoliticalStatus = in.readString();
        }

        public static final Creator<BasicInfo> CREATOR = new Creator<BasicInfo>() {
            @Override
            public BasicInfo createFromParcel(Parcel in) {
                return new BasicInfo(in);
            }

            @Override
            public BasicInfo[] newArray(int size) {
                return new BasicInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mCustomerName);
            dest.writeString(mSexId);
            dest.writeString(mSex);
            dest.writeString(mBirthday);
            dest.writeString(mHouseholdId);
            dest.writeString(mHousehold);
            dest.writeString(mWorkStartTime);
            dest.writeString(mLiveCityId);
            dest.writeString(mLiveCity);
            dest.writeString(mMobile);
            dest.writeString(mEmail);
            dest.writeString(mMarryId);
            dest.writeString(mMarry);
            dest.writeString(mCountryId);
            dest.writeString(mCountry);
            dest.writeString(mPoliticalStatusId);
            dest.writeString(mPoliticalStatus);
        }
    }
    //工作经历
    public static class WorkExperience implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        //企业名称
        @SerializedName("enterprise")
        public String mEnterprise;
        //行业类别
        @SerializedName("industryId")
        public String mIndustryId;
        @SerializedName("industry")
        public String mIndustry;
        //职位类别
        @SerializedName("jobTypeId")
        public String mJobTypeId;
        //职位名称
        @SerializedName("jobType")
        public String mJobType;
        @SerializedName("jobName")
        public String mJobName;
        @SerializedName("startWorkTime")
        public String mWorkStartTime;
        @SerializedName("endWorkTime")
        public String mWorkEndTime;
        //薪资
        @SerializedName("salaryId")
        public String mSalaryId;
        @SerializedName("salary")
        public String mSalary;
        @SerializedName("workDescription")
        public String mWorkDescription;

        public WorkExperience(){
            mId = "";
            mCustomerId = "";
            mEnterprise = "";
            mIndustryId = "";
            mIndustry = "";
            mJobTypeId = "";
            mJobType = "";
            mJobName = "";
            mWorkStartTime = "";
            mWorkEndTime = "";
            mSalaryId = "";
            mSalary = "";
            mWorkDescription = "";
        }

        protected WorkExperience(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mEnterprise = in.readString();
            mIndustryId = in.readString();
            mIndustry = in.readString();
            mJobTypeId = in.readString();
            mJobType = in.readString();
            mJobName = in.readString();
            mWorkStartTime = in.readString();
            mWorkEndTime = in.readString();
            mSalaryId = in.readString();
            mSalary = in.readString();
            mWorkDescription = in.readString();
        }

        public static final Creator<WorkExperience> CREATOR = new Creator<WorkExperience>() {
            @Override
            public WorkExperience createFromParcel(Parcel in) {
                return new WorkExperience(in);
            }

            @Override
            public WorkExperience[] newArray(int size) {
                return new WorkExperience[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mEnterprise);
            dest.writeString(mIndustryId);
            dest.writeString(mIndustry);
            dest.writeString(mJobTypeId);
            dest.writeString(mJobType);
            dest.writeString(mJobName);
            dest.writeString(mWorkStartTime);
            dest.writeString(mWorkEndTime);
            dest.writeString(mSalaryId);
            dest.writeString(mSalary);
            dest.writeString(mWorkDescription);
        }
    }
    //项目经验
    public static class ProjectExperience implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        @SerializedName("projectName")
        public String mProjectName;
        @SerializedName("proStartTime")
        public String mProjectStartTime;
        @SerializedName("proEndTime")
        public String mProjectEndTime;
        //项目职责
        @SerializedName("proDuty")
        public String mProjectDuty;
        @SerializedName("proDescribe")
        public String mProjectDescription;

        public ProjectExperience(){
            mId = "";
            mCustomerId = "";
            mProjectName = "";
            mProjectStartTime = "";
            mProjectEndTime = "";
            mProjectDuty = "";
            mProjectDescription = "";
        }

        protected ProjectExperience(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mProjectName = in.readString();
            mProjectStartTime = in.readString();
            mProjectEndTime = in.readString();
            mProjectDuty = in.readString();
            mProjectDescription = in.readString();
        }

        public static final Creator<ProjectExperience> CREATOR = new Creator<ProjectExperience>() {
            @Override
            public ProjectExperience createFromParcel(Parcel in) {
                return new ProjectExperience(in);
            }

            @Override
            public ProjectExperience[] newArray(int size) {
                return new ProjectExperience[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mProjectName);
            dest.writeString(mProjectStartTime);
            dest.writeString(mProjectEndTime);
            dest.writeString(mProjectDuty);
            dest.writeString(mProjectDescription);
        }
    }
    //教育经历
    public static class EducationExperience implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        @SerializedName("startEducationTime")
        public String mEducationStartTime;
        @SerializedName("endEducationTime")
        public String mEducationEndTime;
        @SerializedName("schoolName")
        public String mUniversity;
        //是否统招
        @SerializedName("isRecruitId")
        public String mIsRecruitId;
        @SerializedName("isRecruit")
        public String mIsRecruit;
        //专业名称
        @SerializedName("majorId")
        public String mMajorId;
        @SerializedName("major")
        public String mMajor;
        //学位
        @SerializedName("degreeId")
        public String mDegreeId;
        @SerializedName("degree")
        public String mDegree;

        public EducationExperience(){
            mId = "";
            mCustomerId = "";
            mEducationStartTime = "";
            mEducationEndTime = "";
            mUniversity = "";
            mIsRecruitId = "";
            mIsRecruit = "";
            mMajorId = "";
            mMajor = "";
            mDegreeId = "";
            mDegree = "";
        }

        protected EducationExperience(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mEducationStartTime = in.readString();
            mEducationEndTime = in.readString();
            mUniversity = in.readString();
            mIsRecruitId = in.readString();
            mIsRecruit = in.readString();
            mMajorId = in.readString();
            mMajor = in.readString();
            mDegreeId = in.readString();
            mDegree = in.readString();
        }

        public static final Creator<EducationExperience> CREATOR = new Creator<EducationExperience>() {
            @Override
            public EducationExperience createFromParcel(Parcel in) {
                return new EducationExperience(in);
            }

            @Override
            public EducationExperience[] newArray(int size) {
                return new EducationExperience[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mEducationStartTime);
            dest.writeString(mEducationEndTime);
            dest.writeString(mUniversity);
            dest.writeString(mIsRecruitId);
            dest.writeString(mIsRecruit);
            dest.writeString(mMajorId);
            dest.writeString(mMajor);
            dest.writeString(mDegreeId);
            dest.writeString(mDegree);
        }
    }
    //获得证书
    public static class Certificate implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        @SerializedName("certificateName")
        public String mCertificateName;
        @SerializedName("getTime")
        public String mGetTime;

        public Certificate() {
            mId = "";
            mCustomerId = "";
            mCertificateName = "";
            mGetTime = "";
        }

        protected Certificate(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mCertificateName = in.readString();
            mGetTime = in.readString();
        }

        public static final Creator<Certificate> CREATOR = new Creator<Certificate>() {
            @Override
            public Certificate createFromParcel(Parcel in) {
                return new Certificate(in);
            }

            @Override
            public Certificate[] newArray(int size) {
                return new Certificate[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mCertificateName);
            dest.writeString(mGetTime);
        }
    }
    //专业技能
    public static class Skill implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        //技能类别
        @SerializedName("skillsTypeId")
        public String mSkillTypeId;
        @SerializedName("skillsType")
        public String mSkillType;
        //技能名称
        @SerializedName("skillsName")
        public String mSkillName;
        //使用时间，单位：月
        @SerializedName("useTime")
        public String mUseTime;
        //掌握程度
        @SerializedName("masteryId")
        public String mMasteryId;
        @SerializedName("mastery")
        public String mMastery;

        public Skill() {
            mId = "";
            mCustomerId = "";
            mSkillTypeId = "";
            mSkillType = "";
            mSkillName = "";
            mUseTime = "";
            mMasteryId = "";
            mMastery = "";
        }

        protected Skill(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mSkillTypeId = in.readString();
            mSkillType = in.readString();
            mSkillName = in.readString();
            mUseTime = in.readString();
            mMasteryId = in.readString();
            mMastery = in.readString();
        }

        public static final Creator<Skill> CREATOR = new Creator<Skill>() {
            @Override
            public Skill createFromParcel(Parcel in) {
                return new Skill(in);
            }

            @Override
            public Skill[] newArray(int size) {
                return new Skill[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mSkillTypeId);
            dest.writeString(mSkillType);
            dest.writeString(mSkillName);
            dest.writeString(mUseTime);
            dest.writeString(mMasteryId);
            dest.writeString(mMastery);
        }
    }
    //证书附件
    public static class CertificateAttachment implements Parcelable{
        @SerializedName("id")
        public String mId;
        @SerializedName("customerId")
        public String mCustomerId;
        @SerializedName("miaoshu")
        public String mDescription;
        @SerializedName("attUrl")
        public String mUrl;

        public CertificateAttachment() {
            mId = "";
            mCustomerId = "";
            mDescription = "";
            mUrl = "";
        }

        protected CertificateAttachment(Parcel in) {
            mId = in.readString();
            mCustomerId = in.readString();
            mDescription = in.readString();
            mUrl = in.readString();
        }

        public static final Creator<CertificateAttachment> CREATOR = new Creator<CertificateAttachment>() {
            @Override
            public CertificateAttachment createFromParcel(Parcel in) {
                return new CertificateAttachment(in);
            }

            @Override
            public CertificateAttachment[] newArray(int size) {
                return new CertificateAttachment[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mId);
            dest.writeString(mCustomerId);
            dest.writeString(mDescription);
            dest.writeString(mUrl);
        }
    }
    //平台项目经验
    public static class PlatformProjectExperience implements Parcelable {
        @SerializedName("projectTypeId")
        public String mProjectTypeId;
        @SerializedName("projectTypeName")
        public String mProjectTypeName;
        @SerializedName("taskList")
        public List<Task> mTasks;

        public static class Task implements Parcelable{
            @SerializedName("taskTypeId")
            public String mId;
            @SerializedName("taskTypeName")
            public String mName;
            @SerializedName("count")
            public int mCount;

            protected Task(Parcel in) {
                mId = in.readString();
                mName = in.readString();
                mCount = in.readInt();
            }

            public static final Creator<Task> CREATOR = new Creator<Task>() {
                @Override
                public Task createFromParcel(Parcel in) {
                    return new Task(in);
                }

                @Override
                public Task[] newArray(int size) {
                    return new Task[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(mId);
                dest.writeString(mName);
                dest.writeInt(mCount);
            }
        }

        protected PlatformProjectExperience(Parcel in) {
            mProjectTypeId = in.readString();
            mProjectTypeName = in.readString();
            mTasks = in.createTypedArrayList(Task.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mProjectTypeId);
            dest.writeString(mProjectTypeName);
            dest.writeTypedList(mTasks);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlatformProjectExperience> CREATOR = new Creator<PlatformProjectExperience>() {
            @Override
            public PlatformProjectExperience createFromParcel(Parcel in) {
                return new PlatformProjectExperience(in);
            }

            @Override
            public PlatformProjectExperience[] newArray(int size) {
                return new PlatformProjectExperience[size];
            }
        };
    }
}
