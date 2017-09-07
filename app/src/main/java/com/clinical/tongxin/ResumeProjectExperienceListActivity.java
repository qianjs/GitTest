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

import com.clinical.tongxin.adapter.ResumeProjectExperienceAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.clinical.tongxin.ResumeInfoActivity.KEY_RESUME_PROJECT_EXPERIENCE_LIST;

/**
 * Created by qjs on 2017/8/28.
 */

public class ResumeProjectExperienceListActivity extends BaseActivity {
    public final static String KEY_RESUME_PROJECT_EXPERIENCE = "key_resume_project_experience";

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.list_view)
    RecyclerView mListView;

    private Unbinder mUnbinder;
    private List<ResumeEntity.ProjectExperience> mProjectExperiences = new ArrayList<>();
    private ResumeProjectExperienceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_project_experience_list);

        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mProjectExperiences);
        setResult(0, intent);
    }

    private void initData(){
        mTitle.setText("项目经验");
        loadData();

        mAdapter = new ResumeProjectExperienceAdapter(this, new IOnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                Intent intent = new Intent(ResumeProjectExperienceListActivity.this, ResumeProjectExperienceInfoActivity.class);
                intent.putExtra(KEY_RESUME_PROJECT_EXPERIENCE, mProjectExperiences.get(position));
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
                        map.put("projectId", mProjectExperiences.get(position).mId);
                        XUtil.Get(UrlUtils.URL_POST_RESUME_PROJECT_EXPERIENCE_DELETE, map, new MyCallBack<String>(){
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
                                        mProjectExperiences.remove(position);
                                        mAdapter.setData(mProjectExperiences);
                                        mAdapter.notifyDataSetChanged();

                                        Intent intent = new Intent();
                                        intent.putParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mProjectExperiences);
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
        mAdapter.setData(mProjectExperiences);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);
    }

    private void loadData(){
        if(getIntent() != null && getIntent().getParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST) != null) {
            mProjectExperiences = getIntent().getParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST);
        }
    }

    @OnClick(R.id.add_btn)
    void onClickAddButton(View view){
        startActivityForResult(new Intent(this, ResumeProjectExperienceInfoActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //新增
            case 0:
                if(data != null && data.getParcelableExtra(KEY_RESUME_PROJECT_EXPERIENCE) != null) {
                    mProjectExperiences.add((ResumeEntity.ProjectExperience)data.getParcelableExtra(KEY_RESUME_PROJECT_EXPERIENCE));
                    mAdapter.setData(mProjectExperiences);
                    mAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mProjectExperiences);
                    setResult(0, intent);
                }
                break;
            //修改
            case 1:
                if(data != null && data.getParcelableExtra(KEY_RESUME_PROJECT_EXPERIENCE) != null) {
                    ResumeEntity.ProjectExperience projectExperience = data.getParcelableExtra(KEY_RESUME_PROJECT_EXPERIENCE);
                    int pos = indexOf(mProjectExperiences, projectExperience);
                    if(pos > -1) {
                        mProjectExperiences.remove(pos);
                        mProjectExperiences.add(pos, projectExperience);
                    }
                    mAdapter.setData(mProjectExperiences);
                    mAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(KEY_RESUME_PROJECT_EXPERIENCE_LIST, (ArrayList<? extends Parcelable>) mProjectExperiences);
                    setResult(0, intent);
                }
                break;
        }
    }

    private int indexOf(List<ResumeEntity.ProjectExperience> projectExperiences, ResumeEntity.ProjectExperience projectExperience){
        for(int i = 0; i < projectExperiences.size(); i ++){
            if(TextUtils.equals(projectExperiences.get(i).mId, projectExperience.mId)){
                return i;
            }
        }
        return -1;
    }
}
