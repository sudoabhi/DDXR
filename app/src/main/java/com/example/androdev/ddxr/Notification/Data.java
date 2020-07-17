package com.example.androdev.ddxr.Notification;

/**
 * Created by Asus on 10/15/2019.
 */

public class Data {

    private String user;
    private String body;
    private String title;
    private int icon;
    private String sented;

    public Data() {
    }

    public Data(String user, String body, String title, int icon, String sented){
        this.user=user;
        this.body=body;
        this.title=title;
        this.icon=icon;
        this.sented=sented;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
