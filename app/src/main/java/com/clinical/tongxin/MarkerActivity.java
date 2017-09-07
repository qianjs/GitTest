package com.clinical.tongxin;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.clinical.tongxin.entity.TaskSiteItemEntity;
import com.clinical.tongxin.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.clinical.tongxin.R.drawable.progress;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class MarkerActivity extends BaseActivity implements OnClickListener,LocationSource,AMapLocationListener {
	private MarkerOptions markerOption;
	private AMap aMap;
	private MapView mapView;
//	private LatLng latlng = new LatLng(45.7460244, 126.69025898);
//	private LatLng latlng1 = new LatLng(45.75273236, 126.68699741);
//	private LatLng latlng2 = new LatLng(45.7408731, 126.68867111);
	private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
	private AMapLocationClient mlocationClient;
	private OnLocationChangedListener mListener;
	private AMapLocationClientOption mLocationOption;
	private boolean already =true;
	private List<TaskSiteItemEntity> taskSiteItemEntities;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_activity);
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
//		testdata();
		taskSiteItemEntities = (List<TaskSiteItemEntity>) getIntent().getSerializableExtra("TaskSiteItemEntity");
		init();
	}


	/**
	 * 初始化AMap对象
	 */
	private void init() {
		Button clearMap = (Button) findViewById(R.id.clearMap);
		clearMap.setOnClickListener(this);
		Button resetMap = (Button) findViewById(R.id.resetMap);
		resetMap.setOnClickListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			addMarkersToMap();// 往地图上添加marker
			setUpMap();
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
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if(null != mlocationClient){
			mlocationClient.onDestroy();
		}
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		ArrayList<MarkerOptions> list = new ArrayList<>();
		if (taskSiteItemEntities != null && taskSiteItemEntities.size() >0){
			for (TaskSiteItemEntity taskSiteItemEntity: taskSiteItemEntities){
				LatLng latlng = new LatLng(Double.valueOf(taskSiteItemEntity.getLatitude()),Double.valueOf(taskSiteItemEntity.getLongitude()));
				MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),R.mipmap.location_marker))).title("站名："+taskSiteItemEntity.getName()).snippet("站址："+taskSiteItemEntity.getAddress())
						.position(latlng)
						.draggable(true);
				list.add(markerOption);
			}
		}else {
			ToastUtils.showToast(MarkerActivity.this,"附近无站点");
			return;
		}

		aMap.addMarkers(list,true);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 清空地图上所有已经标注的marker
		 */
		case R.id.clearMap:
			if (aMap != null) {
				aMap.clear();
			}
			break;
		/**
		 * 重新标注所有的marker
		 */
		case R.id.resetMap:
			if (aMap != null) {
				aMap.clear();
				addMarkersToMap();
			}
			break;
		default:
			break;
		}
	}

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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				if (already){
					aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
					LatLng latLng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
					Circle circle = aMap.addCircle(new CircleOptions().
							center(latLng).
							radius(1000).
							fillColor(Color.argb(progress, 1, 1, 1)).
							strokeColor(Color.argb(progress, 1, 1, 1)).
							strokeWidth(1));
					already = false;
				}
//				aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				Toast.makeText(context,"定位失败",Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 定义 Marker 点击事件监听
	AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
		// marker 对象被点击时回调的接口
		// 返回 true 则表示接口已响应事件，否则返回false
		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.isInfoWindowShown()){
				marker.hideInfoWindow();
			}else {
				marker.showInfoWindow();
			}
			return false;
		}
	};



}
