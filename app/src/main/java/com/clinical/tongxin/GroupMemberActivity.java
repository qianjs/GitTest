package com.clinical.tongxin;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.GroupMemberAdapter;
import com.clinical.tongxin.entity.GroupMemberEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 群组成员列表
 * Created by Administrator on 2017/1/6 0006.
 */

public class GroupMemberActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private ListView mListView;
    private List<GroupMemberEntity> mList;
    private GroupMemberAdapter mAdapter;
    private ImageView iv_more, img_back;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private String GroupId = "";
    private String SortId="0";
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        initView();
        initListener();
//        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constant.ACTION_REFRESH_MESSAGE);
//
//        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        initData("0");
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText(getIntent().getExtras().getString("GroupName"));
        GroupId = getIntent().getExtras().getString("GroupId");
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");

        mListView = (ListView) this.findViewById(R.id.load_more_listview1);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 20)));
        mList = new ArrayList<GroupMemberEntity>();
        mAdapter = new GroupMemberAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) this.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                mList.clear();
                initData("0");
            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                SortId="<"+mList.get(mList.size()-1).getSortId();
                initData(SortId);
            }

        });
        iv_more = (ImageView) findViewById(R.id.iv_more);
        img_back = (ImageView) findViewById(R.id.img_back);
        iv_more.setVisibility(View.VISIBLE);
        iv_more.setImageResource(R.mipmap.qunzu);

    }

    private void initListener() {
        iv_more.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final GroupMemberEntity model= (GroupMemberEntity) adapterView.getItemAtPosition(i);
                if (model == null ||TextUtils.isEmpty(model.getHXNumber()) || "null".equals(model.getHXNumber())){
                    Toast.makeText(context,"聊天账号为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(GroupMemberActivity.this,ChatActivity.class);
                //intent.putExtra(EaseConstant.EXTRA_USER_ID,model.getMemberId());
                intent.putExtra(EaseConstant.EXTRA_USER_ID,model.getHXNumber());
                intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                //intent.putExtra(EaseConstant.EXTRA_NICKNAME,model.getMemberName());
                startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final GroupMemberEntity model= (GroupMemberEntity) adapterView.getItemAtPosition(i);
                Dialog dialog=new AlertDialog.Builder(GroupMemberActivity.this)
                        .setTitle("提示")
                        .setMessage("确认删除该成员？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.remove(i);
                                deleteMember(model);

                                dialog.dismiss();
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });
    }

    //删除群组
    private void deleteMember(final GroupMemberEntity model){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("groupid", GroupId);
        map.put("CustomerIdList", model.getMemberId());

        XUtil.Post(UrlUtils.URL_DelAppgroupCustomer,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")) {
//                        IMHelper.getInstance().removeUnread(model.getHXNumber());
                        Toast.makeText(GroupMemberActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
                mDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_more:
            {
                Intent intent=new Intent(this,FindSignListActivity.class);
                intent.putExtra("GroupId",GroupId);
                startActivityForResult(intent,0517);
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==0517){
                String CustomerIdList=data.getExtras().getString("ids");
                addGroup(CustomerIdList);

            }
        }


    }

    //获取群组成员列表数据
    private void initData(String sortId) {
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("GroupId", GroupId);
        map.put("SortId", sortId);
        XUtil.Post(UrlUtils.URl_ShowAllAppgroupCustomer,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")){
                        //                     是否还有数据
                        boolean ishave = false;
                        // 数据是否为空
                        boolean isnull = false;
                        List<GroupMemberEntity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<GroupMemberEntity>>() {
                        }.getType());
                        if (null != list) {
                            if (list.size() > 0) {
                                ishave = true;
                            }
                            for (GroupMemberEntity model : list) {
                                mList.add(model);
                            }
                        } else {
                            ishave = false;
                        }
                        mPtrFrameLayout.refreshComplete();
                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (mList.size() <= 0) {
                            isnull = true;
                        }
                        mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                        mAdapter.setList(mList);
                        mAdapter.notifyDataSetChanged();
//                        if (list != null && list.size() >0){
//                            Constant.sExecutorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (GroupMemberEntity groupMemberEntity:list){
//                                        //int count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                        int count = 0;
//                                        try {
//                                            count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        if (count > 0){
//                                            groupMemberEntity.setUnRead(true);
//                                        }else {
//                                            groupMemberEntity.setUnRead(false);
//                                        }
//                                        mList.add(groupMemberEntity);
//                                    }
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mAdapter.setList(mList);
//                                            mAdapter.notifyDataSetChanged();
//                                            mPtrFrameLayout.refreshComplete();
//                                            mDialog.dismiss();
//                                        }
//                                    });
//                                }
//                            });
//                        }else {
//                            mPtrFrameLayout.refreshComplete();
//                            mDialog.dismiss();
//                        }

                    } else {
                        Toast.makeText(GroupMemberActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
                mDialog.dismiss();

            }
        });

    }
    //添加群组成员
    private void addGroup(String id){
        if (id.equals("")){
            Toast.makeText(this,"请选择添加的成员",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("groupid", GroupId);
        map.put("CustomerIdList", id);

        XUtil.Post(UrlUtils.URL_AddAppgroupCustomer,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")) {
                        Toast.makeText(GroupMemberActivity.this,myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
                mDialog.dismiss();
            }
        });

    }

//    BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Constant.ACTION_REFRESH_MESSAGE)) {
//                Constant.sExecutorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (GroupMemberEntity groupMemberEntity:mList){
//                            //int count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                            int count = 0;
//                            try {
//                                count = IMHelper.getInstance().getUnreadMsg("linchao1");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (count > 0){
//                                groupMemberEntity.setUnRead(true);
//                            }else {
//                                groupMemberEntity.setUnRead(false);
//                            }
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.setList(mList);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                });
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        //mLocalBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
