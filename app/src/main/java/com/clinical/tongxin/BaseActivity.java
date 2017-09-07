package com.clinical.tongxin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;

import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.IActivitySupport;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class BaseActivity extends FragmentActivity implements IActivitySupport {
    protected Context context = null;
    protected SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = this;
        preferences = getSharedPreferences("LOGINSP", 0);
        MyApplication.addActivity(this);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void showToast(String text, int longint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showToast(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getUserCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SharedPreferences getLoginUserSharedPre() {
        // TODO Auto-generated method stub
        return preferences;
    }

    @Override
    public void saveLoginConfig(UserEntity user) {
        // TODO Auto-generated method stub

        preferences.edit().putString("Status", user.getUserId()).apply();
        preferences.edit().putString("userId", user.getUserId()).apply();
        preferences.edit().putString("Ukey", user.getUserId()).apply();
        preferences.edit().putString("Mobile", user.getUserId()).apply();
        preferences.edit().putString("NickName", user.getUserId()).apply();
        preferences.edit().putString("RealName", user.getUserId()).apply();
        preferences.edit().putString("IDNumber", user.getUserId()).apply();
        preferences.edit().putString("HXNumber", user.getUserId()).apply();
        preferences.edit().putString("BankName", user.getUserId()).apply();
        preferences.edit().putString("CardNumber", user.getUserId()).apply();
        preferences.edit().putString("PhotoUrl", user.getUserId()).apply();
        preferences.edit().putString("IsRefuseTask", user.getUserId()).apply();
        preferences.edit().putString("role", user.getUserId()).apply();
        preferences.edit().putString("PassWord", user.getUserId()).apply();
        preferences.edit().putString("Rating", user.getRating()).apply();
        preferences.edit().putString("RatingType", user.getRatingType()).apply();
    }

    @Override
    public UserEntity getLoginConfig() {
        // TODO Auto-generated method stub
        UserEntity user = new UserEntity();

        user.setStatus(preferences.getString("Status", ""));
        user.setUserId(preferences.getString("userId", ""));
        user.setUkey(preferences.getString("Ukey", ""));
        user.setPhone(preferences.getString("Mobile", ""));
        user.setNickName(preferences.getString("NickName", ""));
        user.setUserName(preferences.getString("RealName", ""));
        user.setiDNumber(preferences.getString("IDNumber", ""));
        user.setHxAccount(preferences.getString("HXNumber", ""));
        user.setBankName(preferences.getString("BankName", ""));
        user.setCardNumber(preferences.getString("CardNumber", ""));
        user.setPhotoUrl(preferences.getString("PhotoUrl", ""));
        user.setIsRefuseTask(preferences.getString("IsRefuseTask", ""));
        user.setRole(preferences.getString("role", ""));
        user.setPwd(preferences.getString("PassWord", ""));
        user.setRating(preferences.getString("Rating", "0"));
        user.setRatingType(preferences.getString("RatingType", "0"));

        return user;
    }
    public String List2String(List<?> mlist){
        String ResultListString="";
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(mlist);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            ResultListString = new String(Base64.encode(
                    byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            // 关闭objectOutputStream
            objectOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResultListString;
    }
    public List<?> String2List(String ListString)
    {
        List<?> ResultList=null;
        byte[] mobileBytes = Base64.decode(ListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            ResultList = (List<?>) objectInputStream
                    .readObject();
            objectInputStream.close();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResultList;
    }
    public void back(View view)
    {
        finish();
    }
}
