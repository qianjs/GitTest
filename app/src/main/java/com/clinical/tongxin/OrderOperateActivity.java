package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.GridViewPicAdapter;
import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyGridView;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class OrderOperateActivity extends BaseActivity{
    // 进度
    private MyProgressDialog mDialog;
    private GridViewPicAdapter mAdapter;
    private MyGridView myGridView;
    private ImageView img_add;
    private EditText edtTxt_content;
    private String type="0";//订单分类 0申请退款 1申请调节
    private List<String> listpic;
    private Button btn_ok;
    private String orderId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderoperate);
        initView();
        initListener();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);

        edtTxt_content=(EditText)findViewById(R.id.edtTxt_content);
        listpic=new ArrayList<String>();
        img_add=(ImageView)findViewById(R.id.img_add);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        type=getIntent().getStringExtra("type");
        orderId=getIntent().getStringExtra("oid");
        if(type.equals("0"))
        {
            title.setText("申请退款");
        }
        else
        {
            title.setText("申请调节");
        }
        myGridView=(MyGridView)findViewById(R.id.gv_pic);
        mAdapter=new GridViewPicAdapter(this,listpic);
        myGridView.setAdapter(mAdapter);
    }
    private void initListener()
    {
        mAdapter.setOnClickClearImageListener(new GridViewPicAdapter.OnClickClearImageListener() {
            @Override
            public void OnClearImage(int position) {
                listpic.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderOperateActivity.this,SelectCameraDialog.class);
                Bundle bundleObject = new Bundle();
                GoClass myclass=new GoClass();
                myclass.setGoToClass(ReportActivity.class);
                bundleObject.putSerializable("myclass", myclass);
                intent.putExtras(bundleObject);
                startActivity(intent);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTxt_content.getText().toString().equals(""))
                {
                    Toast.makeText(OrderOperateActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                operate();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            ArrayList<String> list = (ArrayList<String>)bundle.getSerializable("list");
            listpic.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void operate()
    {
        if(type.equals("0")) {
            mDialog.show();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", getLoginUserSharedPre().getString("userId", ""));
            map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
            map.put("oId", orderId);
            map.put("content", edtTxt_content.getText().toString());
            for (int i = 0; i < listpic.size(); i++) {
                map.put("file" + i, new File(listpic.get(i)));
            }

            //map.put("file2", new File("/sdcard/DCIM/img_20160722070054.jpg"));
            XUtil.UpLoadFileAndText(UrlUtils.URL_ApplyRefunds, map, new MyCallBack<String>() {


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

                    mDialog.dismiss();
                    finish();
                }

            });
        }
        else
        {
            mDialog.show();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", getLoginUserSharedPre().getString("userId", ""));
            map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
            map.put("oId", orderId);
            map.put("content", edtTxt_content.getText().toString());
            for (int i = 0; i < listpic.size(); i++) {
                map.put("file" + i, new File(listpic.get(i)));
            }

            //map.put("file2", new File("/sdcard/DCIM/img_20160722070054.jpg"));
            XUtil.UpLoadFileAndText(UrlUtils.URL_ApplyResolve, map, new MyCallBack<String>() {


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

                    mDialog.dismiss();
                    finish();
                }

            });
        }
    }
}
