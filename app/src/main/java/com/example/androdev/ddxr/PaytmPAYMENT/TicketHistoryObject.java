package com.example.androdev.ddxr.PaytmPAYMENT;

import java.util.Map;

/**
 * Created by Asus on 10/28/2019.
 */

public class TicketHistoryObject {


    String UserName;
    String UserClubLocation;
    String ProfileImage;
    String Activity;
    String EventKey;
    String TicketKey;
    String EntryKey;
    String TeamName;

    public TicketHistoryObject(String UserName,String UserClubLocation,String ProfileImage,String EventKey,String TicketKey,String EntryKey ,String TeamName){
        this.UserName= UserName;
        this.UserClubLocation=UserClubLocation;
        this.ProfileImage=ProfileImage;
        this.Activity=Activity;
        this.TeamName=TeamName;
        this.EventKey=EventKey;
        this.TicketKey=TicketKey;
        this.EntryKey= EntryKey;
    }



    public String getEntryKey() {
        return EntryKey;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public void setEntryKey(String entryKey) {
        EntryKey = entryKey;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getTicketKey() {
        return TicketKey;
    }

    public void setTicketKey(String ticketKey) {
        TicketKey = ticketKey;
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


