package com.example.androdev.ddxr.UserFragments;

import com.google.firebase.firestore.auth.User;

/**
 * Created by Asus on 7/21/2019.
 */

public class ParticipantsObject {

    String UserName;
    String UserClubLocation;

    String ProfileImage;
    String Activity;
    String UserUID;
    String Category;

    public ParticipantsObject(String UserName,String UserClubLocation,String ProfileImage,String Activity,String UserUID,String Category){
        this.UserName= UserName;
        this.UserClubLocation=UserClubLocation;
        this.ProfileImage=ProfileImage;
        this.Activity=Activity;
        this.UserUID=UserUID;
        this.Category=Category;

    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
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
