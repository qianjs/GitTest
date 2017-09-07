package com.clinical.tongxin.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MyBaseEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class TextAdapter extends ArrayAdapter<MyBaseEntity> {


    private Context mContext;
    private List<MyBaseEntity> mListData;
    private List<MyBaseEntity> mArrayData;
    private int selectedPos = -1;
    private String selectedText = "";
    private int normalDrawbleId;
    private Drawable selectedDrawble;
    private float textSize;
    private View.OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;

    public TextAdapter(Context context, List<MyBaseEntity> listData, int sId, int nId) {
        super(context, R.string.no_data, listData);
        mContext = context;
        mListData = listData;
        selectedDrawble = mContext.getResources().getDrawable(sId);
        normalDrawbleId = nId;

        init();
    }

    private void init() {
        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos);
                }
            }
        };
    }

    public TextAdapter(Context context, List<MyBaseEntity> arrayData, int ty,int sId, int nId) {
        super(context, R.string.no_data, arrayData);
        mContext = context;
        mArrayData = arrayData;
        selectedDrawble = mContext.getResources().getDrawable(sId);
        normalDrawbleId = nId;
        init();
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (mListData != null && pos < mListData.size()) {
            selectedPos = pos;
            selectedText = mListData.get(pos).getText();
            notifyDataSetChanged();
        } else if (mArrayData != null && pos < mArrayData.size()) {
            selectedPos = pos;
            selectedText = mArrayData.get(pos).getText();
            notifyDataSetChanged();
        }

    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (mListData != null && pos < mListData.size()) {
            selectedText = mListData.get(pos).getText();
        } else if (mArrayData != null && pos < mArrayData.size()) {
            selectedText = mArrayData.get(pos).getText();
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mArrayData != null && selectedPos < mArrayData.size()) {
            return selectedPos;
        }
        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }

        return -1;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.choose_item, parent, false);
        } else {
            view = (TextView) convertView;
        }
        view.setTag(position);
        String mString = "";
        if (mListData != null) {
            if (position < mListData.size()) {
                mString = mListData.get(position).getText();
            }
        } else if (mArrayData != null) {
            if (position < mArrayData.size()) {
                mString = mArrayData.get(position).getText();
            }
        }
        if (mString.contains("不限"))
            view.setText("不限");
        else
            view.setText(mString);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);

        if (selectedText != null && selectedText.equals(mString)) {
            view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
        } else {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
        }
        view.setPadding(20, 0, 0, 0);
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


}
