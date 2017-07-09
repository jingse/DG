package com.example.guihuan.chatwifitest.contacts;

public class Contact {

    private String name;
    private int imageId;

    private String state;


    public Contact(String name, int imageId, String state) {
        this.name = name;
        this.imageId = imageId;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}