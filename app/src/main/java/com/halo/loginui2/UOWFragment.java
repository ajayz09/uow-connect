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

public class UOWFragment extends Fragment implements View.OnClickListener{

    public UOWFragment(){

    }
    CardView Courses,TimeTables,StaffFinder,KeyDates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Uni Life");
        View view = inflater.inflate(R.layout.fragment_uow, container, false);

        findIds(view);

        return view;
    }

    private void findIds(View view)
    {

        Courses=view.findViewById(R.id.courses);
        TimeTables=view.findViewById(R.id.timetables);
        StaffFinder=view.findViewById(R.id.stafffinder);
        KeyDates=view.findViewById(R.id.keydates);
        ///////////////////////////////// ONCLICK LISTENERS ///////

        Courses.setOnClickListener(this);
        TimeTables.setOnClickListener(this);
        StaffFinder.setOnClickListener(this);
        KeyDates.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        switch (v.getId())
        {

            case R.id.courses :
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain,new CourseFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.timetables :
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain,new TimeTableFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.stafffinder :
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain,new StaffFinderFragment());
                fr.addToBackStack(null).commit();
                break;
            case R.id.keydates :
//                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.flMain,new KeyDatesFragment());
                fr.addToBackStack(null).commit();
                break;

        }

    }
}

