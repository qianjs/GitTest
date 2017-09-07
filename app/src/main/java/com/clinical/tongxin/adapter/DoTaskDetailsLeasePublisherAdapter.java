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
import com.clinical.tongxin.entity.LeaseEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 做任务详情 租赁 二期
 *
 * @author LINCHAO
 *         2017/8/15
 */
public class DoTaskDetailsLeasePublisherAdapter extends BaseAdapter {

    private List<LeaseEntity> list;
    private Context context;
    private LayoutInflater mInflater;
    private onClickDetailsLister onClickDetailsLister;
    private boolean isContractor;
    public void setOnClickDetailsLister(DoTaskDetailsLeasePublisherAdapter.onClickDetailsLister onClickDetailsLister) {
        this.onClickDetailsLister = onClickDetailsLister;
    }


    public DoTaskDetailsLeasePublisherAdapter(Context context, List<LeaseEntity> list,boolean isContractor) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.isContractor = isContractor;
    }

    public void setList(List<LeaseEntity> leaseEntities) {
        this.list = leaseEntities;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LeaseEntity getItem(int i) {
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
            view = mInflater.inflate(R.layout.item_do_task_lease_pulisher, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final LeaseEntity leaseEntity = list.get(i);
        try {
            viewHolder.tvName.setText(leaseEntity.getName());
            viewHolder.rbScore.setRating(Float.valueOf(leaseEntity.getScore()));
            viewHolder.tvPhone.setText("电话："+leaseEntity.getPhone());
            viewHolder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickDetailsLister.call(leaseEntity);
                }
            });
            viewHolder.ivCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickDetailsLister.onClickCalendar(leaseEntity);
                }
            });
            viewHolder.tvCount.setText(leaseEntity.getConfirmCount());

            if ("0".equals(leaseEntity.getStatus())){ // 进行中
                viewHolder.tv1.setVisibility(View.VISIBLE);
                viewHolder.tv1.setText("终止");
                viewHolder.tv2.setVisibility(View.VISIBLE);
                viewHolder.tv2.setText("仲裁");
                viewHolder.tv3.setVisibility(View.VISIBLE);
                viewHolder.tv3.setText("支付");
                viewHolder.tv4.setVisibility(View.INVISIBLE);
                viewHolder.tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickDetailsLister.onClickBtnOver(leaseEntity);
                    }
                });
                viewHolder.tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickDetailsLister.onClickBtnArbitration(leaseEntity);
                    }
                });
                viewHolder.tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickDetailsLister.onClickBtnPay(leaseEntity);
                    }
                });
            }else if ("1".equals(leaseEntity.getStatus())){// 终止
                viewHolder.tv1.setVisibility(View.INVISIBLE);
                viewHolder.tv2.setVisibility(View.INVISIBLE);
                viewHolder.tv3.setVisibility(View.INVISIBLE);
                viewHolder.tv4.setVisibility(View.INVISIBLE);
            }else if ("2".equals(leaseEntity.getStatus())){// 仲裁
                viewHolder.tv1.setVisibility(View.VISIBLE);
                viewHolder.tv1.setText("仲裁信息");
                viewHolder.tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickDetailsLister.onClickBtnArbitrationInfo(leaseEntity);
                    }
                });
                viewHolder.tv2.setVisibility(View.INVISIBLE);
                viewHolder.tv3.setVisibility(View.INVISIBLE);
                viewHolder.tv4.setVisibility(View.INVISIBLE);
            }else if ("3".equals(leaseEntity.getStatus())){// 已完成
                if ((isContractor && "0".equals(leaseEntity.getIsAcceptScore()))
                        ||("0".equals(leaseEntity.getIsSendScore())&&!isContractor)){
                    viewHolder.tv1.setVisibility(View.VISIBLE);
                    viewHolder.tv1.setText("评价");
                    viewHolder.tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onClickDetailsLister.onClickBtnEvaluate(leaseEntity);
                        }
                    });

                }else {
                    viewHolder.tv1.setVisibility(View.INVISIBLE);
                }
                viewHolder.tv2.setVisibility(View.INVISIBLE);
                viewHolder.tv3.setVisibility(View.INVISIBLE);
                viewHolder.tv4.setVisibility(View.INVISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    public interface onClickDetailsLister {
        void onClickBtnOver(LeaseEntity leaseEntity);
        void onClickBtnArbitration(LeaseEntity leaseEntity);
        void onClickBtnPay(LeaseEntity leaseEntity);
        void onClickBtnEvaluate(LeaseEntity leaseEntity);
        void call(LeaseEntity leaseEntity);
        void onClickCalendar(LeaseEntity leaseEntity);
        void onClickBtnArbitrationInfo(LeaseEntity leaseEntity);
    }





    static class ViewHolder {
        @BindView(R.id.iv_header)
        ImageView ivHeader;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.rb_score)
        RatingBar rbScore;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.iv_calendar)
        ImageView ivCalendar;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;
        @BindView(R.id.tv_count)
        TextView tvCount;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
