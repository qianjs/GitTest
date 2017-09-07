package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.TeamManagementAdapter;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.OrderEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
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

import static com.clinical.tongxin.R.id.lv_member;
import static com.clinical.tongxin.R.id.tv_complete;


/**
 * 团队管理
 * @author LINCHAO
 * 2016/12/26
 */
@ContentView(R.layout.activity_team_management)
public class TeamManagementActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 更多
    @ViewInject(R.id.iv_more)
    ImageView iv_more;

    // 搜索框
    @ViewInject(R.id.et_search)
    EditText et_search;

    // 清除按钮
    @ViewInject(R.id.iv_search_clear)
    ImageView iv_search_clear;

    // 团队列表
    @ViewInject(R.id.lv_team)
    ListView lv_team;

    // 空结果
    @ViewInject(R.id.empty_view)
    View empty_view;

    @ViewInject(R.id.load_more_list_view_ptr_frame)
    PtrFrameLayout mPtrFrameLayout;

    @ViewInject(R.id.load_more_list_view_container)
    LoadMoreListViewContainer mLoadMoreListViewContainer;

    private List<TeamEntity> teamEntityList;
    private TeamManagementAdapter adapter;
    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private String keyWord;
    private static final int CREATE_GROUP = 3001;
    // 是否还有数据
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initView();
        initLister();
        requestData("0","");
    }

    private void initView() {

        mDialog = new MyProgressDialog(this,"请稍后...");
        tv_title.setText(getResources().getString(R.string.team_management));
        et_search.setHint(R.string.team_search);
        iv_more.setVisibility(View.VISIBLE);
        iv_more.setImageResource(R.mipmap.qunzu);
        teamEntityList = new ArrayList<>();
        adapter = new TeamManagementAdapter(this,teamEntityList);
        lv_team.setAdapter(adapter);
        userEntity = getLoginConfig();


        // .设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_team, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                requestData("0",keyWord);
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
                if (teamEntityList.size() > 0){
                    String maxid = teamEntityList.get(teamEntityList.size() - 1).getSortId();
                    requestData("<"+maxid,keyWord);
                }else {
                    requestData("0",keyWord);
                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 初始化监听
     */
    private void initLister() {
        iv_more.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    hideSoftKeyboard();
                    requestData("0",keyWord);
                    return true;
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = et_search.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())){
                    iv_search_clear.setVisibility(View.GONE);
                }else {
                    iv_search_clear.setVisibility(View.VISIBLE);
                }
            }
        });
        lv_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TeamManagementActivity.this,TeamDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TeamEntity", (Serializable) adapterView.getAdapter().getItem(i));
                intent.putExtras(bundle);
                startActivityForResult(intent,CREATE_GROUP);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_search_clear:
                et_search.setText("");
                iv_search_clear.setVisibility(View.GONE);
                break;
            case R.id.iv_more:
                Intent intent = new Intent(TeamManagementActivity.this, TeamCreateActivity.class);

                startActivityForResult(intent,CREATE_GROUP);
                // 跳转
                break;
        }
    }

    private void requestData(final String sortId,String key){
        mDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDialog.dismiss();
//                        mPtrFrameLayout.refreshComplete();
//                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
//                        teamEntityList.addAll(testData());
//                        adapter.setList(teamEntityList);
//                    }
//                });
//            }
//        },2000);
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("sortId",sortId);
        if (!TextUtils.isEmpty(key)){
            Gson gson = new Gson();

            KeywordEntity keyword = new KeywordEntity();
            keyword.setField("name");
            keyword.setOp("contains");
            keyword.setValue(key);
            List<KeywordEntity> list = new ArrayList<>();
            list.add(keyword);
            String keyJson = gson.toJson(list,new TypeToken<List<KeywordEntity>>(){}.getType());
            map.put("filterRules",keyJson);
        }

        XUtil.Post(UrlUtils.URL_TEAM_GET_LIST, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        teamEntityList.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TeamEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TeamEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<TeamEntity> list = resultEntity.getResult();
                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave = false;
                            }else {
                                ishave = true;
                            }
                            teamEntityList.addAll(list);
                        }else {
                            ishave = false;
                            Toast.makeText(TeamManagementActivity.this,"没有更多数据",Toast.LENGTH_SHORT).show();
                        }


                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (teamEntityList.size() <= 0) {
                            isnull = true;
                            empty_view.setVisibility(View.VISIBLE);
                            lv_team.setVisibility(View.GONE);
                        }else {
                            isnull = false;
                            empty_view.setVisibility(View.GONE);
                            lv_team.setVisibility(View.VISIBLE);
                        }

                        adapter.setList(teamEntityList);
                    }else {
                        Toast.makeText(TeamManagementActivity.this,"获取团队列表失败",Toast.LENGTH_SHORT).show();
                    }
                    mPtrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                } catch (Exception e) {
                    Toast.makeText(TeamManagementActivity.this,"获取团队列表失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mPtrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(TeamManagementActivity.this,"获取团队列表失败",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
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

    private List<TeamEntity> testData(){
        List<TeamEntity> teamEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TeamEntity team = new TeamEntity();
            team.setDirectorGroupId(""+i);
            team.setComplaitCnt("6");
            team.setFinish("50");
            team.setProjectManagerCustomerName("linchao"+i);
            team.setRemark("项目完成时间2016/12/12 负责人纱礼服的斯洛伐克速度发技术历史记录打飞机奥斯卡了大家弗兰克");
            team.setName("项目组"+i);
            teamEntities.add(team);
        }
        return teamEntities;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){

            et_search.setText("");
            requestData("0",keyWord);

        }
    }

    private List<TeamEntity> searchData(){
        List<TeamEntity> teamEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TeamEntity team = new TeamEntity();
            team.setName("lin"+i);
            teamEntities.add(team);
        }
        return teamEntities;
    }

}
