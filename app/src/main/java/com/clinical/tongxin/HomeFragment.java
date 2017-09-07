package com.clinical.tongxin;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.clinical.tongxin.adapter.PermisssAdapter;
import com.clinical.tongxin.adapter.TaskAdapter;
import com.clinical.tongxin.entity.BannerEntity;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.PermissionEntity;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyGridView;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.SlideShowView2;
import com.clinical.tongxin.util.Constant;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.ToastUtils;
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

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private SlideShowView2 ssvTop;
    private List<BannerEntity> navlistTop;
    private List<PermissionEntity>pList;
    private PtrFrameLayout ptrFrameLayout;
    //热门竞拍
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<TaskEntity> tList;
    private TaskAdapter tAdapter;
    private LayoutInflater inflater;
    //private TextView tv_toProjects;
    private TextView txt_city;
    private RelativeLayout ll_top;
    //功能权限
    private PermisssAdapter mAdapter;
    private MyGridView myGridView;
    private MyProgressDialog dialog;
    private UserEntity userEntity;
    private AMapLocation location;
    // 是否还有数据 待接收
    private boolean ishave = true;
    // 数据是否为空
    private boolean isnull = false;
    // 是否还有数据  全部
    private boolean ishave1 = true;
    // 数据是否为空
    private boolean isnull1 = false;
    // 是否还有数据 附近
    private boolean ishave2 = true;
    // 数据是否为空
    private boolean isnull2 = false;
    // 是否还有数据 竞价中
    private boolean ishave3 = true;
    // 数据是否为空
    private boolean isnull3 = false;
    private View empty_view;
    private boolean requestData = true;
    private LinearLayout ll_scoll_transverse;

    //游标
    private ImageView cursor;
    private LinearLayout ll_cursor;
    private int width;
    private int lastPosition = 0;
    //全部，附近，待接收，定价中
    private TextView txt_total,txt_nearby,txt_accept,txt_pricing;
    private String status="2";
    private ImageView img_message; // 消息提醒
    private ImageView iv_unread; // 未读消息红点
    private LocalBroadcastManager mLocalBroadcastManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initListener();
        showBannerTop(false);
        showPermiss();
//        analyzeJsonTop("");
//        showPublicTask();
        //待接收，
        if (status.equals("1")){
            requestData("0");

        }else if (status.equals("2")){
        //全部
            requestData1("0");
        }else if (status.equals("3")){
            requestData2("0");
        //附近
        }else if (status.equals("public")){
            //竞价中
            requestData3("0");
        }
        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        addCursor();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnread();
    }

    private void initView()
    {//123
        inflater=LayoutInflater.from(getActivity());
        dialog = new MyProgressDialog(getActivity(),"请稍等...");
        userEntity = ((MainActivity)getActivity()).getLoginConfig();
        navlistTop=new ArrayList<BannerEntity>();
        pList=new ArrayList<PermissionEntity>();
        tList=new ArrayList<TaskEntity>();
//        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            params.setMargins(44,80,44,0);
//        }
//        else
//        {
//            params.setMargins(44,24,44,0);
//        }
        mListView = (ListView) getActivity().findViewById(R.id.load_more_listview);
        View headerView = inflater.inflate(R.layout.home_header,null);
        ssvTop = (SlideShowView2) headerView.findViewById(R.id.slideshowViewTop);
        ll_scoll_transverse = (LinearLayout) headerView.findViewById(R.id.ll_scoll_transverse);
        img_message = (ImageView) headerView.findViewById(R.id.img_message);
        iv_unread = (ImageView) headerView.findViewById(R.id.iv_unread);
        myGridView=(MyGridView)headerView.findViewById(R.id.mgridview);
        txt_city=(TextView)headerView.findViewById(R.id.txt_city);
        txt_total=(TextView)headerView.findViewById(R.id.txt_total);
        txt_nearby=(TextView)headerView.findViewById(R.id.txt_nearby);
        txt_accept=(TextView)headerView.findViewById(R.id.txt_accept);
        txt_pricing=(TextView)headerView.findViewById(R.id.txt_pricing);
        ll_top=(RelativeLayout)headerView.findViewById(R.id.ll_top);
        ll_cursor = (LinearLayout)headerView.findViewById(R.id.ll_cursor);
        //ll_top.setLayoutParams(params);
        mListView.addHeaderView(headerView);
        empty_view = getActivity().findViewById(R.id.empty_view);
        tAdapter = new TaskAdapter(getActivity(), tList);
        mListView.setAdapter(tAdapter);
        ptrFrameLayout = (PtrFrameLayout)getActivity().findViewById(R.id.ptr_frame);
        PtrClassicDefaultHeader header=new PtrClassicDefaultHeader(getActivity());
        //ll_doctor9= (LinearLayout) getActivity().findViewById(R.id.ll_doctor9);
        header.setPadding(0, Utils.dip2px(getActivity(), 20), 0, Utils.dip2px(getActivity(), 20));
        ptrFrameLayout.disableWhenHorizontalMove(true);//解决横向滑动冲突
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                        ssvTop.stopPlay();
                        showBannerTop(true);
//                        findHotAuction(true);
                        ptrFrameLayout.refreshComplete();
//                        showPublicTask();
                        //待接收，
                        if (status.equals("1")){
                            requestData("0");
                        }else if (status.equals("2")){
                            requestData1("0");
                        }else if (status.equals("3")){
                            requestData2("0");
                        }else if (status.equals("public")){
                            //竞价中
                            requestData3("0");
                        }

                    }
                }, 1500);
            }
        });
