package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.POJO.ArtistDetailsModel;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    String name;
    String imageUrl;
    String popularity;
    String totalFollowers;
    String spotifyUrl;
    String artistId;
    List<String> artistArray;
    ArrayList<ArtistDetailsModel> artistDetailsModels = new ArrayList<>();
    public List<String> artistAlbumUrls = new ArrayList<>();
    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_artist, container, false);

        Handler handler = new Handler();
        recyclerView = view.findViewById(R.id.artist_recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.artist_progress_bar);

        if(SharedDetails.getInstance().isMusic() == false){
            CardView cardView = view.findViewById(R.id.no_music_card);
            cardView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            System.out.println("NOT A MUSIC EVENT");
        }else {

            artistArray = SharedDetails.getInstance().getArtistArray();



            RequestQueue queue = Volley.newRequestQueue(getContext());
            for (int i = 0; i < artistArray.size(); i++) {
                artistAlbumUrls.clear();
                String url = "https://homework8-381519.wl.r.appspot.com/artist/" + artistArray.get(i);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String jsonResponse) {
                                Log.d("API Response", jsonResponse);
                                try {
                                    progressBar.setVisibility(View.VISIBLE);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Hide the text after 3 seconds
                                            progressBar.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);

                                        }
                                    }, 3000);
                                    JSONObject rootResponse = new JSONObject(jsonResponse);
                                    JSONObject response = rootResponse.getJSONObject("response");
                                    JSONObject body = response.getJSONObject("body");
                                    JSONObject artists = body.getJSONObject("artists");
                                    JSONArray items = artists.getJSONArray("items");
                                    JSONObject item = items.getJSONObject(0);
                                    name = item.getString("name");
                                    popularity = item.getString("popularity");
                                    JSONArray images = item.getJSONArray("images");
                                    JSONObject image = images.getJSONObject(0);
                                    imageUrl = image.getString("url");
                                    JSONObject followers = item.getJSONObject("followers");
                                    totalFollowers = followers.getString("total");
                                    JSONObject external_urls = item.getJSONObject("external_urls");
                                    spotifyUrl = external_urls.getString("spotify");
                                    artistId = item.getString("id");

                                    artistDetailsModels.add(new ArtistDetailsModel(name, imageUrl, popularity, totalFollowers, spotifyUrl, artistId));
                                    ArtistRecyclerViewAdapter artistRecyclerViewAdapter = new ArtistRecyclerViewAdapter(getContext(), artistDetailsModels);
                                    recyclerView.setAdapter(artistRecyclerViewAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", error.toString());
                    }
                });
                queue.add(stringRequest);
            }
        }

        return view;
    }

}