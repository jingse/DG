package com.example.guihuan.chatwifitest.contacts;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.xlistview.XExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends Fragment {

    //群组名称
    private XExpandableListView mxListView;  //自定义控件
    private ContactsAdapter mAdapter;   //适配器
    private List<String> mGroup;// 组名
    private Map<Integer, List<Contact>> mChildren;// 每一组对应的child
    private View view;


    static ContactsFragment newInstance(String s) {
        ContactsFragment newFragment = new ContactsFragment();

       /* Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        */
        return newFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
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
                initContactData1();     //测试下拉刷新的数据
                mxListView.setAdapter(mAdapter);
                mxListView.stopRefresh();
                mxListView.setRefreshTime(System.currentTimeMillis());
            }

            @Override
            public void onLoadMore() {
            }
        });

        //初始化的数据
        initContactData();
    }

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

    //初始化的数据
    @SuppressLint("UseSparseArrays")
    private void initContactData() {
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
            Contact contact = new Contact(name, icon, "离线", 1);
            mChildren.get(0).add(contact);
        }

        //加载数据，新建适配器
        mAdapter = new ContactsAdapter(getContext(), mGroup, mChildren);
        //列表绑定适配器
        mxListView.setAdapter(mAdapter);
    }

    public static  int[] heads = { R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4};

    public static String[] nameArray = new String[21];

    private static final String[] groups = {"我的好友", "群"};

}