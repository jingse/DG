package com.example.guihuan.chatwifitest.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.chat.ChatActivity;

import java.util.List;
import java.util.Map;

public class ContactsAdapter extends BaseExpandableListAdapter {

    private List<Group> mGroup;
    private Map<Integer, List<Contact>> mChildren;
    private Context context;
    LayoutInflater inflater;
    public ContactsAdapter(Context context, List<Group> mGroup, Map<Integer, List<Contact>> mChildren){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.mGroup=mGroup;
        this.mChildren = mChildren;
    }
    public Contact getChild(int groupPosition, int childPosition) {
        return mChildren.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
                             ViewGroup arg4) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.buddy_listview_child_item, null);
            vHolder = new ViewHolder();
            vHolder.nickTextView = (TextView) convertView.findViewById(R.id.buddy_listview_child_nick);
            vHolder.headPortrait = (ImageView) convertView.findViewById(R.id.buddy_listview_child_avatar);
            vHolder.stateTextView =(TextView)convertView.findViewById(R.id.buddy_listview_child_state);

            convertView.setTag(vHolder);
        }else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        final Contact contact = getChild(groupPosition, childPosition);
        vHolder.nickTextView.setText(contact.getName());
        vHolder.headPortrait.setImageResource(contact.getImageId());
        vHolder.stateTextView.setText(contact.getState());
        convertView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                int imageId = contact.getImageId();
                bundle.putString("chatting_friend_name", contact.getName()); //将朋友名称传进去
                bundle.putInt("chatting_friend_head", imageId); //将朋友头像传进去
                intent.setClass(context, ChatActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mChildren.get(groupPosition).size();
    }

    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    public int getGroupCount() {
        return mGroup.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.buddy_listview_group_item, null);
        }
        TextView groupNameTextView=(TextView) convertView.findViewById(R.id.buddy_listview_group_name);

        TextView onlineNum = (TextView) convertView.findViewById(R.id.buddy_listview_group_num);
        Group group = mGroup.get(groupPosition);
        groupNameTextView.setText(group.getName());
        if (group.getOnlineNum() == -1){
            onlineNum.setText("");
        }
        else {
            System.out.println("在线好友个数：" + group.getOnlineNum());
            onlineNum.setText(group.getOnlineNum()+ "/" + getChildrenCount(groupPosition));
        }

        ImageView indicator = (ImageView) convertView.findViewById(R.id.buddy_listview_image);
        if (isExpanded)
            indicator.setImageResource(R.drawable.ic_icon_unfold);
        else
            indicator.setImageResource(R.drawable.ic_icon_fold);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }
    // 子选项是否可以选择
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
    class ViewHolder{
        public TextView nickTextView;
        public ImageView headPortrait;
        public TextView stateTextView;
    }

}

