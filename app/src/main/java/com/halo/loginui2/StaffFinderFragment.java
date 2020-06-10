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

public class StaffFinderFragment extends Fragment {

    public StaffFinderFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Staff Finder");
        View view = inflater.inflate(R.layout.staff_finder_fragment, container, false);

        Button btn=view.findViewById(R.id.staffbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Uri uri = Uri.parse("https://ssl.informatics.uow.edu.au/uowsf");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}
