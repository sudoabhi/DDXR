package com.example.androdev.ddxr.SearchFragments;

/**
 * Created by Asus on 3/17/2019.
 */

public class SearchOrganizersObject {

    String ClubUID;
    String ClubImageUrl;
    String ClubName;
    String CollegeName;
    String ClubFollowers;

    SearchOrganizersObject(String ClubUID,String ClubImageUrl, String ClubName, String CollegeName, String ClubFollowers){
        this.ClubUID=ClubUID;
        this.ClubFollowers=ClubFollowers;
        this.ClubImageUrl=ClubImageUrl;
        this.ClubName=ClubName;
        this.CollegeName=CollegeName;

    }


    public String getClubUID() {
        return ClubUID;
    }

    public void setClubUID(String clubUID) {
        ClubUID = clubUID;
    }

    public String getClubImageUrl() {
        return ClubImageUrl;
    }

    public void setClubImageUrl(String clubImageUrl) {
        ClubImageUrl = clubImageUrl;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
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
