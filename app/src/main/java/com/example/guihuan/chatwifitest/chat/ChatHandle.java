package com.example.guihuan.chatwifitest.chat;



public class ChatHandle {

    private String chatting_friend_name;  //正在聊天的人的名字
    private String my_name;               //我的名字
    private String received_msg;          //接受的消息
    private String sent_msg;              //发送的消息
    private int chat_type;                //1是私聊，2是群聊


    //处理接收到的消息
    public void handleReceivedMsg(String from, String received_msg, int chat_type) {


    }


    //发送消息
    public void sendMsg(String chatting_friend_name, String sent_msg, String my_name, int chat_type) {

    }

}
