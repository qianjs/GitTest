package com.clinical.tongxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.MessageAdapter;
import com.clinical.tongxin.entity.MessageEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/19 0019.
 */

public class MessageActivity extends BaseActivity {
    private ListView listView;
    private List<MessageEntity> list;
    private MessageAdapter adapter;
    private TextView title;
    private EMConversation conversation;
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initListener();
    }

    private void initView() {
        dialog=new MyProgressDialog(this,"请稍等...");
        title= (TextView) findViewById(R.id.title);
        title.setText("消息验证");
        listView= (ListView) findViewById(R.id.listView);
        list=new ArrayList<>();
        List<MessageEntity> list1 = XUtil.searchSelector(MessageEntity.class, "uid",getLoginConfig().getUserId() );
        if (list1 == null){
            list1 = new ArrayList<>();
        }
        for (int i=list1.size()-1;i>=0;i--){
            list.add(list1.get(i));
        }

        adapter=new MessageAdapter(this,list);
        listView.setAdapter(adapter);
        conversation = EMClient.getInstance().chatManager().getConversation("admin");
    }

    private void initListener(){
        adapter.setOnOkorNoClickListener(new MessageAdapter.OnOkorNoClickListener() {
            //接受
            @Override
            public void setOk(MessageEntity messageEntity) {
                try {
                    findagree(messageEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //取消
            @Override
            public void setNo(MessageEntity messageEntity) {
                try {
                    Toast.makeText(MessageActivity.this,"忽略",Toast.LENGTH_SHORT).show();
                    messageEntity.setType("0");
                    XUtil.update(messageEntity);
                    //把一条消息置为已读
                    conversation.markMessageAsRead(messageEntity.getMsgId());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void findagree(final MessageEntity messageEntity){
        dialog.show();
        Map<String ,String> map=new HashMap<>();
        map.put("CustomerId",getLoginUserSharedPre().getString("userId",""));
        map.put("Ukey",getLoginUserSharedPre().getString("Ukey",""));
        map.put("groupid",messageEntity.getGroupId());
        map.put("groupType",messageEntity.getGroupType());
        XUtil.Post(UrlUtils.URL_receiveInvitation,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson!=null){
                    if (myjson.getCode().equals("200")){
						messageEntity.setType("1");
                        XUtil.update(messageEntity);

                        //把一条消息置为已读
                        conversation.markMessageAsRead(messageEntity.getMsgId());
                        Toast.makeText(MessageActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getLoginUserSharedPre().edit().clear().commit();
                        Intent intent = new Intent(context,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else if (myjson.getCode().equals("201")){
                        messageEntity.setType("3");
                        XUtil.update(messageEntity);
                        //把一条消息置为已读
                        conversation.markMessageAsRead(messageEntity.getMsgId());
                        Toast.makeText(MessageActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
                dialog.dismiss();
            }
        });


    }
}
