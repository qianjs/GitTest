package com.clinical.tongxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clinical.tongxin.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 仲裁问题适配器
 * @author LINCHAO
 * 2016/12/26
 */
public class ArbitrationQuestionAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<Question> list;
    public ArbitrationQuestionAdapter(Context context, List<String> list) {
        this.list = getList(list);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<Question> getList(List<String> list) {
        List<Question> questions = new ArrayList<>();
        for (String question:list){
            Question question1 = new Question();
            question1.setQuestion(question);
            questions.add(question1);
        }
        return questions;
    }

    public String questionStr(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()){
                if (i == list.size() - 1){
                    sb.append(list.get(i).getQuestion());
                }else {
                    sb.append(list.get(i).getQuestion()).append(",");
                }
            }

        }
        return sb.toString();
    }

    public void setList(List<String> questionList){
        this.list = getList(questionList);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.item_arbitration,null);
            holder = new ViewHolder();
            x.view().inject(holder,view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Question question = list.get(i);
        holder.cb_choose.setChecked(list.get(i).isCheck());
        holder.tv_question.setText(question.getQuestion());
        holder.ll_choose_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            list.get(i).setCheck(!list.get(i).isCheck());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    class ViewHolder{
        @ViewInject(R.id.cb_choose)
        CheckBox cb_choose;
        @ViewInject(R.id.tv_question)
        TextView tv_question;
        @ViewInject(R.id.ll_choose_item)
        View ll_choose_item;
    }

    class Question{
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        private String question;
        private boolean isCheck;
    }
}
