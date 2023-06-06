package com.example.myapplication.POJO;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsModel {
    String artistName;
    String artistImageUrl;
    String artistPopularity;
    String artistFollowers;
    String artistSpotify;
    String artistId;


    public ArtistDetailsModel(String artistName, String artistImageUrl, String artistPopularity,
                              String artistFollowers, String artistSpotify, String artistId) {
        this.artistName = artistName;
        this.artistImageUrl = artistImageUrl;
        this.artistPopularity = artistPopularity;
        this.artistFollowers = artistFollowers;
        this.artistSpotify = artistSpotify;
        this.artistId = artistId;

    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public String getArtistPopularity() {
        return artistPopularity;
    }

    public String getArtistFollowers() {
        return artistFollowers;
    }

    public String getArtistSpotify() {
        return artistSpotify;
    }

    public String getArtistId() {
        return artistId;
    }

}
