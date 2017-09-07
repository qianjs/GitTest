package com.clinical.tongxin;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.clinical.tongxin.myview.CustomVideoView;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_start;
    private CustomVideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_welcome);
        initView();
    }
    private void initView()
    {


        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound));
        //播放
        //videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //videoview.start();
                gotoMain();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //播放
        videoview.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                gotoMain();
                //Toast.makeText(this,"进入了主页",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void gotoMain()
    {

        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
