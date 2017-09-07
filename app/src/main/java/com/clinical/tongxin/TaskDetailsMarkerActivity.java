package com.clinical.tongxin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.clinical.tongxin.adapter.ArbitrationQuestionAdapter;
import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.StopEntity;
import com.clinical.tongxin.entity.Tag;
import com.clinical.tongxin.entity.TaskDetailsContractorEntity;
import com.clinical.tongxin.entity.TaskSiteItemEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MapContainer;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.TagListView;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.d.j.ar;
import static com.baidu.location.d.j.f;
import static com.clinical.tongxin.R.drawable.progress;
import static com.clinical.tongxin.R.id.cb_proportion;
import static com.clinical.tongxin.R.id.container;
import static com.clinical.tongxin.R.id.listview;
import static com.clinical.tongxin.R.id.ll_evaluate;
import static com.clinical.tongxin.R.id.ll_top;
import static com.clinical.tongxin.R.id.tv_amount;
import static com.clinical.tongxin.R.id.tv_commit;
import static com.clinical.tongxin.R.id.tv_notify;
import static com.clinical.tongxin.R.id.tv_project_type;
import static com.clinical.tongxin.R.id.view;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class TaskDetailsMarkerActivity extends BaseActivity implements OnClickListener, LocationSource, AMapLocationListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(tv_project_type)
    TextView tvProjectType;
    @BindView(R.id.tv_task_type)
    TextView tvTaskType;
    @BindView(R.id.tv_task_amount)
    TextView tvTaskAmount;
    @BindView(R.id.tv_task_address)
    TextView tvTaskAddress;
    @BindView(R.id.tv_publish_time)
    TextView tvPublishTime;
    @BindView(R.id.tv_task_scale)
    TextView tvTaskScale;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_complete_time)
    TextView tvCompleteTime;
    @BindView(ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;

    @BindView(R.id.tagview)
    TagListView tagview;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.map_container)
    MapContainer mapContainer;
    @BindView(R.id.iv_evaluate)
    ImageView ivEvaluate;
    @BindView(R.id.tv_btn1)
    TextView tvBtn1;
    @BindView(R.id.tv_btn2)
    TextView tvBtn2;

    private MyProgressDialog mDialog;
    private MarkerOptions markerOption;
    private AMap aMap;

    private LatLng latlng = new LatLng(45.7460244, 126.69025898);
    private LatLng latlng1 = new LatLng(45.75273236, 126.68699741);
    private LatLng latlng2 = new LatLng(45.7408731, 126.68867111);
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private boolean already = true;
    private List<TaskSiteItemEntity> taskSiteItemEntities;
    private final String[] titles = {"验收不及时 10", "态度蛮横 5", "需求反复 1", "沟通不畅 20",
            "未提供必要协助 3"};
    private AMapLocation amapLocation;
    private float maxDistance; //最大距离
    private List<EvaluateListEntity> evaluateList;
    private String taskId;
    private String status;
    private String publishId;
    private TaskDetailsContractorEntity taskDetailsContractorEntity;
    private AlertDialog dialog;
    private boolean addMaker = true;
    private String isAcceptScore; // 接包方是否已评价
    private String isSendScore; // 发包方是否已评价
    private boolean isBidding;
    private AlertDialog approvalDialog; // 审批
    private AlertDialog dialogConfirm; // 提交
    private List<String> arbitrations;
    private AlertDialog arbitrationDialog; // 仲裁
    private AlertDialog stopDialog; // 仲裁
    private int stopType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_marker);
        ButterKnife.bind(this);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);

        map.onCreate(savedInstanceState); // 此方法必须重写
        mapContainer.setScrollView(scrollView);
        //taskSiteItemEntities = (List<TaskSiteItemEntity>) getIntent().getSerializableExtra("TaskSiteItemEntity");
        taskId = getIntent().getStringExtra("taskId");
        status = getIntent().getStringExtra("status");
        isBidding = getIntent().getBooleanExtra("isBidding", false);
        if (isBidding) {
            addOpenCount();
        }
        init();
        requestDetailsData();
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        title.setText("任务详情");
        ivMore.setImageResource(R.mipmap.chat);
        mDialog = new MyProgressDialog(context, "请稍后...");
