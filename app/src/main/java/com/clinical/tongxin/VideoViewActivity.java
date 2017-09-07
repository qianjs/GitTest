package com.clinical.tongxin;

import android.os.Bundle;
import com.yyl.videolist.video.VlcVideoView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qjs on 2017/8/7.
 */

public class VideoViewActivity extends BaseActivity {
    public  final static String KEY_VIDEO_URL = "key_video_url";

    @BindView(R.id.video_view)
    VlcVideoView mVideoView;

    private Unbinder mUnbinder;

    //播放地址
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        mUnbinder = ButterKnife.bind(this);

        mVideoView.onAttached(this);
        mUrl = getIntent().getExtras().getString(KEY_VIDEO_URL);
        mVideoView.playVideo(mUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
    @Override
    public void onBackPressed() {
        if (mVideoView.onBackPressed(this)) return;
        super.onBackPressed();
    }
}
