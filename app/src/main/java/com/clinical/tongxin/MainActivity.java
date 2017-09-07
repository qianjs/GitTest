package com.clinical.tongxin;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.clinical.tongxin.entity.AppNoticeModel;
import com.clinical.tongxin.entity.HXMyFriend;
import com.clinical.tongxin.entity.NotifyMessageEntity;
import com.clinical.tongxin.entity.OrderEntity;
import com.clinical.tongxin.entity.ProvinceEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.entity.VersionEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.service.DownloadService;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.ToastUtils;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clinical.tongxin.util.Utils.MSG_LOCATION_STOP;

public class MainActivity extends BaseActivity implements AMapLocationListener,GroupFragmentNew.OnFragmentInteractionListener{

    private ImageView[] mTabs;
    private HomeFragment homeFragment;
    private DoTaskFragment doTaskFragment;
    private NearbyFragment nearbyFragment;
    private GroupFragmentNew groupFragment;
    private  TaskFragment taskFragment;
    private  SerciveFragment serciveFragment;
    //private HXGroupFragment hxGroupFragment;
    private MyFragment myFragment;
    private MoreFragment moreFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    //private ImageView toggle_btn;
    //app现版本号
    private int version=0;
    //app服务器获取最新版本号
    private int Ver=0;
    private String LoginType="0";//0为正常登录 1为自动登录
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Handler handler = new Handler();
    private PopWindowUpdate window;
    public DownloadService.DownloadBinder downloadBinder;
    private ImageView iv_unread;
    private ServiceConnection conn1=new DownloadServiceConnection();
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        PackageInfo pinfo;
        try {
            pinfo = getPackageManager().getPackageInfo("com.clinical.tongxin", PackageManager.GET_CONFIGURATIONS);
            version=pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LoginType=getIntent().getStringExtra("LoginType");
        msgRecieve();
        initView();
        initListener();
        getSupportFragmentManager().beginTransaction().add(R.id.show_layout,taskFragment)
                .add(R.id.show_layout,doTaskFragment)
                .add(R.id.show_layout, serciveFragment)
                .add(R.id.show_layout, myFragment)
                .hide(serciveFragment)
                .hide(doTaskFragment)
                .hide(myFragment)
                .show(taskFragment).commit();

        Intent intent2=new Intent("com.clinical.tongxin.service.download");
        intent2.setPackage(getPackageName());
        bindService(intent2, conn1, 1);

        //HXLogin();
        //HXListener();
        //EMClient.getInstance().chatManager().addMessageListener(msgListener);
        //定位
        initGps();
        //获取服务端版本号
        getVersion();
        if(LoginType.equals("1")) {
            checkAutoLogin();
        }
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_REFRESH_MESSAGE);
        filter.addAction(Constant.ACTION_LOGIN);
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
        //Test();
    }

    private void msgRecieve() {
        try {
            String msg = getIntent().getStringExtra("msg");
            NotifyMessageEntity<AppNoticeModel> taskMsgEntity = new Gson().fromJson(msg, new TypeToken<NotifyMessageEntity<AppNoticeModel>>() {
            }.getType());
            AppNoticeModel appNoticeModel = taskMsgEntity.getContent();
            Intent intent = new Intent(context,TaskDetailsMarkerActivity.class);
            if ("1".equals(appNoticeModel.getNoticeType())){
                intent.putExtra("isBidding",true);
            }
            intent.putExtra("status",appNoticeModel.getTaskStatus());
            intent.putExtra("taskId",appNoticeModel.getTaskId());
            startActivity(intent);
//            switch (appNoticeModel.getNoticeType()){
//                case "1":
//                    Intent intent = new Intent(context,TaskDetailsMarkerActivity.class);
//                    intent.putExtra("taskId",appNoticeModel.getTaskId());
//                    startActivity(intent);
//                    break;
//
//                default:
//                    Intent intent1 = new Intent(context,TaskDetailsActivity.class);
//                    intent1.putExtra("taskId",appNoticeModel.getTaskId());
//                    startActivity(intent1);
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        if (hasFocus)
//        {
//
//        }
//    }
    private void initView()
    {
        mTabs=new ImageView[4];
        mTabs[0]=(ImageView)findViewById(R.id.iv_menu_0);
        mTabs[1]=(ImageView)findViewById(R.id.iv_menu_1);
        mTabs[2]=(ImageView)findViewById(R.id.iv_menu_2);
        mTabs[3]=(ImageView)findViewById(R.id.iv_menu_3);
        mTabs[0].setSelected(true);

        doTaskFragment=new DoTaskFragment();
        nearbyFragment=new NearbyFragment();
        groupFragment=GroupFragmentNew.newInstance(null,null);
        taskFragment = new TaskFragment();
        myFragment=new MyFragment();
        moreFragment=new MoreFragment();
        serciveFragment=new SerciveFragment();
        //hxGroupFragment = new HXGroupFragment();

        fragments=new Fragment[]{taskFragment,doTaskFragment,serciveFragment,myFragment};
        iv_unread = (ImageView) findViewById(R.id.iv_unread);
        //toggle_btn=(ImageView)findViewById(R.id.toggle_btn);
    }
    private void initListener()
    {
//        toggle_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent intent=new Intent(MainActivity.this,FindVideoActivity.class);
//                //startActivity(intent);
//            }
//        });
    }
    //检测自动登录 用户信息是否正确
    private void checkAutoLogin()
    {
        final String pwd=getLoginUserSharedPre().getString("PassWord","");
        String Mobile=getLoginUserSharedPre().getString("Mobile","");
        Map<String, String> map = new HashMap<String, String>();
        map.put("Mobile",Mobile);
        map.put("ValidateCode", "");
        map.put("DeviceCode", Utils.getPhoneIMEI());
        map.put("LoginPassword", pwd);
//        map.put("ValidateCode", edtTxt_code.getText().toString());
        XUtil.Post(UrlUtils.URL_UserLogin, map, new MyCallBack<String>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
                    if (myjson.getCode().equals("200")) {
                        try {

//                            getLoginUserSharedPre().edit().putInt()
                            String ss=new JSONObject(myjson.getResult()).optString("Status");
                            myFragment.getStatus(ss);
                            preferences.edit().putString("Status", new JSONObject(myjson.getResult()).optString("Status")).commit();
                            preferences.edit().putString("userId", new JSONObject(myjson.getResult()).optString("CustomerId")).commit();
                            preferences.edit().putString("Ukey", new JSONObject(myjson.getResult()).optString("Ukey")).commit();
                            preferences.edit().putString("Mobile", new JSONObject(myjson.getResult()).optString("Mobile")).commit();
                            preferences.edit().putString("NickName", new JSONObject(myjson.getResult()).optString("NickName")).commit();
                            preferences.edit().putString("RealName", new JSONObject(myjson.getResult()).optString("RealName")).commit();
                            preferences.edit().putString("IDNumber", new JSONObject(myjson.getResult()).optString("IDNumber")).commit();
                            preferences.edit().putString("HXNumber", new JSONObject(myjson.getResult()).optString("HXNumber")).commit();

                            preferences.edit().putString("BankName", new JSONObject(myjson.getResult()).optString("BankName")).commit();
                            preferences.edit().putString("CardNumber", new JSONObject(myjson.getResult()).optString("CardNumber")).commit();
                            preferences.edit().putString("PhotoUrl", new JSONObject(myjson.getResult()).optString("PhotoUrl")).commit();
                            preferences.edit().putString("IsRefuseTask", new JSONObject(myjson.getResult()).optString("IsRefuseTask")).commit();
                            preferences.edit().putString("role", new JSONObject(myjson.getResult()).optString("Role")).commit();
                            preferences.edit().putString("PassWord", pwd).commit();
                            preferences.edit().putString("Rating", new JSONObject(myjson.getResult()).optString("Rating")).commit();
                            preferences.edit().putString("RatingType", new JSONObject(myjson.getResult()).optString("RatingType")).commit();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //Toast.makeText(context, "为了您的账户安全请输入验证码", Toast.LENGTH_SHORT).show();
                        preferences.edit().putString("Mobile", "").commit();
                        preferences.edit().putString("PassWord", "").commit();

                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

    }
    private void HXLogin()
    {
        UserEntity user=getLoginConfig();
        if(user.getHxAccount().equals(""))
        {
            return;
        }
        EMClient.getInstance().login(user.getHxAccount(),user.getPwd(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
//                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
//                EMMessage message = EMMessage.createTxtSendMessage("登录测试", "maji");
////如果是群聊，设置chattype，默认是单聊
////发送消息
//                EMClient.getInstance().chatManager().sendMessage(message);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }
    private void HXListener()
    {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }
    public void onTabClicked(View view) {

        switch (view.getId()) {
            case R.id.ll_menu_0:
            {
                index=0;
            }
            break;
            case R.id.ll_menu_1:
            {
                index=1;
            }
            break;
            case R.id.ll_menu_2:
            {
                index=2;
            }
            break;
            case R.id.ll_menu_3:
            {
                index=3;
            }
            break;
//            case R.id.ll_menu_4:
//            {
//                index=4;
//            }
//            break;


        }
        if(currentTabIndex!=index)
        {
            FragmentTransaction trx=this.getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if(!fragments[index].isAdded())
            {
                trx.add(R.id.show_layout, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }

        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex=index;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this))
                        {

                        }
                        //连接不到聊天服务器
                        else
                        {

                        }
                        //当前网络不可用，请检查网络设置
                    }
                }
            });
        }
    }
    private void Test()
    {

        String myjson=Utils.getAssetsFileText("city.json");
        List<ProvinceEntity> list = new Gson().fromJson(myjson, new TypeToken<List<ProvinceEntity>>() {
                }.getType());
        int aa=list.size();
//        JSONObject jsonObject=new JSONObject();
//        try {
//            jsonObject.put("page","1");
//            jsonObject.put("rows","10");
//            jsonObject.put("sort","CityName");
//            jsonObject.put("order","desc");
//            JSONArray array=new JSONArray();
//            jsonObject.put("filterRules",array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Map<String,String> map=new HashMap<String,String>();
//        //map.put("func","list");
//        map.put("page","0");
//        map.put("rows","200");
//        map.put("sort","ProvinceName");
//        map.put("order","asc");
//
//        XUtil.Get("http://192.168.0.237:81/inter/provinceInterface.ashx?func=list", map, new MyCallBack<String>() {
//
//
//            @Override
//            public void onError(Throwable arg0, boolean arg1) {
//                // TODO Auto-generated method stub
//                super.onError(arg0, arg1);
//                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(String json) {
//
//                // TODO Auto-generated method stub
//                super.onSuccess(json);
//                try {
////                    JSONObject obj = new JSONObject(json);
////                    String code=obj.getString("code");
////                    String msg=obj.getString("msg");
////                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception ex)
//                {
//
//                }
//            }
//
//        });
    }
    private void getVersion()
    {
        XUtil.Post(UrlUtils.URL_GetAppNewVersion, null, new MyCallBack<String>(){

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(String json) {
                // TODO Auto-generated method stub
                super.onSuccess(json);
                try {
                    ResultEntity<VersionEntity> resultEntity = new Gson().fromJson(json,new TypeToken<ResultEntity<VersionEntity>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){
                        if (Integer.valueOf(resultEntity.getResult().getVersion())>version){
                            upAPP(resultEntity.getResult());
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

//                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
//                if (myjson != null) {
//                    if (myjson.getCode().equals("200")) {
//                        try {
//                            String myversion=new JSONObject(myjson.getResult()).getString("version");
//                            Ver=Integer.parseInt(myversion);
//                            if(Ver>version)
//                            {
//                                upAPP();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }

            }

        });
    }
    private void upAPP(final VersionEntity result)
    {

        findViewById(R.id.show_layout).post(new Runnable() {
            @Override
            public void run() {
                window = new PopWindowUpdate(MainActivity.this,result,downloadBinder);
                window.showAtLocation(findViewById(R.id.show_layout), Gravity.CENTER, 0, 0);
            }
        });

    }
    public class DownloadServiceConnection implements ServiceConnection
    {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            downloadBinder=(DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    }
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            Utils.Vibrate(MainActivity.this, 1000);
            EMClient.getInstance().chatManager().loadAllConversations();

            for(EMMessage msg:messages)
            {
                String nickName=msg.getStringAttribute("nickName","");
                String headUrl=msg.getStringAttribute("headUrl","");
                List<HXMyFriend> list= XUtil.searchSelector(HXMyFriend.class, "name", msg.getFrom());
                if(list!=null) {
                    if (list.size() > 0) {
                        if (!headUrl.equals(list.get(0).getHeadurl()) || !nickName.equals(list.get(0).getNickname())) {
                            list.get(0).setNickname(nickName);
                            list.get(0).setHeadurl(headUrl);
                            XUtil.saveOrUpdate(list.get(0));
                        }
                    }
                    else
                    {
                        HXMyFriend model=new HXMyFriend();
                        model.setHeadurl(headUrl);
                        model.setNickname(nickName);
                        model.setName(msg.getFrom());
                        XUtil.save(model);
                    }
                }
                else
                {
                    HXMyFriend model=new HXMyFriend();
                    model.setHeadurl(headUrl);
                    model.setNickname(nickName);
                    model.setName(msg.getFrom());
                    XUtil.save(model);
                }

            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Override
    protected void onResume() {
        updateUnread();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
        unbindService(conn1);
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        IMHelper.getInstance().removeConnectListener();
    }
    public void initGps(){

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        startLocation();
    }
    private void startLocation()
    {
        initOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        //locationOption.setNeedAddress(false);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        //locationOption.setGpsFirst(true);

        //if (!TextUtils.isEmpty(strInterval)) {
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(Long.valueOf(5000));
        //}

    }
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc&&loc.getErrorCode() == 0) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }else {
            Message msg = mHandler.obtainMessage();
            msg.what = Utils.MSG_LOCATION_STOP;
            mHandler.sendMessage(msg);
        }
        // TODO Auto-generated method stub
//		String str = paramAMapLocation.getCity();
//		if(!str.equals(""))
//		{
//			this.tv_location.setText(str);
//			locationClient.stopLocation();
//		}
    }
    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case Utils.MSG_LOCATION_START:
                    //tv_location.setText("正在定位...");
                    break;
                // 定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
//                    //String result = Utils.getLocationStr(loc);
//                   // tv_location.setText(loc.getCity());
//                   // Toast.makeText(MainActivity.this,loc.getCity(),Toast.LENGTH_SHORT).show();
//                    if(homeFragment!=null && loc != null)
//                    {
//                        homeFragment.setCity(loc.getCity().replace("市",""));
//                        homeFragment.setLocation(loc);
//                    }
                    if (taskFragment != null){
                        taskFragment.setLocation(loc);
                    }
                    // 停止定位
                    locationClient.stopLocation();
                    break;
                //停止定位
                case MSG_LOCATION_STOP:
                   // tv_location.setText("定位停止");
                    //homeFragment.setLocation(null);
                    break;
                default:
                    break;
            }
        };
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_REFRESH_MESSAGE)) {
                updateUnread();
            }else if (intent.getAction().equals(Constant.ACTION_LOGIN)){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("账号在其它设备上登录，请重新登录");
                builder.setTitle("通知");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getLoginUserSharedPre().edit().clear().commit();
                        Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    };

    private void updateUnread(){
        int count = IMHelper.getInstance().getUnreadMsgCountTotal();
        if (count > 0 ){
            iv_unread.setVisibility(View.VISIBLE);
            if (myFragment != null){
                myFragment.setIvUnread(true);
            }
        }else {
            iv_unread.setVisibility(View.GONE);
            if (myFragment != null){
                myFragment.setIvUnread(false);
            }
        }
    }

}
