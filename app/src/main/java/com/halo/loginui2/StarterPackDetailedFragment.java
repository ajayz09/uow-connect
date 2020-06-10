package com.halo.loginui2;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StarterPackDetailedFragment extends Fragment {

    public StarterPackDetailedFragment(){

    }
    TextView Title,Disc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.starter_pack_detailed_fragment, container, false);


        Disc=view.findViewById(R.id.disc);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String title = bundle.getString("Title");
            String discription = bundle.getString("Disc");
            Disc.setText(discription);
            ((MainActivity) getActivity()).setActionBarTitle(title);

            if(title.equals("Summer")){
                View view1 = inflater.inflate(R.layout.summer_starter, container, false);
                return view1;
            }

            if(title.equals("Winter")){
                View view2 = inflater.inflate(R.layout.winter_starter, container, false);
                return view2;
            }
        }

        return view;
    }
}
