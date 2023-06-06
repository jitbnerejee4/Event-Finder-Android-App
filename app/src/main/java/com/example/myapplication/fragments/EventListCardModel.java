package com.example.myapplication.fragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventListCardModel implements Parcelable {
    String eventName;
    String eventDate;
    String eventStaduim;
    String eventTime;
    String eventGenre;
    String eventImageUrl;
    boolean isFavorite;
    String eventId;


    public EventListCardModel(String eventName, String eventDate, String eventStaduim, String eventTime, String eventGenre, String eventImageUrl, String eventId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStaduim = eventStaduim;
        this.eventTime = eventTime;
        this.eventGenre = eventGenre;
        this.eventImageUrl = eventImageUrl;
        this.eventId = eventId;
    }

    protected EventListCardModel(Parcel in) {
        eventName = in.readString();
        eventDate = in.readString();
        eventStaduim = in.readString();
        eventTime = in.readString();
        eventGenre = in.readString();
        eventImageUrl = in.readString();
        isFavorite = in.readByte() != 0;
        eventId = in.readString();
    }

    public static final Creator<EventListCardModel> CREATOR = new Creator<EventListCardModel>() {
        @Override
        public EventListCardModel createFromParcel(Parcel in) {
            return new EventListCardModel(in);
        }

        @Override
        public EventListCardModel[] newArray(int size) {
            return new EventListCardModel[size];
        }
    };

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventStaduim() {
        return eventStaduim;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventGenre() {
        return eventGenre;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventDate);
        dest.writeString(eventStaduim);
        dest.writeString(eventTime);
        dest.writeString(eventGenre);
        dest.writeString(eventImageUrl);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(eventId);
    }
}
