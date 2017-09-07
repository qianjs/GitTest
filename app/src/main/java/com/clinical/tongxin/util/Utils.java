package com.clinical.tongxin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import libcore.io.DiskLruCache;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinical.tongxin.ImagePagerActivity;
import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.entity.ResultJsonC;
import com.clinical.tongxin.entity.ResultJsonP;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.UserEntity;
import com.clinical.tongxin.inteface.IDeleteDialogCallBack;

import static android.content.Context.TELEPHONY_SERVICE;

public class Utils {
	public static String GetDateNow() {
		SimpleDateFormat formatBuilder = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return formatBuilder.format(new Date());
	}

	public static String GetDateNowYear() {
		SimpleDateFormat formatBuilder = new SimpleDateFormat("yyyy");
		return formatBuilder.format(new Date());
	}

	public static String GetDateNow(String format) {
		SimpleDateFormat formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(new Date());
	}

	public static String getYearMonthDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	public static Date strToDateLog(String string){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(string, pos);
		return strtodate;
	}
	public static String dateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}
	public static  String dateToStringShort(String date)
	{
		String params[]=date.split(" ");
		return  params[0];
	}
	@SuppressLint("SimpleDateFormat")
	public static String TimeStamp2Date(String timestampString, String formats){
		Long timestamp = Long.parseLong(timestampString);
		String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
		return date;
	}
	//	public static String TimeStamp2Date2(String timestampString){
//		  Date date=new Date(timestampString);
//		  String year=date.getYear()+"";
//		  String month=(date.getMonth()+1)<10?"0"+(date.getMonth()+1):(date.getMonth()+1)+"";
//		  String mdate= date.getDate()<10?"0"+date.getDate():date.getDate()+"";
//		  String hours=date.getHours()<10?"0"+date.getHours():date.getHours()+"";
//		  String minutes=date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()+"";
//		  return year+"-"+month+"-"+mdate+" "+hours+":"+minutes;
//
//		}
	public static String getPhone(String phone) {
		String rel = "";
		if (phone.length() == 11) {
			rel = phone.substring(0, 3) + "****" + phone.substring(7);
		} else {
			rel = "手机号错误";
		}
		return rel;
	}
	public static String getPhoneFormat(String phone)
	{
		String rel = "";
		if (phone.length() == 11) {
			rel = phone.substring(0, 3) + "-" + phone.substring(3,7)+"-"+phone.substring(7,11);
		} else {
			rel = "手机号错误";
		}
		return rel;
	}
	/**
	 * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
	 * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */

	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static void Vibrate(final Activity activity, long[] pattern,
							   boolean isRepeat) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

	/**
	 *
	 * @param  dataStr 转换成汉字
	 * @return
	 */
