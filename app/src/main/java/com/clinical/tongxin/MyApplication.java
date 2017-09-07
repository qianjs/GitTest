package com.clinical.tongxin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.clinical.tongxin.entity.HXMyFriend;
import com.clinical.tongxin.util.IMHelper;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.PlatformConfig;

import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import libcore.io.DiskLruCache;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class MyApplication extends Application {
    public static Context applicationContext;
    public static DisplayImageOptions normalOption;
    public static DisplayImageOptions roundedOption;
    public static DiskLruCache mDiskLruCache = null;
    private static List<Activity> activityList = new LinkedList<Activity>();

    // 添加Activity到容器中
    public static void addActivity(Activity activity) {

        activityList.add(activity);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        applicationContext = this;
        initImageLoader(getApplicationContext());
        initImageLoaderOptions();
        //设置字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/SYFZLTKHJW.TTF").setFontAttrId(R.attr.fontPath).build());

        // 初始化
        x.Ext.init(this);
        // 设置是否输出debug
        x.Ext.setDebug(false);
        IMHelper.getInstance().init(applicationContext);
//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，设置不需要验证
//        options.setAcceptInvitationAlways(true);
//        //初始化
//        //EMClient.getInstance().init(this, options);
//        EaseUI.getInstance().init(applicationContext,options);
//        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
//
//            @Override
//            public EaseUser getUser(String username) {
//                return getUserInfo(username);
//            }
//        });
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        //EMClient.getInstance().setDebugMode(true);
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(this);
        // 缓存
        try {
            File cacheDir = Utils.getDiskLruCacheDir(applicationContext,
                    "yimeijingcache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir,
                    Utils.getAppVersionCode(applicationContext), 1,
                    10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
//    private EaseUser getUserInfo(String username)
//    {
//        EaseUser easeUser = new EaseUser(username);
//        List<HXMyFriend> list=XUtil.searchSelector(HXMyFriend.class, "name", username);
//        if(list!=null) {
//            if (list.size() > 0) {
//                easeUser.setAvatar(list.get(0).getHeadurl());
//                easeUser.setNick(list.get(0).getNickname());
//            }
//        }
////        easeUser.setAvatar("http://img5.imgtn.bdimg.com/it/u=1056973720,185019313&fm=11&gp=0.jpg");
////        easeUser.setNick("张三");
//        return easeUser;
//
//    }

    // 遍历所有Activity并finish
    public static void exitapp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
        try {
            mDiskLruCache.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);

    }
    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //豆瓣RENREN平台目前只能在服务器端配置
        //新浪微博
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //易信
        // PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("1105313909", "DSutPNBGrzlJY41X");
        //PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        //PlatformConfig.setAlipay("2015111700822536");
        //PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        //PlatformConfig.setPinterest("1439206");
    }
    public static void initImageLoader(Context context) {
//        // imageLoader 说明：http://www.mincoder.com/article/3800.shtml
//        // 缓存文件的目录
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//                "imageloader/Cache");
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context)
//                .memoryCacheExtraOptions(480, 800)
//                        // max width, max height，即保存的每个缓存文件的最大长宽
//                .threadPoolSize(3)
//                        // 线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You
//                        // can
//                        // pass
//                        // your
//                        // own
//                        // memory
//                        // cache
//                        // implementation/你可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
//                .discCacheSize(50 * 1024 * 1024)// 50 Mb sd卡(本地)缓存的最大值
//                .tasksProcessingOrder(QueueProcessingType.LIFO)// 由原先的discCache
//                        // -> diskCache
//                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
//                .imageDownloader(
//                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
//                        // (5
//                        // s),
//                        // readTimeout
//                        // (30
//                        // s)超时时间
//                .writeDebugLogs() // Remove for release app
//                .build();
//        // 全局初始化此配置
//        ImageLoader.getInstance().init(config);
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(com.photoselector.R.drawable.ic_picture_loading)
                .showImageOnFail(com.photoselector.R.drawable.ic_picture_loadfailed)
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true).considerExifParams(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(480, 800)
                        // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(5)
                        // default Thread.NORM_PRIORITY - 1
                .threadPriority(Thread.NORM_PRIORITY)
                        // default FIFO
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                        // default
                .diskCache(
                        new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
                                context, true)))
                        // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        // default
                .imageDownloader(new BaseImageDownloader(context))
                        // default
                .imageDecoder(new BaseImageDecoder(false))
                        // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // default
                .defaultDisplayImageOptions(imageOptions).build();

        ImageLoader.getInstance().init(config);
    }

    private static void initImageLoaderOptions() {
        roundedOption = new DisplayImageOptions.Builder()

                // .showImageOnLoading(R.drawable.tupian_bg_tmall) //
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.head)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.head)
                        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)
                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                        // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(360)) // 设置成圆角图片
                .build(); // 构建完成

        normalOption = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.iv_fail)
                .showImageForEmptyUri(R.mipmap.fail)
                .showImageOnFail(R.mipmap.fail).cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // .considerExifParams(true)
                .delayBeforeLoading(100).resetViewBeforeLoading(true).build();
    }
}
