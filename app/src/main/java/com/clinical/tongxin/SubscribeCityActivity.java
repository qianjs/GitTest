package com.clinical.tongxin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ResultListAdapter;
import com.clinical.tongxin.adapter.ResultSubscribeListAdapter;
import com.clinical.tongxin.adapter.SubscribeCityAdapter;

import com.clinical.tongxin.entity.CityEntity;
import com.clinical.tongxin.entity.ProvinceEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.SubscribeEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.SideLetterBar2;

import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lincho on 2017/6/21.
 */

public class SubscribeCityActivity extends BaseActivity implements View.OnClickListener{

    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar2 mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private TextView title;
    private TextView tv_save;
    private SubscribeCityAdapter mCityAdapter;
    private ResultSubscribeListAdapter mResultAdapter;
    private List<CityEntity> mAllCities;
    private List<CityEntity> chooseCities;
    private MyProgressDialog mDialog;
    private boolean isSubscribeSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_city);

        initData();
        initView();

    }


    private void initData() {
//        dbManager = new DBManager(this);
//        dbManager.copyDBFile();
        chooseCities = (List<CityEntity>) getIntent().getSerializableExtra("chooseCities");
        mAllCities = new ArrayList<>();
        String myjson= Utils.getAssetsFileText("city.json");
        List<ProvinceEntity> provinceEntities = new Gson().fromJson(myjson, new TypeToken<List<ProvinceEntity>>() {
        }.getType());
        for (ProvinceEntity provinceEntity:provinceEntities){
            mAllCities.addAll(provinceEntity.getCitys());
        }

        try {
            Collections.sort(mAllCities, new CityComparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        mCityAdapter = new SubscribeCityAdapter(this, mAllCities,chooseCities);
        mCityAdapter.setOnCityClickListener(new SubscribeCityAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(CityEntity cityEntity) {
                boolean exist = false;
                for (CityEntity entity: chooseCities){
                    if (entity.getCityCode().equals(cityEntity.getCityCode())){
                        exist = true;
                        break;
                    }
                }
                if (!exist){
                    chooseCities.add(cityEntity);
                    mCityAdapter.refreshGridView(chooseCities);
                }

            }

            @Override
            public void onLocateClick() {

            }
        });

        mResultAdapter = new ResultSubscribeListAdapter(this, null);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText("选择城市");
        mDialog = new MyProgressDialog(context, "请稍后...");
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar2) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar2.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<CityEntity> result = searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!chooseCities.contains(mResultAdapter.getItem(position))){
                    chooseCities.add(mResultAdapter.getItem(position));
                    mCityAdapter.refreshGridView(chooseCities);
                    searchBox.setText("");
                }
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);

        clearBtn.setOnClickListener(this);
        tv_save = (TextView) findViewById(R.id.tv_complete);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setText("保存");
        tv_save.setOnClickListener(this);
    }

    private List<CityEntity> searchCity(String keyword) {
        List<CityEntity> cityEntities = new ArrayList<>();
        for (CityEntity cityEntity: mAllCities){
            if (cityEntity.getCityName().contains(keyword)){
                cityEntities.add(cityEntity);
            }
        }
        return cityEntities;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;

            case R.id.tv_complete:
//                if (chooseCities.size() == 0){
//                    ToastUtils.showToast(context,"请选择订阅城市");
//                    return;
//                }
                subscribeCities();
                break;
        }
    }

    @Override
    public void finish() {
        if (isSubscribeSuccess){
            super.finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("订阅城市");
            builder.setMessage("订阅的城市还未保存，是否退出？");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    subscribeCities();
                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    SubscribeCityActivity.super.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<CityEntity> {
        @Override
        public int compare(CityEntity lhs, CityEntity rhs) {
            String a = lhs.getPinYin().substring(0, 1);
            String b = rhs.getPinYin().substring(0, 1);
            return a.compareTo(b);
        }
    }

    /**
     * 订阅城市
     */
    private void subscribeCities() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("citys",getCityId());

        XUtil.Get(UrlUtils.URL_saveAppAreaSubscription, map, new MyCallBack<String>() {

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
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<Object>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        isSubscribeSuccess = true;
                        Toast.makeText(context, "订阅成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "订阅失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }finally {
                    mDialog.dismiss();
                }

            }
        });
    }

    private String getCityId() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chooseCities.size(); i++) {
            if (i == chooseCities.size() -1){
                sb.append(chooseCities.get(i).getCityCode());
            }else {
                sb.append(chooseCities.get(i).getCityCode()).append(";");
            }
        }
        return sb.toString();
    }

}
