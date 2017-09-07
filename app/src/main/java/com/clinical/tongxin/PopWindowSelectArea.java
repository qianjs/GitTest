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
public class PopWindowSelectArea extends PopupWindow {
    private View conentView;
    private OnSelectItemChangeListener listener;
    private LayoutInflater inflater;
    private MyProgressDialog mDialog;
    private Context context;
    private TypeMenu typeMenu;
    ArrayList<MyBaseEntity> groups=new ArrayList<MyBaseEntity>();
    SparseArray<LinkedList<MyBaseEntity>> children = new SparseArray<LinkedList<MyBaseEntity>>();
    public PopWindowSelectArea(Context paramContext) {
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
        findProvince();
    }
    private void initListener()
    {
        typeMenu.setOnSelectListener(new TypeMenu.OnSelectListener() {
            @Override
            public void getValue(MyBaseEntity showModel,int pPosition,int cPosition) {
                //Toast.makeText(SelectClassifyActivity.this, showModel.getId() + showModel.getText(), Toast.LENGTH_SHORT).show();
                MyBaseEntity model=groups.get(pPosition);
                listener.onSelectChange(showModel.getText(),showModel.getId(),model.getId());
                dismiss();
            }
        });
    }
    public void setOnSelectItemChangeListener(OnSelectItemChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectItemChangeListener {
        public void onSelectChange(String paramString,String id,String pid);
    }
    private void findProvince()
    {
        mDialog.show();


        XUtil.Get(UrlUtils.URL_FindProvince, null, new MyCallBack<String>() {

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
                            String cid = obj.getString("cid");
                            String cname = obj.getString("cname");
                            MyBaseEntity model = new MyBaseEntity();
                            model.setId(cid);
                            model.setText(cname);
                            groups.add(model);
                            JSONArray array1 = obj.getJSONArray("province");
                            LinkedList<MyBaseEntity> tItem = new LinkedList<MyBaseEntity>();
                            MyBaseEntity modelTop = new MyBaseEntity();
                            modelTop.setId("");
                            modelTop.setText(cname+"全部地区");
                            tItem.add(modelTop);
                            for (int j = 0; j < array1.length(); j++) {
                                JSONObject obj2 = array1.getJSONObject(j);
                                String pid = obj2.getString("pid");
                                String pName = obj2.getString("pName");
                                MyBaseEntity model2 = new MyBaseEntity();
                                model2.setId(pid);
                                model2.setText(pName);
                                tItem.add(model2);

                            }
                            children.put(i, tItem);
                        }

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
