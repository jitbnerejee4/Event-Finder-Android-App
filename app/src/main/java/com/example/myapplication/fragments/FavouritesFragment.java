package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.POJO.FavouritesModel;
import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavouritesFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    CardView cardView;
    ProgressBar progressBar;
    FavouritesRecyclerViewAdapter favouritesRecyclerViewAdapter;
    EventRecyclerViewAdapter eventRecyclerViewAdapter;
    List<EventListCardModel> favouritesList;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerView = view.findViewById(R.id.favourites_recyclerview);
        cardView = view.findViewById(R.id.no_favourites_card);
        progressBar = view.findViewById(R.id.favourites_progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println(FavouritesModel.getInstance(view.getContext()).getFavoritesList());
        favouritesRecyclerViewAdapter = new FavouritesRecyclerViewAdapter(FavouritesModel.getInstance(view.getContext()).getFavoritesList());
        recyclerView.setAdapter(favouritesRecyclerViewAdapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                System.out.println("THE POSITION IS");
                System.out.println(position);

                favouritesList = FavouritesModel.getInstance(view.getContext()).getFavoritesList();
                EventListCardModel item = favouritesList.get(position);
                Snackbar.make(view,  item.getEventName() + " removed from favourites", Snackbar.LENGTH_LONG).show();
                FavouritesModel.getInstance(view.getContext()).removeItem(favouritesList.get(position));
                favouritesRecyclerViewAdapter.setFavouritesList(FavouritesModel.getInstance(view.getContext()).getFavoritesList());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        favouritesRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfListIsEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfListIsEmpty();
            }
        });


//        if(FavouritesModel.getInstance().getFavoritesList().isEmpty()){
//            cardView = view.findViewById(R.id.no_favourites_card);
//            cardView.setVisibility(View.VISIBLE);
//        }else{
//            cardView = view.findViewById(R.id.no_favourites_card);
//            cardView.setVisibility(View.GONE);
//        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(FavouritesModel.getInstance(view.getContext()).getFavoritesList().size() <= 0){
            cardView = view.findViewById(R.id.no_favourites_card);
            recyclerView.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }else{
            cardView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            Handler handler = new Handler();
            cardView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide the text after 3 seconds
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }, 3000);
        }
        favouritesRecyclerViewAdapter.setFavouritesList(FavouritesModel.getInstance(view.getContext()).getFavoritesList());
    }

    private void checkIfListIsEmpty() {
        if (FavouritesModel.getInstance(view.getContext()).getFavoritesList().isEmpty()) {
            cardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            Handler handler = new Handler();
            cardView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide the text after 3 seconds
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }, 3000);
        }
    }

}