package com.clinical.tongxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.EvaluateQuestionAdapter;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.TeamEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clinical.tongxin.R.id.empty_view;
import static com.clinical.tongxin.R.id.et_name;
import static com.clinical.tongxin.R.id.lv_task;
import static com.clinical.tongxin.R.id.rl_team_aptitude;
import static com.clinical.tongxin.R.id.tv_aptitude;
import static com.clinical.tongxin.R.id.tv_complaint;
import static com.clinical.tongxin.R.id.tv_complete;
import static com.clinical.tongxin.R.id.tv_finish;
import static com.clinical.tongxin.R.id.tv_manager;
import static com.clinical.tongxin.R.id.tv_num;
import static com.clinical.tongxin.R.id.tv_remark;
import static com.clinical.tongxin.R.id.tv_team_edit;
import static com.clinical.tongxin.R.id.tv_team_remove;


/**
 * 评价
 * @author LINCHAO
 * 2017/1/7
 */
@ContentView(R.layout.activity_evaluate)
public class  EvaluateActivity extends BaseActivity implements View.OnClickListener{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 问题列表
    @ViewInject(R.id.lv_question)
    ListView lv_question;

    // 评价
//    @ViewInject(R.id.et_evaluate)
    private EditText et_evaluate;

    // 提交
//    @ViewInject(R.id.tv_commit)
    private TextView tv_commit;

    private TextView tv_person;
    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private String taskId;
    private Context context;
    private EvaluateQuestionAdapter adapter;
    private List<String> questions;
    private String person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initData();
        initView();
        initLister();
        requestData();
    }

    private void initData() {
        if (getIntent().getStringExtra("taskId") != null){
            taskId = getIntent().getStringExtra("taskId");
        }
        if (getIntent().getStringExtra("person") != null){
            person = getIntent().getStringExtra("person");
        }
        userEntity = getLoginConfig();
        context = this;
        questions = new ArrayList<>();
        adapter = new EvaluateQuestionAdapter(context,questions);
    }

    private void initView() {
        tv_title.setText("评价信息");
        mDialog = new MyProgressDialog(this,"请稍后...");
        View headerView = LayoutInflater.from(context).inflate(R.layout.evaluate_header,null);
        //lv_question.addHeaderView(headerView);
        tv_person = (TextView) headerView.findViewById(R.id.tv_person);
        tv_person.setText(person);
        View footerView = LayoutInflater.from(context).inflate(R.layout.evaluate_footer,null);
        et_evaluate = (EditText) footerView.findViewById(R.id.et_evaluate);
//        tv_commit = (TextView) footerView.findViewById(R.id.tv_commit);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        //lv_question.addFooterView(footerView);
        lv_question.setAdapter(adapter);
    }


    /**
     * 初始化监听
     */
    private void initLister() {
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_commit:
                commitEvaluate();
                break;
        }
    }

    private void commitEvaluate() {
//        if (TextUtils.isEmpty(et_evaluate.getText())){
//            Toast.makeText(EvaluateActivity.this, "请添加评语", Toast.LENGTH_SHORT).show();
//            return;
//        }

        mDialog.show();

        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("TaskId", taskId);
        map.put("ReviewText",et_evaluate.getText().toString());
        map.put("TaskReviewItemNameList",adapter.questionStr());
        XUtil.Post(UrlUtils.URL_TASK_EVALUATE, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(EvaluateActivity.this, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj=new JSONObject(arg0);
                    if (obj.getString("code").equals("200")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK,new Intent(context,TaskDetailsActivity.class));
                                finish();
                            }
                        },1000);
                        Toast.makeText(EvaluateActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(EvaluateActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(EvaluateActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestData(){
        mDialog.show();
        Map map=new HashMap<>();
        map.put("SendOrReceive",userEntity.getRole().equals("接包方")?"1":"0");

        XUtil.Post(UrlUtils.URL_GET_QUESTION, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<List<String>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<String>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        List<String> list = resultEntity.getResult();
                        adapter.setList(list);
                    }else {

                        Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context,"获取列表失败",Toast.LENGTH_SHORT).show();
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


}
