package com.clinical.tongxin.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjs on 2017/8/21.
 */

public class OptionItemEntity implements Parcelable {
    @SerializedName("code")
    public String mCode;
    @SerializedName("name")
    public String mName;
    @SerializedName("sub")
    public ArrayList<Sub> mSubs;

    public static class Sub implements Parcelable{
        @SerializedName("code")
        public String mCode;
        @SerializedName("name")
        public String mName;

        protected Sub(Parcel in) {
            mCode = in.readString();
            mName = in.readString();
        }

        public static final Creator<Sub> CREATOR = new Creator<Sub>() {
            @Override
            public Sub createFromParcel(Parcel in) {
                return new Sub(in);
            }

            @Override
            public Sub[] newArray(int size) {
                return new Sub[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mCode);
            dest.writeString(mName);
        }
    }

    protected OptionItemEntity(Parcel in) {
        mCode = in.readString();
        mName = in.readString();
        mSubs = in.createTypedArrayList(Sub.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeString(mName);
        dest.writeTypedList(mSubs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OptionItemEntity> CREATOR = new Creator<OptionItemEntity>() {
        @Override
        public OptionItemEntity createFromParcel(Parcel in) {
            return new OptionItemEntity(in);
        }

        @Override
        public OptionItemEntity[] newArray(int size) {
            return new OptionItemEntity[size];
        }
    };
}