// 4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer)getActivity().findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        //mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                if (tList.size() > 0){
                    String maxid = tList.get(tList.size() - 1).getSortId();
                    //待接收
                    if (status.equals("1")){
                        requestData("<"+maxid);
                    }else if (status.equals("2")){
                        requestData1("<"+maxid);
                    }else if (status.equals("3")){
                        requestData2("<"+maxid);
                    }else if (status.equals("public")){
                        //竞价中
                        requestData3("<"+maxid);
                    }

                }else {
//                    requestData("0");
                }

            }
        });

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_REFRESH_MESSAGE);
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void initListener()
    {
        txt_city.setOnClickListener(this);
        txt_total.setOnClickListener(this);
        txt_nearby.setOnClickListener(this);
        txt_accept.setOnClickListener(this);
        txt_pricing.setOnClickListener(this);
        img_message.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String taskId = ((TaskEntity)adapterView.getAdapter().getItem(i)).getTaskId();
                Intent intent = new Intent(getActivity(),TaskDetailsActivity.class);
                intent.putExtra("taskId",taskId);
                intent.putExtra("isPublicTask",true);
                intent.putExtra("isAllTask",false);//status.equals("2")
                intent.putExtra("isAcceptScore",((TaskEntity)adapterView.getAdapter().getItem(i)).getIsAcceptScore());
                intent.putExtra("isSendScore",((TaskEntity)adapterView.getAdapter().getItem(i)).getIsSendScore());
                if (location != null){
                    intent.putExtra("longitude",location.getLongitude());
                    intent.putExtra("latitude",location.getLatitude());
                }
                startActivity(intent);
            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String name = ((PermissionEntity)adapterView.getAdapter().getItem(i)).getName();
                if ("我的团队".equals(name)){
                    UserEntity userEntity = ((MainActivity)getActivity()).getLoginConfig();

                    if ("总监".equals(userEntity.getRole())){
                        getActivity().startActivity(new Intent(getActivity(),TeamManagementActivity.class));
                    }else if("接包方".equals(userEntity.getRole()) ||"项目经理".equals(userEntity.getRole())){
                        getActivity().startActivity(new Intent(getActivity(),ManagerMemberActivity.class));
                    }

                }else if ("我的任务".equals(name)){
                    Intent intent = new Intent(getActivity(),TaskActivity.class);
                    intent.putExtra("taskStatus",i);
                    getActivity().startActivity(intent);
//                    getActivity().startActivity(new Intent(getActivity(),TaskActivity.class));
                    }else if ("我的资产".equals(name)){
                    Intent intent=new Intent(getActivity(),PropertyActivity.class);
                    startActivity(intent);
                }else if ("统计分析".equals(name)){
                    Intent intent=new Intent(getActivity(),CountActivity.class);
                    startActivity(intent);
                }else if ("分配预算".equals(name)) {
                    Intent intent=new Intent(getActivity(),DistributionBudgetActivity.class);
                    startActivity(intent);
                }else if ("预算查询".equals(name)) {
                    UserEntity userEntity = ((MainActivity)getActivity()).getLoginConfig();
                    if ("总监".equals(userEntity.getRole())){
                        getActivity().startActivity(new Intent(getActivity(),BudgetQueryActivity.class));
                    }else if ("项目经理".equals(userEntity.getRole())){
                        getActivity().startActivity(new Intent(getActivity(),ManagerActivity.class));
                    }
                }else if ("我的预算".equals(name)){
                    getActivity().startActivity(new Intent(getActivity(),BudgetActivity.class));
                }

            }
        });


    }

    private void addCursor() {
        cursor = new ImageView(getActivity());
        Gallery.LayoutParams params = new Gallery.LayoutParams(width/4, Gallery.LayoutParams.MATCH_PARENT);
        cursor.setLayoutParams(params);
        cursor.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.btn_corner_red));
        ll_cursor.addView(cursor);
        txt_total.setTextColor(getResources().getColor(R.color.holo_red_light));
    }
    /**
     * 初始化动画
     */
    private void startAnimation(int start,int end) {
        Animation animation = null;
        if (start== 0){
            if (end == 1) animation = new TranslateAnimation(0, width/4, 0, 0);
            if (end == 2) animation = new TranslateAnimation(0, width*2/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(0, width*3/4, 0, 0);
        }else if (start == 1){
            if (end == 0) animation = new TranslateAnimation(width/4, 0, 0, 0);
            if (end == 2) animation = new TranslateAnimation(width/4, width*2/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(width/4, width*3/4, 0, 0);
        }else if (start == 2){
            if (end == 0) animation = new TranslateAnimation(width*2/4, 0, 0, 0);
            if (end == 1) animation = new TranslateAnimation(width*2/4, width*1/4, 0, 0);
            if (end == 3) animation = new TranslateAnimation(width*2/4, width*3/4, 0, 0);
        }else if (start == 3){
            if (end == 0) animation = new TranslateAnimation(width*3/4, 0, 0, 0);
            if (end == 1) animation = new TranslateAnimation(width*3/4, width*1/4, 0, 0);
            if (end == 2) animation = new TranslateAnimation(width*3/4, width*2/4, 0, 0);
        }
        if (animation != null){
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }
    public void setCity(String city)
    {
        txt_city.setText(city);
    }
    public void setLocation(AMapLocation location)


    {
        if ( location != null){
            this.location = location;
            if (requestData){
//                requestData("0");
                if (status.equals("1")){
                    requestData("0");
                }else if (status.equals("2")){
                    requestData1("0");
                }else if (status.equals("3")){
                    requestData2("0");
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
                requestData=false;
            }
        }
    }
    private void showBannerTop(boolean isfresh)
    {
//        if(!isfresh) {
//            String json = Utils.getDataFromDiskLruCache(UrlUtils.URL_FindBanner);
//
//            if (!json.equals("")) {
//                analyzeJsonTop(json);
//                return;
//            }
//        }
        XUtil.Post(UrlUtils.URL_FindBanner, null, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
//                Toast.makeText(getActivity(), arg0.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
                    Utils.saveCache(UrlUtils.URL_FindBanner,myjson.getResult());
                    analyzeJsonTop(myjson.getResult());
                }
            }

        });
    }

    private void analyzeJsonTop(String json)
    {
        ssvTop.removeAllViews();
        navlistTop.clear();
        List<BannerEntity> list=new ArrayList<BannerEntity>();
//        try {
//
//            List<String> liststr=new Gson().fromJson(json,new TypeToken<List<String>>(){}.getType()) ;
        try {
            JSONArray array=new JSONArray(json);
            for (int i=0;i<array.length();i++){
                BannerEntity model=new BannerEntity();
                JSONObject obj=array.getJSONObject(i);
                String picUrl=obj.getString("ImgUrl");
                String webUrl=obj.getString("LinkUrl");
                model.setType("");
                model.setPicUrl(picUrl);
                model.setId("");
                model.setName("");
                model.setWebUrl(webUrl);
                model.setContent("");
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        list=new Gson().fromJson(json,new TypeToken<List<BannerEntity>>(){}.getType()) ;
//            for(int i=0;i<liststr.size();i++)
//            {
//                BannerEntity model=new BannerEntity();
////                JSONObject obj=array.getJSONObject(i);
////                String picUrl=obj.getString("picUrl");
////                String webUrl=obj.getString("webUrl");
//                model.setType("");
//                model.setPicUrl(UrlUtils.BASE_URL+liststr.get(i));
//                model.setId("");
//                model.setName("");
//                model.setContent("");
//
//                list.add(model);
//            }
//        } catch (JSONException e) {
//
//        }
//        List<String> listStr=new ArrayList<>();
//        BannerEntity model=new BannerEntity();
//        model.setType("");
//        model.setPicUrl("http://www.dabaoku.com/sucai/renwenlei/geguojianzhu/0103.jpg");
//        model.setId("");
//        model.setName("");
//        model.setContent("");
//        listStr.add(model.getPicUrl());
//        list.add(model);
//        BannerEntity model1=new BannerEntity();
//        model1.setType("");
//        model1.setPicUrl("http://www.2004yani.com/upload/20140811094317332.jpg");
//        model1.setId("");
//        model1.setName("");
//        model1.setContent("");
//        listStr.add(model1.getPicUrl());
//        list.add(model1);
//        BannerEntity model2=new BannerEntity();
//        model2.setType("");
//        model2.setPicUrl("http://img2.imgtn.bdimg.com/it/u=141078664,3287068204&fm=23&gp=0.jpg");
//        model2.setId("");
//        model2.setName("");
//        model2.setContent("");
//        list.add(model2);
//        listStr.add(model2.getPicUrl());
//        String lists=listStr.toArray().toString();
//        Utils.saveCache(UrlUtils.URL_FindBanner,lists);
        navlistTop.addAll(list);
        ssvTop.setData(navlistTop);
    }

    private void showPermiss()
    {
        if ("总监".equals(userEntity.getRole())){
            PermissionEntity model=new PermissionEntity();
            model.setId("1");
            model.setName("我的任务");
            pList.add(model);
            PermissionEntity model4=new PermissionEntity();
            model4.setId("2");
            model4.setName("我的资产");
            pList.add(model4);
            PermissionEntity model5=new PermissionEntity();
            model5.setId("3");
            model5.setName("分配预算");
            pList.add(model5);
            PermissionEntity model6=new PermissionEntity();
            model6.setId("4");
            model6.setName("预算查询");
            pList.add(model6);
//            PermissionEntity model8=new PermissionEntity();
//            model8.setId("5");
//            model8.setName("我的团队");
//            pList.add(model8);
//            PermissionEntity model9=new PermissionEntity();
//            model9.setId("6");
//            model9.setName("统计分析");
//            pList.add(model9);
        }  else if ("项目经理".equals(userEntity.getRole())){
            PermissionEntity model=new PermissionEntity();
            model.setId("1");
            model.setName("我的任务");
            pList.add(model);
            PermissionEntity model4=new PermissionEntity();
            model4.setId("2");
            model4.setName("我的预算");
            pList.add(model4);
//            PermissionEntity model8=new PermissionEntity();
//            model8.setId("3");
//            model8.setName("我的团队");
//            pList.add(model8);
            PermissionEntity model9=new PermissionEntity();
            model9.setId("2");
            model9.setName("我的资产");
            pList.add(model9);
        } else if ("发包方".equals(userEntity.getRole())){
            PermissionEntity model=new PermissionEntity();
            model.setId("1");
            model.setName("我的任务");
            pList.add(model);
        } else if ("接包方".equals(userEntity.getRole())){
            PermissionEntity model=new PermissionEntity();
            model.setId("1");
            model.setName("我的任务");
            pList.add(model);
//            PermissionEntity model8=new PermissionEntity();
//            model8.setId("3");
//            model8.setName("我的团队");
//            pList.add(model8);
            PermissionEntity model9=new PermissionEntity();
            model9.setId("2");
            model9.setName("我的资产");
            pList.add(model9);
        }
        for(int i=0;i<pList.size();i++){
            PermissionEntity model=pList.get(i);
            View convertView=inflater.inflate(R.layout.item_permiss, null);
            ImageView img_pic=(ImageView)convertView.findViewById(R.id.img_pic);
            TextView txt_name=(TextView) convertView.findViewById(R.id.txt_name);
            txt_name.setText(model.getName());
            convertView.setId(i);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id1=view.getId();
                    String name = pList.get(id1).getName();
                    if ("我的团队".equals(name)){
                        UserEntity userEntity = ((MainActivity)getActivity()).getLoginConfig();

                        if ("总监".equals(userEntity.getRole())){
                            getActivity().startActivity(new Intent(getActivity(),TeamManagementActivity.class));
                        }else if("接包方".equals(userEntity.getRole()) ||"项目经理".equals(userEntity.getRole())){
                            getActivity().startActivity(new Intent(getActivity(),ManagerMemberActivity.class));
                        }

                    }else if ("我的任务".equals(name)){
                        Intent intent = new Intent(getActivity(),TaskActivity.class);
                        intent.putExtra("taskStatus",id1);
                        getActivity().startActivity(intent);
//                    getActivity().startActivity(new Intent(getActivity(),TaskActivity.class));
                    }else if ("我的资产".equals(name)){
                        Intent intent=new Intent(getActivity(),PropertyActivity.class);
                        startActivity(intent);
                    }else if ("统计分析".equals(name)){
                        Intent intent=new Intent(getActivity(),CountActivity.class);
                        startActivity(intent);
                    }else if ("分配预算".equals(name)) {
                        Intent intent=new Intent(getActivity(),DistributionBudgetActivity.class);
                        startActivity(intent);
                    }else if ("预算查询".equals(name)) {
                        UserEntity userEntity = ((MainActivity)getActivity()).getLoginConfig();
                        if ("总监".equals(userEntity.getRole())){
                            getActivity().startActivity(new Intent(getActivity(),BudgetQueryActivity.class));
                        }else if ("项目经理".equals(userEntity.getRole())){
                            getActivity().startActivity(new Intent(getActivity(),ManagerActivity.class));
                        }
                    }else if ("我的预算".equals(name)){
                        getActivity().startActivity(new Intent(getActivity(),BudgetActivity.class));
                    }


                }
            });
            if(model.getName().equals("我的任务"))
            {
                img_pic.setImageResource(R.mipmap.index_icon03);
            }
            else if(model.getName().equals("我的资产"))
            {
               img_pic.setImageResource(R.mipmap.index_icon04);
            }
            else if(model.getName().equals("分配预算"))
            {
                img_pic.setImageResource(R.mipmap.index_icon05);
            }
            else if(model.getName().equals("预算查询"))
            {
                img_pic.setImageResource(R.mipmap.index_icon06);
            }
            else if(model.getName().equals("项目经理"))
            {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon07);
            }
            else if(model.getName().equals("我的团队"))
            {
                img_pic.setImageResource(R.mipmap.index_icon08);
            }
            else if(model.getName().equals("统计分析"))
            {
                img_pic.setImageResource(R.mipmap.index_icon09);
            }
            else if(model.getName().equals("抢单"))
            {
//            viewHolder.img_pic.setImageResource(R.mipmap.index_icon010);
            }else if (model.getName().equals("我的预算")){
                img_pic.setImageResource(R.mipmap.index_icon09);
            }
            ll_scoll_transverse.addView(convertView);
        }
