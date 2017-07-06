package com.example.guihuan.chatwifitest.messages;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guihuan.chatwifitest.R;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {

    private int resourceId;

    public MsgAdapter(Context context, int textViewResourceId,
                        List<Msg> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.friendImage = view.findViewById(R.id.friend_image);
            viewHolder.friendName = view.findViewById(R.id.friend_name);
            viewHolder.latestMsg = view.findViewById(R.id.latest_msg);
            viewHolder.latestMsgTime = view.findViewById(R.id.latest_msg_time);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.friendImage.setImageResource(msg.getImageId());
        viewHolder.friendName.setText(msg.getName());
        viewHolder.latestMsg.setText(msg.getLatestMsg());
        viewHolder.latestMsgTime.setText(msg.getLatestMsgTime());
        return view;
    }

    class ViewHolder {

        ImageView friendImage;
        TextView friendName;
        TextView latestMsg;
        TextView latestMsgTime;

    }

}