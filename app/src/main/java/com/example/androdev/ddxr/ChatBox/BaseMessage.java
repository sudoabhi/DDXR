package com.example.androdev.ddxr.ChatBox;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by Asus on 6/3/2019.
 */

public  class BaseMessage {

    String message;
    String date;
    String time;
    String user;
    Boolean date_change;
   // String otherparty;





    BaseMessage(String message, String date, String time,String user,Boolean date_change){


        this.message=message;
        this.date=date;
        this.time=time;
        this.user=user;
        this.date_change=date_change;


    }



    public Boolean getDate_change() {
        return date_change;
    }

    public void setDate_change(Boolean date_change) {
        this.date_change = date_change;
    }

    /* public String getOtherparty() {
        return otherparty;
    }

    public void setOtherparty(String otherparty) {
        this.otherparty = otherparty;
    }*/

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
