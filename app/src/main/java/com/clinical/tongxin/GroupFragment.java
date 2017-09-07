package com.clinical.tongxin;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.GroupAdapter;
import com.clinical.tongxin.entity.GroupEntity;
import com.clinical.tongxin.entity.GroupMemberEntity;
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
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

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
 * Created by apple on 2016/11/22.
 */

public class GroupFragment extends Fragment {
    private List<GroupEntity> list;
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<GroupEntity> mList;
    private GroupAdapter mAdapter;
    private ImageView iv_more,img_back;
    private LinearLayout ll_back;
    private View network_error;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.groupfragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
        mList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        findNumber();
    }

    private void initView() {
        title= (TextView) getActivity().findViewById(R.id.title);
        title.setText("群组");

        // 加载中
        mDialog = new MyProgressDialog(getActivity(), "请稍后...");

        mListView = (ListView) getActivity().findViewById(R.id.load_more_listview1);
        View headerMarginView = new View(getActivity());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 20)));
        mList = new ArrayList<GroupEntity>();
        mAdapter = new GroupAdapter(getActivity(),mList );
        mListView.setAdapter(mAdapter);
        // .设置下拉刷新组件和事件监听
//        mPtrFrameLayout = (PtrFrameLayout) getActivity().findViewById(R.id.load_more_list_view_ptr_frame);
//        mPtrFrameLayout.setLoadingMinTime(1000);
//        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                // TODO Auto-generated method stub
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                // TODO Auto-generated method stub
//                mList.clear();
////                initData("0");
//            }
//
//        });
//        // 4.加载更多的组件
//        mLoadMoreListViewContainer = (LoadMoreListViewContainer) getActivity().findViewById(R.id.load_more_list_view_container);
//        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
//        mLoadMoreListViewContainer.useDefaultHeader();
//        // 5.添加加载更多的事件监听
//        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
//            @Override
//            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
//            }
//
//        });
        iv_more= (ImageView) getActivity().findViewById(R.id.iv_more);
        ll_back= (LinearLayout) getActivity().findViewById(R.id.ll_back);
        iv_more.setVisibility(View.VISIBLE);
//        iv_more.setImageDrawable(R.mipmap.qunzu);
        iv_more.setImageResource(R.mipmap.qunzu);
        ll_back.setVisibility(View.GONE);
        network_error = getActivity().findViewById(R.id.network_error);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CONNECT_OK);
        filter.addAction(Constant.ACTION_CONNECT_ERROR);
//        filter.addAction(Constant.ACTION_REFRESH_MESSAGE);
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    private void initListener() {
        //创建群组对话框
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final  PopwindowAddGroup popwindowAddGroup=new PopwindowAddGroup(getActivity());
                popwindowAddGroup.setOnConfirmClickListener(new PopwindowAddGroup.OnConfirmClickListener() {
                    @Override
                    public void onconfirm(String Name) {
                        addGroup( Name);
                        popwindowAddGroup.dismiss();

                    }
                });
                popwindowAddGroup.showAtLocation(iv_more, Gravity.CENTER,0,0);

            }
        });
        //进入群组成员
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupEntity model= (GroupEntity) adapterView.getItemAtPosition(i);
                if (model!=null){
                    Intent intent=new Intent(getActivity(),GroupsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("GroupEntity",model);
//                    intent.putExtras(bundle);
                    intent.putExtra("GroupId",model.getGroupid());
                    intent.putExtra("GroupName",model.getGroupName());
                    startActivity(intent);
                }
            }
        });
        //删除群组列表的提示框
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final GroupEntity model= (GroupEntity) adapterView.getItemAtPosition(i);
                Dialog dialog=new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确认删除该群组？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteGroup(model.getGroupid(),i);

                                dialog.dismiss();

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
    //创建群组
    private void addGroup(String Name){
        String name=Name.replace("\n","");
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        map.put("GroupName", name);
        XUtil.Post(UrlUtils.URL_AddAppgroup,map,new MyCallBack<String>(){
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
                        Toast.makeText(getActivity(),myjson.getMsg(),Toast.LENGTH_SHORT).show();
                        mList.clear();
                        findNumber();
                    }else{
                        Toast.makeText(getActivity(),myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    }
                mDialog.dismiss();

            }
        });

    }


    //获取群组列表数据
    private void findNumber() {
        mDialog.show();
        Map<String, String> map = new HashMap<>();

        map.put("CustomerId", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Post(UrlUtils.URL_ShowAllAppgroup,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                mList.clear();
                if (myjson != null) {
                    if (myjson.getCode().equals("200")){
                        List<GroupEntity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<GroupEntity>>() {
                        }.getType());
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
//                        if (list != null && list.size() > 0){
//                            Constant.sExecutorService.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (GroupEntity groupEntity : list){
//                                        int countGroup = 0;
//                                        for (GroupMemberEntity groupMemberEntity:groupEntity.getMemberList()){
//                                            //int count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                            int count = 0;
//                                            try {
//                                                count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                            countGroup = count +countGroup;
//                                        }
//                                        if (countGroup > 0){
//                                            groupEntity.setUnRead(true);
//                                        }else {
//                                            groupEntity.setUnRead(false);
//                                        }
//                                        mList.add(groupEntity);
//                                    }
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mAdapter.notifyDataSetChanged();
//                                            mDialog.dismiss();
//                                        }
//                                    });
//                                }
//                            });
//
//                        }else {
//                            mDialog.dismiss();
//                        }

                    } else {
                        Toast.makeText(getActivity(),myjson.getMsg(),Toast.LENGTH_SHORT).show();
                }
                }
                mDialog.dismiss();

            }
        });

    }

    //删除群组
    private void deleteGroup(String id ,final int i){
        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", ((BaseActivity)getActivity()).getLoginUserSharedPre().getString("Ukey", ""));
        map.put("groupid", id);
        XUtil.Post(UrlUtils.URL_delAppgroup,map,new MyCallBack<String>(){
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
                        mList.remove(i);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),myjson.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    }
                mDialog.dismiss();
            }
        });

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_CONNECT_OK)) {
                network_error.setVisibility(View.GONE);
            }else if (intent.getAction().equals(Constant.ACTION_CONNECT_ERROR)){
                network_error.setVisibility(View.VISIBLE);
            }else if (intent.getAction().equals(Constant.ACTION_REFRESH_MESSAGE)){
//                Constant.sExecutorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (GroupEntity groupEntity : mList){
//                            int countGroup = 0;
//                            for (GroupMemberEntity groupMemberEntity:groupEntity.getMemberList()){
//                                //int count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                int count = 0;
//                                try {
//                                    count = IMHelper.getInstance().getUnreadMsg(groupMemberEntity.getHXNumber());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                countGroup = count +countGroup;
//                            }
//                            if (countGroup > 0){
//                                groupEntity.setUnRead(true);
//                            }else {
//                                groupEntity.setUnRead(false);
//                            }
//                            //mList.add(groupEntity);
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                });
            }
        }
    };

    @Override
    public void onDestroy() {
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
