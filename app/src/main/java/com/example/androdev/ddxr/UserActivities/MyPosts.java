package com.example.androdev.ddxr.UserActivities;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Asus on 2/12/2019.
 */

public class MyPosts {

    String ClubName;
     String ClubLocation;
    String  ClubProfileImage;
    String EventBannner;
    String update_date;
    String EventName;
    String Event_Dates;
    String EventDescription;
    String Club_Image_Url;
    String Event_Banner;
    String UpdatedDay;
    String UploadedBy;
    String like;
    String Key;
    String bookmark;
    String EventTypeX;
    String ClubDomain;
    String EventLocation;
    String EventType;
    String Interested;
    long InterestedCount;
    ArrayList<String> InterestedPhoto;
    String PriceSegment;
    int likeCount;
    int bookmarkCount;


   public MyPosts(){}  //For Firebase

   // public MyPosts(String eventDescription,String event_Dates,String eventName){

   public MyPosts(String ClubName,String ClubLocation,String eventDescription,String event_Dates,String eventName,
                  String club_image_url,String event_banner,String UpdatedDay,String UploadedBy,String like,String Key,
                  String bookmark,String EventTypeX,String ClubDomain,String EventLocation,String EventType,
                  String Interested,long InterestedCount,ArrayList<String> InterestedPhoto,String PriceSegment,
                  int likeCount,int bookmarkCount){
        this.ClubName=ClubName;
        this.EventTypeX=EventTypeX;
        this.ClubDomain=ClubDomain;
        this.PriceSegment=PriceSegment;
        this.InterestedPhoto=InterestedPhoto;
        this.EventLocation=EventLocation;
        this.ClubLocation=ClubLocation;
        this.bookmark=bookmark;
        this.EventType=EventType;
        this.InterestedCount=InterestedCount;
      // this.update_date=update_date;
       Event_Dates=event_Dates;
       this.Key=Key;
       EventName=eventName;
       EventDescription=eventDescription;
       Club_Image_Url=club_image_url;
       Event_Banner=event_banner;
       this.UpdatedDay=UpdatedDay;
       this.UploadedBy=UploadedBy;
       this.like=like;
       this.Interested=Interested;
       this.likeCount= likeCount;
       this.bookmarkCount= bookmarkCount;



    }

    public String getPriceSegment() {
        return PriceSegment;
    }

    public void setPriceSegment(String priceSegment) {
        PriceSegment = priceSegment;
    }

    public long getInterestedCount() {
        return InterestedCount;
    }

    public ArrayList<String> getInterestedPhoto() {
        return InterestedPhoto;
    }

    public void setInterestedPhoto(ArrayList<String> interestedPhoto) {
        InterestedPhoto = interestedPhoto;
    }

    public void setInterestedCount(long interestedCount) {
        InterestedCount = interestedCount;
    }

    public String getBookmark() {
        return bookmark;
    }

    public String getEventTypeX() {
        return EventTypeX;
    }

    public String getInterested() {
        return Interested;
    }

    public void setInterested(String interested) {
        Interested = interested;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public void setEventTypeX(String eventTypeX) {
        EventTypeX = eventTypeX;
    }

    public String getClubDomain() {
        return ClubDomain;
    }

    public void setClubDomain(String clubDomain) {
        ClubDomain = clubDomain;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }

    public String getUpdatedDay() {
        return UpdatedDay;
    }

    public void setUpdatedDay(String updatedDay) {
        UpdatedDay = updatedDay;
    }

    public String getEvent_Banner() {
        return Event_Banner;
    }

    public void setEvent_Banner(String event_Banner) {
        Event_Banner = event_Banner;
    }

    public String getClub_Image_Url() {
        return Club_Image_Url;
    }

    public void setClub_Image_Url(String club_Image_Url) {
        Club_Image_Url = club_Image_Url;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getClubLocation() {
        return ClubLocation;
    }

    public void setClubLocation(String clubLocation) {
        ClubLocation = clubLocation;
    }

    public String getClubProfileImage() {
        return ClubProfileImage;
    }

    public void setClubProfileImage(String clubProfileImage) {
        ClubProfileImage = clubProfileImage;
    }

    public String getEventBannner() {
        return EventBannner;
    }

    public void setEventBannner(String eventBannner) {
        EventBannner = eventBannner;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEvent_Dates() {
        return Event_Dates;
    }

    public void setEvent_Dates(String event_Dates) {
        Event_Dates = event_Dates;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public int getLikeCount(){
       return likeCount;
    }
    public void setLikeCount(int likecount){
       likeCount=likecount;
    }
    public int getBookmarkCount(){
        return bookmarkCount;
    }
    public void setBookmarkCount(int count){
        bookmarkCount=count;
    }


}
