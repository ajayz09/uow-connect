package com.halo.loginui2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NearbyPlaceAdapter extends RecyclerView.Adapter<NearbyPlaceAdapter.NearbyItemViewHolder> {

    private List<NearbyPlaceItem> nearbyPlaceItems;

    public NearbyPlaceAdapter(List<NearbyPlaceItem> nearbyPlaceItems) {
        this.nearbyPlaceItems = nearbyPlaceItems;
    }

    @NonNull
    @Override
    public NearbyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearbyplaces_layout, parent, false);

        return new NearbyItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NearbyItemViewHolder nearbyItemViewHolder, final int i) {
        final NearbyPlaceItem nearbyPlaceItem = nearbyPlaceItems.get(i);

        nearbyItemViewHolder.nameText.setText(nearbyPlaceItem.getPlaceName());
        nearbyItemViewHolder.addressText.setText(nearbyPlaceItem.getPlaceAddress());
        Picasso.get().load(nearbyPlaceItem.getPlaceImage()).into(nearbyItemViewHolder.cafeImage);
        nearbyItemViewHolder.statusText.setText(nearbyPlaceItem.getPlaceStatus());

        if (nearbyPlaceItem.getPlaceStatus().equals("Open")){
            nearbyItemViewHolder.statusText.setTextColor(Color.GREEN);
        }
        else {
            nearbyItemViewHolder.statusText.setTextColor(Color.RED);
        }

        nearbyItemViewHolder.cafeRating.setRating(Float.parseFloat(nearbyPlaceItem.getPlaceRating()));

        nearbyItemViewHolder.cafePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phoneNumber =  Uri.parse("tel:" + nearbyPlaceItem.getPlacePhone());
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(phoneNumber);
                v.getContext().startActivity(i);
            }
        });

        nearbyItemViewHolder.cafeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse(nearbyPlaceItem.getPlaceDirection()));
//                v.getContext().startActivity(intent);

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + nearbyPlaceItem.getPlaceName() + ", " + nearbyPlaceItem.getPlaceAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                v.getContext().startActivity(mapIntent);
            }
        });

        nearbyItemViewHolder.cafeWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(nearbyPlaceItem.getPlaceWebsite()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyPlaceItems.size();
    }

    public class NearbyItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText, addressText,statusText;
        public ImageView cafeImage;
        public RatingBar cafeRating;
        public Button cafePhone, cafeWebsite, cafeDirection;


        public NearbyItemViewHolder(@NonNull View itemView) {
            super(itemView);


            nameText = itemView.findViewById(R.id.nameText);
            addressText = itemView.findViewById(R.id.addressText);
            cafeImage = itemView.findViewById(R.id.cafeImage);
            statusText = itemView.findViewById(R.id.status);
            cafeRating = itemView.findViewById(R.id.ratingBar);

            cafePhone = itemView.findViewById(R.id.phone);
            cafeDirection = itemView.findViewById(R.id.direction);
            cafeWebsite = itemView.findViewById(R.id.website);


        }
    }

}

