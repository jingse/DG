package com.example.guihuan.chatwifitest.chat;



public class ChatMsg {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private String content;
    private int type;

    public Boolean getGroupMsg() {
        return isGroupMsg;
    }

    public void setGroupMsg(Boolean groupMsg) {
        isGroupMsg = groupMsg;
    }

    private Boolean isGroupMsg;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    private String friendName;
    private int friendImageId;



    public ChatMsg(String content, int type, String friendName, int friendImageId, Boolean isGroupMsg) {
        this.content = content;
        this.type = type;
        this.friendName = friendName;
        this.friendImageId = friendImageId;
        this.isGroupMsg = isGroupMsg;
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
