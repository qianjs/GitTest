package com.clinical.tongxin;

import android.content.Context;
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
import com.clinical.tongxin.entity.MyBudgetEntity;
import com.clinical.tongxin.entity.ResultEntity;
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

import static com.clinical.tongxin.R.id.et_evaluate;
import static com.clinical.tongxin.R.id.lv_question;
import static com.clinical.tongxin.R.id.tv_commit;


/**
 * 我的预算
 * @author LINCHAO
 * 2017/1/22
 */
@ContentView(R.layout.activity_budget)
public class BudgetActivity extends BaseActivity{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;

    // 进行中
    @ViewInject(R.id.tv_ing)
    TextView tv_ing;
    // 已使用
    @ViewInject(R.id.tv_use)
    TextView tv_use;
    // 未使用
    @ViewInject(R.id.tv_unuse)
    TextView tv_unuse;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); // xUtils 初始化view

        initData();
        initView();
        requestData();
    }

    private void initData() {
        userEntity = getLoginConfig();
        context = this;
    }

    private void initView() {
        mDialog = new MyProgressDialog(this,"请稍后...");
        tv_title.setText("我的预算");
    }


    private void requestData(){
        mDialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());

        XUtil.Post(UrlUtils.URL_GET_BUDGET, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<MyBudgetEntity> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<MyBudgetEntity>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        setText(resultEntity.getResult());
                    }else {

                        Toast.makeText(context,"获取信息失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context,"获取信息失败",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context,"获取信息失败",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void setText(MyBudgetEntity result) {
        try {
            tv_ing.setText(String.valueOf(result.getIng()));
            tv_use.setText(String.valueOf(result.getUse()));
            tv_unuse.setText(String.valueOf(result.getUnuse()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
