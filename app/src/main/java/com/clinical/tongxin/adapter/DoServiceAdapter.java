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
import com.clinical.tongxin.entity.TaskServiceEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二期 任务列表适配器
 * Created by linchao on 2017/6/15.
 */

public class DoServiceAdapter extends BaseAdapter {
    private Context mcontext;
    private List<TaskServiceEntity> list;
    private LayoutInflater inflater;
    private onClickLeaseLister onClickLeaseLister;
    private boolean isContractor;
    private String status; // 0进行中 1已完成 2仲裁 3终止 

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setContractor(boolean contractor) {
        isContractor = contractor;
    }


    public void setOnClickLeaseLister(DoServiceAdapter.onClickLeaseLister onClickLeaseLister) {
        this.onClickLeaseLister = onClickLeaseLister;
    }

    public DoServiceAdapter(Context mcontext, List<TaskServiceEntity> list) {
        this.mcontext = mcontext;
        this.list = list;
        inflater = LayoutInflater.from(mcontext);
    }

    public void setList(List<TaskServiceEntity> taskEntities) {
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
            view = inflater.inflate(R.layout.item_contractor_service_task, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final TaskServiceEntity entity = list.get(i);
        if (isContractor) {
            viewHolder.llWorks.setVisibility(View.GONE);
            viewHolder.llBtn.setVisibility(View.VISIBLE);
            viewHolder.txtOrderStatus.setVisibility(View.VISIBLE);
            viewHolder.txtOrderStatus.setText("未签到（"+entity.getAbsenceCount()+"）");
        } else {
            viewHolder.llWorks.setVisibility(View.VISIBLE);
            viewHolder.tvWorkers.setText("所需人员："+entity.getWorkers());
            viewHolder.tvPrice.setText(entity.getPrice()+"元/人天");
            viewHolder.llBtn.setVisibility(View.GONE);
            viewHolder.txtOrderStatus.setVisibility(View.GONE);
        }
        viewHolder.tvName.setText(entity.getTaskName());
        viewHolder.tvCity.setText(entity.getCity());
        viewHolder.tvTime.setVisibility(View.VISIBLE);
        viewHolder.tvTime.setText("工期:" + entity.getDeadline());
        viewHolder.TxtOrderNumber.setText("任务编号" + entity.getOrderId());
        if ("0".equals(status)){
            viewHolder.tvBtn1.setVisibility(View.VISIBLE);
            viewHolder.tvBtn2.setVisibility(View.VISIBLE);
            viewHolder.tvBtn2.setText("签到");
            viewHolder.tvBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeaseLister.onClickBtnArbitration(entity);
                }
            });
            viewHolder.tvBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeaseLister.onClickBtnSignIn(entity);
                }
            });
        }else  if ("1".equals(status)){
            if ((isContractor && "0".equals(entity.getIsAcceptScore()))
                    ||("0".equals(entity.getIsSendScore())&&!isContractor)){

                viewHolder.tvBtn2.setVisibility(View.VISIBLE);
                viewHolder.tvBtn2.setText("评价");
                viewHolder.tvBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickLeaseLister.onClickBtnEvaluate(entity);
                    }
                });

            }else {
                viewHolder.tvBtn2.setVisibility(View.GONE);
            }
            viewHolder.tvBtn1.setVisibility(View.GONE);


        }else  if ("2".equals(status)){
            viewHolder.tvBtn1.setVisibility(View.GONE);
            viewHolder.tvBtn2.setText("仲裁信息");
            viewHolder.tvBtn2.setVisibility(View.VISIBLE);
            viewHolder.tvBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeaseLister.onClickBtnArbitrationInfo(entity);
                }
            });
        }else  if ("3".equals(status)){
            viewHolder.tvBtn1.setVisibility(View.GONE);
            viewHolder.tvBtn2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(entity.getProjectTypeName())) {
            switch (entity.getProjectTypeName()) {
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


    public interface onClickLeaseLister {
        void onClickBtnArbitration(TaskServiceEntity taskServiceEntity);
        void onClickBtnSignIn(TaskServiceEntity taskServiceEntity);
        void onClickBtnEvaluate(TaskServiceEntity taskServiceEntity);
        void onClickBtnArbitrationInfo(TaskServiceEntity taskServiceEntity);
    }

    static class ViewHolder {
        @BindView(R.id.Txt_order_number)
        TextView TxtOrderNumber;
        @BindView(R.id.txt_order_status)
        TextView txtOrderStatus;
        @BindView(R.id.img_pic)
        ImageView imgPic;
        @BindView(R.id.img_jiaobiao)
        ImageView imgJiaobiao;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_workers)
        TextView tvWorkers;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.ll_works)
        LinearLayout llWorks;
        @BindView(R.id.tv_btn1)
        TextView tvBtn1;
        @BindView(R.id.tv_btn2)
        TextView tvBtn2;
        @BindView(R.id.ll_btn)
        LinearLayout llBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
