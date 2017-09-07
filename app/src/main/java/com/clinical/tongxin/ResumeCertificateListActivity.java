package com.clinical.tongxin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.ResumeCertificateAdapter;
import com.clinical.tongxin.entity.ResultEntity;
import com.clinical.tongxin.entity.ResumeEntity;
import com.clinical.tongxin.inteface.IDeleteDialogCallBack;
import com.clinical.tongxin.inteface.IOnClickItemListener;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.util.LoadingDialogManager;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_CERTIFICATE_LIST;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeCertificateListActivity extends BaseActivity {
    public final static String KEY_RESUME_CERTIFICATE = "key_resume_certificate";

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.list_view)
    RecyclerView mListView;

    private Unbinder mUnbinder;
    private List<ResumeEntity.Certificate> mCertificates = new ArrayList<>();
    private ResumeCertificateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_certificate_list);

        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initData(){
        mTitle.setText("获得证书");
        loadData();

        mAdapter = new ResumeCertificateAdapter(this, new IOnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                Intent intent = new Intent(ResumeCertificateListActivity.this, ResumeCertificateInfoActivity.class);
                intent.putExtra(KEY_RESUME_CERTIFICATE, mCertificates.get(position));
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClickItem(final int position) {
                Utils.showDeleteDialog(context, new IDeleteDialogCallBack() {
                    @Override
                    public void onClickPositiveButton(DialogInterface dialog, int which) {
                        LoadingDialogManager.getInstance().show(context);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("flag", "android");
                        map.put("Uid", getLoginConfig().getUserId());
                        map.put("certificateId", mCertificates.get(position).mId);
                        XUtil.Get(UrlUtils.URL_POST_RESUME_CERTIFICATE_DELETE, map, new MyCallBack<String>(){
                            @Override
                            public void onError(Throwable arg0, boolean arg1) {
                                super.onError(arg0, arg1);
                                LoadingDialogManager.getInstance().dismiss();
                                Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException arg0) {
                                super.onCancelled(arg0);
                                LoadingDialogManager.getInstance().dismiss();
                            }

                            @Override
                            public void onSuccess(String arg0) {
                                super.onSuccess(arg0);
                                LoadingDialogManager.getInstance().dismiss();
                                try {
                                    ResultEntity resultEntity = new Gson().fromJson(arg0, new TypeToken<ResultEntity>() {
                                    }.getType());
                                    if (resultEntity.getCode() == 200) {
                                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        mCertificates.remove(position);
                                        mAdapter.setData(mCertificates);
                                        mAdapter.notifyDataSetChanged();

                                        Intent intent = new Intent();
                                        intent.putParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST, (ArrayList<? extends Parcelable>) mCertificates);
                                        setResult(0, intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "返回数据格式错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        mAdapter.setData(mCertificates);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);
    }

    private void loadData(){
        if(getIntent() != null && getIntent().getParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST) != null) {
            mCertificates = getIntent().getParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST);
        }
    }

    @OnClick(R.id.add_btn)
    void onClickAddButton(View view){
        startActivityForResult(new Intent(this, ResumeCertificateInfoActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //新增
            case 0:
                if(data != null && data.getParcelableExtra(KEY_RESUME_CERTIFICATE) != null) {
                    mCertificates.add((ResumeEntity.Certificate)data.getParcelableExtra(KEY_RESUME_CERTIFICATE));
                    mAdapter.setData(mCertificates);
                    mAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST, (ArrayList<? extends Parcelable>) mCertificates);
                    setResult(0, intent);
                }
                break;
            //修改
            case 1:
                if(data != null && data.getParcelableExtra(KEY_RESUME_CERTIFICATE) != null) {
                    ResumeEntity.Certificate certificate = data.getParcelableExtra(KEY_RESUME_CERTIFICATE);
                    int pos = indexOf(mCertificates, certificate);
                    if(pos > -1) {
                        mCertificates.remove(pos);
                        mCertificates.add(pos, certificate);
                    }
                    mAdapter.setData(mCertificates);
                    mAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(KEY_RESUME_CERTIFICATE_LIST, (ArrayList<? extends Parcelable>) mCertificates);
                    setResult(0, intent);
                }
                break;
        }
    }

    private int indexOf(List<ResumeEntity.Certificate> list, ResumeEntity.Certificate item){
        for(int i = 0; i < list.size(); i ++){
            if(TextUtils.equals(list.get(i).mId, item.mId)){
                return i;
            }
        }
        return -1;
    }
}
