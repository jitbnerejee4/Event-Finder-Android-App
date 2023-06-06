package com.example.myapplication.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.POJO.EventModel;
import com.example.myapplication.POJO.FavouritesModel;
import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {
    Context context;
    public ArrayList<EventListCardModel> eventListCardModels;
    public List<EventListCardModel> favList;
    private final ClickListener clickListener;
    boolean found;

    boolean isFound;

    public EventRecyclerViewAdapter(Context context, ArrayList<EventListCardModel> eventListCardModels, ClickListener clickListener){
        this.context = context;
        this.eventListCardModels = eventListCardModels;

        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new EventRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.MyViewHolder holder, int position) {
        isFound = false;

        String url = eventListCardModels.get(position).getEventImageUrl();
        //Glide.with(context).load(url).apply(new RequestOptions().override(300, 500)).into(holder.eventThumbnail);
        Picasso.get().load(url).resize(350, 350).centerCrop().into(holder.eventThumbnail);
        holder.eventName.setText(eventListCardModels.get(position).getEventName());
        holder.eventDate.setText(eventListCardModels.get(position).getEventDate());
        holder.eventVenue.setText(eventListCardModels.get(position).getEventStaduim());
        holder.eventTime.setText(eventListCardModels.get(position).getEventTime());
        holder.eventGenre.setText(eventListCardModels.get(position).getEventGenre());
        List<EventListCardModel> fav = FavouritesModel.getInstance(holder.itemView.getContext()).getFavoritesList();
        if(fav.size() > 0){
            for(EventListCardModel item: fav){
                String eventId = item.getEventId();
                if(eventId.equals(eventListCardModels.get(position).getEventId())){
                    holder.heartImage.setImageResource(R.drawable.heart_filled);
                    isFound = true;
                    break;
                }
            }
        }

        if(isFound == false){
            holder.heartImage.setImageResource(R.drawable.heart_outline);
        }
//        if(FavouritesModel.getInstance().isFavorite(eventListCardModels.get(position))){
//            holder.heartImage.setImageResource(R.drawable.heart_filled);
//        }else{
//            holder.heartImage.setImageResource(R.drawable.heart_outline);
//        }


    }

    @Override
    public int getItemCount() {
        return eventListCardModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ImageView eventThumbnail;
        TextView eventName;
        TextView eventDate;
        TextView eventVenue;
        TextView eventTime;
        TextView eventGenre;
        ImageView heartImage;
        public boolean isHeartBlank = true;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            eventThumbnail = itemView.findViewById(R.id.imageView3);
            eventName = itemView.findViewById(R.id.textView5);
            eventDate = itemView.findViewById(R.id.textView6);
            eventVenue = itemView.findViewById(R.id.textView7);
            eventTime = itemView.findViewById(R.id.textView8);
            eventGenre = itemView.findViewById(R.id.textView9);
            HorizontalScroll horizontalScroll = new HorizontalScroll(eventName);
            HorizontalScroll horizontalScroll2 = new HorizontalScroll(eventVenue);
            HorizontalScroll horizontalScroll3 = new HorizontalScroll(eventGenre);

            heartImage = itemView.findViewById(R.id.imageView2);
            heartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    found = false;
                    System.out.println("CLICKED");
                    List<EventListCardModel> fav = FavouritesModel.getInstance(v.getContext()).getFavoritesList();
                    for(EventListCardModel item: fav){
                        String eventId = item.getEventId();
                        if(eventId.equals(eventListCardModels.get(getAdapterPosition()).getEventId())){
                            heartImage.setImageResource(R.drawable.heart_outline);
                            Snackbar.make(v,  eventListCardModels.get(getAdapterPosition()).getEventName() + " removed from favourites", Snackbar.LENGTH_LONG).show();
                            FavouritesModel.getInstance(v.getContext()).removeItem(eventListCardModels.get(getAdapterPosition()));
                            found = true;
                            break;
                        }
                    }
                    if(found == false){
                        heartImage.setImageResource(R.drawable.heart_filled);
                        Snackbar.make(v,  eventListCardModels.get(getAdapterPosition()).getEventName() + " added to favourites", Snackbar.LENGTH_LONG).show();
                        FavouritesModel.getInstance(v.getContext()).addItem(eventListCardModels.get(getAdapterPosition()));
                    }
//
                }
            });



        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                clickListener.onItemClick(position, v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                clickListener.onItemLongClick(position, v);
                return true;
            }
            return false;
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
