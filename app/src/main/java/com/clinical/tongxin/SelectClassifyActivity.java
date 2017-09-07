package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.entity.MyBaseEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.TypeMenu;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class SelectClassifyActivity extends BaseActivity{
    // 进度
    private MyProgressDialog mDialog;
    private TypeMenu typeMenu;
    ArrayList<MyBaseEntity> groups=new ArrayList<MyBaseEntity>();
    SparseArray<LinkedList<MyBaseEntity>> children = new SparseArray<LinkedList<MyBaseEntity>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_classify);
        initView();
        initListener();
        findClassify();
    }
    private void initView()
    {
        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        TextView title=(TextView)findViewById(R.id.title);
        title.setText("选择分类");
        typeMenu=(TypeMenu)findViewById(R.id.commtype);


    }
    private void initListener()
    {
        typeMenu.setOnSelectListener(new TypeMenu.OnSelectListener() {
            @Override
            public void getValue(MyBaseEntity showModel,int pPosition,int cPosition) {
                //Toast.makeText(SelectClassifyActivity.this, showModel.getId()+showModel.getText(), Toast.LENGTH_SHORT).show();
                Bundle bundleObject = getIntent().getExtras();
                GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
                Intent intent=new Intent(SelectClassifyActivity.this,goClass.getGoToClass());
                intent.putExtra("typeId",showModel.getId());
                intent.putExtra("typeName",showModel.getText());
                intent.putExtra("pPosition",pPosition);
                intent.putExtra("cPosition",cPosition);
                startActivity(intent);
                finish();
            }
        });
    }
    private void findClassify()
    {
        mDialog.show();


        XUtil.Get(UrlUtils.URL_FindClassify, null, new MyCallBack<String>() {

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
                    // 是否还有数据
                    try {
                        JSONArray array=new JSONArray(myjson.getI().getList());
                        for (int i=0;i<array.length();i++)
                        {
                            JSONObject obj=array.getJSONObject(i);
                            String pId=obj.getString("pId");
                            String pName=obj.getString("pName");
                            MyBaseEntity model=new MyBaseEntity();
                            model.setId(pId);
                            model.setText(pName);
                            groups.add(model);
                            JSONArray array1=obj.getJSONArray("cList");
                            LinkedList<MyBaseEntity> tItem = new LinkedList<MyBaseEntity>();
                            for (int j=0;j<array1.length();j++)
                            {
                                JSONObject obj2=array1.getJSONObject(j);
                                String cId=obj2.getString("cId");
                                String cName=obj2.getString("cName");
                                MyBaseEntity model2=new MyBaseEntity();
                                model2.setId(cId);
                                model2.setText(cName);
                                tItem.add(model2);

                            }
                            children.put(i,tItem);
                        }
                        typeMenu.setDataSource(groups,children);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }
}
