package com.halo.loginui2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("UOW Connect");
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        CardView test = view.findViewById(R.id.hangoutsCard);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new ViewUsersFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView studentLife = view.findViewById(R.id.studentLife);
        studentLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new StudentLifeFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView uow = view.findViewById(R.id.uowCard);
        uow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new UOWFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView forum = view.findViewById(R.id.forum);
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new ForumFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView nearbyEvents = view.findViewById(R.id.nearbyEvents);
        nearbyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new NearbyFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView FAQCard = view.findViewById(R.id.faqs);
        FAQCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new FAQFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView AboutCard = view.findViewById(R.id.aboutUOWCard);
        AboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new AboutUOWFragment());
                fr.addToBackStack(null).commit();
            }
        });

        CardView StarterPack = view.findViewById(R.id.starterPack);
        StarterPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain, new StarterPackFragment());
                fr.addToBackStack(null).commit();
            }
        });
        return view;
    }

}
