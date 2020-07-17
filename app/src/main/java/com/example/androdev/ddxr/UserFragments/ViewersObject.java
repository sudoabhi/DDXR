package com.example.androdev.ddxr.UserFragments;

/**
 * Created by Asus on 12/27/2019.
 */

public class ViewersObject {

    String UserName;
    String UserClubLocation;
    String ProfileImage;
    String Activity;
    String Time;
    String Date;
    String UserUID;

    public ViewersObject(String UserName,String UserClubLocation,String ProfileImage,String Activity,String Time,String Date,String UserUID){
        this.UserName= UserName;
        this.UserClubLocation=UserClubLocation;
        this.ProfileImage=ProfileImage;
        this.Activity=Activity;
        this.Date=Date;
        this.Time=Time;
        this.UserUID=UserUID;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserClubLocation() {
        return UserClubLocation;
    }

    public void setUserClubLocation(String userClubLocation) {
        UserClubLocation = userClubLocation;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }
}


