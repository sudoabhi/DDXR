package com.example.androdev.ddxr.SearchFragments;

import com.google.firebase.firestore.auth.User;

/**
 * Created by Asus on 3/5/2019.
 */

public class SearchPeopleObject {
    String ImageUrl;
    String UserName;
    String UserSkill;
    String UserCollege;
    String UserUID;

    SearchPeopleObject(String ImageUrl,String UserName,String UserSkill,String UserCollege,String UserUID){

        this.ImageUrl=ImageUrl;
        this.UserName=UserName;
        this.UserSkill= UserSkill;
        this.UserCollege=UserCollege;
        this.UserUID=UserUID;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserSkill() {
        return UserSkill;
    }

    public void setUserSkill(String userSkill) {
        UserSkill = userSkill;
    }

    public String getUserCollege() {
        return UserCollege;
    }

    public void setUserCollege(String userCollege) {
        UserCollege = userCollege;
    }
}