//        Button clearMap = (Button) findViewById(R.id.clearMap);
//        clearMap.setOnClickListener(this);
//        Button resetMap = (Button) findViewById(R.id.resetMap);
//        resetMap.setOnClickListener(this);
        if (aMap == null) {
            aMap = map.getMap();
            //addMarkersToMap();// 往地图上添加marker
            // 绑定 Marker 被点击事件
            aMap.setOnMarkerClickListener(markerClickListener);
            //绑定信息窗点击事件
            aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
//					ToastUtils.showToast(MarkerActivity.this,"===============");
                }
            });

        }
    }

    private void setViewText(TaskDetailsContractorEntity taskDetailsContractorEntity) {
        if (Utils.statusSwtichToStr(Integer.parseInt(taskDetailsContractorEntity.getTaskStatusName()), getLoginConfig(), false).equals("待提交") || Utils.statusSwtichToStr(Integer.parseInt(taskDetailsContractorEntity.getTaskStatusName()), getLoginConfig(), false).equals("待审批")) {
            llEvaluate.setVisibility(View.GONE);
        } else {
            llEvaluate.setVisibility(View.VISIBLE);
        }

        if (getLoginConfig().getRole().equals("接包方")) {
            publishId = taskDetailsContractorEntity.getPublisherId();
        } else {
            publishId = taskDetailsContractorEntity.getBidCustomerId();
        }

        tvProjectType.setText(taskDetailsContractorEntity.getProjectTypeName());
        tvTaskType.setText(taskDetailsContractorEntity.getTaskTypeName());
        tvTaskAmount.setText(taskDetailsContractorEntity.getAmount());
        tvTaskAddress.setText(taskDetailsContractorEntity.getCity());
        tvPublishTime.setText(taskDetailsContractorEntity.getCreateOrderTime());
        tvCompleteTime.setText(taskDetailsContractorEntity.getCompleteTime());
        tvTaskScale.setText(taskDetailsContractorEntity.getScale());
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//		setupLocationStyle();
    }


    /**
     * 设置自定义定位蓝点
     */
