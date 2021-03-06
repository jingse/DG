package com.example.guihuan.chatwifitest.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.emoji.EmojiUtil;
import com.example.guihuan.chatwifitest.utils.CircleImageView;

import java.io.IOException;
import java.util.List;

public class ChatMsgAdapter extends ArrayAdapter<ChatMsg> {
    private int resourceId;

    public ChatMsgAdapter(Context context, int textViewResourceId, List<ChatMsg> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg chatMsg = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = view.findViewById(R.id.right_msg);
            viewHolder.friendName = view.findViewById(R.id.chatting_name);
            viewHolder.friendImage = view.findViewById(R.id.chatting_person_head);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (chatMsg.getType() == ChatMsg.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);

            viewHolder.leftMsg.setText(chatMsg.getContent());
            displayTextView(viewHolder.leftMsg);
        } else if(chatMsg.getType() == ChatMsg.TYPE_SENT) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);

            viewHolder.rightMsg.setText(chatMsg.getContent());
            displayTextView(viewHolder.rightMsg);
        }
        viewHolder.friendName.setText(chatMsg.getFriendName());
        viewHolder.friendImage.setImageResource(chatMsg.getFriendImageId());
        return view;
    }


    private void displayTextView(TextView textView) {
        try {
            EmojiUtil.handlerEmojiText(textView, textView.getText().toString(), getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ViewHolder {

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        CircleImageView friendImage;
        TextView friendName;

    }

}