//        mAdapter=new PermisssAdapter(getActivity(),pList);
//        myGridView.setAdapter(mAdapter);
    }
public void showPublicTask()
{
    TaskEntity model=new TaskEntity();
    model.setAmount("12000");
    model.setBidCount("13");
    model.setProjectName("阿城200个站点勘测任务");
    model.setTaskId("1");
    model.setTypePicUrl("http://img4.duitang.com/uploads/item/201405/21/20140521183956_Ghafh.jpeg");
    tList.add(model);
    TaskEntity model2=new TaskEntity();
    model2.setAmount("8000");
    model2.setBidCount("3");
    model2.setProjectName("哈尔滨铁塔图纸绘制");
    model2.setTaskId("2");
    model2.setTypePicUrl("http://res.co188.com/data/drawing/read/39/60308739/1398489193045549.dwg.2000.jpg.midscreen.jpg");
    tList.add(model2);
    ptrFrameLayout.refreshComplete();
    // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据

    mLoadMoreListViewContainer.loadMoreFinish(false, false);
    tAdapter.notifyDataSetChanged();

}
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.txt_city:
            {
                Intent intent=new Intent(getActivity(),CityPickerActivity.class);
                startActivityForResult(intent,1);

            }
            break;
            case R.id.txt_total:
            {
                startAnimation(lastPosition,0);
                lastPosition = 0;
                status="2";
                txt_total.setTextColor(getResources().getColor(R.color.holo_red_light));
                txt_nearby.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_accept.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_pricing.setTextColor(getResources().getColor(R.color.alpha_gray));
                //待接收，竞价中
                if (status.equals("1")){
                    requestData("0");

                }else if (status.equals("2")){
                    //全部
                    requestData1("0");
                }else if (status.equals("3")){
                    //附近
                    requestData2("0");
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
            }
            break;
            case R.id.txt_nearby:
            {
                status="3";
                startAnimation(lastPosition,1);
                lastPosition = 1;
                txt_total.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_nearby.setTextColor(getResources().getColor(R.color.holo_red_light));
                txt_accept.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_pricing.setTextColor(getResources().getColor(R.color.alpha_gray));
//                requestData("0");
                //待接收，竞价中

                if (status.equals("1")){
                    requestData("0");

                }else if (status.equals("2")){
                    //全部
                    requestData1("0");
                }else if (status.equals("3")){
                    //附近
                    requestData2("0");
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
            }
            break;
            case R.id.txt_accept:
            {
                status="1";
                startAnimation(lastPosition,2);
                lastPosition = 2;
                txt_total.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_nearby.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_accept.setTextColor(getResources().getColor(R.color.holo_red_light));
                txt_pricing.setTextColor(getResources().getColor(R.color.alpha_gray));
//                requestData("0");
                //待接收，竞价中
                if (status.equals("1")){
                    requestData("0");

                }else if (status.equals("2")){
                    //全部
                    requestData1("0");
                }else if (status.equals("3")){
                    //附近
                    requestData2("");
//                    tList.clear();
//                    tAdapter.notifyDataSetChanged();
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
            }
            break;
            case R.id.txt_pricing:
            {
                startAnimation(lastPosition,3);
                lastPosition = 3;
                txt_total.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_nearby.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_accept.setTextColor(getResources().getColor(R.color.alpha_gray));
                txt_pricing.setTextColor(getResources().getColor(R.color.holo_red_light));
                status="public";
//                requestData("0");
                //待接收，竞价中
                if (status.equals("1")){
                    requestData("0");

                }else if (status.equals("2")){
                    //全部
                    requestData1("0");
                }else if (status.equals("3")){
                    //附近
                    requestData2("0");
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
            }
            break;
            case R.id.img_message:
                getActivity().startActivity(new Intent(getActivity(),ConversationListActivity.class));
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(data!=null)
            {
                String city=data.getStringExtra("picked_city");
                txt_city.setText(city);
                if (status.equals("1")){
                    requestData("0");
                }else if (status.equals("2")){
                    requestData1("0");
                }else if (status.equals("3")){
                    requestData2("0");
                }else if (status.equals("public")){
                    //竞价中
                    requestData3("0");
                }
            }
        }
    }

    private void requestData(final String sortId){
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());

//        if (location != null){
//            map.put("Longitude",location.getLongitude()+"");
//            map.put("Latitude",location.getLatitude()+"");
//        }
        if (((BaseActivity)getActivity()).getLoginConfig().getRole().equals("总监")||((BaseActivity)getActivity()).getLoginConfig().getRole().equals("项目经理"))
        {
                map.put("status","accepted");

        }else if (((BaseActivity)getActivity()).getLoginConfig().getRole().equals("发包方")){
            map.put("status","accepted");

        }else if (((BaseActivity)getActivity()).getLoginConfig().getRole().equals("接包方")){
            map.put("status","1");
            map.put("contionType","toReceive");
        }

//        if (strtype.equals("待接受")){
//            map.put("Status","accepted");
//        }else if (strtype.equals("竞价中")){
//            map.put("Status","1");
//        }else {
//            map.put("Status",status);
//        }

        map.put("filterRules","");
        map.put("sort","");
        map.put("order","");
        map.put("sortId", sortId);

                XUtil.Post(UrlUtils.URL_GET_TASK_LIST, map, new MyCallBack<String>(){
                    @Override
                    public void onSuccess(String arg0) {
                        super.onSuccess(arg0);
                        try {
                            if (("0").equals(sortId)){
                                tList.clear();
                            }
                            Gson gson = new Gson();
                            ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskEntity>>>(){}.getType());
                            if (resultEntity != null && resultEntity.getCode() == 200){
                                List<TaskEntity> list = resultEntity.getResult();

                                if (list != null && list.size() > 0){
                                    if (list.size() < Utils.PAGE_SIZE){
                                        ishave = false;
                                    }else {
                                        ishave = true;
                                    }
                                    tList.addAll(list);
                                }else {
                                    ishave = false;
                                    Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                                }

                                // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                                if (tList.size() <= 0) {
                                    isnull = true;
                                    //empty_view.setVisibility(View.VISIBLE);
                                    //mListView.setVisibility(View.GONE);
                                }else {
                                    isnull = false;
                                    //empty_view.setVisibility(View.GONE);
                                    //mListView.setVisibility(View.VISIBLE);
                                }

                                tAdapter.setList(tList);
                            }else {

                                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                            }
                            ptrFrameLayout.refreshComplete();
                            mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                ptrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                if (("0").equals(sortId)){
                    tList.clear();
                }
                dialog.dismiss();
            }

        });
    }
    private void requestData1(final String sortId){
        dialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
//        map.put("Status",status);
//        map.put("City",txt_city.getText());
//        if (location != null){
//            map.put("Longitude",location.getLongitude()+"");
//            map.put("Latitude",location.getLatitude()+"");
//        }
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_querySingleAllTaskList, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        tList.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<TaskEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave1 = false;
                            }else {
                                ishave1 = true;
                            }
                            tList.addAll(list);
                        }else {
                            ishave1 = false;
                            Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (tList.size() <= 0) {
                            isnull1 = true;
                            //empty_view.setVisibility(View.VISIBLE);
                            //mListView.setVisibility(View.GONE);
                        }else {
                            isnull1 = false;
                            //empty_view.setVisibility(View.GONE);
                            //mListView.setVisibility(View.VISIBLE);
                        }

                        tAdapter.setList(tList);
                    }else {

                        Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                    ptrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull1, ishave1);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                ptrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull1, ishave1);
                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (("0").equals(sortId)){
                    tList.clear();
                }
            }

        });
    }
    private void requestData2(final String sortId){
        dialog.show();
        Map map=new HashMap<>();
        map.put("CustomerId",userEntity.getUserId());
        map.put("Ukey",userEntity.getUkey());
//        map.put("Status",status);
//        map.put("City",txt_city.getText());
        if (location != null){
            map.put("Longitude",location.getLongitude()+"");
            map.put("Latitude",location.getLatitude()+"");
        }
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_ShowTaskjwList, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        tList.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<TaskEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave2 = false;
                            }else {
                                ishave2 = true;
                            }
                            tList.addAll(list);
                        }else {
                            ishave2 = false;
                            Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (tList.size() <= 0) {
                            isnull2 = true;
                            //empty_view.setVisibility(View.VISIBLE);
                            //mListView.setVisibility(View.GONE);
                        }else {
                            isnull2 = false;
                            //empty_view.setVisibility(View.GONE);
                            //mListView.setVisibility(View.VISIBLE);
                        }

                        tAdapter.setList(tList);
                    }else {

                        Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                    ptrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull2, ishave2);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)){
                    tList.clear();
                }
                ptrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull2, ishave2);
                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }
    //竞价中
    private void requestData3(final String sortId){
        dialog.show();
        Map map=new HashMap<>();
//        map.put("CustomerId",userEntity.getUserId());
//        map.put("Ukey",userEntity.getUkey());
////        map.put("Status",status);
        map.put("City",txt_city.getText());
//        if (location != null){
//            map.put("Longitude",location.getLongitude()+"");
//            map.put("Latitude",location.getLatitude()+"");
//        }
        map.put("sortId", sortId);

        XUtil.Post(UrlUtils.URL_queryAllBiddingTask, map, new MyCallBack<String>(){
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    if (("0").equals(sortId)){
                        tList.clear();
                    }
                    Gson gson = new Gson();
                    ResultEntity<List<TaskEntity>> resultEntity = gson.fromJson(arg0,new TypeToken<ResultEntity<List<TaskEntity>>>(){}.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200){

                        List<TaskEntity> list = resultEntity.getResult();

                        if (list != null && list.size() > 0){
                            if (list.size() < Utils.PAGE_SIZE){
                                ishave3 = false;
                            }else {
                                ishave3 = true;
                            }
                            tList.addAll(list);
                        }else {
                            ishave3 = false;
                            Toast.makeText(getActivity(),"没有更多数据",Toast.LENGTH_SHORT).show();
                        }

                        // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        if (tList.size() <= 0) {
                            isnull3 = true;
                            //empty_view.setVisibility(View.VISIBLE);
                            //mListView.setVisibility(View.GONE);
                       }else {
                            isnull3 = false;
                            //empty_view.setVisibility(View.GONE);
                            //mListView.setVisibility(View.VISIBLE);
                        }

                        tAdapter.setList(tList);
                    }else {

                        Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    }
                    ptrFrameLayout.refreshComplete();
                    mLoadMoreListViewContainer.loadMoreFinish(isnull3, ishave3);
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                if (("0").equals(sortId)){
                    tList.clear();
                }
                ptrFrameLayout.refreshComplete();
                mLoadMoreListViewContainer.loadMoreFinish(isnull3, ishave3);
                Toast.makeText(getActivity(),"获取列表失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_REFRESH_MESSAGE)){
                updateUnread();
            }
        }
    };

    private void updateUnread(){
        int count = IMHelper.getInstance().getUnreadMsgCountTotal();
        if (count > 0 ){
            iv_unread.setVisibility(View.VISIBLE);
        }else {
            iv_unread.setVisibility(View.INVISIBLE);
        }
    }
}