//	private void setupLocationStyle(){
//		// 自定义系统定位蓝点
//		MyLocationStyle myLocationStyle = new MyLocationStyle();
//		// 自定义定位蓝点图标
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//				fromResource(R.mipmap.dot_focus));
//		// 自定义精度范围的圆形边框颜色
//		myLocationStyle.strokeColor(STROKE_COLOR);
//		//自定义精度范围的圆形边框宽度
//		myLocationStyle.strokeWidth(5);
//		// 设置圆形的填充颜色
//		myLocationStyle.radiusFillColor(FILL_COLOR);
//		// 将自定义的 myLocationStyle 对象添加到地图上
//		aMap.setMyLocationStyle(myLocationStyle);
//	}

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        ArrayList<MarkerOptions> list = new ArrayList<>();
//
//        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(), R.mipmap.location_marker))).title("站名：").snippet("站址：")
//                .position(latlng)
//                .draggable(true);
//        MarkerOptions markerOption1 = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(), R.mipmap.location_marker))).title("站名：").snippet("站址：")
//                .position(latlng1)
//                .draggable(true);
//        MarkerOptions markerOption2 = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(), R.mipmap.location_marker))).title("站名：").snippet("站址：")
//                .position(latlng2)
//                .draggable(true);
//        list.add(markerOption);
//        list.add(markerOption1);
//        list.add(markerOption2);
        if (taskSiteItemEntities != null && taskSiteItemEntities.size() > 0) {
            for (TaskSiteItemEntity taskSiteItemEntity : taskSiteItemEntities) {
                if (!TextUtils.isEmpty(taskSiteItemEntity.getLatitude())
                        && !TextUtils.isEmpty(taskSiteItemEntity.getLongitude())) {

                    LatLng latlng = new LatLng(Double.valueOf(taskSiteItemEntity.getLatitude()), Double.valueOf(taskSiteItemEntity.getLongitude()));
                    if (amapLocation != null) {
                        LatLng start = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                        float distance = AMapUtils.calculateLineDistance(start, latlng);
                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }
                    }
                    MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.maker)))
                            .title("站址：" + taskSiteItemEntity.getAddress())
                            .snippet("联系人：" + taskSiteItemEntity.getLinkman() + "\n" + "联系电话：" + taskSiteItemEntity.getLinkTelephone())
                            .position(latlng)
                            .draggable(true);
                    list.add(markerOption);
                }


            }
        } else {
            ToastUtils.showToast(TaskDetailsMarkerActivity.this, "附近无站点");
            return;
        }

        aMap.addMarkers(list, true);
        tvDistance.setText(Utils.formatDistance(maxDistance));
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            /**
//             * 清空地图上所有已经标注的marker
//             */
//            case R.id.clearMap:
//                if (aMap != null) {
//                    aMap.clear();
//                }
//                break;
//            /**
//             * 重新标注所有的marker
//             */
//            case R.id.resetMap:
//                if (aMap != null) {
//                    aMap.clear();
//                    addMarkersToMap();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation location) {
        mDialog.dismiss();
        if (mListener != null && location != null) {
            if (location != null
                    && location.getErrorCode() == 0) {
                amapLocation = location;
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                if (already) {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                    LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    Circle circle = aMap.addCircle(new CircleOptions().
                            center(latLng).
                            radius(1000).
                            fillColor(Color.argb(progress, 1, 1, 1)).
                            strokeColor(Color.argb(progress, 1, 1, 1)).
                            strokeWidth(1));
                    already = false;
                }
                if (addMaker) {
                    addMarkersToMap();// 往地图上添加marker
                    addMaker = false;
                }

//				aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
            return false;
        }
    };


    @OnClick({ll_evaluate})
    public void onClick(View view) {
        switch (view.getId()) {
            case ll_evaluate:
                if (tagview.getVisibility() == View.GONE) {
                    tagview.setVisibility(View.VISIBLE);
                    if (evaluateList != null) {
                        if (evaluateList.size() == 0) {
                            ToastUtils.showToast(context, "暂无评价");
                            return;
                        }
                        setUpData(evaluateList);
                    } else {
                        requestEvaluateData();
                    }
                } else {
                    tagview.setVisibility(View.GONE);
                }

                ivEvaluate.setImageResource(tagview.getVisibility() == View.GONE ? R.mipmap.icon_sacle_showall : R.mipmap.icon_scale_hideall);


                break;
        }
    }

    private void showPrice() {
        if (taskDetailsContractorEntity == null) {
            ToastUtils.showToast(context, "网络异常，请稍后再试");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_price, null);
        TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        tv_amount.setText(taskDetailsContractorEntity.getAmount());
        TextView tv_project_type = (TextView) view.findViewById(R.id.tv_project_type);
        tv_project_type.setText(taskDetailsContractorEntity.getProjectTypeName());
        TextView tv_task_type = (TextView) view.findViewById(R.id.tv_task_type);
        tv_task_type.setText(taskDetailsContractorEntity.getTaskTypeName());
        final EditText et_price = (EditText) view.findViewById(R.id.et_price);
        TextView tv_OK = (TextView) view.findViewById(R.id.tv_OK);
        tv_OK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    // 竞价
                    try {
                        if (Double.valueOf(taskDetailsContractorEntity.getAmount()) < Double.valueOf(et_price.getText().toString())) {
                            ToastUtils.showToast(context, "竞价价格不能大于总价");
                            return;
                        }
                        if (Double.valueOf(et_price.getText().toString()) < Double.valueOf(taskDetailsContractorEntity.getAmount()) * 0.7) {
                            ToastUtils.showToast(context, "竞价价格不能小于总价的70%");
                            return;
                        }
                        auction(et_price.getText().toString());
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }


    private void setUpData(List<EvaluateListEntity> evaluateListEntities) {
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < evaluateListEntities.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(evaluateListEntities.get(i).getComment() + " " + evaluateListEntities.get(i).getCount());
            tag.setIsterms(evaluateListEntities.get(i).getIsterms());
            mTags.add(tag);
        }
        tagview.setTags(mTags);
    }


    private void requestDetailsData() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("status", status);
        XUtil.Get(UrlUtils.URL_ShowTaskInfo, map, new MyCallBack<String>() {

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
                    ResultEntity<TaskDetailsContractorEntity> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<TaskDetailsContractorEntity>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        taskDetailsContractorEntity = resultEntity.getResult();
                        setViewText(taskDetailsContractorEntity);
                        taskSiteItemEntities = taskDetailsContractorEntity.getTaskItem();
                        setUpMap();
                        showButton();
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取任务详情失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });


    }

    private void requestEvaluateData() {
        if (TextUtils.isEmpty(publishId)) {
            ToastUtils.showToast(context, "获取发布人失败");
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", publishId);
        XUtil.Get(UrlUtils.URL_queryTaskAllReview, map, new MyCallBack<String>() {

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
                    ResultEntity<List<EvaluateListEntity>> resultEntity = gson.fromJson(json, new TypeToken<ResultEntity<List<EvaluateListEntity>>>() {
                    }.getType());

                    if (resultEntity.getCode() == 200) {
                        evaluateList = resultEntity.getResult();
                        setUpData(evaluateList);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "获取评价失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });


    }

    private void auction(String price) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("BidType", "1");
        map.put("BidPrice", price);

        XUtil.Post(UrlUtils.URL_SHOW_AUCTION, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject jsonObject = new JSONObject(arg0);
                    if (jsonObject.getInt("code") == 200) {
                        // 删除成功
                        Toast.makeText(context, "竞价成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "竞价失败，请重试", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

            }
        });
    }


    private void showButton() {
        if (isBidding && "接包方".equals(getLoginConfig().getRole())) {
            tvBtn1.setVisibility(View.VISIBLE);
            tvBtn1.setText("我要竞价");
            tvBtn1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getLoginConfig().getStatus().equals("0")) { // 申请实名
                        Intent intent = new Intent(context, NameAuthenticationActivity.class);
                        startActivity(intent);
                    } else if (getLoginConfig().getStatus().equals("1")) { // 设置支付信息
                        Intent intent = new Intent(context, PaySetUpPwdActivity.class);
                        startActivity(intent);
                    } else if (getLoginConfig().getStatus().equals("2") || getLoginConfig().getStatus().equals("3")) {
                        Toast.makeText(context, "认证审核中！", Toast.LENGTH_SHORT).show();
                    } else if (getLoginConfig().getStatus().equals("4") || getLoginConfig().getStatus().equals("6")) { // 初审失败
                        Intent intent = new Intent(context, ReasonActivity.class);
                        startActivity(intent);
                    } else if (getLoginConfig().getStatus().equals("7")) {
                        showPrice();
                    }

                }
            });
            return;
        }
        if ("总监".equals(getLoginConfig().getRole())) {
            return;
        }
        if ("项目经理".equals(getLoginConfig().getRole()) || "发包方".equals(getLoginConfig().getRole())) {
            if (Integer.valueOf(taskDetailsContractorEntity.getTaskStatusName()) > 1
                    && !"5".equals(taskDetailsContractorEntity.getTaskStatusName())
                    && !"7".equals(taskDetailsContractorEntity.getTaskStatusName())
                    && !"10".equals(taskDetailsContractorEntity.getTaskStatusName())) {// 聊天功能
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(taskDetailsContractorEntity.getContractorHxNum())) {
                            Toast.makeText(context, "接包人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(getLoginConfig().getHxAccount())) {
                            Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsContractorEntity.getContractorHxNum());
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                            startActivity(intent);
                        }
                    }
                });
