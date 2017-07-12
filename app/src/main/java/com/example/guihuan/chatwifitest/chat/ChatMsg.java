package com.example.guihuan.chatwifitest.chat;



public class ChatMsg {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private String content;
    private int type;

    private int friendImageId;



    public ChatMsg(String content, int type, int friendImageId) {
        this.content = content;
        this.type = type;
        this.friendImageId = friendImageId;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public int getFriendImageId() {
        return friendImageId;
    }

    public void setFriendImageId(int friendImageId) {
        this.friendImageId = friendImageId;
    }


}
