package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.ArtistFragment;
import com.example.myapplication.fragments.DetailsFragment;
import com.example.myapplication.fragments.FavouritesFragment;
import com.example.myapplication.fragments.SearchHolderFragment;
import com.example.myapplication.fragments.VenueFragment;

public class EventDetailsTabAdapter extends FragmentStateAdapter {
    public EventDetailsTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public EventDetailsTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public EventDetailsTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ArtistFragment();
            case 2:
                return new VenueFragment();
            default:
                return new DetailsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
