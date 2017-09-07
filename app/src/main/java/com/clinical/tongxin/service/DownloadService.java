package com.clinical.tongxin.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.xutils.common.Callback.Cancelable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.clinical.tongxin.R;
import com.clinical.tongxin.inteface.MyProgressCallBack;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;

/**
 *
 * @ClassName: DownloadService
 * @Description: TODO(下载service)
 * @author 马骥
 * @date 2016-5-10 下午12:47:50
 *
 */
public class DownloadService extends Service {

	private Context context;
	private Cancelable cancelable;
	private static Map<String, Integer> mUpdateProgress = new HashMap();
	public NotificationManager mNotificationManager;
	public Notification notification;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/txAndroid.apk";
	//public ProgressBar version_update_progressbar;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new DownloadBinder();
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		this.mNotificationManager = ((NotificationManager)getSystemService("notification"));
	}
	public class DownloadBinder extends Binder
	{
		public DownloadBinder()
		{

		}
		public void downloadApp(String url)
		{
//			String savePath="";
//			File file = new File(savePath);
//			if(!file.exists()){
//				file.mkdir();
//			}
			mUpdateProgress.put(url, Integer.valueOf(0));
			download(url);
		}



		private void download(final String url)
		{

			cancelable= XUtil.DownLoadFile(UrlUtils.URL_APK, savePath,
					new MyProgressCallBack<File>() {

						@Override
						public void onSuccess(File result) {
							super.onSuccess(result);
							mNotificationManager.cancel(100);
							mUpdateProgress.remove(url);
							File apkfile = new File(savePath);
							if (!apkfile.exists()) {
								return;
							}
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
							context.startActivity(i);
							Toast.makeText(getApplicationContext(), "下载成功",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(Throwable ex, boolean isOnCallback) {
							super.onError(ex, isOnCallback);
							if(null!=cancelable)
							{
								cancelable.cancel();
							}
							mUpdateProgress.remove(url);
							Toast.makeText(getApplicationContext(), "下载错误",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onLoading(long total, long current,
											  boolean isDownloading) {
							super.onLoading(total, current, isDownloading);
							int i = (int) (100.0F * ((float) current / (float) total));
							mUpdateProgress.put(url, Integer.valueOf(i));
							notificationProgress(url);
						}
					});
		}
		private void notificationProgress(String paramString)
		{
			RemoteViews localRemoteViews = new RemoteViews(DownloadService.this.getPackageName(), R.layout.notification_update);
			if(notification==null)
			{
				notification = new Notification(R.mipmap.ic_launcher, "我有任务", System.currentTimeMillis());
				notification.contentView=localRemoteViews;
				notification.flags=Notification.FLAG_INSISTENT;
				Intent localIntent = new Intent();
				PendingIntent localPendingIntent = PendingIntent.getActivity(DownloadService.this, 0, localIntent, 134217728);
				notification.contentIntent = localPendingIntent;
			}
			notification.contentView.setProgressBar(R.id.version_update_progressbar, 100, mUpdateProgress.get(paramString).intValue(), false);
			notification.contentView.setTextViewText(R.id.tv_version_name, "我有任务正在更新" + mUpdateProgress.get(paramString) + "% ...");
			mNotificationManager.notify(100, notification);
		}
	}
}
