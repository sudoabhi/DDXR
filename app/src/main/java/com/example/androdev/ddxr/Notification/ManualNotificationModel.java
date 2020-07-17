package com.example.androdev.ddxr.Notification;

/**
 * Created by Asus on 1/31/2020.
 */

public class ManualNotificationModel {

    String NotificationText;
    String Date;
    String Time;
    String NotificationTitle;
    String TicketName;
    String EventName;

    public ManualNotificationModel(String NotificationText, String Date, String Time,String NotificationTitle,String TicketName,String EventName){

        this.NotificationText=NotificationText;
        this.NotificationTitle=NotificationTitle;
        this.Date=Date;
        this.EventName=EventName;

        this.Time=Time;
        this.TicketName=TicketName;


    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String ticketName) {
        TicketName = ticketName;
    }

    public String getNotificationTitle() {
        return NotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        NotificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return NotificationText;
    }

    public void setNotificationText(String notificationText) {
        NotificationText = notificationText;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
