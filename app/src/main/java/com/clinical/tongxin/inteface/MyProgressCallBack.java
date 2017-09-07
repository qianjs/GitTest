package com.clinical.tongxin.inteface;
import org.xutils.common.Callback;;
public class MyProgressCallBack<ResultType> implements Callback.ProgressCallback<ResultType> {

	@Override
	public void onCancelled(CancelledException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(ResultType arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoading(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWaiting() {
		// TODO Auto-generated method stub
		
	}

}
