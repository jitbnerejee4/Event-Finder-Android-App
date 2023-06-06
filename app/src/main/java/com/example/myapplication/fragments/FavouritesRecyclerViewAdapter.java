package com.example.myapplication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.POJO.FavouritesModel;
import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.ViewHolder> {
    List<EventListCardModel> favouritesList;

    public FavouritesRecyclerViewAdapter(List<EventListCardModel> favouritesList) {
        this.favouritesList = favouritesList;
    }

    public void setFavouritesList(List<EventListCardModel> favouritesList){
        this.favouritesList = favouritesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesRecyclerViewAdapter.ViewHolder holder, int position) {
        String url = favouritesList.get(position).getEventImageUrl();
        //Glide.with(context).load(url).apply(new RequestOptions().override(300, 500)).into(holder.eventThumbnail);
        Picasso.get().load(url).resize(350, 350).centerCrop().into(holder.eventImage);
        holder.eventName.setText(favouritesList.get(position).getEventName());
        holder.eventDate.setText(favouritesList.get(position).getEventDate());
        holder.eventVenue.setText(favouritesList.get(position).getEventStaduim());
        holder.eventTime.setText(favouritesList.get(position).getEventTime());
        holder.eventGenre.setText(favouritesList.get(position).getEventGenre());
        holder.heartImage.setImageResource(R.drawable.heart_filled);

    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventDate;
        TextView eventVenue;
        TextView eventTime;
        TextView eventGenre;

        ImageView heartImage;


        public ViewHolder(View view) {
            super(view);
            eventImage = view.findViewById(R.id.favourites_artist_image);
            eventName = view.findViewById(R.id.favourites_artist_name);
            eventDate = view.findViewById(R.id.favourites_event_date);
            eventVenue = view.findViewById(R.id.favourites_venue_name);
            eventTime = view.findViewById(R.id.favourites_event_time);
            eventGenre = view.findViewById(R.id.favourites_event_genre);
            HorizontalScroll horizontalScroll1 = new HorizontalScroll(eventName);
            HorizontalScroll horizontalScroll2 = new HorizontalScroll(eventVenue);
            HorizontalScroll horizontalScroll3 = new HorizontalScroll(eventGenre);

            heartImage = view.findViewById(R.id.favourites_heart_image);
            heartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    heartImage.setImageResource(R.drawable.heart_outline);
                    Snackbar.make(v,  favouritesList.get(getAdapterPosition()).getEventName() + " removed from favourites", Snackbar.LENGTH_LONG).show();
                    FavouritesModel.getInstance(v.getContext()).removeItem(favouritesList.get(getAdapterPosition()));
                    setFavouritesList(FavouritesModel.getInstance(v.getContext()).getFavoritesList());


                }
            });

        }
    }
}
