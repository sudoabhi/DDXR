package com.example.androdev.ddxr.SearchFragments;

/**
 * Created by Asus on 3/17/2019.
 */

public class SearchEventsObject {

    String EventKey;
    String EventImageUrl;
    String EventName;
    String CollegeName;
    String ClubFollowers;

    SearchEventsObject(String EventKey, String EventImageUrl, String EventName, String CollegeName, String ClubFollowers){
        this.EventKey=EventKey;
        this.ClubFollowers=ClubFollowers;
        this.EventImageUrl=EventImageUrl;
        this.EventName=EventName;
        this.CollegeName=CollegeName;

    }


    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getEventImageUrl() {
        return EventImageUrl;
    }

    public void setEventImageUrl(String clubImageUrl) {
        EventImageUrl = clubImageUrl;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String clubName) {
        EventName = clubName;
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public void setCollegeName(String collegeName) {
        CollegeName = collegeName;
    }

    public String getClubFollowers() {
        return ClubFollowers;
    }

    public void setClubFollowers(String clubFollowers) {
        ClubFollowers = clubFollowers;
    }
}
