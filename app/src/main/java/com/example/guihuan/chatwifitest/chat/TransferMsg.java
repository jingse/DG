package com.example.guihuan.chatwifitest.chat;


public class TransferMsg {
    private String to;
    private String from;
    private String content;

    public TransferMsg(String to, String from, String content) {
        this.to = to;
        this.from = from;
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