//                container.addView(setButton(R.mipmap.yjrw01, "交流互动", new TaskDetailsActivity.AddViewOnClickListener() {
//                    @Override
//                    public void onClick() {
//                        if (TextUtils.isEmpty(taskDetailsEntity.getContractorHxNum())) {
//                            Toast.makeText(context, "接包人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
//                        } else if (TextUtils.isEmpty(userEntity.getHxAccount())) {
//                            Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Intent intent = new Intent(context, ChatActivity.class);
//                            intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsEntity.getContractorHxNum());
//                            intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
//                            startActivity(intent);
//                        }
//                    }
//                }));
            }
            switch (taskDetailsContractorEntity.getTaskStatusName()) {
                case "-2":// 提交任务
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("提交");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View commitView = LayoutInflater.from(context).inflate(R.layout.dialog_commit, null);
                            TextView tv_confirm = (TextView) commitView.findViewById(R.id.tv_confirm);
                            TextView tv_cancel = (TextView) commitView.findViewById(R.id.tv_cancel);
                            builder.setView(commitView);


                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (dialogConfirm != null) {
                                        dialogConfirm.dismiss();
                                    }
                                }
                            });
                            tv_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    commitTask(dialogConfirm);
                                }
                            });
                            dialogConfirm = builder.create();
                            dialogConfirm.show();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setTitle("提交任务").setMessage("是否确认提交").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    // 提交任务接口
