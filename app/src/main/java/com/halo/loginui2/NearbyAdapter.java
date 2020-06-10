package com.halo.loginui2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyEventsViewHolder> {

    private List<NearbyItem> eventsList;
    private FirebaseAuth auth;
    private OnClickListener mOnClickListener;

    public NearbyAdapter(List<NearbyItem> eventsList, OnClickListener onClickListener) {
        this.eventsList = eventsList;
        this.mOnClickListener = onClickListener;
    }


    @NonNull
    @Override
    public NearbyAdapter.NearbyEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.events_layout, viewGroup, false);

        return new NearbyAdapter.NearbyEventsViewHolder(view, mOnClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull NearbyAdapter.NearbyEventsViewHolder nearbyViewHolder, int i) {
        NearbyItem nearbyItem = eventsList.get(i);
        nearbyViewHolder.eventsTitle.setText(nearbyItem.getEventTitle());
        Picasso.get().load(nearbyItem.getEventImage()).into(nearbyViewHolder.eventsImage);
        String start = nearbyItem.getEventStartTime();
        String end = nearbyItem.getEventEndTime();
        String time = start + " " + end;
//        nearbyViewHolder.eventsTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class NearbyEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        OnClickListener onItemClickListner;
        public ImageView eventsImage;
        public TextView eventsTitle;
//        public TextView eventsTime;


        public NearbyEventsViewHolder(@NonNull View itemView, OnClickListener onItemClickListner) {
            super(itemView);
            this.onItemClickListner = onItemClickListner;
            eventsImage = itemView.findViewById(R.id.eventsImage);
            eventsTitle = itemView.findViewById(R.id.eventsTitle);
//            eventsTime = itemView.findViewById(R.id.eventsTime);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onItemClickListner.onItemClick(getAdapterPosition());
        }
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }
}