//Unicode转中文
	public static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	public static void callPhone(String phone) {
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:" + phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		MyApplication.applicationContext.startActivity(intent);
	}
	public static String PadLeft(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			// sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}
	public static String PadRight(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			//sb.append("0").append(str);// 左(前)补0
			sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}
	public static File getDiskLruCacheDir(Context context, String dataType) {
		String dirPath;
		File cacheFile = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			dirPath = context.getExternalCacheDir().getPath();
		} else {
			dirPath = context.getCacheDir().getPath();
		}
		cacheFile = new File(dirPath + File.separator + dataType);
		return cacheFile;
	}

	/**
	 * 获取APP当前版本号
	 *
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 1;
		try {
			String packageName = context.getPackageName();
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	public static String getPhoneIMEI()
	{
		String imei = ((TelephonyManager) MyApplication.applicationContext.getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		return imei;
	}
	/**
	 * 将字符串用MD5编码. 比如在改示例中将url进行MD5编码
	 */
	public static String getStringByMD5(String string) {
		String md5String = null;
		try {
			// Create MD5 Hash
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(string.getBytes());
			byte messageDigestByteArray[] = messageDigest.digest();
			if (messageDigestByteArray == null
					|| messageDigestByteArray.length == 0) {
				return md5String;
			}

			// Create hexadecimal String
			StringBuffer hexadecimalStringBuffer = new StringBuffer();
			int length = messageDigestByteArray.length;
			for (int i = 0; i < length; i++) {
				hexadecimalStringBuffer.append(Integer
						.toHexString(0xFF & messageDigestByteArray[i]));
			}
			md5String = hexadecimalStringBuffer.toString();
			return md5String;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5String;
	}
	//网络视频缩略图生成
	public  static Bitmap createVideoThumbnail(String url, int width, int height) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		try {
			if (Build.VERSION.SDK_INT >= 14) {
				retriever.setDataSource(url, new HashMap<String, String>());
			} else {
				retriever.setDataSource(url);
			}
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException ex) {
			// Assume this is a corrupt video file
		} catch (RuntimeException ex) {
			// Assume this is a corrupt video file.
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				// Ignore failures while cleaning up.
			}
		}
		if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	public static void setimageview(String imageUrls[],int position){

		Intent intent = new Intent(MyApplication.applicationContext, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApplication.applicationContext.startActivity(intent);

	}
	public static void saveCache(String cachekey,String json)
	{
		try {
			String key = Utils.getStringByMD5(cachekey);
			DiskLruCache.Editor editor = MyApplication.mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				outputStream.write(json.getBytes());
				outputStream.flush();
				editor.commit();
				if(outputStream!=null)
				{
					outputStream.close();
				}
				MyApplication.mDiskLruCache.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//从DiskLruCache中读取数据
	public static String getDataFromDiskLruCache(String cachekey){
		String rel="";
		try {
			//第一步:获取已缓存的图片的对应唯一key值.
			String key = Utils.getStringByMD5(cachekey);
			//第二步:依据key获取到其对应的snapshot
			DiskLruCache.Snapshot snapshot=MyApplication.mDiskLruCache.get(key);
			if (snapshot!=null) {
				//第三步:从snapshot中获取到InputStream
				InputStream inputStream=snapshot.getInputStream(0);
				rel=Utils.inputStream2String(inputStream);

			}
		} catch (Exception e) {

		}
		return rel;
	}
	/*
	 * 把webservice返回的json转成model
	 */
	public static ResultJsonP wsJsonToModel(String json) {

		ResultJsonP model = new ResultJsonP();
		ResultJsonC cmodel=new ResultJsonC();
		try {
			JSONObject obj = new JSONObject(json);
			if(Integer.valueOf(obj.getString("s"))==0)
			{
				model.setS(obj.getString("s"));
				model.setM(obj.getString("m"));
				model.setV(obj.getString("v"));
				model.setR(obj.getString("r"));
				JSONObject dobj = new JSONObject(obj.getString("i"));
				cmodel.setV(dobj.getString("v"));
				cmodel.setData(dobj.getString("Data"));
				cmodel.setList(dobj.getString("List"));
				model.setI(cmodel);
			}
			else if(Integer.valueOf(obj.getString("s"))<0)
			{
				model=null;
				Toast.makeText(MyApplication.applicationContext,obj.getString("m"),Toast.LENGTH_SHORT).show();
			}
			else
			{
				model=null;
				Toast.makeText(MyApplication.applicationContext,obj.getString("m"),Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
			Toast.makeText(MyApplication.applicationContext,"服务器返回格式错误",Toast.LENGTH_SHORT).show();
		}

		return model;
	}

	/*
 * 把webservice返回的json转成model
 */
	public static ResultJsonP1 wsJsonToModel1(String json) {

		ResultJsonP1 model = new ResultJsonP1();
		try {
			JSONObject obj = new JSONObject(json);
			if(Integer.valueOf(obj.getString("code"))==200 || Integer.valueOf(obj.getString("code"))==-1)
			{
				model.setCode(obj.getString("code"));
				model.setMsg(obj.optString("msg"));
				model.setResult(obj.optString("result"));

			}
			else
			{
				model.setCode(obj.optString("code"));
				model.setMsg(obj.optString("msg"));
				model.setResult(obj.optString("result"));
				Toast.makeText(MyApplication.applicationContext,obj.getString("msg"),Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
			Toast.makeText(MyApplication.applicationContext,"服务器返回格式错误",Toast.LENGTH_SHORT).show();
		}

		return model;
	}
	/**
	 * 接口返回json中存在null 判断String是否为null字符串
	 *
	 * @param str
	 *            返回去掉null的字符串
	 * @return
	 */
	public static String getStringReplaceNull(String str) {
		if (str.equals("null")) {
			return "未设置";
		} else {
			return str;
		}
	}

	public static String getMyString(String str, String rel) {
		if (str != null && !str.equals("") && !str.equals("null")) {
			return str;
		} else {
			return rel;
		}
	}

	/**
	 * Float 类型保留2位小数
	 * @param f
	 * @return
	 */
	public static String getFloatTo2(float f)
	{
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		String rel=decimalFormat.format(f);
		return  rel;
	}
	/**
	 * Double 类型保留2位小数
	 * @param d
	 * @return
	 */
	public static String getDoubleTo2(Double d)
	{
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		String rel=decimalFormat.format(d);
		return  rel;
	}
	/**
	 *
	 * @Title: stringToInt
	 * @Description: TODO(字符串转int)
	 * @param @param str 需要转换的字符串
	 * @param @param def 转换异常返回的默认值
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
	public static int stringToInt(String str,int def)
	{
		float f=0;
		int rel=def;
		try
		{
			f=Float.parseFloat(str);
			rel=Math.round(f);
			//rel=Integer.parseInt(str);//.valueOf(str);
		}
		catch(Exception ex)
		{

		}
		return rel;
	}

	/**
	 * 获取assets中的文件内容
	 * @param fileName 文件名
	 * @return
     */
	public static String getAssetsFileText(String fileName)
	{
		String res="";
		try{

			//得到资源中的asset数据流
			InputStream in = MyApplication.applicationContext.getResources().getAssets().open(fileName);

			int length = in.available();
			byte [] buffer = new byte[length];

			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "UTF-8");

		}catch(Exception e){

			e.printStackTrace();

		}
		return res;
	}
	public static int getRandomInt(int end)
	{
		Random r = new Random();
		//r.nextInt(end);
		//int number = (int) (Math.random() * (end-start+1)) + start;
		return r.nextInt(end);
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// 获取屏幕的宽度
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	// 获取屏幕的高度
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 检测网络是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;

	}


	/**
	 * 开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP = 2;

	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/location.html";

//	/**
//	 * 根据定位结果返回定位信息的字符串
//	 *
//	 * @param loc
//	 * @return
//	 */
//	public synchronized static String getLocationStr(AMapLocation location) {
//		if (null == location) {
//			return null;
//		}
//		StringBuffer sb = new StringBuffer();
//		// errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
//		if (location.getErrorCode() == 0) {
//			sb.append("定位成功" + "\n");
//			sb.append("定位类型: " + location.getLocationType() + "\n");
//			sb.append("经    度    : " + location.getLongitude() + "\n");
//			sb.append("纬    度    : " + location.getLatitude() + "\n");
//			sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//			sb.append("提供者    : " + location.getProvider() + "\n");
//
//			if (location.getProvider().equalsIgnoreCase(
//					android.location.LocationManager.GPS_PROVIDER)) {
//				// 以下信息只有提供者是GPS时才会有
//				sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//				sb.append("角    度    : " + location.getBearing() + "\n");
//				// 获取当前提供定位服务的卫星个数
//				sb.append("星    数    : " + location.getSatellites() + "\n");
//			} else {
//				// 提供者是GPS时是没有以下信息的
//				sb.append("国    家    : " + location.getCountry() + "\n");
//				sb.append("省            : " + location.getProvince() + "\n");
//				sb.append("市            : " + location.getCity() + "\n");
//				sb.append("城市编码 : " + location.getCityCode() + "\n");
//				sb.append("区            : " + location.getDistrict() + "\n");
//				sb.append("区域 码   : " + location.getAdCode() + "\n");
//				sb.append("地    址    : " + location.getAddress() + "\n");
//				sb.append("兴趣点    : " + location.getPoiName() + "\n");
//			}
//		} else {
//			// 定位失败
//			sb.append("定位失败" + "\n");
//			sb.append("错误码:" + location.getErrorCode() + "\n");
//			sb.append("错误信息:" + location.getErrorInfo() + "\n");
//			sb.append("错误描述:" + location.getLocationDetail() + "\n");
//		}
//		return sb.toString();
//	}


	/**
	 * 任务状态汉字转任务码
	 * @param status 任务状态
	 * @return  状态码
	 */
	public static int statusSwtichToInt(String status){
		int code = 0;
		switch (status){
			case "待提交":
				code = -2;
				break;
			case "已下架":
				code = -1;
				break;
			case "待审批":
				code = 0;
				break;
			case "待接受":
				code = 1;
				break;
			case "竞价中":
				code = 1;
				break;
			case "待分配":
				code = 11;
				break;
			case "待验收":
				code = 2;
				break;
			case "待支付":
				code = 3;
				break;
			case "待评价":
				code = 4;
				break;
			case "已完成":
				code = 5;
				break;
			case "仲裁":
				code = 6;
				break;
			case "终止":
				code = 10;
				break;
		}
		return code;
	}

	/**
	 * 任务码转汉字
	 * @param code 任务状态
	 * @param isPublicTask
	 * @return  状态码
	 */
	public static String statusSwtichToStr(int code, UserEntity userEntity, boolean isPublicTask){
		String statusStr = "";
		switch (code){
			case -2:
				statusStr = "待提交";
				break;
			case -1:
				statusStr = "已下架";
				break;
			case 0:
				statusStr = "待审批";
				break;
			case 1:
				if ("接包方".equals(userEntity.getRole()) && !isPublicTask){
					statusStr = "待接受";
				}else {
					statusStr = "竞价中";
				}

				break;
			case 2:
				statusStr = "待验收";
				break;
			case 3:
				statusStr = "待支付";
				break;
			case 4:
				statusStr = "待评价";
				break;
			case 5:
				statusStr = "已完成";
				break;
			case 6:
				statusStr = "仲裁中";
				break;
			case 7:
				statusStr = "仲裁完成";
				break;
			case 8:
				statusStr = "验收未通过";
				break;
			case 10:
				statusStr = "终止";
				break;
			case 11:
				statusStr = "待分配";
				break;
		}
		return statusStr;
	}
	/**
	 * 任务点状态码转汉字
	 * @param code 任务点状态码
	 * @return  状态
	 */
	public static String itemStatusSwtichToStr(int code){
		String statusStr = "";
		switch (code){

			case 0:
				statusStr = "已发布";
				break;
			case 1:
				statusStr = "已提交";
				break;
			case 2:
				statusStr = "已验收";
				break;
			case 3:
				statusStr = "已分配";
				break;
			case 4:
				statusStr = "验收不通过";
				break;
			case 5:
				statusStr = "已接收";
				break;
		}
		return statusStr;
	}
	public static final int PAGE_SIZE = 20;
	public static String getFileAbsolutePath(Context context, Uri fileUri) {
		if (context == null || fileUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
			if (isExternalStorageDocument(fileUri)) {
				String docId = DocumentsContract.getDocumentId(fileUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(fileUri)) {
				String id = DocumentsContract.getDocumentId(fileUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(fileUri)) {
				String docId = DocumentsContract.getDocumentId(fileUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(fileUri))
				return fileUri.getLastPathSegment();
			return getDataColumn(context, fileUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
			return fileUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	/**
	 * 启动相机
	 */
	public static String startCamera(Activity activity, int requestCode) {
		// 指定相机拍摄照片保存地址
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String cameraPath = Environment.getExternalStorageDirectory().getPath()+"/tongxin/" + System.currentTimeMillis() + ".png";
			Intent intent = new Intent();
			// 指定开启系统相机的Action
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			String out_file_path = Environment.getExternalStorageDirectory().getPath()+"/tongxin/";
			File dir = new File(out_file_path);
			if (!dir.exists()) {
				dir.mkdirs();
			} // 把文件地址转换成Uri格式
			Uri uri = Uri.fromFile(new File(cameraPath));
			// 设置系统相机拍摄照片完成后图片文件的存放地址
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			activity.startActivityForResult(intent, requestCode);
			return cameraPath;
		} else {
			Toast.makeText(activity, "请确认已经插入SD卡",
					Toast.LENGTH_LONG).show();
			return null;
		}
	}


	public static String formatDistance (float distance) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString = "";
		if (distance < 1000) {
			fileSizeString = df.format((float) distance) + "m";
		} else {
			fileSizeString = df.format((float) distance / 1000) + "km";
		}
		return fileSizeString;
	}

	// 判断是否符合身份证号码的规范
	public static boolean isIDCard(String IDCard) {
		if (IDCard != null) {
			String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
			return IDCard.matches(IDCardRegex);
		}
		return false;
	}

	public static void showVideoThumbView(final Context context, final ImageView iv, final String thumbnailUrl) {
		Glide.with(context).load(thumbnailUrl).into(iv);
	}

	/**
	 * 验证手机号码
	 *
	 * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
	 * 联通号码段:130、131、132、136、185、186、145
	 * 电信号码段:133、153、180、189
	 *
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobileNumber(String mobile) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
		return mobile.matches(regex);
	}

	/**
	 * 验证电子邮箱
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
		return email.matches(regex);
	}

	/**
	 * 打开输入法键盘
	 * @param activity
	 */
	public static void showInputMethod(Activity activity){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 关闭输入法键盘
	 * @param activity
	 */
	public static void closeInputMethod(Activity activity){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
	}

	public static void showDeleteDialog(Context context, final IDeleteDialogCallBack iDeleteDialogCallBack){
		String message = "确认要删除吗？";
		new AlertDialog.Builder(context)
				.setCancelable(false)
				.setMessage(message)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						iDeleteDialogCallBack.onClickPositiveButton(dialog, which);
					}
				})
				.create()
				.show();
	}

}
