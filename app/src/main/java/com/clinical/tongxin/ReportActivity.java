package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * 我要举报
 * Created by 马骥 on 2016/7/30 0030.
 */
public class ReportActivity extends BaseActivity{
    // 进度
    private MyProgressDialog mDialog;
    private GridViewPicAdapter mAdapter;
    private MyGridView myGridView;
    private EditText edtTxt_content,edtTxt_name,edtTxt_phone,edtTxt_idnumber;
    private ImageView img_add;
    private RadioGroup rg_type;
    private Button btn_ok;
    private String type="0";//举报分类 0打假 1服务
    private String isRealName="true";//是否实名举报（true，false）
    private List<String> listpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
        initListener();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("我要举报");
        listpic=new ArrayList<String>();
        edtTxt_content=(EditText)findViewById(R.id.edtTxt_content);
        edtTxt_name=(EditText)findViewById(R.id.edtTxt_name);
        edtTxt_phone=(EditText)findViewById(R.id.edtTxt_phone);
        edtTxt_idnumber=(EditText)findViewById(R.id.edtTxt_idnumber);
        img_add=(ImageView)findViewById(R.id.img_add);
        rg_type=(RadioGroup)findViewById(R.id.rg_type);
        btn_ok=(Button)findViewById(R.id.btn_ok);
        type=getIntent().getStringExtra("type");
        ((RadioButton) rg_type.getChildAt(0)).setChecked(true);

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
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_type_shi)
                {
                    edtTxt_name.setEnabled(true);
                    edtTxt_phone.setEnabled(true);
                    edtTxt_idnumber.setEnabled(true);
                    isRealName="true";
                }
                else
                {
                    edtTxt_name.setText("");
                    edtTxt_phone.setText("");
                    edtTxt_idnumber.setText("");
                    edtTxt_name.setEnabled(false);
                    edtTxt_phone.setEnabled(false);
                    edtTxt_idnumber.setEnabled(false);
                    isRealName="false";
                }
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReportActivity.this,SelectCameraDialog.class);
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
                    Toast.makeText(ReportActivity.this, "举报内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isRealName.equals("true"))
                {
                    if(edtTxt_name.getText().toString().equals(""))
                    {
                        Toast.makeText(ReportActivity.this, "举报人姓名不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(edtTxt_phone.getText().toString().equals(""))
                    {
                        Toast.makeText(ReportActivity.this, "举报人电话不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(edtTxt_idnumber.getText().toString().equals(""))
                    {
                        Toast.makeText(ReportActivity.this, "举报人身份证号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                witnesses();
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
    private void witnesses()
    {
        mDialog.show();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
        map.put("type",type);
        map.put("content",edtTxt_content.getText().toString());
        map.put("isRealName",isRealName);
        map.put("name",edtTxt_name.getText().toString());
        map.put("phone",edtTxt_phone.getText().toString());
        map.put("idNumber",edtTxt_idnumber.getText().toString());
        for (int i=0;i<listpic.size();i++)
        {
            map.put("file"+i, new File(listpic.get(i)));
        }

        //map.put("file2", new File("/sdcard/DCIM/img_20160722070054.jpg"));
        XUtil.UpLoadFileAndText(UrlUtils.URL_Witnesses, map, new MyCallBack<String>() {


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
