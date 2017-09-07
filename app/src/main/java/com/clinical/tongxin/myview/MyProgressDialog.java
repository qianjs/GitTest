package com.clinical.tongxin.myview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;

public class MyProgressDialog {
	
	public Dialog mDialog;
	private AnimationDrawable animationDrawable = null;
	
	public MyProgressDialog(Context context, String message) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.progress_view, null);

		TextView text = (TextView) view.findViewById(R.id.progress_message);
		text.setText(message);
		ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
		loadingImage.setImageResource(R.anim.progress_loading_animation);
		animationDrawable = (AnimationDrawable)loadingImage.getDrawable();
		animationDrawable.setOneShot(false);
		animationDrawable.start();
		
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		
	}
	
	public void show() {
		try {
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
	
	public void dismiss() {
		try {
			if(mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
				animationDrawable.stop();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
