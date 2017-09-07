package com.clinical.tongxin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

import com.clinical.tongxin.util.FileUtil;

public class PlayService extends Service implements OnPreparedListener, MediaPlayer.OnCompletionListener {

	private Context context;
	public int continueState;
	private MediaPlayer player;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		context = this;
		return new PlayBinder();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (mp.equals(player)) {
			Intent localIntent1 = new Intent("voice_play_end");
			this.context.sendBroadcast(localIntent1);
			this.player.seekTo(0);
			this.player.pause();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.player = new MediaPlayer();
		this.player.setOnPreparedListener(this);
		this.player.setOnCompletionListener(this);
	}

	public class PlayBinder extends Binder {
		public PlayBinder() {

		}

		public int getProgress() {
			return (int) (1.0D * (1.0D * (100 * PlayService.this.player.getCurrentPosition()) / PlayService.this.player.getDuration()));
		}

		public boolean isPlaying() {
			return PlayService.this.player.isPlaying();
		}

		public void pause() {
			if (PlayService.this.player.isPlaying()) {
				PlayService.this.player.pause();
			}
		}

//		public void play(Jieshuo paramJieshuo) {
//			try {
//				// "/sdcard/yuyin/4-010.mp3";//
//				String str = FileUtil.getLocalFile(FileUtil.getApkStorageFile(PlayService.this), paramJieshuo.getYuyin()).getAbsolutePath();
//				PlayService.this.player.reset();
//				PlayService.this.player.setDataSource(str);
//				PlayService.this.player.prepare();
//				return;
//			} catch (Exception localException) {
//				localException.printStackTrace();
//			}
//		}

//		public void play(String weburl) {
//			try {
//				// "/sdcard/yuyin/4-010.mp3";//
//				// String str =
//				// FileUtil.getLocalFile(FileUtil.getApkStorageFile(PlayService.this),
//				// paramJieshuo.getYuyin()).getAbsolutePath();
//				PlayService.this.player.reset();
//				PlayService.this.player.setDataSource(weburl);
//				PlayService.this.player.prepare();
//				
//				return;
//			} catch (Exception localException) {
//				localException.printStackTrace();
//			}
//		}
		public void play(String path) {
			try {
				// "/sdcard/yuyin/4-010.mp3";//
				String str = FileUtil.getLocalFile(FileUtil.getApkStorageFile(PlayService.this), path).getAbsolutePath();
				PlayService.this.player.reset();
				PlayService.this.player.setDataSource(str);
				PlayService.this.player.prepare();
				return;
			} catch (Exception localException) {
				localException.printStackTrace();
			}
	}

		public void resume() {
			PlayService.this.player.start();
		}

		public void stop() {
			PlayService.this.player.stop();
		}
	}

}
