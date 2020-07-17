package com.example.androdev.ddxr.ChatBox;

/**
 * Created by Asus on 6/4/2019.
 */

public class HomeMessageObject {

    String sender_name;
    String recent_text;
    String time;
    String image_url;
    String other_party;
    String sender_uid;
    String receiver_name;
    String sender_image_url;




    HomeMessageObject(String sender_name,String recent_text, String time,String image_url,String other_party,String sender_uid,String receiver_name,String sender_image_url){

        this.sender_name=sender_name;
        this.other_party=other_party;
        this.recent_text=recent_text;
        this.time=time;
        this.image_url=image_url;
        this.sender_uid=sender_uid;
        this.receiver_name=receiver_name;
        this.sender_image_url=sender_image_url;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSender_image_url() {
        return sender_image_url;
    }

    public void setSender_image_url(String sender_image_url) {
        this.sender_image_url = sender_image_url;
    }

    public String getOther_party() {
        return other_party;
    }

    public void setOther_party(String other_party) {
        this.other_party = other_party;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecent_text() {
        return recent_text;
    }

    public void setRecent_text(String recent_text) {
        this.recent_text = recent_text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
