package com.example.guihuan.chatwifitest.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.guihuan.chatwifitest.R;
import com.example.guihuan.chatwifitest.utils.CircleImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {

    private List<ChatMsg> chatMsgList = new ArrayList<>();
    private ListView chatMsgListView;
    private ChatMsgAdapter adapter;

    private EditText inputText;
    private ImageButton send;
    private ImageButton recordSound;
    private ImageButton showPicture;
    private ImageButton showCamera;
    private ImageButton showVideo;
    private ImageButton showFile;
    private ImageButton showEmoji;

    private CircleImageView chatting_person_head;
    private Text chatting_person_name;







    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initChatMsgs();

        adapter = new ChatMsgAdapter(ChatActivity.this, R.layout.chat_msg_item, chatMsgList);
        chatMsgListView = findViewById(R.id.msg_list_view);
        chatMsgListView.setAdapter(adapter);

        chatting_person_head = findViewById(R.id.chatting_person_head);
        chatting_person_name = findViewById(R.id.chatting_person_name);



        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);





        send.setOnClickListener(new ListView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    ChatMsg chatMsg = new ChatMsg(content, ChatMsg.TYPE_SENT);
                    chatMsgList.add(chatMsg);
                    adapter.notifyDataSetChanged();
                    chatMsgListView.setSelection(chatMsgList.size());
                    inputText.setText("");
                }
            }
        });

        /*recordSound.setOnClickListener(new ListView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        showPicture.setOnClickListener(new ListView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        showCamera.setOnClickListener(new ListView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        showVideo.setOnClickListener(new ListView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        showFile.setOnClickListener(new ListView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        showEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/



    }

    private void initChatMsgs() {
        ChatMsg chatMsg1 = new ChatMsg("Hello guy.", ChatMsg.TYPE_RECEIVED);
        chatMsgList.add(chatMsg1);
        ChatMsg chatMsg2 = new ChatMsg("Hello. Who is that?", ChatMsg.TYPE_SENT);
        chatMsgList.add(chatMsg2);
        ChatMsg chatMsg3 = new ChatMsg("This is Tom. Nice talking to you. ", ChatMsg.TYPE_RECEIVED);
        chatMsgList.add(chatMsg3);
    }



}
