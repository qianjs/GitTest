package com.clinical.tongxin;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ArbitrationEntity;
import com.clinical.tongxin.entity.MyBudgetEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import static com.clinical.tongxin.R.id.tv_ing;
import static com.clinical.tongxin.R.id.tv_unuse;
import static com.clinical.tongxin.R.id.tv_use;


/**
 * 仲裁结果
 * @author LINCHAO
 * 2017/1/22
 */
@ContentView(R.layout.activity_arbitration)
public class ArbitrationInfoActivity extends BaseActivity{

    // 标题
    @ViewInject(R.id.title)
    TextView tv_title;


    @ViewInject(R.id.tv_maker)
    TextView tv_maker;


    @ViewInject(R.id.tv_make_time)
    TextView tv_make_time;

    @ViewInject(R.id.tv_make_reason)
    TextView tv_make_reason;

    @ViewInject(R.id.tv_disposer)
    TextView tv_disposer;

    @ViewInject(R.id.tv_dispose_time)
    TextView tv_dispose_time;

    @ViewInject(R.id.tv_dispose_reason)
    TextView tv_dispose_reason;
    @ViewInject(R.id.tv_question)
    TextView tv_question;

    private UserEntity userEntity;
    private MyProgressDialog mDialog;
    private Context context;
    private String taskId;

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
        if (getIntent().getStringExtra("taskId") != null){
            taskId = getIntent().getStringExtra("taskId");

        }
    }

    private void initView() {
        mDialog = new MyProgressDialog(this,"请稍后...");
        tv_title.setText("仲裁结果");
    }


    private void requestData(){
        mDialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
        map.put("TaskId",taskId);

        XUtil.Post(UrlUtils.URL_queryTaskArbitrateInfo, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<ArbitrationEntity> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<ArbitrationEntity>>(){}.getType());
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

    private void setText(ArbitrationEntity result) {
        try {
            tv_maker.setText(result.getMaker());
            tv_make_reason.setText(result.getMakeReason());
            tv_make_time.setText(result.getMakeTime());
            tv_disposer.setText(result.getDisposer());
            tv_dispose_reason.setText(result.getDisposeReason());
            tv_dispose_time.setText(result.getDisposeTime());
            tv_question.setText(result.getQestion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
