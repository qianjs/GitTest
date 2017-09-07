package com.clinical.tongxin;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.clinical.tongxin.adapter.QualificationsAdapter;
import com.clinical.tongxin.entity.ItemQualificationsEntity;
import com.clinical.tongxin.entity.QualificationListEntity;
import com.clinical.tongxin.entity.QualificationsEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class QualificationsActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listView)
    ListView listView;
    // 进度
    private MyProgressDialog mDialog;
    private List<QualificationListEntity> list;
    private QualificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualifications);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        mDialog = new MyProgressDialog(this, "请稍后...");
        title.setText("工程资质");
        list = new ArrayList<>();
        adapter = new QualificationsAdapter(this, list);
        listView.setAdapter(adapter);

    }

    private void initData() {

        mDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        XUtil.Get(UrlUtils.URL_queryQualifications, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);

                ResultJsonP1 myjson = Utils.wsJsonToModel1(arg0);
                if (myjson != null) {
//                     是否还有数据
                    boolean ishave = false;
                    // 数据是否为空
                    boolean isnull = false;
                    List<QualificationsEntity> list1 = new Gson().fromJson(myjson.getResult(), new TypeToken<List<QualificationsEntity>>() {
                    }.getType());
                    showData(list1);
                }
                mDialog.dismiss();
            }
        });
    }

    private void showData(List<QualificationsEntity> mlist) {
        List<QualificationListEntity> list11 = new ArrayList<>();
        for (int j = 0; j < mlist.size(); j++) {
            if (list11.size() > 0) {
                int aa = 0;
                for (int s = 0; s < list11.size(); s++) {
                    if (mlist.get(j).getProjectTypeId().equals(list11.get(s).getProjectTypeId())) {
                        aa++;
                    }
                }
                if (aa == 0) {
                    QualificationListEntity model = new QualificationListEntity();
                    model.setProjectTypeId(mlist.get(j).getProjectTypeId());
                    model.setProjectTypeName(mlist.get(j).getProjectTypeName());
                    list11.add(model);
                }
            } else {
                QualificationListEntity model = new QualificationListEntity();
                model.setProjectTypeId(mlist.get(j).getProjectTypeId());
                model.setProjectTypeName(mlist.get(j).getProjectTypeName());
                list11.add(model);
            }
        }
        for (int a = 0; a < list11.size(); a++) {
            QualificationListEntity aa = list11.get(a);
            List<ItemQualificationsEntity> listaa = new ArrayList<>();
            for (int g = 0; g < mlist.size(); g++) {
                if (aa.getProjectTypeId().equals(mlist.get(g).getProjectTypeId())) {
                    ItemQualificationsEntity itemQualificationsEntity = new ItemQualificationsEntity();
                    itemQualificationsEntity.setTaskTypeId(mlist.get(g).getTaskTypeId());
                    itemQualificationsEntity.setTaskTypenName(mlist.get(g).getTaskTypenName());
                    itemQualificationsEntity.setType(mlist.get(g).getType());
                    listaa.add(itemQualificationsEntity);
                }
            }
            list11.get(a).setList(listaa);
        }
        list.addAll(list11);
        adapter.notifyDataSetChanged();

    }
}
