package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myapplication.POJO.FavouritesModel;
import com.example.myapplication.fragments.DetailsFragment;
import com.example.myapplication.fragments.EventListCardModel;
import com.example.myapplication.fragments.EventRecyclerViewAdapter;
import com.example.myapplication.fragments.FavouritesRecyclerViewAdapter;
import com.example.myapplication.fragments.HorizontalScroll;
import com.example.myapplication.fragments.SearchTableFragment;
import com.example.myapplication.fragments.SharedDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EventDetails extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    boolean isFavourite = false;
    EventDetailsTabAdapter eventDetailsTabAdapter;
    EventListCardModel eventListCardModel;

    EventRecyclerViewAdapter eventRecyclerViewAdapter;
    FavouritesRecyclerViewAdapter favouritesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

//        getting bundle
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String event_name = bundle.getString("event_name");
        EventListCardModel evt = bundle.getParcelable("event_model");
        System.out.println("THE EVENT IS");
        System.out.println(evt);

        //WORKING ON TOOLBAR

//        setting toolbar text as event name
        Toolbar myToolbar = findViewById(R.id.event_toolbar);
        TextView toolbarTitle = myToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(event_name);
        HorizontalScroll horizontalScroll = new HorizontalScroll(toolbarTitle);

//        hiding actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

//        changing back arrow color
        ImageView imageView = myToolbar.findViewById(R.id.back_arrow_to_search_table);
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#34C522"));
        imageView.setImageTintList(colorStateList);

        //changing color of heart image
        ImageView heartImage = findViewById(R.id.imageView8);
        List<EventListCardModel> favourites = FavouritesModel.getInstance(this).getFavoritesList();
        for(EventListCardModel item: favourites){
            if(item.getEventId().equals(evt.getEventId())){
                isFavourite = true;
                heartImage.setImageResource(R.drawable.heart_filled);
                break;
            }
        }
        heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavourite){
                    Snackbar.make(v,  evt.getEventName() + " removed from favourites", Snackbar.LENGTH_LONG).show();
                    heartImage.setImageResource(R.drawable.heart_outline);
                    FavouritesModel.getInstance(v.getContext()).removeItem(evt);

                }else{
                    Snackbar.make(v,  evt.getEventName() + " added to favourites", Snackbar.LENGTH_LONG).show();
                    heartImage.setImageResource(R.drawable.heart_filled);
                    FavouritesModel.getInstance(v.getContext()).addItem(evt);
                }
            }
        });


        //SETTING UP TAB LAYOUT

        tabLayout = findViewById(R.id.event_details_tab_layout);
        viewPager2 = findViewById(R.id.event_details_view_pager);
        eventDetailsTabAdapter = new EventDetailsTabAdapter(this);
        viewPager2.setAdapter(eventDetailsTabAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });



        //System.out.println("https://homework8-381519.wl.r.appspot.com/"+id);
        SharedDetails.getInstance().setId(evt.getEventId());
        SharedDetails.getInstance().setName(evt.getEventName());

        ImageView backToTableButton = findViewById(R.id.back_arrow_to_search_table);
        backToTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void openTwitterLink(View view) {
        String ticketUrl = SharedDetails.getInstance().getTicketUrl();
        String url = "https://twitter.com/intent/tweet?text="+ticketUrl;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void openFacebookLink(View view) {
        String ticketUrl = SharedDetails.getInstance().getTicketUrl();
        String url = "https://www.facebook.com/dialog/feed?app_id=731610485282365&display=popup&link="+ticketUrl;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }
    }






}