package com.example.guihuan.chatwifitest.contacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guihuan.chatwifitest.MainActivity;
import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.Var;
import com.example.guihuan.chatwifitest.jsip_ua.impl.DeviceImpl;
import com.example.guihuan.chatwifitest.messages.Msg;
import com.example.guihuan.chatwifitest.xlistview.XExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends Fragment {

    //群组名称
    private XExpandableListView mxListView;  //自定义控件
    private ContactsAdapter mAdapter;   //适配器
    private List<Group> mGroup;// 组名
    private Map<Integer, List<Contact>> mChildren;// 每一组对应的child
    private View view;
    private String[] friendName;
    private String[] onlineFriendName;
    private Map<String, Integer> isOnline;
    private Handler contactsHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Var.FriendDown:
                    String n1 = String.valueOf(msg.obj);
                    refresh(n1, 0);
                    break;
                case Var.FriendUp:
                    String n2 = String.valueOf(msg.obj);
                    refresh(n2, 1);
                    break;

                default:
                    break;
            }
        }
    };

    static ContactsFragment newInstance(String friendList) {
        ContactsFragment newFragment = new ContactsFragment();
        return newFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        DeviceImpl.getInstance().setChatHandler(contactsHandler);
        initUI();

        return view;
    }

    private void initUI() {
        //关联控件的id
        mxListView = (XExpandableListView) view.findViewById(R.id.expandableListView);
        mxListView.setGroupIndicator(null);
        mxListView.setDivider(new ColorDrawable(getResources().getColor(R.color.light_grey)));
        mxListView.setDividerHeight(1);
        mxListView.setChildDivider(new ColorDrawable(getResources().getColor(R.color.light_grey)));
        mxListView.setPullLoadEnable(false);// 禁止下拉加载更多
        mxListView.setXListViewListener(new XExpandableListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                //设置显示刷新的提示
                //initContactData1();     //测试下拉刷新的数据
                mxListView.setAdapter(mAdapter);
                mxListView.stopRefresh();
                mxListView.setRefreshTime(System.currentTimeMillis());
                mxListView.expandGroup(0);
                mxListView.expandGroup(1);
            }

            @Override
            public void onLoadMore() {
            }
        });
//        friendName = Var.friendList.split("&");
  //      onlineFriendName = Var.onlineList.split("&");
        //初始化的数据
        isOnline = new HashMap<String, Integer>();
        initContactData();
    }
/*
    //测试下拉刷新的数据
    @SuppressLint("UseSparseArrays")
    private void initContactData1() {
        mGroup = new ArrayList<String>();
        mChildren = new HashMap<Integer, List<Contact>>();// 给每一组实例化child

        // 初始化组名和child
        for (int i = 0; i < groups.length; ++i) {
            mGroup.add(groups[i]);// 组名
            List<Contact> childUsers = new ArrayList<Contact>();// 每一组的child
            mChildren.put(i, childUsers);
        }
        for (int i = 0; i < 4; i++) {
            int icon = heads[i];
            String name = "朋友" + String.valueOf(i + 1);
            Contact contact = new Contact(name, icon, "在线", 1);
            mChildren.get(0).add(contact);
        }

        mAdapter = new ContactsAdapter(getContext(), mGroup, mChildren);
        mxListView.setAdapter(mAdapter);

    }
    */

    //初始化的数据
    @SuppressLint("UseSparseArrays")
    private void initContactData() {
        mGroup = new ArrayList<Group>();
        mChildren = new HashMap<Integer, List<Contact>>();// 给每一组实例化child
        // 初始化组名和child
        for (int i = 0; i < groups.length; ++i) {
            Group group = new Group();
            group.setName(groups[i]);// 组名
            group.setOnlineNum(0);
            if (groups[i].equals("群")){
                group.setOnlineNum(-1);
                mGroup.add(group);
                List<Contact> childUsers = new ArrayList<Contact>();// 每一组的child
                Contact c = new Contact("群",  R.drawable.headgroup, "", 1);
                mChildren.put(i, childUsers);
                mChildren.get(i).add(c);
            }
            else {
                mGroup.add(group);
                List<Contact> childUsers = new ArrayList<Contact>();// 每一组的child
                mChildren.put(i, childUsers);
            }
        }



        friendName = Var.friendList.split("&");
        onlineFriendName = Var.onlineList.split("&");
        int onlineNum = 0;


        if (!(friendName == null || friendName.length == 0)) {
            for (int i = 0; i < friendName.length; i++) {
                isOnline.put(friendName[i], 0);
                for (int j = 0; j < onlineFriendName.length; j++) {
                    if (onlineFriendName[j].equals(friendName[i])) {
                        isOnline.remove(friendName[i]);
                        isOnline.put(friendName[i], 1);
                        onlineNum +=1;
                        break;
                    }
                }
            }
            for (int i = 0; i < friendName.length; i++) {
                int icon;

                if (i < 4) {
                    icon = heads[i];
                } else {
                    icon = R.drawable.ic_person;
                }
                String state;
                if (isOnline.get(friendName[i]) == 1) {
                    state = "在线";
                } else {
                    state = "离线";
                }
                Contact contact = new Contact(friendName[i], icon, state,0);
                mChildren.get(0).add(contact);
            }
            mGroup.get(0).setOnlineNum(onlineNum);
        }

        //加载数据，新建适配器
        mAdapter = new ContactsAdapter(getContext(), mGroup, mChildren);
        //列表绑定适配器
        mxListView.setAdapter(mAdapter);

    }
    @SuppressLint("UseSparseArrays")
    private void refresh(String name, int state) {

        if (isOnline.get(name)!=null) {
            int onlineNum = mGroup.get(0).getOnlineNum();
            int oldState = isOnline.get(name);
            if (oldState == state){
                return;
            }
            else {
                isOnline.remove(name);
                isOnline.put(name, state);
                if (state == 1){
                    onlineNum ++;
                }
                else{
                    onlineNum--;
                }
            }


            mGroup.clear();
            mChildren.clear();
            //mGroup = new ArrayList<String>();
            //mChildren = new HashMap<Integer, List<Contact>>();// 给每一组实例化child
            //初始化组名和child
            for (int i = 0; i < groups.length; ++i) {
                Group group = new Group();
                group.setName(groups[i]);// 组名
                if (groups[i].equals("群")) {
                    group.setOnlineNum(-1);
                    mGroup.add(group);
                    List<Contact> childUsers = new ArrayList<Contact>();// 每一组的child
                    Contact c = new Contact("群", R.drawable.headgroup, "", 1);
                    mChildren.put(i, childUsers);
                    mChildren.get(i).add(c);
                } else {
                    mGroup.add(group);
                    List<Contact> childUsers = new ArrayList<Contact>();// 每一组的child
                    mChildren.put(i, childUsers);
                }
            }

            if (!(friendName == null || friendName.length == 0)) {
                for (int i = 0; i < friendName.length; i++) {
                    int icon;

                    if (i < 4) {
                        icon = heads[i];
                    } else {
                        icon = R.drawable.ic_person;
                    }
                    String newState;
                    if (isOnline.get(friendName[i]) == 1) {
                        newState = "在线";
                    } else {
                        newState = "离线";
                    }
                    Contact contact = new Contact(friendName[i], icon, newState, 1);
                    mChildren.get(0).add(contact);
                }
            }
            mGroup.get(0).setOnlineNum(onlineNum);
            mAdapter.notifyDataSetChanged();
        }
    }
    public static  int[] heads = { R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4};


    private static final String[] groups = {"我的好友", "群"};
}