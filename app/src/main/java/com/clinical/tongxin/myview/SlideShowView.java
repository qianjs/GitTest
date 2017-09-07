package com.clinical.tongxin.myview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.BannerEntity;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;



/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 *
 */

public class SlideShowView extends FrameLayout {

    // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    //private String[] imageUrls;
    private List<BannerEntity> navlist;
    //放轮播图片的ImageView 的list
    private List<View> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initImageLoader(context);

//        initData();
//        if(isAutoPlay){
//            startPlay();
//        }

    }
    public void setData(List<BannerEntity> mNavlist)
    {
        imageViewsList = new ArrayList<View>();
        dotViewsList = new ArrayList<View>();
        navlist=new ArrayList<BannerEntity>();
        navlist.addAll(mNavlist);
        navlist.add(0,mNavlist.get(mNavlist.size()-1));
        navlist.add(mNavlist.get(0));
        initUI(context);
        viewPager.setCurrentItem(1, false);
        if(isAutoPlay){
            startPlay();
        }
    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    public void stopPlay(){
        if(null!=scheduledExecutorService)
        {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context){
        //if(imageUrls == null || imageUrls.length == 0)
        if(navlist==null || navlist.size()==0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < navlist.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
            view.setTag(navlist.get(i).getPicUrl());
            //if(i==0)//给一个默认图
            //view.setBackgroundResource(R.mipmap.banner_img);
            //view.setScaleType(ScaleType.FIT_XY);
//            if(i==0)
//            {
//                view.setTag(navlist.get(navlist.size()-1).getPicUrl());
//                imageViewsList.add(view);
//            }
            // view.setTag(navlist.get(i).getPicUrl());
            imageViewsList.add(view);
//            if(i==navlist.size()-1)
//            {
//                view.setTag(navlist.get(0).getPicUrl());
//                imageViewsList.add(view);
//            }

        }
        for (int i = 0; i < navlist.size()-2; i++) {
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }
        for(int i=0;i < dotViewsList.size();i++){
            if(i == 0){
                ((View)dotViewsList.get(0)).setBackgroundResource(R.mipmap.dot_focus);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.mipmap.dot_blur);
            }
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

    }

    /**
     * 填充ViewPager的页面适配器
     *
     */
    private class MyPagerAdapter  extends PagerAdapter{

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager)container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            View view = imageViewsList.get(position);
            RoundAngleImageView imageView=(RoundAngleImageView)view.findViewById(R.id.img_pic);
            LinearLayout ll_bottom=(LinearLayout)view.findViewById(R.id.ll_bottom);
            ImageView img_top=(ImageView)view.findViewById(R.id.img_top);
            TextView tv_name=(TextView)view.findViewById(R.id.tv_name);
            TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
//            if(position==0) {
//                imageView.setImageResource(R.mipmap.index_tu02);
//            }
//            else
//            {
//                imageView.setImageResource(R.mipmap.index_tu03);
//
//            }

            if(Utils.getMyString(navlist.get(position).getType(),"").equals("0"))
            {
                img_top.setImageResource(R.mipmap.hzzx_04);
            }
            else if(navlist.get(position).getType().equals("1"))
            {
                img_top.setImageResource(R.mipmap.hzzx_05);
            }
            else
            {
                img_top.setVisibility(GONE);
                ll_bottom.setVisibility(GONE);
            }
            tv_name.setText(navlist.get(position).getName());
            tv_content.setText(navlist.get(position).getContent());
            imageLoader.displayImage(view.getTag() + "", imageView);
            //((ViewPager)container).addView(imageViewsList.get(position));
            ((ViewPager) container).addView(view);
            // 在这个方法里面设置图片的点击事件
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处理跳转逻辑
                    if(Utils.getMyString(navlist.get(position).getType(),"").equals("0"))
                    {
//                        Intent intent=new Intent(context,DoctorDetailActivity.class);
//                        intent.putExtra("doctorId",navlist.get(position).getId());
//                        context.startActivity(intent);
                    }
                    else if(navlist.get(position).getType().equals("1"))
                    {
//                        Intent intent=new Intent(context,AgencyDetailActivity.class);
//                        intent.putExtra("aid",navlist.get(position).getId());
//                        context.startActivity(intent);
                    }
                    else
                    {


                    }

//                	Intent intent = new Intent(MyApplication.applicationContext, ImagePagerActivity.class);
//    				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
//    				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//    				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    				MyApplication.applicationContext.startActivity(intent);
//                    Intent intent = new Intent(context, ActivityZoneDetail.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("zone", navlist.get(position));
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);

                    //Toast.makeText(getContext(), navlist.get(position).getSrc(), Toast.LENGTH_SHORT).show();
                }
            });
//            imageView.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    view.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });
            return view;//imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(1,false);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 2,false);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
//            if(pos==0)
//            {
//                pos=navlist.size()-3;
//            }
//            else if(pos==navlist.size()-1)
//            {
//                pos=0;
//            }
            if(pos>dotViewsList.size())
            {
                pos=0;
            }
            else if(pos<1)
            {
                pos=dotViewsList.size()-1;
            }
            else
            {
                pos=pos-1;
            }
            currentItem = pos;
            for(int i=0;i < dotViewsList.size();i++){
                if(i == pos){
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.mipmap.dot_focus);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.mipmap.dot_blur);
                }
            }
        }

    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     */
    private void destoryBitmaps() {

//        for (int i = 0; i < IMAGE_COUNT; i++) {
//            ImageView imageView = imageViewsList.get(i);
//            Drawable drawable = imageView.getDrawable();
//            if (drawable != null) {
//                //解除drawable对view的引用
//                drawable.setCallback(null);
//            }
//        }
    }


    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}