//                                    commitTask(dialogInterface);
//                                }
//                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
                        }
                    });
                    break;
                case "0":
                    if (!"发包方".equals(getLoginConfig().getRole())) { // 审批
                        tvBtn1.setVisibility(View.VISIBLE);
                        tvBtn1.setText("审批");
                        tvBtn1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPayDialog(0);
                            }
                        });

                    } else {
                        // 不能审批
                    }

                    break;
                case "1":// 竞价中 指定接包
                    // 显示竞价信息

                    break;
                case "2":// 待验收 申请仲裁 终止
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("申请仲裁");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestArbitrationList();
                        }
                    });
                    tvBtn2.setVisibility(View.VISIBLE);
                    tvBtn2.setText("终止");
                    tvBtn2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            requsetStopInfo();
                        }
                    });
                    break;
                case "4": // 评价
                    if ("0".equals(taskDetailsContractorEntity.getIsSendScore())) {
                        tvBtn1.setVisibility(View.VISIBLE);
                        tvBtn1.setText("评价");
                        tvBtn1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StringBuffer sb = new StringBuffer();
//                                sb.append(taskDetailsContractorEntity.getContractor())
//                                        .append("(")
//                                        .append(taskDetailsContractorEntity.getContractorHxNum())
//                                        .append(")");
                                Intent intent = new Intent(context, EvaluateNewActivity.class);
                                intent.putExtra("taskId", taskId);
                                intent.putExtra("person", sb.toString());
                                startActivityForResult(intent, 1001);
                            }
                        });
//                        StringBuffer sb = new StringBuffer();
//                        sb.append(taskDetailsEntity.getContractor())
//                                .append("(")
//                                .append(taskDetailsEntity.getContractorHxNum())
//                                .append(")");
//                        Intent intent = new Intent(context, EvaluateActivity.class);
//                        intent.putExtra("taskId", taskId);
//                        intent.putExtra("person", sb.toString());
//                        startActivityForResult(intent, 1001);
                    }

                    break;
                case "3": // 支付

                    if (!"发包方".equals(getLoginConfig().getRole())) {
                        tvBtn1.setVisibility(View.VISIBLE);
                        tvBtn1.setText("支付");
                        tvBtn1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPayDialog(3);
                            }
                        });

                    }

                    break;
                case "6": // 仲裁中
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("仲裁信息");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));

                        }
                    });
                    break;
                case "7":// 仲裁结束
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("仲裁信息");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));

                        }
                    });
                    break;
                default:

                    break;
            }
        } else if ("接包方".equals(getLoginConfig().getRole())) {
            if (Integer.valueOf(taskDetailsContractorEntity.getTaskStatusName()) > 1
                    && !"5".equals(taskDetailsContractorEntity.getTaskStatusName())
                    && !"7".equals(taskDetailsContractorEntity.getTaskStatusName())
                    && !"10".equals(taskDetailsContractorEntity.getTaskStatusName())) {// 聊天功能
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(taskDetailsContractorEntity.gethXNumber())) {
                            Toast.makeText(context, "发布人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(getLoginConfig().getHxAccount())) {
                            Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsContractorEntity.gethXNumber());
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                            startActivity(intent);
                        }
                    }
                });
            }
