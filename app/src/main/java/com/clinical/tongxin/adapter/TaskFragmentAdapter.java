package com.clinical.tongxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.TaskListEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二期 任务列表适配器
 * Created by linchao on 2017/6/15.
 */

public class TaskFragmentAdapter extends BaseAdapter {
    private Context mcontext;
    private List<TaskListEntity> list;
    private LayoutInflater inflater;

    private boolean isContractor;


    public TaskFragmentAdapter(Context mcontext, List<TaskListEntity> list,boolean isContractor) {
        this.mcontext = mcontext;
        this.list = list;
        this.isContractor = isContractor;
        inflater = LayoutInflater.from(mcontext);
    }

    public void setList(List<TaskListEntity> taskEntities) {
        list = taskEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_contractor_task, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        TaskListEntity entity = list.get(i);


        viewHolder.tvName.setText(entity.getTaskName());
        viewHolder.tvCity.setText(entity.getCity());

        viewHolder.tvAmount.setText("￥" + entity.getTotalAmount());
        if (isContractor) {
            if ("1".equals(list.get(i).getType())){ // 租赁
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.llWorks.setVisibility(View.VISIBLE);
                viewHolder.tvWorkers.setText("所需人数："+entity.getWorkers());
                viewHolder.tvPrice.setText(entity.getPrice()+"元/人天");
                viewHolder.llDeadline.setVisibility(View.VISIBLE);
                viewHolder.tvAmount.setVisibility(View.GONE);
                viewHolder.tvDeadline.setText(entity.getDeadline());
            }else{
                viewHolder.tvTime.setVisibility(View.VISIBLE);
                viewHolder.llWorks.setVisibility(View.GONE);
                viewHolder.llDeadline.setVisibility(View.GONE);
                viewHolder.tvAmount.setVisibility(View.VISIBLE);
                viewHolder.tvTime.setText(entity.getTime());
            }
            viewHolder.llNotify.setVisibility(View.GONE);

        } else {
            if ("1".equals(list.get(i).getType())){ // 租赁
                viewHolder.llWorks.setVisibility(View.VISIBLE);
                viewHolder.tvWorkers.setText("所需人数："+entity.getWorkers());
                viewHolder.tvPrice.setText(entity.getPrice()+"元/人天");
                viewHolder.llDeadline.setVisibility(View.VISIBLE);
                viewHolder.tvAmount.setVisibility(View.GONE);
                viewHolder.tvDeadline.setText(entity.getDeadline());
            }else {
                viewHolder.llWorks.setVisibility(View.GONE);
                viewHolder.llDeadline.setVisibility(View.GONE);
                viewHolder.tvAmount.setVisibility(View.VISIBLE);
            }
            viewHolder.llNotify.setVisibility(View.VISIBLE);
            viewHolder.tvTime.setVisibility(View.GONE);
            viewHolder.tvNotify.setText("通知" + entity.getNotifyCount());
            viewHolder.tvOpen.setText("查看" + entity.getOpenCount());
            viewHolder.tvBidding.setText("竞价" + entity.getBiddingCount());
        }
        if (!TextUtils.isEmpty(entity.getProjectType())) {
            switch (entity.getProjectType()) {
                case "传输设备":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_cssb);
                    break;
                case "光缆线路":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_glxl);
                    break;
                case "宽带接入":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_kdjr);
                    break;
                case "室内分布":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_snfb);
                    break;
                case "土建配套":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_tjpt);
                    break;
                case "铁塔配套":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_ttpt);
                    break;
                case "无线主体":
                    viewHolder.imgPic.setImageResource(R.mipmap.project_wxzt);
                    break;
            }
        }

//        if (TextUtils.isEmpty(entity.getTypePicUrl())){
//            entity.setTypePicUrl("upload/index_tu9.png");
//        }
//        ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL+entity.getTypePicUrl(), viewHolder.img_pic, MyApplication.normalOption);
        return view;
    }




    static class ViewHolder {
        @BindView(R.id.img_pic)
        ImageView imgPic;
        @BindView(R.id.img_jiaobiao)
        ImageView img_jiaobiao;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_notify)
        TextView tvNotify;
        @BindView(R.id.tv_open)
        TextView tvOpen;
        @BindView(R.id.tv_bidding)
        TextView tvBidding;
        @BindView(R.id.ll_notify)
        LinearLayout llNotify;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_workers)
        TextView tvWorkers;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.ll_works)
        LinearLayout llWorks;
        @BindView(R.id.tv_deadline)
        TextView tvDeadline;
        @BindView(R.id.ll_deadline)
        LinearLayout llDeadline;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
