package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailsFragment extends Fragment {
    View view;
    String eventId;
    String artists="";
    String genres = "";
    String price = "";
    String ticketStatus = "";
    String formattedDate = "";
    String localTime = "";
    String eventURL = "";
    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_details, container, false);
         System.out.println(SharedDetails.getInstance().getId());
         System.out.println(SharedDetails.getInstance().getName());
         eventId = SharedDetails.getInstance().getId();
        Handler handler = new Handler();
        ProgressBar progressBar = view.findViewById(R.id.details_progress_bar);
        CardView cardView = view.findViewById(R.id.details_card);

        String url = "https://homework8-381519.wl.r.appspot.com/"+eventId;
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                    cardView.setVisibility(View.VISIBLE);

                                }
                            }, 3000);
                            JSONObject rootResponse = new JSONObject(jsonResponse);
                            JSONObject response = rootResponse.getJSONObject("response");
                            if(response.has("_embedded")){
                                JSONObject embedded = response.getJSONObject("_embedded");

                                //GETTING ARTIST NAMES
                                TextView artistHeader;
                                TextView artistName;
                                artistName = view.findViewById(R.id.textView4);
                                artistHeader = view.findViewById(R.id.textView3);
                                HorizontalScroll horizontalScroll = new HorizontalScroll(artistName);
                                if(embedded.has("attractions")){
                                    JSONArray attractions = embedded.getJSONArray("attractions");
                                    List<String> artistArray = new ArrayList<>();
                                    for(int i = 0; i < attractions.length(); i++){
                                        JSONObject attraction = attractions.getJSONObject(i);
                                        String name = attraction.getString("name");
                                        artistArray.add(name);
                                        artists+=name;
                                        artists+="|";
                                    }
                                    SharedDetails.getInstance().setArtistArray(artistArray);
                                }
                                if(artists.length() > 0){
                                    artists = artists.substring(0, artists.length() - 1);
                                    artistName.setText(artists);
                                }else{
                                    artistHeader.setVisibility(View.GONE);
                                    artistName.setVisibility(View.GONE);
                                }

                                //GETTING VENUE
                                TextView venueHeader;
                                TextView venueBox;
                                venueHeader = view.findViewById(R.id.textView10);
                                venueBox = view.findViewById(R.id.textView11);
                                HorizontalScroll horizontalScroll1 = new HorizontalScroll(venueBox);
                                if(embedded.has("venues")){
//                                    getting venue names
                                    JSONArray venues = embedded.getJSONArray("venues");
                                    JSONObject venue = venues.getJSONObject(0);
                                    String venueName = venue.getString("name");
                                    SharedDetails.getInstance().setVenueName(venueName);
                                    venueBox.setText(venueName);
                                }else{
                                    venueHeader.setVisibility(View.GONE);
                                    venueBox.setVisibility(View.GONE);
                                }
                            }

                            if(response.has("dates")){
                                JSONObject dates = response.getJSONObject("dates");
                                if(dates.has("start")){
                                    JSONObject start = dates.getJSONObject("start");

                                    //GETTING DATE
                                    TextView dateHeader;
                                    TextView dateBox;
                                    dateHeader = view.findViewById(R.id.textView12);
                                    dateBox = view.findViewById(R.id.textView13);
                                    HorizontalScroll horizontalScroll = new HorizontalScroll(dateBox);
                                    if(start.has("localDate")){
                                        String tempDate = start.getString("localDate");
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            LocalDate date = LocalDate.parse(tempDate);
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                                            formattedDate = date.format(formatter);
                                            dateBox.setText(formattedDate);
                                        }

                                    }else{
                                        dateHeader.setVisibility(View.GONE);
                                        dateBox.setVisibility(View.GONE);
                                    }

                                    //GETTING TIME
                                    TextView timeHeader;
                                    TextView timeBox;
                                    timeHeader = view.findViewById(R.id.textView14);
                                    timeBox = view.findViewById(R.id.textView15);
                                    HorizontalScroll horizontalScroll1 = new HorizontalScroll(timeBox);
                                    if(start.has("localTime")){
                                        String tempTime = start.getString("localTime");
                                        // Define the input time format
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                                        // Define the output time format
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm aa", Locale.US);

                                        Date time = inputFormat.parse(tempTime);
                                        localTime = outputFormat.format(time);
                                        timeBox.setText(localTime);
                                    }else{
                                        timeHeader.setVisibility(View.GONE);
                                        timeBox.setVisibility(View.GONE);
                                    }
                                }
                            }
                                    //GETTING GENRE
                            TextView genreHeader = view.findViewById(R.id.textView16);
                            TextView genreBox = view.findViewById(R.id.textView17);
                            HorizontalScroll horizontalScroll = new HorizontalScroll(genreBox);
                            if(response.has("classifications")){
                                JSONArray classifications = response.getJSONArray("classifications");
                                JSONObject classification = classifications.getJSONObject(0);
                                if(classification.has("segment")){
                                    JSONObject segment = classification.getJSONObject("segment");
                                    if(segment.has("name")){
                                        String segmentName = segment.getString("name");
                                        if(!segmentName.equals("Undefined")){
                                            if(segmentName.equals("Music")){
                                                SharedDetails.getInstance().setMusic(true);
                                            }else{
                                                SharedDetails.getInstance().setMusic(false);
                                            }
                                            genres+=segmentName;
                                            genres+=" | ";
                                        }
                                    }
                                }
                                if(classification.has("genre")){
                                    JSONObject genre = classification.getJSONObject("genre");
                                    if(genre.has("name")){
                                        String genreName = genre.getString("name");
                                        if(!genreName.equals("Undefined")){
                                            genres+=genreName;
                                            genres+=" | ";
                                        }
                                    }
                                }
                                if(classification.has("subGenre")){
                                    JSONObject subGenre = classification.getJSONObject("subGenre");
                                    if(subGenre.has("name")){
                                        String subGenreName = subGenre.getString("name");
                                        if(!subGenreName.equals("Undefined")){
                                            genres+=subGenreName;
                                            genres+=" | ";
                                        }
                                    }
                                }
                                if(classification.has("type")){
                                    JSONObject type = classification.getJSONObject("type");
                                    if(type.has("name")){
                                        String typeName = type.getString("name");
                                        if(!typeName.equals("Undefined")){
                                            genres+=typeName;
                                            genres+=" | ";
                                        }
                                    }
                                }
                                if(classification.has("subType")){
                                    JSONObject subType = classification.getJSONObject("subType");
                                    if(subType.has("name")){
                                        String subTypeName = subType.getString("name");
                                        if(!subTypeName.equals("Undefined")){
                                            genres+=subTypeName;
                                            genres+=" | ";
                                        }
                                    }
                                }
                                genres = genres.substring(0, genres.length()-2) + "";
                                if(genres.length() > 0){
                                    genreBox.setText(genres);
                                }else{
                                    genreHeader.setVisibility(View.GONE);
                                    genreBox.setVisibility(View.GONE);
                                }

                            }
                            //GETTING PRICE RANGES
                            TextView priceHeader = view.findViewById(R.id.textView18);
                            TextView priceBox = view.findViewById(R.id.textView19);
                            HorizontalScroll horizontalScroll1 = new HorizontalScroll(priceBox);
                            if(response.has("priceRanges")){
                                JSONArray priceRanges = response.getJSONArray("priceRanges");
                                JSONObject priceRange = priceRanges.getJSONObject(0);
                                if(priceRange.has("min")){
                                    String tempPrice = priceRange.getString("min");
                                    if(!tempPrice.equals("Undefined")){
                                        price +=priceRange.getString("min");

                                    }
                                }
                                if(priceRange.has("max")){
                                    String tempPrice = priceRange.getString("max");
                                    if(!tempPrice.equals("Undefined")){
                                        price += " - ";
                                        price +=priceRange.getString("max");
                                        price += "";
                                    }
                                    price+=" (USD)";
                                }

                            }
                            if(price.length() > 0){
                                priceBox.setText(price);
                            }else{
                                priceHeader.setVisibility(View.GONE);
                                priceBox.setVisibility(View.GONE);
                            }


                                //GETTING TICKET STATUS
                            TextView ticketStatusHeader = view.findViewById(R.id.textView20);
                            CardView ticketStatusCard = view.findViewById(R.id.cardView3);
                            TextView ticketStatusBox = ticketStatusCard.findViewById(R.id.textView21);
                            if(response.has("dates")){
                                JSONObject dates = response.getJSONObject("dates");
                                if(dates.has("status")){
                                    JSONObject status = dates.getJSONObject("status");
                                    if(status.has("code")){
                                        String statusTicket = status.getString("code");
                                        if(statusTicket.equals("onsale")){
                                            ticketStatus = "On Sale";
                                            ticketStatusBox.setText(ticketStatus);
                                            ticketStatusCard.setCardBackgroundColor(Color.parseColor("#147917"));
                                            System.out.println(ticketStatus);
                                        } else if (statusTicket.equals("offsale")) {
                                            ticketStatus = "Off Sale";
                                            ticketStatusBox.setText(ticketStatus);
                                            ticketStatusCard.setCardBackgroundColor(Color.RED);
                                        } else if (statusTicket.equals("rescheduled")) {
                                            ticketStatus = "Rescheduled";
                                            ticketStatusBox.setText(ticketStatus);
                                            ticketStatusCard.setCardBackgroundColor(Color.parseColor("#FFA500"));
                                        } else if (statusTicket.equals("cancelled")) {
                                            ticketStatus = "Cancelled";
                                            ticketStatusBox.setText(ticketStatus);
                                            ticketStatusCard.setCardBackgroundColor(Color.BLACK);
                                        } else if (statusTicket.equals("postponed")) {
                                            ticketStatus = "Postponed";
                                            ticketStatusBox.setText(ticketStatus);
                                            ticketStatusCard.setCardBackgroundColor(Color.parseColor("#FFA500"));
                                        }
                                    }else{
                                        ticketStatusHeader.setVisibility(View.GONE);
                                        ticketStatusCard.setVisibility(View.GONE);
                                        ticketStatusBox.setVisibility(View.GONE);
                                    }
                                }else{
                                    ticketStatusHeader.setVisibility(View.GONE);
                                    ticketStatusCard.setVisibility(View.GONE);
                                    ticketStatusBox.setVisibility(View.GONE);
                                }
                            }
                                //GETTING TICKET URL
                            TextView ticketUrlHeader = view.findViewById(R.id.textView22);
                            TextView ticketUrlBox = view.findViewById(R.id.textView23);
                            HorizontalScroll horizontalScroll2 = new HorizontalScroll(ticketUrlBox);
                            if(response.has("url")){
                                eventURL = response.getString("url");
                                ticketUrlBox.setText(eventURL);
                                SharedDetails.getInstance().setTicketUrl(eventURL);
                                ticketUrlBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(eventURL));
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                ticketUrlHeader.setVisibility(View.GONE);
                                ticketUrlBox.setVisibility(View.GONE);
                            }

                            //GETTING SEAT MAP
                            ImageView seatingImageBox;
                            seatingImageBox = view.findViewById(R.id.seating_image);
                            if(response.has("seatmap")){
                                JSONObject seatmap = response.getJSONObject("seatmap");
                                if(seatmap.has("staticUrl")){
                                    String seatmapUrl = seatmap.getString("staticUrl");
                                    Picasso.get().load(seatmapUrl).resize(1200, 800).into(seatingImageBox);
                                }else{
                                    seatingImageBox.setVisibility(View.GONE);
                                }
                            }else{
                                seatingImageBox.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        });
        queue.add(stringRequest);

         return view;
    }
}