package com.clinical.tongxin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class PopWindowImportPrice extends PopupWindow implements View.OnClickListener {
    private LayoutInflater layoutInflater;
    private View conentView;
    private EditText et_price;
    private TextView tv_OK;
    private OnOKClickListener listener;
    private Context context;
    private int chooseType = 1;

    public int getChooseType() {
        return chooseType;
    }

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    public PopWindowImportPrice(Context context)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        conentView=layoutInflater.inflate(R.layout.popwindow_importprice, null);
        setContentView(conentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(-1);
        setHeight(-1);
        initView();
    }
    private void initView()
    {
        et_price=(EditText)conentView.findViewById(R.id.et_price);
//        et_price.setText(price);
        tv_OK=(TextView)conentView.findViewById(R.id.tv_OK);
        tv_OK.setOnClickListener(this);
        //单选框
        RadioGroup radioGroup = (RadioGroup)conentView.findViewById(R.id.rg_import_price);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_person) //rb3设定为正确答案
                {
                    setChooseType(1);
                }else if (checkedId == R.id.rb_team){
                    setChooseType(2);
                }

            }
        });
    }
    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_OK:
                if(et_price.getText().toString().equals(""))
                {
                    Toast.makeText(context, "竞拍价格不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.OnOKClick(et_price.getText().toString(),getChooseType());
                break;

            default:
                break;
        }
    }
    public void setOnOKClickListener(OnOKClickListener listener)
    {
        this.listener=listener;
    }
    public interface OnOKClickListener
    {
        public void OnOKClick(String price,int type);
    }
}
