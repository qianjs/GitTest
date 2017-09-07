package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.TeamMemberAdapter;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.MemberEntity;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
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
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.clinical.tongxin.R.id.et_name;
import static com.clinical.tongxin.R.id.tv_aptitude;


/**
 * 选择人员
 * @author LINCHAO
 * 2016/12/26
 */
@ContentView(R.layout.activity_choose_member)
public class ChooseMemberActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 搜索框
    @ViewInject(R.id.et_search)
    EditText et_search;

    // 清除按钮
    @ViewInject(R.id.iv_search_clear)
    ImageView iv_search_clear;

    // 团队列表
    @ViewInject(R.id.lv_member)
    ListView lv_member;

    // 空结果
    @ViewInject(R.id.empty_view)
    View empty_view;
    // 完成
    @ViewInject(R.id.tv_complete)
    TextView tv_complete;
    private String keyWord; // 电话号

    private MyProgressDialog dialog;
    private List<MemberEntity> memberList;
    private UserEntity userEntity;
    private TeamMemberAdapter adapter;
    private List<MemberEntity> checkData;

    private int type; // 1 单选项目经理 2 多选人员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view
        initData();
        initView();
        initLister();
    }

    private void initView() {
        tv_complete.setVisibility(View.VISIBLE);
        tv_title.setText(type == 1?"选择项目经理":"添加人员");
        et_search.setHint("请输入人员手机号");
        memberList = new ArrayList<>();
        adapter = new TeamMemberAdapter(this,memberList,type);
        lv_member.setAdapter(adapter);
        dialog=new MyProgressDialog(this,"请稍等...");
        userEntity = getLoginConfig();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getIntent().getExtras() != null){
            type = getIntent().getIntExtra("type",0);
        }
    }

    /**
     * 初始化监听
     */
    private void initLister() {
        tv_complete.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        hideSoftKeyboard();
                    requestData(keyWord);
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



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_search_clear:
                et_search.setText("");
                iv_search_clear.setVisibility(View.GONE);
                break;
            case R.id.tv_complete:
                checkData = adapter.getCheckMember();
                if (checkData == null || checkData.size() == 0){
                    Toast.makeText(ChooseMemberActivity.this,type == 1?"请选择项目经理":"请选择人员",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("member", (Serializable) checkData);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }

    private void requestData(String key){
        dialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("Mobile",key);


        XUtil.Post(UrlUtils.URL_GET_USER_LIST, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    memberList.clear();
                    Gson gson = new Gson();
                    ResultEntity<List<MemberEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<MemberEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){


                        List<MemberEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            memberList.addAll(list);
                        }else {

                            Toast.makeText(ChooseMemberActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (memberList.size() <= 0) {

                            empty_view.setVisibility(View.VISIBLE);
                            lv_member.setVisibility(View.GONE);
                        }else {

                            empty_view.setVisibility(View.GONE);
                            lv_member.setVisibility(View.VISIBLE);
                        }
                        adapter.setList(memberList);
                    }else {
                        Toast.makeText(ChooseMemberActivity.this,"获取人员失败",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(ChooseMemberActivity.this,"获取人员失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);

                Toast.makeText(ChooseMemberActivity.this,"获取人员失败",Toast.LENGTH_SHORT).show();
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


}
