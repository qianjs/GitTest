package com.clinical.tongxin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.ProjectDetailEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class SubmitOrderActivity extends BaseActivity {
    // 进度
    private MyProgressDialog mDialog;
    private ProjectDetailEntity pModel;
    private ImageView img_pic;
    private TextView tv_name,tv_subTitle,tv_aCharge,tv_price,tv_phone,tv_aCharge2;
    private TextView tv_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitorder);
        initView();
        initListener();
        showInfo();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title= (TextView) findViewById(R.id.title);
        title.setText("提交订单");
        Bundle bundle=getIntent().getExtras();
        pModel= (ProjectDetailEntity) bundle.getSerializable("pmodel");
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_subTitle= (TextView) findViewById(R.id.tv_subTitle);
        tv_aCharge= (TextView) findViewById(R.id.tv_aCharge);
        tv_price= (TextView) findViewById(R.id.tv_price);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_aCharge2= (TextView) findViewById(R.id.tv_aCharge2);
        tv_submit= (TextView) findViewById(R.id.tv_submit);

    }
    private void showInfo()
    {
        tv_name.setText(pModel.getName());
        tv_subTitle.setText(pModel.getSubTitle());
        tv_aCharge.setText("预付款：¥"+pModel.getaPrice());
        tv_price.setText(pModel.getPrice());
        tv_phone.setText(getLoginUserSharedPre().getString("phone",""));
        tv_aCharge2.setText("¥"+pModel.getaPrice());
    }
    private void initListener()
    {
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
            }
        });
    }
    private void submitOrder()
    {
        mDialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
        map.put("pId", pModel.getpId());
        map.put("aCharge", pModel.getaPrice());
        map.put("sum", pModel.getPrice());
        map.put("telphone", getLoginUserSharedPre().getString("phone", ""));

        XUtil.Post(UrlUtils.URL_GenerateOrder, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP myjson = Utils.wsJsonToModel(json);
                if (myjson != null) {
                    finish();
                }
                mDialog.dismiss();
            }

        });
    }
}
