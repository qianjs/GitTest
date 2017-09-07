package com.clinical.tongxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.DistributionBudgetEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class DistributionBudgetAdapter extends BaseAdapter {
    private List<DistributionBudgetEntity> list;
    private LayoutInflater inflater;
    private Context context;
    private OnClickBudgetlistener onClickBudgetlistener;

    public void setOnClickBudgetlistener(OnClickBudgetlistener onClickBudgetlistener) {
        this.onClickBudgetlistener = onClickBudgetlistener;
    }

    public DistributionBudgetAdapter(List<DistributionBudgetEntity> list, Context context){
        this.list=list;
        this.context=context;
        inflater=LayoutInflater.from(context);
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
            view=inflater.inflate(R.layout.item_distribution_budget,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_Name= (TextView) view.findViewById(R.id.txt_Name);
            viewHolder.txt_in_money= (TextView) view.findViewById(R.id.txt_in_money);
            viewHolder.txt_over_money= (TextView) view.findViewById(R.id.txt_over_money);
            viewHolder.txt_not_money= (TextView) view.findViewById(R.id.txt_not_money);
            viewHolder.btn_adjust= (Button) view.findViewById(R.id.btn_adjust);
            viewHolder.txt_ExpectAmount= (TextView) view.findViewById(R.id.txt_ExpectAmount);
            viewHolder.view_line = view.findViewById(R.id.view_line);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        final DistributionBudgetEntity model=list.get(i);
        viewHolder.txt_Name.setText(model.getPMName());
        viewHolder.txt_in_money.setText(Utils.getDoubleTo2(Double.parseDouble(Utils.getMyString(model.getUsingAmount(),"0.0"))));
        viewHolder.txt_over_money.setText(Utils.getDoubleTo2(Double.parseDouble(Utils.getMyString(model.getUsedAmount(),"0.0"))));
        viewHolder.txt_not_money.setText(Utils.getDoubleTo2(Double.parseDouble(Utils.getMyString(model.getUnUsedAmount(),"0.0"))));
        viewHolder.txt_ExpectAmount.setText(Utils.getDoubleTo2(Double.parseDouble(Utils.getMyString(model.getExpectAmount(),"0.0"))));
        viewHolder.btn_adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBudgetlistener.onclick(model.getPMName(),Utils.getMyString(model.getUsingAmount(),"0.0"),Utils.getMyString(model.getUsedAmount(),"0.0"),Utils.getMyString(model.getUnUsedAmount(),"0.0"),Utils.getMyString(model.getExpectAmount(),"0.0"),model.getDirectorBudgetId());
            }
        });
        if (i%2 == 0){
            viewHolder.view_line.setBackgroundColor(Color.RED);
        }else {
            viewHolder.view_line.setBackgroundColor(Color.GREEN);
        }
        return view;
    }
    class  ViewHolder{
        TextView txt_Name,txt_in_money,txt_over_money,txt_not_money,txt_ExpectAmount;
        Button btn_adjust;
        View view_line;
    }
    public  interface  OnClickBudgetlistener{
        void onclick(String name,String in_money,String  over_money,String not_money,String ExpectAmount,String Id);
    }
}
