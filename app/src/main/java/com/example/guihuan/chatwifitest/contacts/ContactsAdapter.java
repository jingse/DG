package com.example.guihuan.chatwifitest.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.R;

public class ContactsAdapter extends BaseExpandableListAdapter {


    private String[] group;
    private Contact[][] contacts;
    private Context context;
    LayoutInflater inflater;

    public ContactsAdapter(Context context, String[] group, Contact[][] contacts){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.group=group;
        this.contacts = contacts;
    }
    public Contact getChild(int groupPosition, int childPosition) {
        return contacts[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
                             ViewGroup arg4) {
        Contact contact = getChild(groupPosition, childPosition);
        convertView = inflater.inflate(R.layout.buddy_listview_child_item, null);
        TextView nickTextView=(TextView) convertView.findViewById(R.id.buddy_listview_child_nick);
        ImageView headPortrait = (ImageView)convertView.findViewById(R.id.buddy_listview_child_avatar);
        TextView stateTextView = (TextView)convertView.findViewById(R.id.buddy_listview_child_state);
        nickTextView.setText(contact.getName());
        headPortrait.setImageResource(contact.getImageId());
        stateTextView.setText(contact.getState());

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return contacts[groupPosition].length;
    }

    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    public int getGroupCount() {
        return group.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
        convertView = inflater.inflate(R.layout.buddy_listview_group_item, null);
        TextView groupNameTextView=(TextView) convertView.findViewById(R.id.buddy_listview_group_name);
        groupNameTextView.setText(getGroup(groupPosition).toString());
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }
    // 子选项是否可以选择
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }
}

