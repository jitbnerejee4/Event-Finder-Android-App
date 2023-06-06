package com.example.myapplication.POJO;

public class EventModel {
    private String imageUrl;
    private String eventName;
    private String eventDate;
    private String eventVenue;
    private String eventTime;
    private String eventGenre;
    private String id;

    public EventModel(String id, String imageUrl, String eventName, String eventDate, String eventVenue, String eventTime, String eventGenre){
        this.id = id;
        this.imageUrl = imageUrl;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
        this.eventGenre = eventGenre;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }
    public String getEventDate(){
        return eventDate;
    }

    public void setEventDate(String eventDate){
        this.eventDate = eventDate;
    }
    public String getEventVenue(){
        return eventVenue;
    }

    public void setEventVenue(String eventVenue){
        this.eventVenue = eventVenue;
    }
    public String getEventTime(){
        return eventTime;
    }

    public void setEventTime(String eventTime){
        this.eventTime = eventTime;
    }
    public String getEventGenre(){
        return eventGenre;
    }

    public void setEventGenre(String eventGenre){
        this.eventGenre = eventGenre;
    }


}
