package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;
import com.example.myapplication.fragments.AutoSuggestAdapter;


public class SearchFragment extends Fragment{

    Button submit;
    Button clear;
    AutoCompleteTextView keyword;
    EditText distance;
    Spinner category;
    Switch mySwitch;
    EditText locationBox;
    String selectedItem = null;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    public double latitude;
    public double longitude;
    public boolean autoLocation = false;
    public String key;
    public int dist;
    public JSONObject eventResponse;
    private AutoSuggestAdapter autoSuggestAdapter;









    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        // Permission granted, do something
                        System.out.println("GRANTED");
                    } else {
                        // Permission denied, show an explanation or disable the feature
                            // Show an explanation
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            // Permission already granted, do something
                        } else {
                            // Permission not granted, show permission request dialog
                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                // Show an explanation
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Location Permission Required")
                                        .setMessage("This app needs location permission to get your current location. Please grant the permission.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            } else {
                                // Permission permanently denied, show app settings dialog
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Location Permission Required")
                                        .setMessage("This app needs location permission to get your current location. Please grant the permission.")
                                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        }

                        System.out.println("NOT GRANTED");
                    }
                }
            }
    );


//    public void getEventDetails( String key, int dist, String selectedItem, double latitude, double longitude){
//        System.out.println(key);
//        System.out.println(dist);
//        System.out.println(selectedItem);
//        System.out.println(latitude);
//        System.out.println(longitude);
//
//
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        @SuppressLint("DefaultLocale") String url = String.format("https://homework8-381519.wl.r.appspot.com/%s/%d/%s/%f/%f", key, dist, selectedItem, latitude, longitude);
//        System.out.println(url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String jsonResponse) {
//                        Log.d("API Response", jsonResponse);
//                        try {
//                            JSONObject rootResponse = new JSONObject(jsonResponse);
//                            JSONObject response = rootResponse.getJSONObject("response");
//                            JSONObject embedded = response.getJSONObject("_embedded");
//                            JSONArray events = embedded.getJSONArray("events");
//                            for (int i = 0; i <events.length(); i++){
//                                JSONObject event = events.getJSONObject(i);
//
//                                // Access the "name" field in the current event object
//                                String name = event.getString("name");
//                                System.out.println(name);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("API Error", error.toString());
//            }
//        });
//        queue.add(stringRequest);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        submit = view.findViewById(R.id.search_button);
        keyword = view.findViewById(R.id.keyword_box);
        distance = view.findViewById(R.id.distance_box);
        category = view.findViewById(R.id.category_box);
        mySwitch = view.findViewById(R.id.location_switch);
        locationBox = view.findViewById(R.id.location_box);
        clear = view.findViewById(R.id.clear_button);




