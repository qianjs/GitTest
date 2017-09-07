package com.clinical.tongxin.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


import com.clinical.tongxin.LoginActivity;
import com.clinical.tongxin.entity.AppNoticeModel;
import com.clinical.tongxin.entity.MessageEntity;
import com.clinical.tongxin.entity.NotifyMessageEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.EMLog;

import java.util.List;


/**
 * Created by linchao on 2017/1/16.
 */

public class IMHelper {
    private Context mContext;
    private LocalBroadcastManager broadcastManager;
    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;
    private EaseUI easeUI;
    private static IMHelper instance;
    protected static final String TAG = "linchao";
    private int notifyId = 0;
    private EMConversation conversation;

    public static IMHelper getInstance() {
        if (instance == null) {
            instance = new IMHelper();
        }
        return instance;
    }

    /**
     * 初始化方法，调用EaseUI的初始化，
     *
     * @param context
     */
    public void init(Context context) {

        try {
            mContext = context;
            EMOptions options = new EMOptions();
            // 默认添加好友时，是不需要验证的，设置不需要验证
            options.setAcceptInvitationAlways(true);
            //初始化
            //EMClient.getInstance().init(this, options);
            EaseUI.getInstance().init(context, options);

            broadcastManager = LocalBroadcastManager.getInstance(context);
            easeUI = EaseUI.getInstance();
            setGlobalListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            broadcastManager = LocalBroadcastManager.getInstance(appContext);
//            initDbDao();
    }

    public void setUserInfo(final UserEntity userEntity) {
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username, userEntity);
            }
        });
    }

    private EaseUser getUserInfo(String username, UserEntity userEntity) {
        EaseUser easeUser = new EaseUser(username);
//        List<HXMyFriend> list=XUtil.searchSelector(HXMyFriend.class, "name", username);
//        if(list!=null) {
//            if (list.size() > 0) {
//                easeUser.setAvatar(list.get(0).getHeadurl());
//                easeUser.setNick(list.get(0).getNickname());
//            }
//        }
//        easeUser.setAvatar("http://img5.imgtn.bdimg.com/it/u=1056973720,185019313&fm=11&gp=0.jpg");
//        easeUser.setNick("测试测试");
        return easeUser;

    }

    EMConnectionListener connectionListener = new EMConnectionListener() {
        //USER_LOGIN_ANOTHER_DEVICE用户在另一台设备上登录用户别删除；USER_REMOVED用户别删除
        @Override
        public void onDisconnected(int error) {
//            Log.e("linchao","环信断开连接===="+error);
            if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 登录异常
                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_LOGIN));
            } else {
                // 已断开
                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONNECT_ERROR));
            }
        }

        @Override
        public void onConnected() {
//            Log.e("linchao","----------环信已连接--------------");
            // 已连接
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONNECT_OK));
        }
    };

    /**
     * set global listener
     */
    protected void setGlobalListeners() {


        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);

        //register message event listener
        registerMessageListener();
        addConnectListener();
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    Log.e(TAG, "onMessageReceived id : " + message.toString());
                    // in background, do not refresh UI, notify it in notification bar

                    if ("admin".equals(message.getFrom())) {
                        String body = message.getBody().toString();
                        String json = body.substring(body.indexOf("\"") + 1, body.lastIndexOf("\""));
                        NotifyMessageEntity<Object> notifyMessageEntity = new Gson().fromJson(json, new TypeToken<NotifyMessageEntity<Object>>() {
                        }.getType());
                        if ("group".equals(notifyMessageEntity.getMessageType())){
                            NotifyMessageEntity<MessageEntity> groupMsgEntity = new Gson().fromJson(json, new TypeToken<NotifyMessageEntity<MessageEntity>>() {
                            }.getType());
                            MessageEntity messageEntity = groupMsgEntity.getContent();
                            messageEntity.setUid(mContext.getSharedPreferences("LOGINSP", 0).getString("userId", ""));
                            messageEntity.setMsgId(message.getMsgId());
                            XUtil.save(messageEntity);
                            //if (!easeUI.hasForegroundActivies()) {
                                easeUI.getNotifier().onNewMsg(message);
                                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_REFRESH_MESSAGE));
                            //}
                        }else if ("task".equals(notifyMessageEntity.getMessageType())){
                            try {
                                conversation = EMClient.getInstance().chatManager().getConversation("admin");
                                conversation.markMessageAsRead(message.getMsgId());
                                conversation.removeMessage(message.getMsgId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            NotifyMessageEntity<AppNoticeModel> taskMsgEntity = new Gson().fromJson(json, new TypeToken<NotifyMessageEntity<AppNoticeModel>>() {
                            }.getType());
                            AppNoticeModel appNoticeModel = taskMsgEntity.getContent();
                            appNoticeModel.setUid(mContext.getSharedPreferences("LOGINSP", 0).getString("userId", ""));
                            appNoticeModel.setMsgId(message.getMsgId());
                            //XUtil.save(appNoticeModel);
                            easeUI.getNotifier().vibrateAndPlayTone(message);
                            onMsgPushTask(appNoticeModel,notifyId++,json);
                        }

                    }else {
                        if (!easeUI.hasForegroundActivies()) {
                            easeUI.getNotifier().onNewMsg(message);
                            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_REFRESH_MESSAGE));
                        }
                        String nickName = message.getStringAttribute("nickName", "");
                        if (!TextUtils.isEmpty(nickName)) {
                            SPUtils.putObject(message.getFrom(), nickName);
                        }
                    }
                }


            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    public void removeConnectListener() {
        if (connectionListener != null) {
            EMClient.getInstance().removeConnectionListener(connectionListener);
        }
    }

    public void addConnectListener() {
        if (connectionListener != null) {
            EMClient.getInstance().addConnectionListener(connectionListener);
        }
    }

    public int getUnreadMsg(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        int unreadMsgCount = 0;
        unreadMsgCount = conversation.getUnreadMsgCount();
        return unreadMsgCount;
    }

    public void removeUnread(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
    }

    /**
     * handle the new message
     * this function can be override
     *
     * @param
     * @param body
     */
    public synchronized void onMsgPushTask(AppNoticeModel appNoticeModel, int id, String body) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            PackageManager packageManager = mContext.getPackageManager();
            String contentTitle = (String) packageManager.getApplicationLabel(mContext.getApplicationInfo());

            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(mContext.getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);

//            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
            Intent msgIntent = new Intent(mContext, LoginActivity.class);
            msgIntent.putExtra("msg",body);
            msgIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentTitle(Utils.decodeUnicode(appNoticeModel.getNoticeName()));
            mBuilder.setTicker(contentTitle);
            mBuilder.setContentText(Utils.decodeUnicode(appNoticeModel.getNoticeContent()));
            mBuilder.setContentIntent(pendingIntent);
            Notification notification = mBuilder.build();
            notificationManager.notify(id, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // check if app running background
//        if (!EasyUtils.isAppRunningForeground(appContext)) {
//            EMLog.d(TAG, "app is running in backgroud");
//            sendNotification(message, false);
//        } else {
//            sendNotification(message, true);
//
//        }


    }

}

