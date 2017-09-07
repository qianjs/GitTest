package com.clinical.tongxin.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class Test implements Parcelable {
    private String name;
    private String age;

    public String getName() {
        return Utils.getMyString(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return Utils.getMyString(age, "");
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.age);
    }

    public Test() {
    }

    protected Test(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
    }

    public static final Parcelable.Creator<Test> CREATOR = new Parcelable.Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel source) {
            return new Test(source);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };
}
