/**
 * 
 */
package com.clinical.tongxin.myview;

import java.util.ArrayList;
import java.util.List;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clinical.tongxin.R;
import com.clinical.tongxin.inteface.RiseNumberBase;
import com.clinical.tongxin.util.Utils;

/**
 * @author 马骥
 *  图片数字滚动 自定义控件
 *
 */
public class RiseNumberLinearLayout extends LinearLayout implements RiseNumberBase {

	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;

	private int number;

	private int fromNumber;
	private Context context;
	private long duration = 1000;
	private EndListener mEndListener = null;
	private int maxLength=7;//数字最大位数
	private int[] imgid=new int[]{R.mipmap.n0,R.mipmap.n1,R.mipmap.n2,R.mipmap.n3,R.mipmap.n4,R.mipmap.n5,R.mipmap.n6,R.mipmap.n7,R.mipmap.n8,R.mipmap.n9};
	private List<ImageView> listimg;
	//private LinearLayout lay;
	/**
	 * @param context
	 */
	public RiseNumberLinearLayout(Context context) {
		super(context);
		this.context=context;
		listimg=new ArrayList<ImageView>();
		// TODO Auto-generated constructor stub
	}
	public RiseNumberLinearLayout(Context context, AttributeSet attr) {
		super(context, attr);
		this.context=context;
		listimg=new ArrayList<ImageView>();
	}

	@SuppressLint("NewApi")
	public RiseNumberLinearLayout(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		listimg=new ArrayList<ImageView>();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setMaxLength(int length)
	{
		listimg.clear();
		maxLength=length;
		//lay=this;
		for(int i=0;i<maxLength;i++)
		{
			ImageView img=new ImageView(context);
			img.setImageResource(R.mipmap.n0);
			int mw=this.getLayoutParams().width;
			LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			int w=mw/maxLength;
			params.width=w;
			img.setLayoutParams(params);
			//img.setTag(i);
			this.addView(img);

			listimg.add(img);
		}
	}
	public interface EndListener {
		public void onEndFinish();
	}
	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}

	@SuppressLint("NewApi")
	private void runInt()
	{
		final ValueAnimator valueAnimator=ValueAnimator.ofInt(fromNumber,number);
		valueAnimator.setDuration(duration);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener(){

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				//设置图片滚动动画
				String nowValue= Utils.PadLeft(valueAnimator.getAnimatedValue().toString(), maxLength);
				for(int i=0;i<nowValue.length();i++)
				{
					int n=Integer.parseInt(nowValue.substring(i, i+1));
					//ImageView img=(ImageView)lay.findViewWithTag(i);
					//img.setImageResource(imgid[n]);
					listimg.get(i).setImageResource(imgid[n]);
				}
				if(valueAnimator.getAnimatedFraction()>=1)
				{
					mPlayingState=STOPPED;
					if(mEndListener!=null)
					{
						mEndListener.onEndFinish();
					}
				}
			}

		});
		valueAnimator.start();
	}
	/* (non-Javadoc)
	 * @see net.linkcar.owner.myinterface.RiseNumberBase#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(!isRunning())
		{
			mPlayingState=RUNNING;
			runInt();
		}
	}

	/* (non-Javadoc)
	 * @see net.linkcar.owner.myinterface.RiseNumberBase#withNumber(int)
	 */
	@Override
	public RiseNumberLinearLayout withNumber(int number) {
		// TODO Auto-generated method stub
		this.number=number;
		fromNumber=0;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.linkcar.owner.myinterface.RiseNumberBase#setDuration(long)
	 */
	@Override
	public RiseNumberLinearLayout setDuration(long duration) {
		this.duration=duration;
		// TODO Auto-generated method stub
		return this;
	}
	/* (non-Javadoc)
	 * @see net.linkcar.owner.myinterface.RiseNumberBase#setOnEnd(net.linkcar.owner.myview.RiseNumberLinearLayout.EndListener)
	 */
	@Override
	public void setOnEnd(EndListener callback) {
		// TODO Auto-generated method stub
		mEndListener=callback;
	}

}
