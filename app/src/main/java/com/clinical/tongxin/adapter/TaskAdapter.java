package com.clinical.tongxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MedicalRecordEntity;
import com.clinical.tongxin.entity.TaskEntity;
import com.clinical.tongxin.util.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by apple on 2016/12/9.
 */

public class TaskAdapter extends BaseAdapter {
    private Context mcontext;
    private List<TaskEntity> list;
    private LayoutInflater inflater;
    public TaskAdapter(Context mcontext,List<TaskEntity> list){
        this.mcontext=mcontext;
        this.list=list;
        inflater=LayoutInflater.from(mcontext);
    }

    public void setList(List<TaskEntity> taskEntities){
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
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.item_public_project,null);
            viewHolder.img_pic= (ImageView) view.findViewById(R.id.img_pic);
            viewHolder.txt_ProjectName= (TextView) view.findViewById(R.id.txt_ProjectName);
            viewHolder.txt_Amount= (TextView) view.findViewById(R.id.txt_Amount);
            viewHolder.txt_BidCount= (TextView) view.findViewById(R.id.txt_BidCount);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        TaskEntity entity=list.get(i);
        viewHolder.txt_ProjectName.setText(entity.getProjectName());
//        viewHolder.txt_Amount.setText("￥"+entity.getAmount());
        viewHolder.txt_BidCount.setText(entity.getBidCount());
        if (!TextUtils.isEmpty(entity.getProjectTypeName())){
            switch (entity.getProjectTypeName()){
                case "传输设备":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_cssb);
                    break;
                case "光缆线路":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_glxl);
                    break;
                case "宽带接入":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_kdjr);
                    break;
                case "室内分布":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_snfb);
                    break;
                case "土建配套":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_tjpt);
                    break;
                case "铁塔配套":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_ttpt);
                    break;
                case "无线主体":
                    viewHolder.img_pic.setImageResource(R.mipmap.project_wxzt);
                    break;
            }
        }

//        if (TextUtils.isEmpty(entity.getTypePicUrl())){
//            entity.setTypePicUrl("upload/index_tu9.png");
//        }
//        ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL+entity.getTypePicUrl(), viewHolder.img_pic, MyApplication.normalOption);
        return view;
    }
    static class  ViewHolder{
        ImageView img_pic;
        TextView txt_ProjectName;
        TextView txt_Amount;
        TextView txt_BidCount;

    }
}
