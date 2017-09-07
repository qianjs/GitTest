package com.clinical.tongxin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.SPUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    String toChatUsername;
    EaseChatFragment chatFragment;
    private boolean isRobot=true;
    protected SharedPreferences preferences;
    private String nickName;
    private int chatType;
    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        //initView();
        preferences = getSharedPreferences("LOGINSP", 0);
        toChatUsername=getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);

        nickName = EaseUserUtils.getNickname(toChatUsername,activityInstance);
        if (TextUtils.isEmpty(nickName) && chatType == EaseConstant.CHATTYPE_SINGLE){
            dialog=new MyProgressDialog(this,"请稍等...");
            dialog.show();
            reqestData(toChatUsername);
        }else {
            chatFragment = new EaseChatFragment();
            getIntent().putExtra("nickName",nickName);
            chatFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.show_layout, chatFragment).commit();
            initListener();
        }

//
////传入参数
//        Bundle args = new Bundle();
//        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//        args.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
//        args.putString(EaseConstant.EXTRA_NICKNAME, nickName);
//        chatFragment.setArguments(args);


    }

    private void reqestData(String toChatUsername) {
        Map map=new HashMap<>();
        map.put("Mobile",toChatUsername);
        XUtil.Post(UrlUtils.URL_GET_NICKNAME, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                //Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                chatFragment = new EaseChatFragment();
                getIntent().putExtra("nickName",nickName);
                chatFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.show_layout, chatFragment).commit();
                initListener();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<List<UserEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<UserEntity>>>(){}.getType());
                    if (resultEntity.getCode() == 200){
                        List<UserEntity> userEntities = resultEntity.getResult();
                        for (UserEntity userEntity:userEntities){
                            SPUtils.putObject(userEntity.getPhone(),userEntity.getNickName());
                        }

                    }else {
                        Toast.makeText(ChatActivity.this,"获取昵称失败，请重试",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                    chatFragment = new EaseChatFragment();
                    getIntent().putExtra("nickName",nickName);
                    chatFragment.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().add(R.id.show_layout, chatFragment).commit();
                    initListener();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    private void initListener()
    {
        chatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
               String nickName=preferences.getString("NickName", "");
               String headUrl=preferences.getString("url","");
               message.setAttribute("nickName",nickName);
               message.setAttribute("headUrl",headUrl);
            }

            @Override
            public void onEnterToChatDetails() {
                if (chatType == Constant.CHATTYPE_GROUP) {
                    EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                    if (group == null) {
                        Toast.makeText(ChatActivity.this, R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(ChatActivity.this, GroupDetailsActivity.class);
                    intent.putExtra("groupId", toChatUsername);
                    startActivity(intent);
                }
            }

            @Override
            public void onAvatarClick(String username) {

            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                //chatFragment.setInputMenuVisible(false);
                //Toast.makeText(ChatActivity.this, "点击事件", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });
    }
}
