package com.halo.loginui2;


import java.io.Serializable;

public class NearbyItem implements Serializable {
    public String eventTitle;
    public String eventImage;
    public String eventHQImage;
    public String eventDescription;


    public String eventStartTime;
    public String eventEndTime;
    public String eventURL;


    public NearbyItem() {
    }




    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventHQImage() {
        return eventHQImage;
    }

    public void setEventHQImage(String eventHQImage) {
        this.eventHQImage = eventHQImage;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventURL() {
        return eventURL;
    }

    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }

}
