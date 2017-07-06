package com.example.guihuan.chatwifitest.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.guihuan.chatwifitest.R;

/**
 * Created by GuiHuan on 2017/7/6.
 */

public class ContactsFragment extends Fragment {

    ExpandableListView expandablelistview;
    //群组名称
    private String[] group = new String[]{"在线好友", "我的好友", "我的同事"};
    //好友名称
    private String[][] buddy = new String[][]{
            {"元芳", "雷丶小贱", "狄大人"},
            {"高太后", "士兵甲", "士兵乙", "士兵丙"},
            {"艺术家", "叫兽", "攻城师", "职业玩家"}};

    static ContactsFragment newInstance(String s) {
        ContactsFragment newFragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        expandablelistview = (ExpandableListView)view.findViewById(R.id.expandablelistview);
        ExpandableListAdapter adapter = new BuddyAdapter(getContext(), group, buddy);
        expandablelistview.setAdapter(adapter);
        //分组展开
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
            }
        });
        //分组关闭
        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            public void onGroupCollapse(int groupPosition) {
            }
        });
        //子项单击
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupPosition, int childPosition, long arg4) {
                Toast.makeText(getActivity(),
                        group[groupPosition] + " : " + buddy[groupPosition][childPosition],
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return view;
    }

}
