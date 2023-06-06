package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.POJO.EventModel;
import com.example.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SearchTableFragment extends Fragment {
    ProgressBar progressBar;
    ArrayList<EventListCardModel> eventListCardModels = new ArrayList<>();
    public List<EventModel> eventModelList;

    String name;
    String id;
    String imageUrl;
    String localDate;
    String localTime;
    String segmentName;
    String venueName;
    EventRecyclerViewAdapter eventRecyclerViewAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_table, container, false);

        Handler handler = new Handler();
        Bundle args = getArguments();
        String keyword = args.getString("keyword");
        int distance = args.getInt("distance");
        String category = args.getString("category");
        double latitude = (double) args.getFloat("latitude");
        double longitude = (double) args.getFloat("longitude");
        System.out.printf("%s %d %s %f %f", keyword, distance, category, latitude, longitude);

        eventModelList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        @SuppressLint("DefaultLocale") String url = String.format("https://homework8-381519.wl.r.appspot.com/%s/%d/%s/%f/%f", keyword, distance, category, latitude, longitude);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d("API Response", jsonResponse);
                        try {
                            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
                            progressBar = view.findViewById(R.id.progressBar);
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
                            JSONObject embedded = response.getJSONObject("_embedded");
                            JSONArray events = embedded.getJSONArray("events");
                            for (int i = 0; i <events.length(); i++){

//                                getting event name
                                JSONObject event = events.getJSONObject(i);
                                name = event.getString("name");

                                id = event.getString("id");

//                                getting event image url
                                JSONArray images = event.getJSONArray("images");
                                JSONObject image = images.getJSONObject(0);
                                imageUrl = image.getString("url");

//                                getting date and time
                                JSONObject dates = event.getJSONObject("dates");
                                JSONObject start = dates.getJSONObject("start");
                                if(start.getString("localDate") != "Undefined" || start.getString("localDate") != null){
                                    localDate = start.getString("localDate");
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
                                    Date date = dateFormat.parse(localDate);
                                    DateFormat outputFormat = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
                                    localDate = outputFormat.format(date);



                                }else{
                                    localDate = "";
                                }
                                if(start.getString("localTime") != "Undefined" || start.getString("localTime") != null){
                                    localTime = start.getString("localTime");
                                    // Define the input time format
                                    SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                                    // Define the output time format
                                    SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                                    Date time = inputFormat.parse(localTime);
                                    localTime = outputFormat.format(time);
                                }else{
                                    localTime = "";
                                }
//                                getting event segment
                                JSONArray classifications = event.getJSONArray("classifications");
                                JSONObject segmentDetails = classifications.getJSONObject(0);
                                JSONObject segment = segmentDetails.getJSONObject("segment");
                                segmentName = segment.getString("name");

//                                getting event venue
                                JSONObject _embedded = event.getJSONObject("_embedded");
                                JSONArray venues = _embedded.getJSONArray("venues");
                                JSONObject venueDetails = venues.getJSONObject(0);
                                venueName = venueDetails.getString("name");
                                EventModel eventModel = new EventModel(id, imageUrl, name, localDate, venueName, localTime, segmentName);
                                eventModelList.add(eventModel);
                                EventListCardModel eventListCardModel = new EventListCardModel(name, localDate, venueName, localTime, segmentName, imageUrl, id);
                                eventListCardModel.setFavorite(false);
                                eventListCardModels.add(eventListCardModel);
                            }

//                            when user clicks on any of the recycle view item
                             eventRecyclerViewAdapter = new EventRecyclerViewAdapter(getContext(), eventListCardModels, new EventRecyclerViewAdapter.ClickListener(){

                                @Override
                                public void onItemClick(int position, View v) {
                                    Log.d(TAG, "onItemClick position: " + position);
                                    System.out.println(eventModelList.get(position).getId());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", eventModelList.get(position).getId());
                                    bundle.putString("event_name",eventModelList.get(position).getEventName());
                                    bundle.putParcelable("event_model", eventListCardModels.get(position));
                                    Navigation.findNavController(view).navigate(R.id.action_searchTableFragment_to_eventDetails, bundle);
                                }

                                @Override
                                public void onItemLongClick(int position, View v) {
                                    Log.d(TAG, "onItemLongClick pos = " + position);

                                    System.out.println(eventModelList.get(position).getId());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", eventModelList.get(position).getId());
                                    bundle.putString("event_name",eventModelList.get(position).getEventName());

                                    Navigation.findNavController(view).navigate(R.id.action_searchTableFragment_to_eventDetails, bundle);
                                }
                            });
                            recyclerView.setAdapter(eventRecyclerViewAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            CardView cardView = view.findViewById(R.id.no_event_card);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Hide the text after 3 seconds
                                    progressBar.setVisibility(View.GONE);
                                    cardView.setVisibility(View.VISIBLE);

                                }
                            }, 3000);
                            System.out.println("NO EVENTS TO SHOW");
                        } catch (ParseException e) {
                            System.out.println("NO EVENTS TO SHOW");
                            CardView cardView = view.findViewById(R.id.no_event_card);
                            cardView.setVisibility(View.VISIBLE);
                            //throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        });
        queue.add(stringRequest);


//        implementing back button
        ImageView imageView = view.findViewById(R.id.imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigateUp();
            }
        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if ( eventRecyclerViewAdapter!= null) {
            eventRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}