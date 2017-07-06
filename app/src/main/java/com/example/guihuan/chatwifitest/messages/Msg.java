package com.example.guihuan.chatwifitest.messages;



public class Msg {

    private String name;
    private int imageId;

    private String latestMsg;
    private String latestMsgTime;


    public Msg(String name, int imageId, String latestMsg, String latestMsgTime) {
        this.name = name;
        this.imageId = imageId;
        this.latestMsg = latestMsg;
        this.latestMsgTime = latestMsgTime;
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


}
