package com.clinical.tongxin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.myview.ClipImageLayout;

/**
 * 图片剪切
 */

public class RectHeadImage extends BaseActivity {

	private ClipImageLayout mClipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rect_head_image);
		MyApplication.addActivity(this);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		String path=getIntent().getStringExtra("path");
		mClipImageLayout.setImage(path);
	}

	private void PicClip()
	{
		Bitmap bitmap = mClipImageLayout.clip();
		String path=savePicture(bitmap);
		Bundle bundleObject = getIntent().getExtras();
		GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
		Intent intent = new Intent(RectHeadImage.this,goClass.getGoToClass());
		intent.putExtra("path", path);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}
	public void clip(View view)
	{
		PicClip();
	}
	//保存图片
	private String savePicture(Bitmap bitmap){
		File imgFileDir = getImageDir();
		if (!imgFileDir.exists() && !imgFileDir.mkdirs()) {
			return null;
		}
//		文件路径路径
		String imgFilePath = imgFileDir.getPath() + File.separator + this.generateFileName();;

		File imgFile = new File(imgFilePath);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(imgFile);
			bos = new BufferedOutputStream(fos);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (Exception error) {
			return null;
		} finally {
			try {
				if(fos != null){
					fos.flush();
					fos.close();
				}
				if(bos != null){
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {}
		}
		return imgFilePath;
	}
	/**
	 *
	 * @return
	 */
	private File getImageDir() {
		String path = null;

		path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();

		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}
	/**
	 * 生成图片名称
	 * @return
	 */
	private String generateFileName(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
		String strDate = dateFormat.format(new Date());
		return "img_" + strDate + ".jpg";
	}
}
