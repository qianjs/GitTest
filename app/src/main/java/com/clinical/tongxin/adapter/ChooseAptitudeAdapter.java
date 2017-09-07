package com.clinical.tongxin.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinical.tongxin.R;
import com.clinical.tongxin.entity.AptitudeEntity;
import com.clinical.tongxin.entity.ProjectDetailEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by linchao on 2016/12/28.
 */

public class ChooseAptitudeAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<Aptitude> aptitudes;
    private List<AptitudeEntity> chooseAptitudes;

    public ChooseAptitudeAdapter(Context context,List<AptitudeEntity> chooseAptitudes) {
        this.context = context;
        aptitudes = new ArrayList<>();
        this.chooseAptitudes = chooseAptitudes;
    }


    public Map<String,String> getData(){
        Map<String,String> map = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        List<AptitudeEntity> aptitudeEntities = new ArrayList<>();
        for (int i = 0; i < aptitudes.size(); i++) {
            String name = aptitudes.get(i).getName();
            String id = aptitudes.get(i).getId();
            StringBuffer sbFunc = new StringBuffer();
            for (int j = 0; j < aptitudes.get(i).getAptitudeChilds().size(); j++) {
                if (aptitudes.get(i).getAptitudeChilds().get(j).isCheck()){
                    sbFunc.append(aptitudes.get(i).getAptitudeChilds().get(j).getChildName()+" ");
                    AptitudeEntity aptitudeEntity = new AptitudeEntity();
                    aptitudeEntity.setProjectTypeId(id);
                    aptitudeEntity.setTaskTypeId(aptitudes.get(i).getAptitudeChilds().get(j).getChildId());
                    aptitudeEntities.add(aptitudeEntity);
                }
            }
            if (!TextUtils.isEmpty(sbFunc.toString())){
                sb.append(name).append(":").append(sbFunc.toString()).append("\n");
            }
        }
        String json = new Gson().toJson(aptitudeEntities,new TypeToken<List<AptitudeEntity>>(){}.getType());
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf("\n") != -1){
            String text = sb.toString().substring(0,sb.toString().lastIndexOf("\n")-1);
            map.put("text",text);
        }
        map.put("json",json);
        return map;
    }

    public String getJson(){
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < aptitudes.size(); i++) {
            String name = aptitudes.get(i).getName();
            StringBuffer sbFunc = new StringBuffer();
            for (int j = 0; j < aptitudes.get(i).getAptitudeChilds().size(); j++) {
                if (aptitudes.get(i).getAptitudeChilds().get(j).isCheck()){
                    sbFunc.append(aptitudes.get(i).getAptitudeChilds().get(j).getChildName()+" ");
                }
            }
            if (!TextUtils.isEmpty(sbFunc.toString())){
                sb.append(name).append(":").append(sbFunc.toString()).append("\n");
            }
        }
        if (!TextUtils.isEmpty(sb.toString()) && sb.toString().lastIndexOf("\n") != -1){
            String temp = sb.toString().substring(0,sb.toString().lastIndexOf("\n")-1);
            return temp;
        }
        return sb.toString();
    }

    public void setList(int groupPosition, int childPosition){
        if (aptitudes.get(groupPosition).getAptitudeChilds().get(childPosition).isCheck()){
            aptitudes.get(groupPosition).getAptitudeChilds().get(childPosition).setCheck(false);
        }else {
            aptitudes.get(groupPosition).getAptitudeChilds().get(childPosition).setCheck(true);
        }
        notifyDataSetChanged();

    };
    @Override
    public int getGroupCount() {
        return aptitudes.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return aptitudes.get(i).getAptitudeChilds().size();
    }

    @Override
    public Object getGroup(int i) {
        return aptitudes.get(i);
    }

    @Override
    public AptitudeChild getChild(int i, int i1) {
        return aptitudes.get(i).getAptitudeChilds().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.aptitude_item,null);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_aptitude);
        tv.setText(aptitudes.get(i).getName());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.team_member_item,null);
        final CheckBox cb_choose = (CheckBox) view.findViewById(R.id.cb_choose);
        cb_choose.setChecked(aptitudes.get(i).getAptitudeChilds().get(i1).isCheck());
        ((ImageView)view.findViewById(R.id.iv_member_head)).setVisibility(View.GONE);
        TextView tv_member_name = (TextView) view.findViewById(R.id.tv_member_name);
        tv_member_name.setText(aptitudes.get(i).getAptitudeChilds().get(i1).getChildName());
        View ll_choose_item = view.findViewById(R.id.ll_choose_item);
        ll_choose_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChild(i,i1).setCheck(!cb_choose.isChecked());
                cb_choose.setChecked(!cb_choose.isChecked());
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setList(List<AptitudeEntity> projects,List<AptitudeEntity> tasks)
    {
        for (AptitudeEntity project:projects){
            List<AptitudeEntity> tasksFilter = new ArrayList<>();
            for (AptitudeEntity task:tasks){
                if (project.getProjectTypeId().equals(task.getProjectTypeId())){
                    tasksFilter.add(task);
                }
            }
            addInfo(project,tasksFilter);
        }
        notifyDataSetChanged();
    }


    private void addInfo(AptitudeEntity project,List<AptitudeEntity> tasks){
        List<AptitudeChild> aptitudeChilds = new ArrayList<>();
        for (AptitudeEntity task:tasks){
            AptitudeChild aptitudeChild = new AptitudeChild();
            aptitudeChild.setChildName(task.getTaskTypeName());
            aptitudeChild.setChildId(task.getTaskTypeId());
            aptitudeChilds.add(aptitudeChild);
            for (AptitudeEntity choose:chooseAptitudes){
                if (task.getTaskTypeId().equals(choose.getTaskTypeId())){
                    aptitudeChild.setCheck(true);
                    break;
                }
            }
        }

        Aptitude aptitude = new Aptitude();
        aptitude.setName(project.getProjectTypeName());
        aptitude.setAptitudeChilds(aptitudeChilds);
        aptitude.setId(project.getProjectTypeId());
        aptitudes.add(aptitude);
    }

    class Aptitude{
        private String name;
        private String id;
        private List<AptitudeChild> aptitudeChilds;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<AptitudeChild> getAptitudeChilds() {
            return aptitudeChilds;
        }

        public void setAptitudeChilds(List<AptitudeChild> aptitudeChilds) {
            this.aptitudeChilds = aptitudeChilds;
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
    class AptitudeChild{

        private boolean isCheck;
        private String childName;
        private String childId;

        public String getChildId() {
            return childId;
        }

        public void setChildId(String childId) {
            this.childId = childId;
        }


        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }


        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }


    }
}
