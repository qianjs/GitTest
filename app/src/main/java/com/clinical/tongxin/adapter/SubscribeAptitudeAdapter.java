package com.clinical.tongxin.adapter;

import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.location.d.j.ap;
import static com.clinical.tongxin.R.id.cb_choose;
import static com.clinical.tongxin.R.id.cb_choose_project;
import static com.clinical.tongxin.R.id.ll_choose_item;
import static com.clinical.tongxin.R.id.ll_choose_task;
import static com.clinical.tongxin.R.id.map;
import static com.clinical.tongxin.R.id.tv_member_name;


/**
 * 任务订阅 按项目接收
 * Created by linchao on 2017/6/22.
 */

public class SubscribeAptitudeAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<Aptitude> aptitudes;


    public SubscribeAptitudeAdapter(Context context) {
        this.context = context;
        aptitudes = new ArrayList<>();

    }


    public String getJsonData(){
        List<AptitudeEntity> aptitudeEntities = new ArrayList<>();
        for (int i = 0; i < aptitudes.size(); i++) {
            String id = aptitudes.get(i).getId();
            for (int j = 0; j < aptitudes.get(i).getAptitudeChilds().size(); j++) {
                if (aptitudes.get(i).getAptitudeChilds().get(j).isCheck()){
                    AptitudeEntity aptitudeEntity = new AptitudeEntity();
                    aptitudeEntity.setProjectTypeId(id);
                    aptitudeEntity.setTaskTypeId(aptitudes.get(i).getAptitudeChilds().get(j).getChildId());
                    aptitudeEntities.add(aptitudeEntity);
              }
            }

        }
        if (aptitudeEntities.size() == 0){
            return "";
        }
        String json = new Gson().toJson(aptitudeEntities,new TypeToken<List<AptitudeEntity>>(){}.getType());
        return json;
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
    public Aptitude getGroup(int i) {
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
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_subscribe_project,null);
        TextView tv_project = (TextView) view.findViewById(R.id.tv_project);
        tv_project.setText(aptitudes.get(i).getName());
        final CheckBox cb_choose_project = (CheckBox)view.findViewById(R.id.cb_choose_project);
        cb_choose_project.setChecked(getGroup(i).isGroupCheck);
        View ll_project = view.findViewById(R.id.ll_project);
        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGroup(i).setGroupCheck(!cb_choose_project.isChecked());
                for (int j = 0; j < getGroup(i).getAptitudeChilds().size(); j++) {
                    getGroup(i).getAptitudeChilds().get(j).setCheck(!cb_choose_project.isChecked());
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_subscribe_task,null);
        final CheckBox cb_choose_task = (CheckBox) view.findViewById(R.id.cb_choose_task);
        cb_choose_task.setChecked(aptitudes.get(i).getAptitudeChilds().get(i1).isCheck());

        TextView tv_task = (TextView) view.findViewById(R.id.tv_task);
        tv_task.setText(aptitudes.get(i).getAptitudeChilds().get(i1).getChildName());

        View ll_choose_task = view.findViewById(R.id.ll_choose_task);
        ll_choose_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChild(i,i1).setCheck(!cb_choose_task.isChecked());
                boolean selectAll = true;
                for (int j = 0; j < getGroup(i).getAptitudeChilds().size(); j++) {
                   if (!getGroup(i).getAptitudeChilds().get(j).isCheck()){
                       selectAll = false;
                       break;
                   }
                }
                getGroup(i).setGroupCheck(selectAll);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setList(List<AptitudeEntity> projects,List<AptitudeEntity> tasks,List<AptitudeEntity> chooseProjects)
    {
        for (AptitudeEntity project:projects){
            List<AptitudeEntity> tasksFilter = new ArrayList<>();
            for (AptitudeEntity task:tasks){
                if (project.getProjectTypeId().equals(task.getProjectTypeId())){
                    tasksFilter.add(task);
                }
            }
            addInfo(project,tasksFilter,chooseProjects);
        }
        notifyDataSetChanged();
    }


    private void addInfo(AptitudeEntity project,List<AptitudeEntity> tasks,List<AptitudeEntity> chooseProjects){
        List<AptitudeChild> aptitudeChilds = new ArrayList<>();
        boolean selectAll = true;
        for (AptitudeEntity task:tasks){
            AptitudeChild aptitudeChild = new AptitudeChild();
            aptitudeChild.setChildName(task.getTaskTypeName());
            aptitudeChild.setChildId(task.getTaskTypeId());
            for (AptitudeEntity choose:chooseProjects){
                if (choose.getTaskTypeId().equals(task.getTaskTypeId())){
                    aptitudeChild.setCheck(true);
                }
            }
            if (!aptitudeChild.isCheck()){
                selectAll = false;
            }
            aptitudeChilds.add(aptitudeChild);
        }

        Aptitude aptitude = new Aptitude();
        aptitude.setName(project.getProjectTypeName());
        aptitude.setAptitudeChilds(aptitudeChilds);
        aptitude.setId(project.getProjectTypeId());
        aptitude.setGroupCheck(selectAll);
        aptitudes.add(aptitude);
    }

    class Aptitude{
        private String name;
        private String id;
        private boolean isGroupCheck;
        private List<AptitudeChild> aptitudeChilds;

        public boolean isGroupCheck() {
            return isGroupCheck;
        }

        public void setGroupCheck(boolean groupCheck) {
            isGroupCheck = groupCheck;
        }

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
