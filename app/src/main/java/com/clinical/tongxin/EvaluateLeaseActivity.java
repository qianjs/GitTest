package com.clinical.tongxin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.entity.EvaluateNewEntity;
import com.clinical.tongxin.entity.ResultEvaluateEntity;
import com.clinical.tongxin.entity.Tag;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.myview.TagListView;
import com.clinical.tongxin.myview.TagView;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 租赁任务评价
 */
public class EvaluateLeaseActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.rb_score)
    RatingBar rbScore;
    @BindView(R.id.tagview)
    TagListView tagview;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.et_evaluate)
    EditText etEvaluate;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_name)
    TextView tvName;

    private MyProgressDialog mDialog;
    private String taskId;
    private List<EvaluateNewEntity> evaluateNewEntities;
    private String isTerms = "0";//是否是正向 0正 1负
    private String signCustomerId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_evaluate);
        ButterKnife.bind(this);
        initView();
        evaluateQuestion();
    }

    private void initView() {
        title.setText("评价");
        taskId = getIntent().getStringExtra("taskId");
        name = getIntent().getStringExtra("name");
        tvName.setText(TextUtils.isEmpty(name)?"":name);
        signCustomerId = getIntent().getStringExtra("signCustomerId");
        mDialog = new MyProgressDialog(context, "请稍后...");
        rbScore.setRating(5);
        rbScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v < 5) {
                    isTerms = "1";
                } else {
                    isTerms = "0";
                }
                if (evaluateNewEntities == null) {
                    evaluateQuestion();
                } else {
                    setUpData(evaluateNewEntities);
                }
            }
        });
        tagview.setVisibility(View.VISIBLE);
        tagview.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                if (tag.isChecked()) {
                    tagView.setBackgroundResource(R.drawable.tag_bg);
                    tagView.setTextColor(getResources().getColor(R.color.txt_gray));
                    tag.setChecked(false);
                } else {
                    tagView.setBackgroundResource(R.drawable.tag_pressed_red);
                    tag.setChecked(true);
                    tagView.setTextColor(Color.WHITE);
                }

            }
        });
    }

    /**
     * 设置评价
     *
     * @param evaluateNewEntities 评价集合
     */
    private void setUpData(List<EvaluateNewEntity> evaluateNewEntities) {
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < evaluateNewEntities.size(); i++) {
            if (isTerms.equals(evaluateNewEntities.get(i).getIsTerms())) {
                Tag tag = new Tag();
                tag.setId(i);
                tag.setChecked(false);
                tag.setTitle(evaluateNewEntities.get(i).getTaskReviewItemName());
                tag.setIsterms(2);
                mTags.add(tag);
            }
        }
        tagview.setTags(mTags);
    }

    /**
     * 获取评价问题接口
     */
    private void evaluateQuestion() {
        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("TaskId", taskId);
        map.put("SendOrReceive", "接包方".equals(getLoginConfig().getRole()) ? "1" : "0");
        XUtil.Post(UrlUtils.URL_queryTaskreviewitemApp, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    Gson gson = new Gson();
                    ResultEvaluateEntity<List<EvaluateNewEntity>> resultEntity = gson.fromJson(arg0, new TypeToken<ResultEvaluateEntity<List<EvaluateNewEntity>>>() {
                    }.getType());
                    if (resultEntity != null && resultEntity.getCode() == 200) {
                        evaluateNewEntities = resultEntity.getResult();
                        tvName.setText(resultEntity.getBeingEvaluated());
                        setUpData(evaluateNewEntities);
                    } else {
                        Toast.makeText(context, resultEntity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "获取评价问题失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, "获取评价问题失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        });
    }

    private void commitEvaluate() {

        mDialog.show();
        Map map = new HashMap<>();
        map.put("CustomerId", getLoginConfig().getUserId());
        map.put("Ukey", getLoginConfig().getUkey());
        map.put("taskId", taskId);
        map.put("reviewText", etEvaluate.getText().toString());
        map.put("taskReviewItemNameList", getQuestion());
        map.put("rating", rbScore.getRating() + "");
        map.put("signCustomerId",signCustomerId);
        XUtil.Post(UrlUtils.URL_reviewTaskLease, map, new MyCallBack<String>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();

            }

            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                try {
                    JSONObject obj = new JSONObject(arg0);
                    if (obj.getString("code").equals("200")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK, new Intent(context, TaskDetailsActivity.class));
                                finish();
                            }
                        }, 1000);
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public String getQuestion() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tagview.getTags().size(); i++) {
            if (tagview.getTags().get(i).isChecked()) {
                if (i == tagview.getTags().size() - 1) {
                    sb.append(tagview.getTags().get(i).getTitle());
                } else {
                    sb.append(tagview.getTags().get(i).getTitle()).append(",");
                }
            }

        }
        return sb.toString();
    }

    @OnClick(R.id.tv_commit)
    public void onClick() {
        commitEvaluate();
    }
}




