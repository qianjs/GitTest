package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ManagerMemberAdapter;
import com.clinical.tongxin.entity.ManagerMemberEntity;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
 * 选择人员
 * @author LINCHAO
 * 2016/12/26
 */
@ContentView(R.layout.activity_manager_member)
public class ManagerMemberActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 团队列表
    @ViewInject(R.id.lv_member)
    ListView lv_member;

    // 空结果
    @ViewInject(R.id.empty_view)
    View empty_view;
    // 完成
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;

    @ViewInject(R.id.load_more_list_view_ptr_frame)
    PtrFrameLayout mPtrFrameLayout;

    @ViewInject(R.id.load_more_list_view_container)
    LoadMoreListViewContainer mLoadMoreListViewContainer;

    private String keyWord;

    @ViewInject(R.id.tv_team_remove)
    TextView tv_team_remove;

    @ViewInject(R.id.tv_team_edit)
    TextView tv_team_edit;

    private MyProgressDialog dialog;
    private List<ManagerMemberEntity> memberList;
    private UserEntity userEntity;
    private ManagerMemberAdapter adapter;
//    private ManagerMemberEntity memberManager;
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    private static final int CHOOSE_MEMBER = 2001;
//    private Handler handler = new Handler(){
//
//        @Override
//        public void dispatchMessage(Message msg) {
//            super.dispatchMessage(msg);
//            switch (msg.what){
//                case ManagerMemberAdapter.MANAGER_DATA:
//                    memberManager = (ManagerMemberEntity) msg.obj;
//                    break;
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        initData();
        initView();
        initLister();
        requestManagerData("0");
    }

    private void initView() {
        tv_title.setText(userEntity.getRole());
        memberList = new ArrayList<>();
        adapter = new ManagerMemberAdapter(this,memberList);
        lv_member.setAdapter(adapter);
        dialog=new MyProgressDialog(this,"请稍等...");
        userEntity = getLoginConfig();

        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_member, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                requestManagerData("0");
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

                if (memberList.size() > 0){
                    String maxid = memberList.get(memberList.size() - 1).getSortId();
                    requestManagerData("<"+maxid);
                }else {
                    requestManagerData("0");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userEntity = getLoginConfig();
    }

    /**
     * 初始化监听
     */
    private void initLister() {
        tv_complete.setOnClickListener(this);
        tv_team_remove.setOnClickListener(this);
        tv_team_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_team_remove:
                isEdit(true);
                adapter.setEdit(true);
                break;

            case R.id.tv_team_edit:
                Intent intent1 = new Intent(ManagerMemberActivity.this,ChooseMemberActivity.class);
                intent1.putExtra("type",2);
                startActivityForResult(intent1,CHOOSE_MEMBER);
                break;
            case R.id.tv_complete:
                List<ManagerMemberEntity> managerMemberEntities = adapter.getList();
                if (managerMemberEntities == null || managerMemberEntities.size() == 0){
                    isEdit(false);
                    adapter.setEdit(false);
                    return;
                }
                delMember(managerMemberEntities);

                break;
        }
    }


    private void requestManagerData(final String sortId){
        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                        mPtrFrameLayout.refreshComplete();
//                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
//                        memberList.addAll(testData());
//                        adapter.setList(memberList);
//                    }
//                });
//            }
//        },2000);
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("sortId", sortId);

        XUtil.Post("项目经理".equals(userEntity.getRole())?UrlUtils.URL_MANAGER_GET_LIST:UrlUtils.URL_GET_CONTRACTOR_MEMBER,
                map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        memberList.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<ManagerMemberEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<ManagerMemberEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<ManagerMemberEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave = false;
                            }else {
                                ishave = true;
                            }

                            memberList.addAll(list);
                        }else {
                            ishave = false;
                            Toast.makeText(ManagerMemberActivity.this,"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (memberList.size() <= 0) {
                            isnull = true;
                            empty_view.setVisibility(View.VISIBLE);
                            lv_member.setVisibility(View.GONE);
                        }else {
                            isnull = false;
                            empty_view.setVisibility(View.GONE);
                            lv_member.setVisibility(View.VISIBLE);
                        }

                        adapter.setList(memberList);
                    }else {
                        Toast.makeText(ManagerMemberActivity.this,"获取人员列表失败",Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(ManagerMemberActivity.this,"获取人员列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(ManagerMemberActivity.this,"获取人员列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private List<ManagerMemberEntity> testData(){
        List<ManagerMemberEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ManagerMemberEntity member = new ManagerMemberEntity();
            member.setNickName("林"+i);
            member.setCustomerId(""+i);
            member.setMobile("131"+i+"123456"+i);
            list.add(member);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null){
            if (requestCode == CHOOSE_MEMBER){
                try {
                    List<MemberEntity> memberEntities = (List<MemberEntity>) data.getSerializableExtra("member");
                    dialog.show();
                    addMember(memberEntities);
                } catch (Exception e) {
                    Toast.makeText(ManagerMemberActivity.this,"添加人员失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
    }

    private void addMember(List<MemberEntity> memberEntities) {
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("CustomerIdList",addMemberIdList(memberEntities));

        XUtil.Post("项目经理".equals(userEntity.getRole())?UrlUtils.URL_MANAGER_ADD_MEMBER:UrlUtils.URL_ADD_CONTRACTOR_MEMBER, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultJsonP1 resultJsonP1 = gson.fromJson(arg0,ResultJsonP1.class);
                    if (resultJsonP1 != null && "200".equals(resultJsonP1.getCode())){
                        Toast.makeText(ManagerMemberActivity.this,"已发送邀请，等待人员确认",Toast.LENGTH_SHORT).show();
                        requestManagerData("0");
                    }else {
                        Toast.makeText(ManagerMemberActivity.this,"添加人员失败",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(ManagerMemberActivity.this,"添加人员失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(ManagerMemberActivity.this,"添加人员失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    private void delMember(List<ManagerMemberEntity> managerMemberEntities) {
        dialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("CustomerIdList",delMemberIdList(managerMemberEntities));

        XUtil.Post("项目经理".equals(userEntity.getRole())?UrlUtils.URL_MANAGER_DEL_MEMBER:UrlUtils.URL_DEL_CONTRACTOR_MEMBER,
                map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultJsonP1 resultJsonP1 = gson.fromJson(arg0,ResultJsonP1.class);
                    if (resultJsonP1 != null && "200".equals(resultJsonP1.getCode())){
                        Toast.makeText(ManagerMemberActivity.this,"删除人员成功",Toast.LENGTH_SHORT).show();
                        isEdit(false);
                        adapter.setEdit(false);
                        adapter.notifyDataSetChanged();
                        requestManagerData("0");

                    }else {
                        Toast.makeText(ManagerMemberActivity.this,"删除人员失败",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(ManagerMemberActivity.this,"删除人员失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(ManagerMemberActivity.this,"删除人员失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    private String addMemberIdList(List<MemberEntity> memberEntities) {
        StringBuffer sb = new StringBuffer();
        for (MemberEntity memberEntity : memberEntities){
            sb.append(memberEntity.getCustomerId()).append(",");
        }
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf(",") != -1){
            String text = sb.toString().substring(0,sb.toString().lastIndexOf(","));
            return text;
        }
        return sb.toString();
    }
    private String delMemberIdList(List<ManagerMemberEntity> memberEntities) {
        StringBuffer sb = new StringBuffer();
        for (ManagerMemberEntity memberEntity : memberEntities){
            sb.append(memberEntity.getCustomerId()).append(",");
        }
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf(",") != -1){
            String text = sb.toString().substring(0,sb.toString().lastIndexOf(","));
            return text;
        }

        return sb.toString();
    }

    private void isEdit(boolean editing){
        if (editing){
            tv_complete.setVisibility(View.VISIBLE);
            tv_team_edit.setVisibility(View.GONE);
            tv_team_remove.setVisibility(View.GONE);
        }else {
            tv_complete.setVisibility(View.GONE);
            tv_team_edit.setVisibility(View.VISIBLE);
            tv_team_remove.setVisibility(View.VISIBLE);
        }
    }
}
