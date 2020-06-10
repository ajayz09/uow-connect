package com.halo.loginui2;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import java.util.*;

public class WeatherForecastActivity extends AppCompatActivity {
    private RequestQueue testQueue;
    private TextView textViewloc, textViewcurtemp, textViewminmax, conditionText ;
    private ImageView weathericon ;
    private final List<ForecastItem> forecastItemsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ForecastAdapter forecastAdapter;
    private String url;
    private String forecastUrl;
    private FusedLocationProviderClient client;
    private double longitude, latitude;

    private RecyclerView forecastListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        textViewloc = findViewById(R.id.place);
        textViewcurtemp=findViewById(R.id.temperature);
        textViewminmax=findViewById(R.id.maxMinTemp);
        weathericon = findViewById(R.id.imageTest);
        conditionText = findViewById(R.id.testSky);
        testQueue = Volley.newRequestQueue(this);


        forecastAdapter = new ForecastAdapter(forecastItemsList);
        forecastListRecyclerView = findViewById(R.id.forecastDaysRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        forecastListRecyclerView.setLayoutManager(linearLayoutManager);
        forecastListRecyclerView.setAdapter(forecastAdapter);
        client = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
    }
    private String getDay(int isDay){
        String day = "";

        switch (isDay) {
            case 0:
                day = "Saturday";
                break;
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Wednesday";
                break;
            case 6:
                day = "Thursday";
                break;
            case 7:
                day = "Friday";
                break;
        }
        return day;
    }

    private int getDayOfWeekNumber(String _date) {
        int day = 0;
        switch (_date) {
            case "Monday":
                day = 0;
                break;
            case "Tuesday":
                day = 1;
                break;
            case "Wednesday":
                day = 2;
                break;
            case "Thursday":
                day = 3;
                break;
            case "Friday":
                day = 4;
                break;
            case "Saturday":
                day = 5;
                break;
            case "Sunday":
                day = 6;
                break;
        }
        return day;
    }


    private int getNextDay(int currentIsDay) {
        int newIsDay;
        if (currentIsDay >= 7){
            newIsDay = 0;

        }
        else{
            newIsDay = currentIsDay + 1;

        }
        return newIsDay;
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation()
                .addOnSuccessListener(
                        this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude+ "&lon=" + longitude + "&appid=b2a8c1fbba169800aae20ddf46c353d6";
                                //dummy
//                                url = "https://api.openweathermap.org/data/2.5/weather?lat=-34.42&lon=150.87&appid=b2a8c1fbba169800aae20ddf46c353d6";
                                GetCurrentWeather();

                                forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude+ "&lon=" + longitude + "&appid=b2a8c1fbba169800aae20ddf46c353d6";
                                //dummy
                                forecastUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=-34.42&lon=150.87&cnt=7&appid=b2a8c1fbba169800aae20ddf46c353d6";
                                GetWeatherForecast();
                            }
                        });
    }

    private void GetWeatherForecast() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, forecastUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray forecastDaysArray = response.getJSONArray("list");
                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                            for (int i = 0; i < forecastDaysArray.length(); i++) {
                                ForecastItem forecastItem = new ForecastItem();

                                String dateString1 = getDay(getNextDay(dayOfWeek));
                                dayOfWeek = getNextDay(dayOfWeek);

                                forecastItem.setDayText(dateString1);
                                JSONObject foreCastDayObject = forecastDaysArray.getJSONObject(i);

                                JSONObject tempOmbject = foreCastDayObject.getJSONObject("temp");
                                float min_temp =  Float.parseFloat(tempOmbject.getString("min"));
                                min_temp = Math.round(min_temp - 273.15F);
                                forecastItem.setLowTemp(String.valueOf(min_temp));

                                float max_temp =  Float.parseFloat(tempOmbject.getString("max"));
                                max_temp = Math.round(max_temp - 273.15F);
                                forecastItem.setHighTemp(String.valueOf(max_temp));

                                JSONArray conditionArray = foreCastDayObject.getJSONArray("weather");
                                forecastItem.setConditionText(conditionArray.getJSONObject(0).getString("main"));
                                String iconText =conditionArray.getJSONObject(0).getString("icon");
                                String iconUrl = "openweathermap.org/img/w/" + iconText + ".png";
                                forecastItem.setForecastImage(iconUrl);

                                forecastItemsList.add(forecastItem);
                                forecastAdapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(WeatherForecastActivity.this, "Error - Forecast Section", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherForecastActivity.this, "Error - Forecast Section", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        testQueue.add(request);
    }

    private void GetCurrentWeather() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String place = response.getString("name");
                            textViewloc.setText(place);
                            JSONArray weatherArray = response.getJSONArray("weather");

                            for (int i = 0; i < weatherArray.length(); i++) {
                                JSONObject weatherObject = weatherArray.getJSONObject(i);
                                String conditiontext =weatherObject.getString("main");
                                conditionText.setText(conditiontext);

                                String iconText =weatherObject.getString("icon");
                                String iconurl = "http://openweathermap.org/img/w/" + iconText + ".png";

                                Picasso.get().load(iconurl).into(weathericon);
                            }

                            JSONObject tempObject = response.getJSONObject("main");

                            float curr_temp =  Float.parseFloat(tempObject.getString("temp"));
                            curr_temp = Math.round(curr_temp - 273.15F);
                            textViewcurtemp.setText(String.valueOf(curr_temp) + "\u00B0");

                            float min_temp =  Float.parseFloat(tempObject.getString("temp_min"));
                            min_temp = Math.round(min_temp - 273.15F);

                            float max_temp =  Float.parseFloat(tempObject.getString("temp_max"));
                            max_temp = Math.round(max_temp - 273.15F);

                            textViewminmax.setText(String.valueOf(max_temp)+ "\u00B0"+" "+String.valueOf(min_temp)+ "\u00B0");

                        } catch (JSONException e) {
                            Toast.makeText(WeatherForecastActivity.this, "Weather - Top Section_Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherForecastActivity.this, "Weather - Top Section_Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        testQueue.add(request);
    }
}
