/**
 * 
 */
package com.clinical.tongxin.inteface;


import com.clinical.tongxin.myview.RiseNumberLinearLayout;

/**
 * @author 马骥
 * 数字变动接口
 *
 */
public interface RiseNumberBase {
	public void start();
	public RiseNumberLinearLayout withNumber(int number);
	public RiseNumberLinearLayout setDuration(long duration);

	public void setOnEnd(RiseNumberLinearLayout.EndListener callback);

}
