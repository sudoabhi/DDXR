package com.example.androdev.ddxr.EventDetails;

/**
 * Created by Asus on 8/28/2019.
 */

public class EventDetailsOrganisersData {

    String OrganiserName;
    String OrganiserRole;
    String OrganiserPhone;
    String OrganiserEmail;
    String OrganiserPic;

    public EventDetailsOrganisersData(String OrganiserName, String OrganiserRole, String OrganiserPhone,
                                      String OrganiserEmail, String OrganiserPic){

        this.OrganiserEmail=OrganiserEmail;
        this.OrganiserPhone=OrganiserPhone;
        this.OrganiserName=OrganiserName;
        this.OrganiserRole=OrganiserRole;
        this.OrganiserPic=OrganiserPic;

    }

    public String getOrganiserName() {
        return OrganiserName;
    }

    public void setOrganiserName(String organiserName) {
        OrganiserName = organiserName;
    }

    public String getOrganiserRole() {
        return OrganiserRole;
    }

    public void setOrganiserRole(String organiserRole) {
        OrganiserRole = organiserRole;
    }

    public String getOrganiserPhone() {
        return OrganiserPhone;
    }

    public void setOrganiserPhone(String organiserPhone) {
        OrganiserPhone = organiserPhone;
    }

    public String getOrganiserEmail() {
        return OrganiserEmail;
    }

    public void setOrganiserEmail(String organiserEmail) {
        OrganiserEmail = organiserEmail;
    }

    public String getOrganiserPic() {
        return OrganiserPic;
    }

    public void setOrganiserPic(String organiserPic) {
        OrganiserPic = organiserPic;
    }



}
