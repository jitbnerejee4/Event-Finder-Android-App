package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class VenueFragment extends Fragment {
    View view;
    String cityName;
    String stateName;
    String cityAndState="";
    TextView venueNameBox;
    TextView venueAddressBox;
    TextView venueCityAndStateBox;
    TextView venueContactBox;
    TextView openHoursBox;
    TextView generalRuleBox;
    TextView childRuleBox;

    LatLng mLocation;
    MapView mMapView;
    GoogleMap mGoogleMap;
    public VenueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_venue, container, false);
        String venue = SharedDetails.getInstance().getVenueName();
        System.out.println("IN VENUE");
        System.out.println(venue);
        Handler handler = new Handler();
        ProgressBar progressBar = view.findViewById(R.id.venue_progress_bar);
        ScrollView scrollView = view.findViewById(R.id.venue_scroll_view);

        mMapView = view.findViewById(R.id.mapView4);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            updateMapLocation();
        });


//       GETTING VENUE
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://homework8-381519.wl.r.appspot.com/venue/"+venue;
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
                                    scrollView.setVisibility(View.VISIBLE);

                                }
                            }, 3000);

                            JSONObject rootResponse = new JSONObject(jsonResponse);
                            JSONObject response = rootResponse.getJSONObject("response");
                            JSONObject embedded = response.getJSONObject("_embedded");
                            JSONArray venues = embedded.getJSONArray("venues");
                            JSONObject venue = venues.getJSONObject(0);

                            //GET VENUE NAME
                            String venueName = venue.getString("name");
                            venueNameBox = view.findViewById(R.id.textView31);
                            venueNameBox.setText(venueName);
                            HorizontalScroll horizontalScroll = new HorizontalScroll(venueNameBox);

                            //GET VENUE ADDRESS
                            JSONObject address = venue.getJSONObject("address");
                            String road = address.getString("line1");
                            venueAddressBox = view.findViewById(R.id.textView33);
                            venueAddressBox.setText(road);
                            HorizontalScroll horizontalScroll2 = new HorizontalScroll(venueAddressBox);

                            //GET VENUE CITY AND STATE
                            try{
                                JSONObject city = venue.getJSONObject("city");
                                cityName = city.getString("name");
                            }catch (Exception e){
                                cityName = "";
                                System.out.println("ERROR FROM CITY");
                            }

                            try{
                                JSONObject state = venue.getJSONObject("state");
                                stateName = state.getString("name");
                            }catch (Exception e){
                                stateName = "";
                                System.out.println("ERROR FROM STATE");
                            }

                            if(cityName.length() > 0){
                                cityAndState+=cityName;

                            }else{
                                cityAndState+="";
                            }
                            if(stateName.length() > 0){
                                cityAndState+=", ";
                                cityAndState+=stateName;
                            }

                            venueCityAndStateBox = view.findViewById(R.id.textView35);
                            venueCityAndStateBox.setText(cityAndState);
                            HorizontalScroll horizontalScroll3 = new HorizontalScroll(venueCityAndStateBox);

                            //GETTING CONTACT DETAILS
                            JSONObject contact = venue.getJSONObject("boxOfficeInfo");
                            try{
                                String phoneNumber = contact.getString("phoneNumberDetail");
                                venueContactBox = view.findViewById(R.id.textView37);
                                venueContactBox.setText(phoneNumber);
                                HorizontalScroll horizontalScroll4 = new HorizontalScroll(venueContactBox);
                            }catch (Exception e){
                                venueContactBox = view.findViewById(R.id.textView37);
                                venueContactBox.setText("");
                                System.out.println("PHONE NUMBER ERROR");
                            }



                            //ADDING GOOGLE MAP
                            JSONObject location = venue.getJSONObject("location");
                            String longitude = location.getString("longitude");
                            String latitude = location.getString("latitude");
                            System.out.println("LATITUDE AND LONGITUDE");
                            System.out.println(latitude);
                            System.out.println(longitude);
                            double lat = Double.parseDouble(latitude);
                            double lon = Double.parseDouble(longitude);
                            LatLng loc = new LatLng(lat, lon);
                            mLocation = loc;
                            updateMapLocation();



//                            mapView = view.findViewById(R.id.mapView4);
//                            mapView.onCreate(savedInstanceState);
//                            mapView.getMapAsync(googleMap1 -> {
//                                googleMap = googleMap1;
//                                LatLng geoLocation = new LatLng(lat,lon);
//                                googleMap.addMarker(new MarkerOptions().position(geoLocation));
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geoLocation, 15f));
//                            });

                            //GETTING OPEN HOURS
                            try{
                                String openHour = contact.getString("openHoursDetail");
                                openHoursBox = view.findViewById(R.id.open_hours_text);
                                openHoursBox.setText(openHour);
                                openHoursBox.setOnClickListener(new View.OnClickListener() {
                                    boolean isExpanded = false;
                                    @Override
                                    public void onClick(View v) {
                                        isExpanded = !isExpanded;
                                        if(isExpanded){
                                            openHoursBox.setMaxLines(Integer.MAX_VALUE);
                                            openHoursBox.setEllipsize(null);
                                        }else{
                                            openHoursBox.setMaxLines(2);
                                            openHoursBox.setEllipsize(TextUtils.TruncateAt.END);
                                        }

                                    }
                                });
                            }catch (Exception e){
                                openHoursBox = view.findViewById(R.id.open_hours_text);
                                openHoursBox.setText("");
                            }



                            //GETTING GENERAL RULE AND CHILD RULE
                            JSONObject generalInfo = venue.getJSONObject("generalInfo");
                            try{
                                String generalRule = generalInfo.getString("generalRule");
                                generalRuleBox = view.findViewById(R.id.general_rules_text);
                                generalRuleBox.setText(generalRule);
                                generalRuleBox.setOnClickListener(new View.OnClickListener() {
                                    boolean isExpanded = false;
                                    @Override
                                    public void onClick(View v) {
                                        isExpanded = !isExpanded;
                                        if(isExpanded){
                                            generalRuleBox.setMaxLines(Integer.MAX_VALUE);
                                            generalRuleBox.setEllipsize(null);
                                        }else{
                                            generalRuleBox.setMaxLines(2);
                                            generalRuleBox.setEllipsize(TextUtils.TruncateAt.END);
                                        }
                                    }
                                });
                            }catch (Exception e){
                                generalRuleBox = view.findViewById(R.id.general_rules_text);
                                generalRuleBox.setText("");
                            }

                            try{
                                String childRule = generalInfo.getString("childRule");
                                childRuleBox = view.findViewById(R.id.child_rules_text);
                                childRuleBox.setText(childRule);
                                childRuleBox.setOnClickListener(new View.OnClickListener() {
                                    boolean isExpanded = false;
                                    @Override
                                    public void onClick(View v) {
                                        isExpanded = !isExpanded;
                                        if(isExpanded){
                                            childRuleBox.setMaxLines(Integer.MAX_VALUE);
                                            childRuleBox.setEllipsize(null);
                                        }else{
                                            childRuleBox.setMaxLines(2);
                                            childRuleBox.setEllipsize(TextUtils.TruncateAt.END);
                                        }
                                    }
                                });
                            }catch (Exception e){
                                childRuleBox = view.findViewById(R.id.child_rules_text);
                                childRuleBox.setText("");
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    private void updateMapLocation() {
        if (mGoogleMap != null && mLocation != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(mLocation);

            mGoogleMap.clear();
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
        }
    }
}