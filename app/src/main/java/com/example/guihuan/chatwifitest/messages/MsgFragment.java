package com.example.guihuan.chatwifitest.messages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;


public class MsgFragment extends Fragment {

    private List<Msg> msgList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        MsgAdapter adapter = new MsgAdapter(getContext(), R.layout.msg_item, msgList);
        ListView listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        initMessages();

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Msg msg = msgList.get(position);
                //Toast.makeText(MainActivity.this, msg.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    private void initMessages() {
        Msg friend1 = new Msg("朋友1", R.drawable.head1, "我在南锣鼓巷", "上午10:00");
        msgList.add(friend1);
        Msg friend2 = new Msg("朋友2", R.drawable.head2, "你好", "刚刚");
        msgList.add(friend2);
        Msg friend3 = new Msg("朋友3", R.drawable.head3, "再见咯", "昨天20:58");
        msgList.add(friend3);
        Msg friend4 = new Msg("朋友4", R.drawable.head4, "吃了么？", "今天12:23");
        msgList.add(friend4);

    }

}
