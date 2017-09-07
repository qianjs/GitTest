package com.clinical.tongxin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.camera.CameraHelper;
import com.clinical.tongxin.myview.MaskSurfaceView;
import com.clinical.tongxin.camera.OnCaptureCallback;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class RectCameraActivity extends BaseActivity implements OnCaptureCallback {
    private MaskSurfaceView surfaceview;
    private ImageView imageView;
    //拍照按钮
    private ImageView img_capture;
    //重拍按钮
    private ImageView img_remake;
    //保存按钮
    private ImageView img_save;
    //底部重拍+保存布局
    private RelativeLayout rl_bottom;


    //	拍照后得到的保存的文件路径
    private String filepath;
    private ArrayList<String> picList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication.addActivity(this);
        this.setContentView(R.layout.camera);
        picList=new ArrayList<String>();
        this.surfaceview = (MaskSurfaceView) findViewById(R.id.surface_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        img_capture= (ImageView) findViewById(R.id.img_capture);
        img_remake= (ImageView) findViewById(R.id.img_remake);
        img_save= (ImageView) findViewById(R.id.img_save);

        rl_bottom= (RelativeLayout) findViewById(R.id.rl_bottom);

        rl_bottom.setVisibility(View.GONE);
//		设置矩形区域大小
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w=dm.widthPixels;
        int h=dm.heightPixels;
        this.surfaceview.setMaskSize(w,h);
        img_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraHelper.getInstance().tackPicture(RectCameraActivity.this);
                rl_bottom.setVisibility(View.VISIBLE);
                img_capture.setVisibility(View.GONE);
            }
        });
//
//		重拍
        img_remake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.setVisibility(View.GONE);
                surfaceview.setVisibility(View.VISIBLE);
                deleteFile();
                CameraHelper.getInstance().startPreview();
                rl_bottom.setVisibility(View.GONE);
                img_capture.setVisibility(View.VISIBLE);
            }
        });
//


        //保存
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bundle bundleObject = getIntent().getExtras();
                GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
                if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.UserSetUpActivity")) {
//                    picList.add(filepath);
//                    imageView.setVisibility(View.GONE);
//                    surfaceview.setVisibility(View.VISIBLE);
//                    CameraHelper.getInstance().startPreview();
//                    rl_bottom.setVisibility(View.GONE);
//                    img_capture.setVisibility(View.VISIBLE);
                    Bundle bundle=new Bundle();
                    Intent intent = new Intent(RectCameraActivity.this,RectHeadImage.class);
                    bundle.putSerializable("myclass", goClass);
                    intent.putExtras(bundle);
                    intent.putExtra("path", filepath);
                    startActivity(intent);
                    RectCameraActivity.this.finish();
                }
                else if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.NameAuthenticationActivity")){
                    Bundle bundle=new Bundle();
                    Intent intent = new Intent(RectCameraActivity.this,goClass.getGoToClass());
//                    bundle.putSerializable("myclass", goClass);
                    intent.putExtras(bundle);
                    intent.putExtra("path", filepath);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                    RectCameraActivity.this.finish();
                }else
                {
                    picList.add(filepath);
                    imageView.setVisibility(View.GONE);
                    surfaceview.setVisibility(View.VISIBLE);
                    CameraHelper.getInstance().startPreview();
                    rl_bottom.setVisibility(View.GONE);
                    img_capture.setVisibility(View.VISIBLE);
                }

            }
        });
//
    }

    /**
     * 删除图片文件呢
     */
    private void deleteFile(){
        if(this.filepath==null || this.filepath.equals("")){
            return;
        }
        File f = new File(this.filepath);
        if(f.exists()){
            f.delete();
        }
    }

    @Override
    public void onCapture(boolean success, String filepath) {
        this.filepath = filepath;
        String message = "拍照成功";
        if(!success){
            message = "拍照失败";
            CameraHelper.getInstance().startPreview();
            this.imageView.setVisibility(View.GONE);
            this.surfaceview.setVisibility(View.VISIBLE);
        }else{
            this.imageView.setVisibility(View.VISIBLE);
            this.surfaceview.setVisibility(View.GONE);
            this.imageView.setImageBitmap(BitmapFactory.decodeFile(filepath));

        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            //TODO 某些操作
        }
        else if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
        {

        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle bundleObject = getIntent().getExtras();
            GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
            Intent intent = new Intent(RectCameraActivity.this,goClass.getGoToClass());
            Bundle bundle=new Bundle();
            bundle.putSerializable("list",picList);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
}
