package com.example.myapplication.POJO;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.fragments.EventListCardModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavouritesModel {
    private static FavouritesModel instance;
    SharedPreferences sharedPreferences;
    Gson gson;
    private List<EventListCardModel> favoritesList;

    private FavouritesModel(Context context) {

        favoritesList = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    public static FavouritesModel getInstance(Context context) {
        if (instance == null) {
            instance = new FavouritesModel(context );
        }
        return instance;
    }

    public void addItem(EventListCardModel item) {
        favoritesList.add(item);

        gson = new Gson();
        String json = gson.toJson(favoritesList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key", json);
        editor.apply();
        System.out.println(favoritesList);
    }

    public void removeItem(EventListCardModel item) {
        String eventId = item.getEventId();
        int idx = 0;
        for(EventListCardModel favourite: favoritesList){
            if(favourite.getEventId().equals(eventId)){
                break;
            }
            idx++;
        }
        favoritesList.remove(idx);
        gson = new Gson();
        String json = gson.toJson(favoritesList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key", json);
        editor.apply();
        System.out.println(favoritesList);
    }

    public boolean isFavorite(EventListCardModel item) {
        return favoritesList.contains(item);
    }
    public List<EventListCardModel> getFavoritesList(){
        String json = sharedPreferences.getString("key", null);
        gson = new Gson();
        if (json != null) {
            Type type = new TypeToken<ArrayList<EventListCardModel>>(){}.getType();
            ArrayList<EventListCardModel> array = gson.fromJson(json, type);
            this.favoritesList = array;
        }

        return favoritesList;
    }
}