//            View view = setButton(R.mipmap.yjrw01, "交流互动", new TaskDetailsActivity.AddViewOnClickListener() {
//                @Override
//                public void onClick() {
//                    if (TextUtils.isEmpty(taskDetailsEntity.gethXNumber())) {
//                        Toast.makeText(context, "发布人账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
//                    } else if (TextUtils.isEmpty(userEntity.getHxAccount())) {
//                        Toast.makeText(context, "账户信息不全，无法聊天", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Intent intent = new Intent(context, ChatActivity.class);
//                        intent.putExtra(EaseConstant.EXTRA_USER_ID, taskDetailsEntity.gethXNumber());
//                        intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
//                        startActivity(intent);
//                    }
//                }
//            });

            switch (taskDetailsContractorEntity.getTaskStatusName()) {
                case "1":
                    // 接受 拒绝
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("接受");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View commitView = LayoutInflater.from(context).inflate(R.layout.dialog_task_confirm, null);
                            TextView tv_title = (TextView) commitView.findViewById(R.id.tv_title);
                            tv_title.setText("接受");
                            TextView tv_message = (TextView) commitView.findViewById(R.id.tv_message);
                            tv_message.setText("是否确认接受？");
                            TextView tv_confirm = (TextView) commitView.findViewById(R.id.tv_confirm);
                            TextView tv_cancel = (TextView) commitView.findViewById(R.id.tv_cancel);
                            builder.setView(commitView);
                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (dialogConfirm != null) {
                                        dialogConfirm.dismiss();
                                    }
                                }
                            });
                            tv_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskConfirm("1");
                                }
                            });
                            dialogConfirm = builder.create();
                            dialogConfirm.show();

                        }
                    });
                    tvBtn2.setVisibility(View.VISIBLE);
                    tvBtn2.setText("拒绝");
                    tvBtn2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View commitView = LayoutInflater.from(context).inflate(R.layout.dialog_task_confirm, null);
                            TextView tv_title = (TextView) commitView.findViewById(R.id.tv_title);
                            tv_title.setText("拒绝");
                            TextView tv_message = (TextView) commitView.findViewById(R.id.tv_message);
                            tv_message.setText("是否确认拒绝？");
                            TextView tv_confirm = (TextView) commitView.findViewById(R.id.tv_confirm);
                            TextView tv_cancel = (TextView) commitView.findViewById(R.id.tv_cancel);
                            builder.setView(commitView);
                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (dialogConfirm != null) {
                                        dialogConfirm.dismiss();
                                    }
                                }
                            });
                            tv_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskConfirm("0");
                                }
                            });
                            dialogConfirm = builder.create();
                            dialogConfirm.show();
                        }
                    });

                    break;
                case "2": {
                    if (taskDetailsContractorEntity.getContractorHxNum().equals(getLoginConfig().getHxAccount())) {
                        tvBtn1.setVisibility(View.VISIBLE);
                        tvBtn1.setText("申请仲裁");
                        tvBtn1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestArbitrationList();
                            }
                        });
                    }

                }
                break;
                case "4":
                    if ("0".equals(taskDetailsContractorEntity.getIsAcceptScore())) { // 还未评价
                        tvBtn1.setVisibility(View.VISIBLE);
                        tvBtn1.setText("评价");
                        tvBtn1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StringBuffer sb = new StringBuffer();
//                                sb.append(taskDetailsContractorEntity.getContractor())
//                                        .append("(")
//                                        .append(taskDetailsContractorEntity.getContractorHxNum())
//                                        .append(")");
                                Intent intent = new Intent(context, EvaluateNewActivity.class);
                                intent.putExtra("taskId", taskId);
                                intent.putExtra("person", sb.toString());
                                startActivityForResult(intent, 1001);
                            }
                        });
