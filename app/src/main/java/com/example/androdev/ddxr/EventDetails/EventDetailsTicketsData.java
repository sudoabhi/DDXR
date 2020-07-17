package com.example.androdev.ddxr.EventDetails;

/**
 * Created by Asus on 8/27/2019.
 */

public class EventDetailsTicketsData {

    public String TicketName;
    public String TicketCategory;
    public String TicketPrice;
    public String TicketDescription;
    public String TeamMembers;
    public String MinTeamMembers;
    public String TEAM_CHECK;
    public String PRICE_CHECK;
    public String TicketKey;

    public EventDetailsTicketsData(String TicketName, String TicketCategory, String TicketDescription, String TicketPrice,
                                   String MinTeamMembers, String TeamMembers, String TEAM_CHECK, String PRICE_CHECK,
                                   String TicketKey){

        this.TicketName=TicketName;
        this.TicketCategory=TicketCategory;
        this.TicketDescription=TicketDescription;
        this.TicketPrice=TicketPrice;
        this.PRICE_CHECK=PRICE_CHECK;
        this.MinTeamMembers=MinTeamMembers;
        this.TeamMembers=TeamMembers;
        this.TEAM_CHECK=TEAM_CHECK;
        this.TicketKey=TicketKey;

    }

    public String getTicketKey() {
        return TicketKey;
    }

    public void setTicketKey(String ticketKey) {
        TicketKey = ticketKey;
    }

    public String getTeamMembers() {
        return TeamMembers;
    }

    public void setTeamMembers(String teamMembers) {
        TeamMembers = teamMembers;
    }

    public String getTEAM_CHECK() {
        return TEAM_CHECK;
    }

    public void setTEAM_CHECK(String TEAM_CHECK) {
        this.TEAM_CHECK = TEAM_CHECK;
    }

    public String getPRICE_CHECK() {
        return PRICE_CHECK;
    }

    public void setPRICE_CHECK(String PRICE_CHECK) {
        this.PRICE_CHECK = PRICE_CHECK;
    }

    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String ticketName) {
        TicketName = ticketName;
    }

    public String getTicketCategory() {
        return TicketCategory;
    }

    public void setTicketCategory(String ticketCategory) {
        TicketCategory = ticketCategory;
    }

    public String getTicketPrice() {
        return TicketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        TicketPrice = ticketPrice;
    }

    public String getTicketDescription() {
        return TicketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        TicketDescription = ticketDescription;
    }

    public String getMinTeamMembers() {
        return MinTeamMembers;
    }

    public void setMinTeamMembers(String MinteamMembers) {
        MinTeamMembers = MinteamMembers;
    }
}
