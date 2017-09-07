package com.clinical.tongxin;

import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.clinical.tongxin.R.id.et_evaluate;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by apple on 2016/11/22.
 */

public class MoreFragment extends Fragment implements View.OnClickListener{
    private LinearLayout ll_about;
    private View ll_messsage;
    private View ll_feedback;
    private ImageView iv_unread;
    private LocalBroadcastManager mLocalBroadcastManager;
    private AlertDialog dialog;
    private MyProgressDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.morefragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        //findNumber();
        initListener();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_REFRESH_MESSAGE);
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);

    }

    private void initView() {
        TextView title= (TextView) getActivity().findViewById(R.id.title1);
        title.setText("更多");
        mDialog = new MyProgressDialog(getActivity(),"请稍后...");
        ImageView back= (ImageView) getActivity().findViewById(R.id.img_back);
        back.setVisibility(View.GONE);
        ll_messsage = getActivity().findViewById(R.id.ll_messsage);
        iv_unread = (ImageView)getActivity().findViewById(R.id.iv_unread);
        ll_about= (LinearLayout) getActivity().findViewById(R.id.ll_about);
        ll_feedback=  getActivity().findViewById(R.id.ll_feedback);
    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_REFRESH_MESSAGE)){
                updateUnread();
            }
        }
    };

    private void updateUnread(){
        int count = IMHelper.getInstance().getUnreadMsgCountTotal();
        if (count > 0 ){
            iv_unread.setVisibility(View.VISIBLE);
        }else {
            iv_unread.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnread();
    }

    @Override
    public void onDestroy() {
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    private void initListener() {
        ll_about.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
        ll_messsage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_about:
            {
                Intent intent=new Intent(getActivity(),AboutMyActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.ll_feedback:
                showFeedBack();
                break;
            case R.id.ll_messsage:
                startActivity(new Intent(getActivity(),ConversationListActivity.class));
                break;
        }
    }

    private void showFeedBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.feedback,null);
        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        final EditText content = (EditText) view.findViewById(R.id.et_feedback);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 请求接口
                commitFeedback(content.getText().toString());
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }


    private void commitFeedback(String content) {
        if (TextUtils.isEmpty(content)){
            Toast.makeText(getActivity(), "请添加反馈问题", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.show();

        Map map=new HashMap<>();
        map.put("CustomerId",((MainActivity)getActivity()).getLoginConfig().getUserId());
        map.put("Ukey",((MainActivity)getActivity()).getLoginConfig().getUkey());
        map.put("content", content);

        XUtil.Post(UrlUtils.URL_saveCustomerQutstionApp, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(getActivity(), arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj=new JSONObject(arg0);
                    if (obj.getString("code").equals("200")){
                        if(dialog != null){
                            dialog.dismiss();
                        }
                      Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }
}
