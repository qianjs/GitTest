package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.EvaluateListEntity;
import com.clinical.tongxin.entity.PublisherEntity;
import com.clinical.tongxin.entity.Tag;
import com.clinical.tongxin.myview.TagListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 任务详情 二期
 *
 * @author LINCHAO
 *         2017/6/20
 */
public class TaskDetailsPublisherAdapter extends BaseAdapter {

    private List<PublisherEntity> list;
    private Context context;
    private LayoutInflater mInflater;
    private onClickDetailsLister onClickDetailsLister;
    public void setOnClickDetailsLister(TaskDetailsPublisherAdapter.onClickDetailsLister onClickDetailsLister) {
        this.onClickDetailsLister = onClickDetailsLister;
    }


    public TaskDetailsPublisherAdapter(Context context, List<PublisherEntity> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<PublisherEntity> publisherEntities) {
        this.list = publisherEntities;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PublisherEntity getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_task_details_publisher, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final PublisherEntity publisherEntity = list.get(i);
        try {
            viewHolder.rbScore.setRating(Float.valueOf(publisherEntity.getScore()));
            viewHolder.tvPrice.setText(publisherEntity.getBiddingAmount());
            viewHolder.tvScore.setText(publisherEntity.getScore());

            if (publisherEntity.isShowEvaluate() && publisherEntity.getEvaluateListEntities() != null){
                viewHolder.tagview.setVisibility(View.VISIBLE);
                viewHolder.tagview.setTags(setUpData(publisherEntity.getEvaluateListEntities()));
                viewHolder.ivDown.setImageResource(R.mipmap.up);
            }else {
                viewHolder.tagview.setVisibility(View.GONE);
                viewHolder.ivDown.setImageResource(R.mipmap.down);
            }
            viewHolder.llitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickDetailsLister.onItemClick(i);
                }
            });
            viewHolder.tvCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickDetailsLister.onConfirm(publisherEntity);
                }
            });



        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return view;
    }


    static class ViewHolder {
        @BindView(R.id.iv_down)
        ImageView ivDown;
        @BindView(R.id.rb_score)
        RatingBar rbScore;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_score)
        TextView tvScore;
        @BindView(R.id.tagview)
        TagListView tagview;
        @BindView(R.id.ll_tag)
        LinearLayout llTag;
        @BindView(R.id.tv_commit)
        TextView tvCommit;
        @BindView(R.id.ll_item)
        LinearLayout llitem;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onClickDetailsLister{
        void onConfirm(PublisherEntity publisherEntity);
        void onItemClick(int position);
    }

    public void setEvaluate(List<EvaluateListEntity> evaluateList,int position){

        list.get(position ).setEvaluateListEntities(evaluateList);
        list.get(position ).setShowEvaluate(!list.get(position ).isShowEvaluate());
        notifyDataSetChanged();
    }
    private List<Tag> setUpData(List<EvaluateListEntity> evaluateListEntities) {
        List<Tag> mTags = new ArrayList<Tag>();
        for (int i = 0; i < evaluateListEntities.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(evaluateListEntities.get(i).getComment() + " " + evaluateListEntities.get(i).getCount());
            tag.setIsterms(evaluateListEntities.get(i).getIsterms());
            mTags.add(tag);
        }
        return mTags;
    }
}
