package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by lzj667 on 2016/8/26.
 * 发布疑问
 */
public class ReleaseQuestionsActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private EditText edtTxt_problem,edtTxt_title;
    private Button btn_ok;
    //分类编号
    private String typeId="";
    //专家编号
    private String eId="";
    //添加照片
    private ImageView img_add;
    private List<String> listpic;
    private MyGridView myGridView;
    private GridViewPicAdapter mAdapter;
    private MyProgressDialog dialog;
    private RelativeLayout rl_selecttype;
    private TextView tv_type;
    private PopWindowSelectProject popWindowSelectProject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releasequestions);
        initView();
        initListener();
    }

    private void initView() {
        dialog=new MyProgressDialog(this,"请稍等...");
        title= (TextView) findViewById(R.id.title);
        title.setText("发布疑问");
        listpic=new ArrayList<>();
        edtTxt_problem= (EditText) findViewById(R.id.edtTxt_problem);
        edtTxt_title= (EditText) findViewById(R.id.edtTxt_title);
        rl_selecttype= (RelativeLayout) findViewById(R.id.rl_selecttype);
        tv_type= (TextView) findViewById(R.id.tv_type);
        eId=getIntent().getStringExtra("eId");
        btn_ok= (Button) findViewById(R.id.btn_ok);
        img_add= (ImageView) findViewById(R.id.img_add);
        myGridView=(MyGridView)findViewById(R.id.gv_pic);
        mAdapter=new GridViewPicAdapter(this,listpic);
        myGridView.setAdapter(mAdapter);

    }

    private void initListener() {
        btn_ok.setOnClickListener(this);
        img_add.setOnClickListener(this);
        rl_selecttype.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                releasequestios();
                break;
            case R.id.rl_selecttype:
                if(popWindowSelectProject==null)
                {
                    popWindowSelectProject=new PopWindowSelectProject(ReleaseQuestionsActivity.this);
                    popWindowSelectProject.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                    popWindowSelectProject.setOnSelectItemChangeListener(new PopWindowSelectProject.OnSelectItemChangeListener() {
                        @Override
                        public void onSelectChange(String paramString, String id) {
                            //Toast.makeText(MyBeautyActivity.this, paramString, Toast.LENGTH_SHORT).show();
                            tv_type.setText(paramString);
                            typeId = id;

                        }
                    });

                    popWindowSelectProject.ShowData();
                    popWindowSelectProject.showAsDropDown(rl_selecttype);

                }
                else
                {
                    popWindowSelectProject.showAsDropDown(rl_selecttype);
                }
                break;
            case R.id.img_add:
                Intent intent = new Intent(ReleaseQuestionsActivity.this, SelectCameraDialog.class);
                Bundle bundleObject = new Bundle();
                GoClass myclass = new GoClass();
                myclass.setGoToClass(ReleaseQuestionsActivity.class);
                bundleObject.putSerializable("myclass", myclass);
                intent.putExtras(bundleObject);
                startActivity(intent);
                break;
        }
    }

    private void releasequestios() {
        dialog.show();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("uid",getLoginUserSharedPre().getString("userId", ""));
        map.put("ukey", getLoginUserSharedPre().getString("ukey", ""));
        map.put("eId",eId);
        map.put("classifyId",typeId);
        map.put("text",edtTxt_problem.getText().toString());
        for (int i=0;i<listpic.size();i++)
        {
            map.put("file"+i, new File(listpic.get(i)));
        }
        XUtil.UpLoadFileAndText(UrlUtils.URl_ReleaseQuestions,map,new MyCallBack<String>(){
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP resultJsonp= Utils.wsJsonToModel(arg0);
                if (resultJsonp!=null){

                    finish();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
        {
            Bundle bundle=intent.getExtras();
            ArrayList<String> list = (ArrayList<String>)bundle.getSerializable("list");
            listpic.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