//                            StringBuffer sb = new StringBuffer();
//                            sb.append(taskDetailsEntity.getContractor())
//                                    .append("(")
//                                    .append(taskDetailsEntity.getContractorHxNum())
//                                    .append(")");
//                            Intent intent = new Intent(context, EvaluateActivity.class);
//                            intent.putExtra("taskId", taskId);
//                            intent.putExtra("person", sb.toString());
//                            startActivityForResult(intent, 1001);
                    } else {
                        // 已评价
                    }


                    break;
                case "6":
                    // 仲裁中
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("仲裁信息");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));

                        }
                    });
                    break;
                case "7":
                    // 仲裁结果
                    tvBtn1.setVisibility(View.VISIBLE);
                    tvBtn1.setText("仲裁信息");
                    tvBtn1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(context, ArbitrationInfoActivity.class).putExtra("taskId", taskId));

                        }
                    });
                    break;
                default:

                    break;
            }
        }

    }

    /**
     * 任务验收情况，有验收点，按比例验收。没有验收点积分回馈或者好评。
     */
    private void requsetStopInfo() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("TaskId", taskId);
        XUtil.Post(UrlUtils.URL_getAcceptanceRatio, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<StopEntity> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<StopEntity>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        StopEntity stopEntity = resultEntity.getResult();
                        double proportion = Double.valueOf(stopEntity.getPass()) / Double.valueOf(stopEntity.getAllTaskItem());
                        showStopDialog(String.valueOf(proportion).length() > 6 ? String.valueOf(proportion).substring(0, 6) : String.valueOf(proportion));
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "请求验收详情失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "请求验收详情失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }


    /**
     * 终止
     *
     * @param acceptanceRatio 验收比例
     * @param points          反馈积分
     * @param stoptype        终止类型 1按比例验收，2积分回馈，3好评
     */
    private void requsetStop(String acceptanceRatio, String points, String stoptype) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("AcceptanceRatio", acceptanceRatio);
        map.put("Points", points);
        map.put("stoptype", stoptype);
        map.put("TaskId", taskId);
        XUtil.Post(UrlUtils.URL_endTask, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<StopEntity> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<StopEntity>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    /**
     * 显示终止dialog
     *
     * @param proportion 按比例验收
     */
    private void showStopDialog(final String proportion) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_stop, null);
        View ll_proportion = view.findViewById(R.id.ll_proportion);
        TextView tv_notify = (TextView) view.findViewById(R.id.tv_notify);
        View ll_btn = view.findViewById(R.id.ll_btn);
        final CheckBox cb_proportion = (CheckBox) view.findViewById(R.id.cb_proportion);
        final TextView tv_values = (TextView) view.findViewById(R.id.tv_values);

        View ll_integration = view.findViewById(R.id.ll_integration);
        final CheckBox cb_integration = (CheckBox) view.findViewById(R.id.cb_integration);
        final EditText et_integration = (EditText) view.findViewById(R.id.et_integration);

        View ll_evaluate = view.findViewById(R.id.ll_evaluate);
        final CheckBox cb_evaluate = (CheckBox) view.findViewById(R.id.cb_evaluate);
        if (Double.valueOf(proportion) == 1) {
            tv_notify.setVisibility(View.VISIBLE);
            et_integration.setEnabled(false);
            ll_btn.setVisibility(View.GONE);
        } else {
            ll_proportion.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (0 != Double.valueOf(proportion)) {
                        cb_proportion.setChecked(!cb_proportion.isChecked());
                        tv_values.setText(Double.valueOf(proportion) * 100 + "%");

                    } else {
                        ToastUtils.showToast(context, "验收比例为0,无法按比例验收");
                        tv_values.setText("");
                    }
                    stopType = 1;

                }
            });
            ll_integration.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (0 == Double.valueOf(proportion)) {
                        if (!cb_integration.isChecked()) {
                            cb_integration.setChecked(true);
                            et_integration.setEnabled(true);
                        }
                        if (cb_evaluate.isChecked()) {
                            cb_evaluate.setChecked(false);
                        }
                        stopType = 2;
                    } else {
                        ToastUtils.showToast(context, "只能按比例验收");
                    }

                }
            });
            ll_evaluate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (0 == Double.valueOf(proportion)) {
                        if (!cb_evaluate.isChecked()) {
                            cb_evaluate.setChecked(true);

                        }
                        if (cb_integration.isChecked()) {
                            cb_integration.setChecked(false);
                        }
                        et_integration.setEnabled(false);
                        stopType = 3;
                    } else {
                        ToastUtils.showToast(context, "只能按比例验收");
                    }
                }
            });
            TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
            TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requsetStop(Double.valueOf(proportion) * 100 + "", et_integration.getText().toString(), String.valueOf(stopType));
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stopDialog != null) {
                        stopDialog.dismiss();
                    }
                }
            });
        }

        stopDialog = builder.setView(view).create();
        stopDialog.show();
    }

    private void requestArbitrationList() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("SendOrReceive", "接包方".equals(getLoginConfig().getRole()) ? "1" : "0");
        XUtil.Post(UrlUtils.URL_queryTaskarbitrateitemApp, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<List<String>> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<List<String>>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        arbitrations = resultEntity.getResult();
                        setArbitrationView(arbitrations);
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "获取仲裁问题失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void setArbitrationView(List<String> arbitrations) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_arbitration, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_question);
        final ArbitrationQuestionAdapter arbitrationQuestionAdapter = new ArbitrationQuestionAdapter(context, arbitrations);
        listView.setAdapter(arbitrationQuestionAdapter);
        final EditText et_remark = (EditText) view.findViewById(R.id.et_remark);

        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arbitration(et_remark.getText().toString(), arbitrationQuestionAdapter.questionStr());
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arbitrationDialog != null) {
                    arbitrationDialog.dismiss();
                }
            }
        });
        arbitrationDialog = new AlertDialog.Builder(context).setView(view).create();
        arbitrationDialog.show();
    }

    private void commitTask(final DialogInterface dialogInterface) {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        XUtil.Post(UrlUtils.URL_COMMIT, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void showPayDialog(final int type) {

        View view = LayoutInflater.from(context).inflate(R.layout.pay_confirm, null);
        final TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        if (type == 0) {
            tv_amount.setText(taskDetailsContractorEntity.getPayAccount());
        } else {
            if ("1".equals(taskDetailsContractorEntity.getPublishedType())) {//  竞价最终价格
                tv_amount.setText(taskDetailsContractorEntity.getBidPrice());
            } else if ("2".equals(taskDetailsContractorEntity.getPublishedType())) {// 指定金额的价格
                tv_amount.setText(taskDetailsContractorEntity.getPayAccount());
            }

        }
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 审批
                if (type == 0) {
                    // 审批

                    taskApproval(et_pwd, approvalDialog);
                } else if (type == 3) {
                    // 支付接包方
                    taskPay(et_pwd, approvalDialog);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (approvalDialog != null) {
                    approvalDialog.dismiss();
                }
            }
        });
        approvalDialog = new AlertDialog.Builder(context).setView(view).create();
        approvalDialog.show();
    }

    private void taskApproval(EditText et_pwd, final AlertDialog dialog) {
        if (TextUtils.isEmpty(et_pwd.getText().toString())) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("isPass", "1");
        map.put("TradePassword", et_pwd.getText().toString());
        XUtil.Post(UrlUtils.URL_TASK_APPROVAL, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }


    private void taskPay(EditText et_pwd, final AlertDialog dialog) {
        if (TextUtils.isEmpty(et_pwd.getText().toString())) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("isPass", "1");
        map.put("TradePassword", et_pwd.getText().toString());
        XUtil.Post(UrlUtils.URL_PAY_TASK, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ResultJsonP1 resultEntity = Utils.wsJsonToModel1(arg0);
                try {

//                    Gson gson = new Gson();
//                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
//                    }.getType());
                    JSONObject jsonObject = new JSONObject(resultEntity.getResult());
                    if (resultEntity != null && resultEntity.getCode().equals("200") && jsonObject.getString("check").equals("true")) {
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void taskConfirm(final String i) {// 1是接受 2是拒绝
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("Type", i);
        XUtil.Post(UrlUtils.URL_TASK_RECEIVE, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {

                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, i.equals("1") ? "接受任务成功" : "拒绝任务成功", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, i.equals("1") ? "接受任务失败" : "拒绝任务失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }


    private void arbitration(String remark, String questions) {// 1是接受 2是拒绝
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("Remark", remark);
        map.put("TaskArbitrateItemList", questions);
        XUtil.Post(UrlUtils.URL_ShowTaskArbitrate, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEntity<Object> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEntity<Object>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        Toast.makeText(context, "仲裁申请成功", Toast.LENGTH_SHORT).show();
                        if (arbitrationDialog != null) {
                            arbitrationDialog.dismiss();
                        }
                        finish();
                    } else {

                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "仲裁申请失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "仲裁申请失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                finish();
            }
        }
    }

    /**
     * 统计查看次数接口
     */
    private void addOpenCount() {

        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        XUtil.Get(UrlUtils.URL_recordAccessCount, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();

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
                        //Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
