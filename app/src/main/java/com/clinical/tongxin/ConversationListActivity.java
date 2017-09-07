package com.clinical.tongxin;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.clinical.tongxin.entity.HXMyFriend;
import com.clinical.tongxin.entity.MessageEntity;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class ConversationListActivity extends EaseBaseActivity {

    private EaseConversationListFragment conversationListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversationlist);
        initView();
        getSupportFragmentManager().beginTransaction().add(R.id.show_layout, conversationListFragment).show(conversationListFragment).commit();
    }
    private void initView()
    {
//        TextView title=(TextView)findViewById(R.id.title);
//        title.setText("咨询列表");
        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {

                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser())){
                    Toast.makeText(ConversationListActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();

                }else if (username.equals("admin")){// 验证消息
                    Intent intent = new Intent(ConversationListActivity.this, MessageActivity.class);
                    startActivity(intent);
                }
                else {
                    // start chat acitivity
                    Intent intent = new Intent(ConversationListActivity.this, ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
//                startActivity(new Intent(ConversationListActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));

            }
        });
        //EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            Utils.Vibrate(ConversationListActivity.this, 1000);
            EMClient.getInstance().chatManager().loadAllConversations();

            for(EMMessage msg:messages)
            {
                String nickName=msg.getStringAttribute("nickName","");
                String headUrl=msg.getStringAttribute("headUrl","");
//                EaseUser easeUser = new EaseUser(msg.getFrom());
//                easeUser.setAvatar(headUrl);
//                easeUser.setNick(nickName);

                    List<HXMyFriend> list=XUtil.searchSelector(HXMyFriend.class, "name", msg.getFrom());
                    if(list!=null) {
                        if (list.size() > 0) {
                            if (!headUrl.equals(list.get(0).getHeadurl()) || !nickName.equals(list.get(0).getNickname())) {
                                list.get(0).setNickname(nickName);
                                list.get(0).setHeadurl(headUrl);
                                XUtil.saveOrUpdate(list.get(0));
                            }
                        }
                        else
                        {
                            HXMyFriend model=new HXMyFriend();
                            model.setHeadurl(headUrl);
                            model.setNickname(nickName);
                            model.setName(msg.getFrom());
                            XUtil.save(model);
                        }
                    }
                    else
                    {
                        HXMyFriend model=new HXMyFriend();
                        model.setHeadurl(headUrl);
                        model.setNickname(nickName);
                        model.setName(msg.getFrom());
                        XUtil.save(model);
                    }

            }

            conversationListFragment.refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Override
    protected void onDestroy() {
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroy();
    }

}
