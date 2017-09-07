package com.clinical.tongxin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clinical.tongxin.entity.GoClass;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.clinical.tongxin.R.id.img_add2;
import static com.clinical.tongxin.util.Utils.startCamera;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class SelectCameraDialog extends BaseActivity{
    private LinearLayout ll_finish,ll_camera,ll_phonealbum;
    public static final int REQUEST_CODE_LOCAL = 19;//相册
    public static final int REQUEST_CODE_LOCAL_SINGLE = 20;//相册单张
    private ArrayList<String> picList;
    private ImageView imageview;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1001;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcameradialog);
        MyApplication.addActivity(this);
        initView();
        initListener();
        picList=new ArrayList<String>();
    }
    private void initView()
    {
        ll_finish=(LinearLayout)findViewById(R.id.ll_finish);
        ll_camera=(LinearLayout)findViewById(R.id.ll_camera);
        ll_phonealbum=(LinearLayout)findViewById(R.id.ll_phonealbum);
        imageview=(ImageView) findViewById(R.id.image);

    }
    private void initListener()
    {
        ll_finish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }

        });
        ll_camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Bundle bundleObject = getIntent().getExtras();
//                GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
//                Intent intent=new Intent();
//                Bundle bundle=new Bundle();
//                intent.setClass(SelectCameraDialog.this, RectCameraActivity.class);
//                bundle.putSerializable("myclass", goClass);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
                path = Utils.startCamera(SelectCameraDialog.this,REQUEST_CODE_TAKE_PHOTO);
            }

        });
        ll_phonealbum.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //selectPicFromLocal();
                Bundle bundleObject = getIntent().getExtras();
                GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
                if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.UserSetUpActivity")) {
                    //相册单张
                    selectPicFromLocal();
                }
                else if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.NameAuthenticationActivity")){
                    selectPicFromLocal();
                }
                else if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.ResumeCertificateAttachmentInfoActivity")){
                    selectPicFromLocal();
                }
                else if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.ResumeInfoActivity")){
                    selectPicFromLocal();
                }
                else {
                    //相册多张
                    Intent intent = new Intent(getApplicationContext(), com.photoselector.ui.PhotoSelectorActivity.class);
                    intent.putExtra(PhotoSelectorActivity.KEY_MAX, 10);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent, REQUEST_CODE_LOCAL);
                }
            }

        });
    }
    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_LOCAL);
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        //此处调用了图片选择器
        //如果直接写intent.setDataAndType("image/*");
        //调用的是系统图库
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_LOCAL_SINGLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO){
            Bundle bundle=new Bundle();
            Bundle bundleObject = getIntent().getExtras();
            GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
            Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
//                    bundle.putSerializable("myclass", goClass);
//            intent.putExtras(bundle);bundle
            intent.putExtra("path", path);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(requestCode == REQUEST_CODE_LOCAL)
        {
            List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");

//            if(goClass.getGoToClass().getName().equals("com.clinical.medicalbeautymirror.UserSetUpActivity")) {
//                //相册单张
//                Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
////                Bundle bundle=new Bundle();
//                intent.putExtra("path", photos.get(0).getOriginalPath());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }else
                if (data != null && data.getExtras() != null) {

                if (photos == null || photos.isEmpty()) {
                    //UIHelper.ToastMessage(this, R.string.no_photo_selected);
                } else {
                    for(PhotoModel pm:photos)
                    {
                        picList.add(pm.getOriginalPath());
                    }
                    Bundle bundleObject = getIntent().getExtras();
                    GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
                    Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("list",picList);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
            }

        }
        else if(requestCode == REQUEST_CODE_LOCAL_SINGLE)
        {
                if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    PicByUri(selectedImage);
                }
            }
        }

    }
    /**
     * 根据图库图片uri显示缩略图并存入数据库
     *
     * @param selectedImage
     */
    private void PicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        String st8 = "没有找到图片";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            ThumbPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }

            ThumbPicture(file.getAbsolutePath());
        }

    }
    private void ThumbPicture(final String filePath) {

        Bundle bundleObject = getIntent().getExtras();
        GoClass goClass=(GoClass)bundleObject.getSerializable("myclass");
        if(goClass.getGoToClass().getName().equals("com.clinical.tongxin.UserSetUpActivity")) {
            Bundle bundle=new Bundle();
            Intent intent = new Intent(SelectCameraDialog.this,RectHeadImage.class);
            bundle.putSerializable("myclass", goClass);
            intent.putExtras(bundle);
            intent.putExtra("path", filePath);
            startActivity(intent);
        }
        else if (goClass.getGoToClass().getName().equals("com.clinical.tongxin.NameAuthenticationActivity")){
            Bundle bundle=new Bundle();
            Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
//            bundle.putSerializable("myclass", goClass);
            intent.putExtras(bundle);
            intent.putExtra("path", filePath);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if (goClass.getGoToClass().getName().equals("com.clinical.tongxin.ResumeCertificateAttachmentInfoActivity")){
            Bundle bundle=new Bundle();
            Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
//            bundle.putSerializable("myclass", goClass);
            intent.putExtras(bundle);
            intent.putExtra("path", filePath);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if (goClass.getGoToClass().getName().equals("com.clinical.tongxin.ResumeInfoActivity")){
            Bundle bundle=new Bundle();
            Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
//            bundle.putSerializable("myclass", goClass);
            intent.putExtras(bundle);
            intent.putExtra("path", filePath);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else
        {
            picList.add(filePath);
            Intent intent = new Intent(SelectCameraDialog.this,goClass.getGoToClass());
            Bundle bundle=new Bundle();
            bundle.putSerializable("list",picList);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        finish();
    }
}
