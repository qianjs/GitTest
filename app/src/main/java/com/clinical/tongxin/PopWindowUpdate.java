package com.clinical.tongxin;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clinical.tongxin.entity.VersionEntity;
import com.clinical.tongxin.service.DownloadService;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;

public class PopWindowUpdate extends PopupWindow implements OnClickListener{
	private View conentView;
	private Context context;
	private LayoutInflater inflater;
	private TextView tv_version_tishi; // 版本更新内容
	private Button but_update_version;
	private TextView tv_versio_info; // 版本信息 安装包大小

	private DownloadService.DownloadBinder binder;
	public PopWindowUpdate(Context paramContext, VersionEntity versionEntity, DownloadService.DownloadBinder paramDownloadBinder)
	{
		this.context = paramContext;
		binder=paramDownloadBinder;
		inflater = LayoutInflater.from(paramContext);
		this.conentView = inflater.inflate(R.layout.popwindow_update_version, null);
		this.conentView.setFocusableInTouchMode(true);
		setContentView(this.conentView);

		setFocusable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		// setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setWidth(-1);
		setHeight(-1);
		initView(versionEntity);

	}
	private void initView(VersionEntity versionEntity)
	{
		tv_version_tishi=(TextView)conentView.findViewById(R.id.tv_version_tishi);
		but_update_version=(Button)conentView.findViewById(R.id.but_update_version);
		tv_versio_info = (TextView) conentView.findViewById(R.id.tv_versio_info);

		tv_versio_info.setText("新版本："+versionEntity.getVersion()+" / "+versionEntity.getSize());
		tv_version_tishi.setText(versionEntity.getContent().replace(";","\n"));
		but_update_version.setOnClickListener(this);
	}
	private void updateVer()
	{
		dismiss();
		binder.downloadApp(UrlUtils.URL_APK);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.but_update_version:
				updateVer();
				break;

			default:
				break;
		}
	}

}
