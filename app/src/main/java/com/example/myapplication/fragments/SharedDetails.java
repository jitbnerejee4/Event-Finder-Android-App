package com.example.myapplication.fragments;

import java.util.ArrayList;
import java.util.List;

public class SharedDetails {
    private static SharedDetails sharedDetails;
    String id;
    String name;
    String ticketUrl;
    List<String> artistArray = new ArrayList<>();
    String venueName;

    boolean isMusic;
    public SharedDetails(){

    }
    public static SharedDetails getInstance() {
        if (sharedDetails == null) {
            sharedDetails = new SharedDetails();
        }
        return sharedDetails;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTicketUrl(){
        return ticketUrl;
    }
    public void setTicketUrl(String ticketUrl){
        this.ticketUrl = ticketUrl;
    }
    public void setArtistArray(List<String> artistArray){
        this.artistArray = artistArray;
    }

    public List<String> getArtistArray() {
        return artistArray;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public void setMusic(boolean music) {
        isMusic = music;
    }
}
