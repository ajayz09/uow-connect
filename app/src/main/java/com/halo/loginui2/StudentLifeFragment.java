package com.halo.loginui2;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentLifeFragment extends Fragment implements View.OnClickListener{

    public StudentLifeFragment() {

    }

    CardView Clubs,Activites,Voulnteering,Sports;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Student Life");
        View view = inflater.inflate(R.layout.student_life_fragment, container, false);

        findIds(view);

        return view;
    }

    private void findIds(View view)
    {

        Clubs=view.findViewById(R.id.clubs);
        Activites=view.findViewById(R.id.activities);
        Voulnteering=view.findViewById(R.id.voulnteering);
        Sports=view.findViewById(R.id.sports);
        ///////////////////////////////// ONCLICK LISTENERS ///////

        Clubs.setOnClickListener(this);
        Activites.setOnClickListener(this);
        Voulnteering.setOnClickListener(this);
        Sports.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        switch (v.getId())
        {

            case R.id.clubs :
                fr.replace(R.id.flMain,new ClubFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.activities :
                fr.replace(R.id.flMain,new WellnessFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.voulnteering :
                fr.replace(R.id.flMain,new VolunteerFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.sports :
                fr.replace(R.id.flMain,new SportsFragment());
                fr.addToBackStack(null).commit();
                break;

        }


    }
}