//      Keyword suggestion
        //ArrayAdapter<String> suggestionAdapter = new ArrayAdapter<String>(requireContext(),R.layout.autocomplete_dropdown_layout, suggestions);
        autoSuggestAdapter =new AutoSuggestAdapter(requireContext(), R.layout.autocomplete_dropdown_layout);
        AutoCompleteTextView keywordEditText = view.findViewById(R.id.keyword_box);
        keywordEditText.setDropDownBackgroundDrawable(new ColorDrawable(Color.BLACK));
        keywordEditText.setThreshold(1);
        keywordEditText.setAdapter(autoSuggestAdapter);


        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Call the backend API with the current entered text
                String enteredText = charSequence.toString();
                if(enteredText.length() <= 0){
                    return;
                }
                String url = "https://homework8-381519.wl.r.appspot.com/suggest/" + enteredText;
                System.out.println(url);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String jsonResponse) {
                                List <String> suggestions = new ArrayList();
                                Log.d("API Response", jsonResponse);
                                try {
                                    //suggestionAdapter.clear();

                                    JSONObject rootResponse = new JSONObject(jsonResponse);
                                    JSONObject response = rootResponse.getJSONObject("response");
                                    if(response.has("_embedded")){
                                        JSONObject embedded = response.getJSONObject("_embedded");
                                        if(embedded.has("attractions")){
                                            JSONArray attractions = embedded.getJSONArray("attractions");
                                            for(int i = 0; i < attractions.length(); i++){
                                                JSONObject attraction = attractions.getJSONObject(i);
                                                String name = attraction.getString("name");
                                                suggestions.add(name);
                                            }
                                        }
                                    }



//                                    if(embedded.getJSONArray("venues") != null){
//                                        JSONArray venues = embedded.getJSONArray("venues");
//                                        for(int i = 0; i < venues.length(); i++){
//                                            JSONObject venue = venues.getJSONObject(i);
//                                            String name = venue.getString("name");
//                                            suggestions.add(name);
//                                        }
//                                    }



//                                    if(embedded.getJSONArray("events") != null){
//                                        JSONArray events = embedded.getJSONArray("events");
//                                        for(int i = 0; i < events.length(); i++){
//                                            JSONObject event = events.getJSONObject(i);
//                                            String name = event.getString("name");
//                                            suggestions.add(name);
//                                        }
//                                    }
//                                    if(embedded.getJSONArray("products") != null){
//                                        JSONArray products = embedded.getJSONArray("products");
//                                        for(int i = 0; i < products.length(); i++){
//                                            JSONObject product = products.getJSONObject(i);
//                                            String name = product.getString("name");
//                                            suggestions.add(name);
//                                        }
//                                    }

                                    System.out.println(suggestions);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                autoSuggestAdapter.setData(suggestions);
                                autoSuggestAdapter.notifyDataSetChanged();
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
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });




        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    locationBox.setVisibility(View.GONE);
                    locationBox.setText("");
                    autoLocation = true;
                } else {
                    autoLocation = false;
                    locationBox.setVisibility(View.VISIBLE);
                }
            }
        });




        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem == "All"){
                    selectedItem = "default";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoLocation == true){
                    if(keyword.getText().toString().trim().isEmpty() || distance.getText().toString().trim().isEmpty()){
                        Snackbar snackbar = Snackbar.make(v, "Fill all the fields", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{
                        key = keyword.getText().toString();
                        dist = Integer.parseInt(distance.getText().toString());
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            // Permission has been granted, perform the operation
                            try{
                                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

//                                locationCallback = new LocationCallback() {
//                                    @Override
//                                    public void onLocationResult(LocationResult locationResult) {
//                                    if (locationResult == null) {
//                                        System.out.println("ERROR");
//                                        return;
//                                    }
//                                        Location location = locationResult.getLastLocation();
//                                        latitude = location.getLatitude();
//                                        longitude = location.getLongitude();
//                                        System.out.println(latitude);
//                                        System.out.println(longitude);
//                                        Bundle  bundle = new Bundle();
//                                        bundle.putString("keyword", key);
//                                        bundle.putInt("distance", dist);
//                                        bundle.putString("category", selectedItem);
//                                        bundle.putFloat("latitude", (float)latitude);
//                                        bundle.putFloat("longitude", (float)longitude);
//                                        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchTableFragment2, bundle);
//                                        //getEventDetails(key, dist, selectedItem, latitude, longitude);
//                                    }
//                                };
                                fusedLocationClient.requestLocationUpdates(LocationRequest.create(), new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // Get the last known location of the user
                                        Location location = locationResult.getLastLocation();

                                        // Get the latitude and longitude of the user's current location
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        Bundle  bundle = new Bundle();
                                        bundle.putString("keyword", key);
                                        bundle.putInt("distance", dist);
                                        bundle.putString("category", selectedItem);
                                        bundle.putFloat("latitude", (float)latitude);
                                        bundle.putFloat("longitude", (float)longitude);
                                        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchTableFragment2, bundle);

                                        // Do something with the location, e.g. update a map or display the coordinates
                                        Log.d("MyApp", "Latitude: " + latitude + ", Longitude: " + longitude);
                                    }
                                }, null);
                            }catch (Exception e){
                                System.out.println(e);
                            }


                            //fusedLocationClient.requestLocationUpdates(LocationRequest.create(), locationCallback, null);

                        } else {
                            // Permission has not been granted, request the permission
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }
                }else{
                    if(keyword.getText().toString().trim().isEmpty() || distance.getText().toString().trim().isEmpty() || locationBox.getText().toString().trim().isEmpty()){
                        Snackbar.make(v, "Fill all the fields", Snackbar.LENGTH_LONG).show();
                    }else{
                        key = keyword.getText().toString();
                        dist = Integer.parseInt(distance.getText().toString());
                        String enteredLocation = locationBox.getText().toString();
                        System.out.println(enteredLocation);
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(enteredLocation, 1);
                            if (addresses != null && addresses.size() > 0) {
                                Address address = addresses.get(0);
                                latitude = address.getLatitude();
                                longitude = address.getLongitude();
                                // Do something with the latitude and longitude values
                                System.out.println(latitude);
                                System.out.println(longitude);
                                Bundle  bundle = new Bundle();
                                bundle.putString("keyword", key);
                                bundle.putInt("distance", dist);
                                bundle.putString("category", selectedItem);
                                bundle.putFloat("latitude", (float)latitude);
                                bundle.putFloat("longitude", (float)longitude);

                                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_searchTableFragment2, bundle);
                                //getEventDetails(key, dist, selectedItem, latitude, longitude);
                            }else{
                                Snackbar.make(v, "Please enter a valid location", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            // Handle the exception
                            System.out.println(e);

                        }
                    }
                }

//
            }

        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.setText("");
                distance.setText("10");
                category.setSelection(0);
                mySwitch.setChecked(false);
                locationBox.setVisibility(View.VISIBLE);
                locationBox.setText("");
            }
        });

        return view;
    }
}