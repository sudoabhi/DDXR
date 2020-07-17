package com.example.androdev.ddxr.EventDetails;

public class EventDetailsPostersData {


    private String event_pic;
    private String event_pic_key;


    public EventDetailsPostersData(String event_pic, String event_pic_key){


        this.event_pic=event_pic;
        this.event_pic_key=event_pic_key;
    }



    public String getposter_pic() {
        return event_pic;
    }

    public void setposter_pic(String event_pic) {
        this.event_pic = event_pic;
    }

    public String getposter_pic_key() {
        return event_pic_key;
    }

    public void setposter_pic_key(String event_pic_key) {
        this.event_pic_key = event_pic_key;
    }

}
