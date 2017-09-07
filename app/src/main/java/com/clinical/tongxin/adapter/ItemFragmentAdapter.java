package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clinical.tongxin.MyApplication;
import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.ItemFragmententity;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ItemFragmentAdapter extends BaseAdapter {
    private List<ItemFragmententity> list;
    private Context context;
    private LayoutInflater inflater;
    private int wholeType;
    public ItemFragmentAdapter(List<ItemFragmententity> list, Context context, int wholeType) {
        this.list = list;
        this.context = context;
        this.wholeType = wholeType;
        inflater = LayoutInflater.from(context);
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

        if (view == null) {
            view = inflater.inflate(R.layout.item_gl_adapter, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();

        }
        int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        if (wholeType == 0){
            viewHolder.imgPic.setLayoutParams(new LinearLayout.LayoutParams((width - Utils.dip2px(context,34))/2,(width - Utils.dip2px(context,34))*97/320));

        }else if (wholeType ==1){
            viewHolder.imgPic.setLayoutParams(new LinearLayout.LayoutParams(width - Utils.dip2px(context,24),(width - Utils.dip2px(context,24))*254/670));
            viewHolder.imgPic.setPadding(0,Utils.dip2px(context,10),0,0);
        }
        ImageLoader.getInstance().displayImage(UrlUtils.BASE_URL1+list.get(i).getImagePath(), viewHolder.imgPic, MyApplication.normalOption);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.img_pic)
        ImageView imgPic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
