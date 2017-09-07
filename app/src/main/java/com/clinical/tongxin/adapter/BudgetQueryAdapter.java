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
import com.clinical.tongxin.entity.BudgetQueryEntitity;
import com.clinical.tongxin.entity.DistributionBudgetEntity;
import com.clinical.tongxin.util.Utils;

import java.util.List;

import static com.baidu.location.d.j.C;

/**
 * 预算查询
 * Created by Administrator on 2017/1/5 0005.
 */

public class BudgetQueryAdapter extends BaseAdapter {
    private List<BudgetQueryEntitity> list;
    private Context context;
    private LayoutInflater inflater;
    public BudgetQueryAdapter(Context context,List<BudgetQueryEntitity> list){
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
            view=inflater.inflate(R.layout.item_budgetquery,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_Name= (TextView) view.findViewById(R.id.txt_Name);
            viewHolder.txt_AllocationsDate= (TextView) view.findViewById(R.id.txt_AllocationsDate);
            viewHolder.txt_over_money= (TextView) view.findViewById(R.id.txt_over_money);
            viewHolder.txt_not_money= (TextView) view.findViewById(R.id.txt_not_money);
            viewHolder.view_line = view.findViewById(R.id.view_line);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        final BudgetQueryEntitity model=list.get(i);
        viewHolder.txt_Name.setText(model.getPMName());
        viewHolder.txt_AllocationsDate.setText(model.getAllocationsDate());
        viewHolder.txt_over_money.setText(Double.parseDouble(Utils.getMyString(model.getUsedAmount(),"0.0"))+"");
        viewHolder.txt_not_money.setText(Double.parseDouble(Utils.getMyString(model.getUnUsedAmount(),"0.0"))+"");
        if (i%2 == 0){
            viewHolder.view_line.setBackgroundColor(Color.RED);
        }else {
            viewHolder.view_line.setBackgroundColor(Color.GREEN);
        }
        return view;
    }
    class  ViewHolder{
        TextView txt_Name,txt_AllocationsDate,txt_over_money,txt_not_money;
        View view_line;
        Button btn_adjust;
    }


}
