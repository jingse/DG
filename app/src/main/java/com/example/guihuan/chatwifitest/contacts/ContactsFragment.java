package com.example.guihuan.chatwifitest.contacts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.chat.ChatActivity;

public class ContactsFragment extends Fragment {

    ExpandableListView expandablelistview;
    //群组名称
    private String[] group = new String[]{"在线好友", "我的好友", "群"};
    public Contact[][] contacts;


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
        expandablelistview.setChildDivider(new ColorDrawable(getResources().getColor(R.color.light_grey)));
        initContacts();
        ExpandableListAdapter adapter = new ContactsAdapter(getContext(), group, contacts);
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
                // Toast.makeText(getActivity(),
                //       group[groupPosition] + " : " + buddy[groupPosition][childPosition],
                //     Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return view;
    }
    private void initContacts(){
        Contact c1 = new Contact("朋友1", R.drawable.head1,"在线");
        Contact c2 = new Contact("朋友2", R.drawable.head2,"在线");
        Contact c3 = new Contact("朋友3", R.drawable.head3,"在线");
        Contact c4 = new Contact("朋友4", R.drawable.head4,"在线");
        contacts = new Contact[][]{
                {c1,c2,c3,c4},
                {c1,c2,c3,c4},
                {}
        };
    }

}