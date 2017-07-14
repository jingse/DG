package com.example.guihuan.chatwifitest.messages;



public class Msg {

    private String name;
    private int imageId;
    private String latestMsg;
    private String latestMsgTime;

    private Boolean isClicked;  // 判断消息是否被点击,据此判断是否小红点
    private int notReadCount;   // 未读消息数量



    public Msg(String name, int imageId, String latestMsg, String latestMsgTime, Boolean isClicked, int notReadCount) {
        this.name = name;
        this.imageId = imageId;
        this.latestMsg = latestMsg;
        this.latestMsgTime = latestMsgTime;
        this.isClicked = isClicked;
        this.notReadCount = notReadCount;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public String getLatestMsgTime() {
        return latestMsgTime;
    }

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public void setLatestMsgTime(String latestMsgTime) {
        this.latestMsgTime = latestMsgTime;
    }

    public int getNotReadCount() {
        return notReadCount;
    }

    public void setNotReadCount(int notReadCount) {
        this.notReadCount = notReadCount;
    }

}
