package com.clinical.tongxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.SignListAdapter;
import com.clinical.tongxin.entity.KeywordEntity;
import com.clinical.tongxin.entity.ResultJsonP1;
import com.clinical.tongxin.entity.SignEntity;
import com.clinical.tongxin.inteface.MyCallBack;
import com.clinical.tongxin.myview.MyProgressDialog;
import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.clinical.tongxin.util.XUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class FindSignListActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    // 进度
    private MyProgressDialog mDialog;
    private ListView mListView;
    private ImageView iv_more,img_back;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private SignListAdapter mAdapter;
    private List<SignEntity> mlist;
    private Button btn_ok,btn_Query;
    private String sortId="0";
    private String GroupId;
    private EditText editTxt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findsignlist);
        initView();
        initListener();
        initData("0");

    }
    private void initView()
    {
        title= (TextView) this.findViewById(R.id.title);
        title.setText("选择成员");

        // 加载中
        mDialog = new MyProgressDialog(this, "请稍后...");
        GroupId = getIntent().getExtras().getString("GroupId");
        mListView = (ListView) this.findViewById(R.id.load_more_listview1);
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 20)));
        mlist = new ArrayList<SignEntity>();
        mAdapter = new SignListAdapter(this,mlist);
        mListView.setAdapter(mAdapter);
        // .设置下拉刷新组件和事件监听


        mPtrFrameLayout = (PtrFrameLayout) this.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // TODO Auto-generated method stub
                mlist.clear();
                initData("0");
            }

        });
        // 4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) this.findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);// 设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        // 5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                sortId="<"+mlist.get(mlist.size()-1).getSortId();
                initData(sortId);
            }

        });
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_Query= (Button) findViewById(R.id.btn_Query);
        editTxt_name= (EditText) findViewById(R.id.editTxt_name);
    }
    private void initListener()
    {
        btn_ok.setOnClickListener(this);
        btn_Query.setOnClickListener(this);
//        for(int i=0;i<20;i++)
//        {
//            SignEntity model=new SignEntity();
//            model.setName(i+"niha0");
//            model.setId(i+"");
//            mlist.add(model);
//        }


    }
    private void showList()
    {
        mAdapter=new SignListAdapter(this,mlist);
        mListView.setAdapter(mAdapter);
//        btn_ok.setOnClickListener(this);
    }
    private void initData(String sortId)
    {
//        if(editTxt_name.getText().toString().equals("")){
//            Toast.makeText(this,"请输入查询条件",Toast.LENGTH_SHORT).show();
//            return;
//        }
        mDialog.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("CustomerId", getLoginUserSharedPre().getString("userId", ""));
        map.put("Ukey", getLoginUserSharedPre().getString("Ukey", ""));
        map.put("GroupId", GroupId);
        map.put("SortId", sortId);
        KeywordEntity keyword = new KeywordEntity();
            keyword.setField("NickName");
            keyword.setOp("contains");
            keyword.setValue(editTxt_name.getText().toString());
            List<KeywordEntity> list = new ArrayList<>();
            list.add(keyword);
            String keyJson = new Gson().toJson(list,new TypeToken<List<KeywordEntity>>(){}.getType());
        map.put("filterRules",keyJson);
        XUtil.Post(UrlUtils.URL_ShowMayAddCustomer, map, new MyCallBack<String>() {


            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                super.onError(arg0, arg1);
                Toast.makeText(context, arg0.toString(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onSuccess(String json) {

                // TODO Auto-generated method stub
                super.onSuccess(json);
                ResultJsonP1 myjson = Utils.wsJsonToModel1(json);
                if (myjson != null) {
//                     是否还有数据
                    boolean ishave = false;
                    // 数据是否为空
                    boolean isnull = false;


                    List<SignEntity> list = new Gson().fromJson(myjson.getResult(), new TypeToken<List<SignEntity>>() {
                    }.getType());
                    if (null != list) {
                        if (list.size() > 0) {
                            ishave = true;
                        }
                        for (SignEntity model : list) {
                            mlist.add(model);
                        }
                    } else {
                        ishave = false;
                    }
                    mPtrFrameLayout.refreshComplete();
                    // 第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                    if (mlist.size() <= 0) {
                        isnull = true;
                    }
                    mLoadMoreListViewContainer.loadMoreFinish(isnull, ishave);
//                    mListView.invalidate();
//
//                    mListView.setAdapter(mAdapter);
//                    mAdapter.notifyDataSetChanged();
                    mAdapter.setData(mlist);
//                    showList();
                }
                mDialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
            {
                Intent intent=new Intent();
 //               String[] rel=mAdapter.getSelectIds();
                List<SignEntity> signEntities = mAdapter.getSelectEntity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("newmembers", (Serializable) signEntities);
//                intent.putExtra("ids",rel[0]);
//                intent.putExtra("names",rel[1]);
//                intent.putExtra("newmembers",rel);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
            break;
            case R.id.btn_Query:
            {
                mlist.clear();
                initData("0");
            }
            break;
        }
    }
}
