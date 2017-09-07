package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.PopwindowBankAdapter;
import com.clinical.tongxin.entity.BankEntity;
import com.clinical.tongxin.entity.CityEntity;
import com.clinical.tongxin.entity.ProvinceEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class Popwindowbank extends PopupWindow {
    private LayoutInflater layoutInflater;
    private View conentView;
    private EditText et_price;
    private TextView tv_OK;
    private OnOKClickListener listener;
    private ListView listView;
    private List<BankEntity> list;
    private PopwindowBankAdapter adapter;
    private Context context;
    private Gson gson;
    private String province = "";
    private String BankName = "";
    private String cityName = "";
    private MyProgressDialog dialog;

    public Popwindowbank(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        dialog = new MyProgressDialog(context, "请稍等...");
        conentView = layoutInflater.inflate(R.layout.item_listview, null);
        gson = new Gson();
        setContentView(this.conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
    }


    private void initView() {
        list = new ArrayList<BankEntity>();
        listView = (ListView) conentView.findViewById(R.id.listview);
        adapter = new PopwindowBankAdapter(context, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankEntity model = (BankEntity) parent.getItemAtPosition(position);
                if (model != null) {
                    listener.OnOKClick(model.getName());
                    dismiss();
                }
            }
        });
    }
//初始化数据
    public void showList(PopwindowEnum.SetupPayMode popwindowEnum) {
        switch (popwindowEnum) {
            case Bank: {
                getBank();
            }
            break;
            case Province: {
                findProvinceData();
            }
            break;
            case City: {
                City(province);
            }
            break;
            case BankName: {
                getBankName();
            }
            break;
            case paper: {

            }
            break;

        }

    }

    //设置省份
    public void setProvince(String strProvince)
    {
        province=strProvince;
    }
    //设置城市
    public void setCityName(String cityName)
    {
        this.cityName=cityName;
    }
    //设置银行名
    public void setBankName(String bankName)
    {
        this.BankName=bankName;
    }
    //获取开户行列表
    private void getBank() {
        dialog.show();
        XUtil.Post(UrlUtils.URL_BankInterface, null, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray array = new JSONArray(obj.getString("result"));
//                    List<BankEntity> list1=gson.fromJson(obj.getString("rows"),new TypeToken<List<BankEntity>>() {
//                    }.getType());
                    List<BankEntity> list1 = new ArrayList<BankEntity>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        BankEntity bankEntity = new BankEntity();
                        bankEntity.setName(object.getString("BankName"));
                        list1.add(bankEntity);
                    }

                    list.addAll(list1);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });
    }
//获取支行名称
    private void getBankName() {
        list.clear();
        dialog.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("BankName", BankName);
        map.put("CityName", cityName);
        XUtil.Get(UrlUtils.URL_BankBranchInterface, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray array = new JSONArray(obj.getString("result"));
//                    List<BankEntity> list1=gson.fromJson(obj.getString("rows"),new TypeToken<List<BankEntity>>() {
//                    }.getType());
                    List<BankEntity> list1 = new ArrayList<BankEntity>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        BankEntity bankEntity = new BankEntity();
                        bankEntity.setName(object.getString("BankBranchName"));
                        list1.add(bankEntity);
                    }

                    list.addAll(list1);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });
    }

    //根据省份获取城市
    public void City(String spro) {
        list.clear();
        String myjson = Utils.getAssetsFileText("city.json");
        List<ProvinceEntity> list1 = new Gson().fromJson(myjson, new TypeToken<List<ProvinceEntity>>() {
        }.getType());
        for (ProvinceEntity pro : list1) {
            if (pro.getProvinceName().equals(spro)) {
                for (CityEntity city : pro.getCitys()) {
                    BankEntity model = new BankEntity();
                    model.setName(city.getCityName());
                    list.add(model);
                }

            }

        }
        adapter.notifyDataSetChanged();
    }

    //获取省份列表
    private void findProvinceData() {
        list.clear();
        String myjson = Utils.getAssetsFileText("city.json");
        List<ProvinceEntity> list1 = new Gson().fromJson(myjson, new TypeToken<List<ProvinceEntity>>() {
        }.getType());
        for (ProvinceEntity pro : list1) {
            BankEntity model = new BankEntity();
            model.setName(pro.getProvinceName());
            list.add(model);
        }
        adapter.notifyDataSetChanged();


    }

    public void setOnOKClickListener(OnOKClickListener listener) {
        this.listener = listener;
    }

    public interface OnOKClickListener {
        public void OnOKClick(String price);
    }
}
