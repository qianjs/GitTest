package com.clinical.tongxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.clinical.tongxin.adapter.NewGroupAdapter;
import com.clinical.tongxin.util.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;


/**
 * Created by linchao on 2017/1/19.
 */

public class HXGroupFragment extends Fragment {
    public static final String TAG = "GroupsActivity";
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private NewGroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    //public static MainActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText query;
    private ImageButton clearSearch;
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(getActivity(), R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hx_groupfragment,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //instance = (MainActivity) getActivity();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        new Thread(){
            @Override
            public void run(){
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    handler.sendEmptyMessage(0);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
        //grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupListView = (ListView) getActivity().findViewById(R.id.list);
        //show group list
//        groupAdapter = new NewGroupAdapter(getActivity(), 1, grouplist);
//        groupListView.setAdapter(groupAdapter);
		query = (EditText) getActivity().findViewById(R.id.query);
		clearSearch = (ImageButton) getActivity().findViewById(R.id.search_clear);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        //pull down to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // create a new group
					startActivityForResult(new Intent(getActivity(), NewGroupActivity.class), 0);
                }  else {
                    // enter group chat
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 1).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }

        });
        groupListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        query.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
                    groupAdapter.getFilter().filter(s);
					if (s.length() > 0) {
						clearSearch.setVisibility(View.VISIBLE);
					} else {
						clearSearch.setVisibility(View.INVISIBLE);
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
			clearSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					query.getText().clear();
				}
			});
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void initView() {

    }

    private void initListener() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void refresh(){
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new NewGroupAdapter(getActivity(), 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

}
