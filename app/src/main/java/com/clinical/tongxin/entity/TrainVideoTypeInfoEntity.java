package com.clinical.tongxin.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by QJS on 2017-07-25.
 */

public class TrainVideoTypeInfoEntity implements Parcelable {
    @SerializedName("videoTypeId")
    private String mVideoTypeId;
    @SerializedName("videoTypeName")
    private String mVideoTypeName;
    @SerializedName("taskList")
    private List<TrainVideoInfoEntity> mTrainVideoInfoEntitys;

    protected TrainVideoTypeInfoEntity(Parcel in) {
        mVideoTypeId = in.readString();
        mVideoTypeName = in.readString();
        mTrainVideoInfoEntitys = in.createTypedArrayList(TrainVideoInfoEntity.CREATOR);
    }

    public static final Creator<TrainVideoTypeInfoEntity> CREATOR = new Creator<TrainVideoTypeInfoEntity>() {
        @Override
        public TrainVideoTypeInfoEntity createFromParcel(Parcel in) {
            return new TrainVideoTypeInfoEntity(in);
        }

        @Override
        public TrainVideoTypeInfoEntity[] newArray(int size) {
            return new TrainVideoTypeInfoEntity[size];
        }
    };

    public String getVideoTypeId() {
        return mVideoTypeId;
    }

    public void setVideoTypeId(String videoTypeId) {
        mVideoTypeId = videoTypeId;
    }

    public String getVideoTypeName() {
        return mVideoTypeName;
    }

    public void setVideoTypeName(String videoTypeName) {
        mVideoTypeName = videoTypeName;
    }

    public List<TrainVideoInfoEntity> getTrainVideoInfoEntitys() {
        return mTrainVideoInfoEntitys;
    }

    public void setTrainVideoInfoEntitys(List<TrainVideoInfoEntity> trainVideoInfoEntitys) {
        mTrainVideoInfoEntitys = trainVideoInfoEntitys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVideoTypeId);
        dest.writeString(mVideoTypeName);
        dest.writeTypedList(mTrainVideoInfoEntitys);
    }

    public static class TrainVideoInfoEntity implements Parcelable {
        @SerializedName("videoTypeId")
        private String mVideoTypeId;
        @SerializedName("videoTypeName")
        private String mVideoTypeName;
        @SerializedName("chickCount")
        private int mClickTimes;
        @SerializedName("videoId")
        private String mVideoId;
        @SerializedName("videoName")
        private String mVideoName;
        @SerializedName("videoFileName")
        private String mVideoFileName;
        @SerializedName("url")
        private String mUrl;
        @SerializedName("imgUrl")
        private String mThumbnailUrl;
        @SerializedName("videoPath")
        private String mVideoPath;

        protected TrainVideoInfoEntity(Parcel in) {
            mVideoTypeId = in.readString();
            mVideoTypeName = in.readString();
            mClickTimes = in.readInt();
            mVideoId = in.readString();
            mVideoName = in.readString();
            mVideoFileName = in.readString();
            mUrl = in.readString();
            mThumbnailUrl = in.readString();
            mVideoPath = in.readString();
        }

        public static final Creator<TrainVideoInfoEntity> CREATOR = new Creator<TrainVideoInfoEntity>() {
            @Override
            public TrainVideoInfoEntity createFromParcel(Parcel in) {
                return new TrainVideoInfoEntity(in);
            }

            @Override
            public TrainVideoInfoEntity[] newArray(int size) {
                return new TrainVideoInfoEntity[size];
            }
        };

        public String getVideoTypeId() {
            return mVideoTypeId;
        }

        public void setVideoTypeId(String videoTypeId) {
            this.mVideoTypeId = videoTypeId;
        }

        public String getVideoTypeName() {
            return mVideoTypeName;
        }

        public void setmVideoTypeName(String videoTypeName) {
            this.mVideoTypeName = videoTypeName;
        }

        public int getClickTimes() {
            return mClickTimes;
        }

        public void setClickTimes(int clickTimes) {
            this.mClickTimes = clickTimes;
        }

        public String getVideoId() {
            return mVideoId;
        }

        public void setVideoId(String videoId) {
            this.mVideoId = videoId;
        }

        public String getVideoName() {
            return mVideoName;
        }

        public void setVideoName(String videoName) {
            this.mVideoName = videoName;
        }

        public String getVideoFileName() {
            return mVideoFileName;
        }

        public void setVideoFileName(String videoFileName) {
            this.mVideoFileName = videoFileName;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            this.mUrl = url;
        }

        public String getThumbnailUrl() {
            return mThumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.mThumbnailUrl = thumbnailUrl;
        }

        public String getVideoPath() {
            return mVideoPath;
        }

        public void setVideoPath(String videoPath) {
            this.mVideoPath = videoPath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mVideoTypeId);
            dest.writeString(mVideoTypeName);
            dest.writeInt(mClickTimes);
            dest.writeString(mVideoId);
            dest.writeString(mVideoName);
            dest.writeString(mVideoFileName);
            dest.writeString(mUrl);
            dest.writeString(mThumbnailUrl);
            dest.writeString(mVideoPath);
        }
    }

}
