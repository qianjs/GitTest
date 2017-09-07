package com.clinical.tongxin.myview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by qjs on 2017/8/2.
 */

public class RecyclerView extends android.support.v7.widget.RecyclerView {
    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
