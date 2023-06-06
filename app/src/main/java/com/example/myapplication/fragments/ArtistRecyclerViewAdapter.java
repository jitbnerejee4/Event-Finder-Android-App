package com.example.myapplication.fragments;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.POJO.ArtistDetailsModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistRecyclerViewAdapter  extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<ArtistDetailsModel> artistDetailsModels;

    public ArtistRecyclerViewAdapter(Context context, ArrayList<ArtistDetailsModel> artistDetailsModels) {
        this.context = context;
        this.artistDetailsModels = artistDetailsModels;
    }

//    public void artistDetailsSet(ArrayList<ArtistDetailsModel> artistDetailsModels){
//        this.artistDetailsModels = artistDetailsModels;
//        System.out.println(artistDetailsModels);
//    }

//    public void artistAlbumsSet(ArrayList<ArtistAlbumsModel> artistAlbumsModels){
//        this.artistAlbumsModels = artistAlbumsModels;
//        System.out.println("INSIDE ADAPTER");
//        System.out.println(artistAlbumsModels);
//
//    }

    @NonNull
    @Override
    public ArtistRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.artist_card_layout, parent, false);
        return new ArtistRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRecyclerViewAdapter.MyViewHolder holder, int position) {
        //SETTING IMAGE
        String url = artistDetailsModels.get(position).getArtistImageUrl();
        Picasso.get().load(url).resize(350, 350).centerCrop().into(holder.artistImage);

        //SETTING ARTIST NAME
        holder.artistName.setText(artistDetailsModels.get(position).getArtistName());

        //SETTING ARTIST FOLLOWERS
        String followers = "";
        int followerNumber = Integer.parseInt(artistDetailsModels.get(position).getArtistFollowers());
        if(followerNumber >= 1000000){
            double millions = followerNumber / 1000000.0;
            followers = String.format("%.1fM followers", millions);
        } else if (followerNumber >= 1000) {
            double thousands = followerNumber / 1000.0;
            followers = String.format("%.0fK followers", thousands);
        }else{
            followers = Integer.toString(followerNumber);
        }
        holder.artistFollowers.setText(followers);

        //SETTING ARTIST SPOTIFY
        String spotifyUrl = artistDetailsModels.get(position).getArtistSpotify();
        holder.artistSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(spotifyUrl));
                context.startActivity(intent);
            }
        });

        holder.artistPopularity.setProgress(Integer.parseInt(artistDetailsModels.get(position).getArtistPopularity()));
        holder.artistPopularityText.setText(artistDetailsModels.get(position).getArtistPopularity());

        String artistId = artistDetailsModels.get(position).getArtistId();
        RequestQueue queue = Volley.newRequestQueue(context);
        String albumUrl = "https://homework8-381519.wl.r.appspot.com/artist/album/"+artistId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, albumUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
//                        System.out.println("BEFORE CALL");
//                        System.out.println(artistAlbumUrls);
                        Log.d("API Response", jsonResponse);
                        try {
                            JSONObject rootResponse = new JSONObject(jsonResponse);
                            JSONObject response = rootResponse.getJSONObject("response");
                            JSONObject body = response.getJSONObject("body");
                            JSONArray items = body.getJSONArray("items");
                            for (int i = 0; i < 3; i++){
                                JSONObject item = items.getJSONObject(i);
                                JSONArray images = item.getJSONArray("images");
                                JSONObject image = images.getJSONObject(i);
                                String imageURL = image.getString("url");
                                if(i == 0){
                                    Picasso.get().load(imageURL).resize(350, 350).centerCrop().into(holder.albumImage1);
                                } else if (i == 1) {
                                    Picasso.get().load(imageURL).resize(350, 350).centerCrop().into(holder.albumImage2);
                                } else if (i == 2) {
                                    Picasso.get().load(imageURL).resize(350, 350).centerCrop().into(holder.albumImage3);
                                }


                            }

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

    @Override
    public int getItemCount() {
        return artistDetailsModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView artistImage;
        TextView artistName;
        TextView artistFollowers;
        TextView artistSpotify;
        ProgressBar artistPopularity;
        TextView artistPopularityText;
        ImageView albumImage1;
        ImageView albumImage2;
        ImageView albumImage3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.textView2);
            artistFollowers = itemView.findViewById(R.id.textView24);
            artistSpotify = itemView.findViewById(R.id.textView25);
            artistImage = itemView.findViewById(R.id.imageView5);
            artistPopularity = itemView.findViewById(R.id.progressBar3);
            artistPopularityText = itemView.findViewById(R.id.textView28);
            albumImage1 = itemView.findViewById(R.id.imageView9);
            albumImage2 = itemView.findViewById(R.id.imageView10);
            albumImage3 = itemView.findViewById(R.id.imageView11);
        }
    }
}
