package com.halo.loginui2;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutUOWFragment extends Fragment {

    public AboutUOWFragment(){

    }

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("About UOW");
        View view = inflater.inflate(R.layout.about_uowfragment, container, false);
        btn=view.findViewById(R.id.aboutbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Uri uri = Uri.parse("https://www.uow.edu.au/about/locations/campus-map/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }
}
