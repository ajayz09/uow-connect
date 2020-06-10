package com.halo.loginui2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder>{
    private List<ForecastItem> forecastItems;
    public ImageView forecastImage;
    public ForecastAdapter(List<ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ForecastItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.forecast_days_layout, viewGroup, false);

        return new ForecastItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastItemViewHolder forecastItemViewHolder, int i) {
        ForecastItem forecastItem = forecastItems.get(i);
        forecastItemViewHolder.dayTextView.setText(forecastItem.getDayText());
        forecastItemViewHolder.conditionTextView.setText(forecastItem.getConditionText());
        forecastItemViewHolder.maxTempTextView.setText(forecastItem.getHighTemp());
        forecastItemViewHolder.minTempTextView.setText(forecastItem.getLowTemp());
        Picasso.get().load(forecastItem.getForecastImage()).into(forecastImage);
//        Picasso.get().load(forecastImage).into(imageView);
    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    public class ForecastItemViewHolder extends RecyclerView.ViewHolder {

        public TextView dayTextView, conditionTextView, maxTempTextView, minTempTextView;


        public ForecastItemViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.dayText);
            conditionTextView = itemView.findViewById(R.id.conditionText);
            maxTempTextView = itemView.findViewById(R.id.maxTempText);
            minTempTextView = itemView.findViewById(R.id.minTempText);
            forecastImage = itemView.findViewById(R.id.conditionImage);

        }
    }

}
