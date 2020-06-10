package com.halo.loginui2;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.constraint.motion.Debug.getLocation;

public class NearbyEvents extends Fragment implements NearbyAdapter.OnClickListener {

    private final List<NearbyItem> eventsNearbyList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private NearbyAdapter nearbyEventsAdapter;
    private RecyclerView nearbyListRecyclerView;
    private RequestQueue testQueue;
    private String url;
    private FusedLocationProviderClient client;
    private double longitude, latitude;
    private ProgressDialog progressDialog;

    public NearbyEvents(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitle("Nearby Events");
        View view = inflater.inflate(R.layout.fragment_nearby_events, container, false);

        progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        nearbyEventsAdapter = new NearbyAdapter(eventsNearbyList,this);
        nearbyListRecyclerView = view.findViewById(R.id.eventNearbyRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        nearbyListRecyclerView.setLayoutManager(linearLayoutManager);
        nearbyListRecyclerView.setAdapter(nearbyEventsAdapter);
        testQueue = Volley.newRequestQueue(getActivity());
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        progressDialog.setMessage("Loading Events...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        getLocation();

        return view;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation()
                .addOnSuccessListener(
                        getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                url = "https://www.eventbriteapi.com/v3/events/search/?q=Wollongong&location.within=5km&location.latitude=" + latitude + "&location.longitude="+ longitude +"&token=3NRDBH53RUD56DLCM36W";
                                url = "https://www.eventbriteapi.com/v3/events/search/?q=Wollongong&location.within=5km&location.latitude=-34.4093311&location.longitude=150.869622&token=3NRDBH53RUD56DLCM36W";
                                GetEvents();
                            }
                        });
    }

    private void GetEvents(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray events = response.getJSONArray("events");

                            for(int i = 0; i < events.length(); i++){

                                JSONObject eventObject = events.getJSONObject(i);
                                NearbyItem nearbyItem = new NearbyItem();

                                JSONObject nameObject = eventObject.getJSONObject("name");
                                JSONObject descriptionObject = eventObject.getJSONObject("description");
                                JSONObject startObject = eventObject.getJSONObject("start");
                                JSONObject endObject = eventObject.getJSONObject("end");

                                nearbyItem.setEventURL(eventObject.getString("url"));
                                nearbyItem.setEventTitle(nameObject.getString("text"));
                                nearbyItem.setEventDescription(descriptionObject.getString("text"));

                                nearbyItem.setEventStartTime(startObject.getString("local"));
                                nearbyItem.setEventEndTime(endObject.getString("local"));

                                JSONObject imageObject = eventObject.getJSONObject("logo");
                                nearbyItem.setEventImage(imageObject.getString("url"));


                                JSONObject originalImage = imageObject.getJSONObject("original");
                                nearbyItem.setEventHQImage(originalImage.getString("url"));

                                eventsNearbyList.add(nearbyItem);
                                nearbyEventsAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
            }
        });

        testQueue.add(request);
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(int position) {

        FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();

        fr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        IndividualEventFragment newFragment = new IndividualEventFragment();


        Bundle args = new Bundle();
        NearbyItem nearbyItem = eventsNearbyList.get(position);
        args.putSerializable("eventObject", nearbyItem);

        newFragment.setArguments(args);


//        args.eventsNearbyList[position]
       // FragmentTransaction fr = getFragmentManager().beginTransaction();

        fr.replace(R.id.flMain, newFragment);
        fr.addToBackStack(null).commit();
    }
}
