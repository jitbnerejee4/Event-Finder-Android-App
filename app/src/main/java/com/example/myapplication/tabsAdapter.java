package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.FavouritesFragment;
import com.example.myapplication.fragments.SearchFragment;
import com.example.myapplication.fragments.SearchHolderFragment;

public class tabsAdapter extends FragmentStateAdapter {
    public tabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new FavouritesFragment();
            default:
                return new SearchHolderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
