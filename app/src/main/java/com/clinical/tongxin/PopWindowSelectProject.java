package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

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
 * Created by Administrator on 2016/7/29 0029.
 */
public class PopWindowSelectProject extends PopupWindow {
    private View conentView;
    private OnSelectItemChangeListener listener;
    private LayoutInflater inflater;
    private MyProgressDialog mDialog;
    private Context context;
    private TypeMenu typeMenu;
    ArrayList<MyBaseEntity> groups=new ArrayList<MyBaseEntity>();
    SparseArray<LinkedList<MyBaseEntity>> children = new SparseArray<LinkedList<MyBaseEntity>>();
    private int pPosition=0,cPosition=0;
    public PopWindowSelectProject(Context paramContext) {
        this.context=paramContext;
        inflater = LayoutInflater.from(paramContext);
        this.conentView = inflater.inflate(R.layout.popwindow_selectarea, null);
        // 加载中
        mDialog = new MyProgressDialog(context, "请稍后...");
        typeMenu=(TypeMenu)conentView.findViewById(R.id.commtype);

        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
        initListener();

    }
    private void initView()
    {

    }
    public void ShowData()
    {
        findClassify();
    }
    public void setSelect(int pPosition,int cPosition)
    {
        this.pPosition=pPosition;
        this.cPosition=cPosition;
        //typeMenu.setSelect(pPosition,cPosition);
    }
    private void initListener()
    {
        typeMenu.setOnSelectListener(new TypeMenu.OnSelectListener() {
            @Override
            public void getValue(MyBaseEntity showModel,int pPosition,int cPosition) {
                //Toast.makeText(SelectClassifyActivity.this, showModel.getId() + showModel.getText(), Toast.LENGTH_SHORT).show();
                listener.onSelectChange(showModel.getText(),showModel.getId());
                dismiss();
            }
        });
    }
    public void setOnSelectItemChangeListener(OnSelectItemChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectItemChangeListener {
        public void onSelectChange(String paramString,String id);
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
                        JSONArray array = new JSONArray(myjson.getI().getList());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String pId = obj.getString("pId");
                            String pName = obj.getString("pName");
                            MyBaseEntity model = new MyBaseEntity();
                            model.setId(pId);
                            model.setText(pName);
                            groups.add(model);
                            JSONArray array1 = obj.getJSONArray("cList");
                            LinkedList<MyBaseEntity> tItem = new LinkedList<MyBaseEntity>();
                            for (int j = 0; j < array1.length(); j++) {
                                JSONObject obj2 = array1.getJSONObject(j);
                                String cId = obj2.getString("cId");
                                String cName = obj2.getString("cName");
                                MyBaseEntity model2 = new MyBaseEntity();
                                model2.setId(cId);
                                model2.setText(cName);
                                tItem.add(model2);

                            }
                            children.put(i, tItem);
                        }
                        typeMenu.setSelect(pPosition,cPosition);
                        typeMenu.setDataSource(groups, children);

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
