package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.MemberEntity;
import com.clinical.tongxin.entity.SignEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class SignListAdapter extends BaseAdapter {
    private List<SignEntity> list;
    private LayoutInflater inflater;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    private Context context;
    public SignListAdapter(Context context, List<SignEntity> list)
    {
        this.list=list;
        inflater=LayoutInflater.from(context);
        this.context=context;
        //isSelected = new HashMap<Integer, Boolean>();
        //initDate();
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public void setData(List<SignEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public String[] getSelectIds()
    {

        List<String> members = new ArrayList<>();
//        String[] rel=new String[2];
//        String relId="";
//        String relText="";
        Iterator iter=isSelected.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if(val.toString().equals("true")) {
                members.add(list.get(Integer.parseInt(key.toString())).getMobile());
//                relId += list.get(Integer.parseInt(key.toString())).getId() + ",";
//                relText += list.get(Integer.parseInt(key.toString())).getMobile() + ",";
            }
        }
//        if(rel!=null)
//        {
//            if (relId.length()>0){
//                relId=relId.substring(0,relId.length()-1);
//            }if (relText.length()>0){
//            relText=relText.substring(0,relText.length()-1);
//            }
//
//        }
//        rel[0]=relId;
//        rel[1]=relText;
        return members.toArray(new String[members.size()]);
    }

    public List<SignEntity> getSelectEntity(){
        List<SignEntity> signEntities = new ArrayList<>();
        for (SignEntity signEntity:list){
            if (signEntity.isChecked()){
                signEntities.add(signEntity);
            }
        }
//        Iterator iter=isSelected.entrySet().iterator();
//        while (iter.hasNext())
//        {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            if(val.toString().equals("true")) {
//                signEntities.add(list.get(Integer.parseInt(key.toString())));
//            }
//        }
        return signEntities;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SignListAdapter.isSelected = isSelected;
    }
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_checklistview, null);
            viewHolder.txt_name=(TextView)convertView.findViewById(R.id.txt_name);
            viewHolder.checkBox1=(CheckBox)convertView.findViewById(R.id.checkBox1);
            viewHolder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        //isSelected.clear();
//        initDate();
        viewHolder.txt_name.setText(list.get(position).getName());
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setChecked(!list.get(position).isChecked());
                notifyDataSetChanged();
            }
        });
//        viewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                if (isSelected.get(position)) {
//                    isSelected.put(position, false);
//                    setIsSelected(isSelected);
//                } else {
//                    isSelected.put(position, true);
//                    setIsSelected(isSelected);
//                }
//
//            }
//        });
        // 根据isSelected来设置checkbox的选中状况
        viewHolder.checkBox1.setChecked(list.get(position).isChecked());
        return convertView;
    }
    class ViewHolder
    {
        TextView txt_name;
        CheckBox checkBox1;
        View ll_item;
    }

}
