package com.halo.loginui2;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;

public class NearbyFragment extends Fragment {

    private ProgressDialog progressDialog;

    public NearbyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitle("Whats Nearby?");
        View view = inflater.inflate(R.layout.nearby_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);

        CardView nearbyEvents = (CardView) view.findViewById(R.id.eventsCard);
        nearbyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain,new NearbyEvents());
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyCafe = (CardView) view.findViewById(R.id.cafe);
        nearbyCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "cafe");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyGroceries = (CardView) view.findViewById(R.id.groceries);
        nearbyGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "groceries");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyLawyers = (CardView) view.findViewById(R.id.laywerCard);
        nearbyLawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "laywers");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyStore = (CardView) view.findViewById(R.id.storeCard);
        nearbyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "store");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyMedical = (CardView) view.findViewById(R.id.medicineCard);
        nearbyMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "medicals");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyParking = (CardView) view.findViewById(R.id.parkingCard);
        nearbyParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                NearbyPlacesFragment fragment = new NearbyPlacesFragment();
                Bundle bundle = new Bundle();
                bundle.putString( "type", "parking");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
            }
        });

        return view;
    }
}
