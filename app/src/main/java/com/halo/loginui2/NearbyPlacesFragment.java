package com.halo.loginui2;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyPlacesFragment extends Fragment {

    public NearbyPlacesFragment() {

    }

    private RequestQueue testQueue;

    private final List<NearbyPlaceItem> nearbyPlaceItems = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private NearbyPlaceAdapter nearbyPlaceAdapter;
    private RecyclerView nearbyPlacesRecyclerView;
    private String type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        testQueue = Volley.newRequestQueue(getActivity());
//        ((MainActivity)getActivity()).setActionBarTitle("Nearby Cafes");

        nearbyPlaceAdapter = new NearbyPlaceAdapter(nearbyPlaceItems);
        nearbyPlacesRecyclerView = view.findViewById(R.id.nearbyPlacesRecyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        nearbyPlacesRecyclerView.setLayoutManager(linearLayoutManager);

        nearbyPlacesRecyclerView.setAdapter(nearbyPlaceAdapter);



        GetNearbyPlaces();

        return view;
    }



    private void GetNearbyPlaces() {

        String queryType = "";
        switch(type){
            case "cafe":
                queryType = "cafe|resturant";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Cafes");
                break;
            case "groceries":
                queryType = "grocery_or_supermarket|shopping_mall";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Groceries");
                break;
            case "medicals":
                queryType = "hospital|pharmacy";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Medicals");
                break;
            case "laywers":
                queryType = "lawyer";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Lawyers");
                break;
            case "parking":
                queryType = "parking";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Parking");
                break;
            case "store":
                queryType = "convenience_store";
                ((MainActivity)getActivity()).setActionBarTitle("Nearby Stores");
                break;

        }
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-34.4093399,150.8696282&rankby=distance&types="+ queryType +"&sensor=true&key=AIzaSyBi99vISytb1d0NAogNjpwgGy_wElH2ly0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");

                            for (int i = 0; i < resultsArray.length(); i++) {

                                JSONObject nearbyPlaceObject = resultsArray.getJSONObject(i);
                                NearbyPlaceItem nearbyPlaceItem = new NearbyPlaceItem();
                                JSONObject statusObject = nearbyPlaceObject.isNull("opening_hours") ? null : nearbyPlaceObject.getJSONObject("opening_hours");
                                JSONArray photoObject = nearbyPlaceObject.isNull("photos") ? null : nearbyPlaceObject.getJSONArray("photos");

                                String photoURL = "", photoReference = "";

                                photoURL = "https://www.nilfiskcfm.com/wp-content/uploads/2016/12/placeholder.png";
                                if (photoObject != null) {
                                    photoReference = photoObject.getJSONObject(0).getString("photo_reference");
                                    photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=3000&photoreference=" + photoReference + "&key=AIzaSyBi99vISytb1d0NAogNjpwgGy_wElH2ly0";

                                }
                                nearbyPlaceItem.setPlaceImage(photoURL);
                                nearbyPlaceItem.setPlaceName(nearbyPlaceObject.getString("name"));
                                nearbyPlaceItem.setPlaceAddress(nearbyPlaceObject.getString("vicinity"));
                                String placeUrl = "http://www.google.com/maps/place/?q=place_id:" + nearbyPlaceObject.getString("place_id");
                                nearbyPlaceItem.setPlaceDirection(placeUrl);
                                GetPlaceContactDetails(nearbyPlaceItem, nearbyPlaceObject.getString("place_id"));
                                nearbyPlaceItem.setPlaceRating(nearbyPlaceObject.isNull("rating") ? "0" : nearbyPlaceObject.getString("rating"));
                                String openingHourText = "Opening Hours Not Available";
                                if (statusObject != null) {
                                    openingHourText = statusObject.getBoolean("open_now") ? "Open" : "Closed";
                                }
                                nearbyPlaceItem.setPlaceStatus(openingHourText);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        testQueue.add(request);

    }

    private void GetPlaceContactDetails(final NearbyPlaceItem nearbyItem, String placeID){

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=AIzaSyBi99vISytb1d0NAogNjpwgGy_wElH2ly0";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject resultObject = response.getJSONObject("result");

                            nearbyItem.setPlacePhone(resultObject.isNull("formatted_phone_number") ? "0" : resultObject.getString("formatted_phone_number"));
                            nearbyItem.setPlaceWebsite(resultObject.isNull("website") ? "http://google.com.au" : resultObject.getString("website"));

                            nearbyPlaceItems.add(nearbyItem);


                            nearbyPlaceAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        testQueue.add(request);
    }

}
