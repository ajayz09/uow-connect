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

public class CourseFragment extends Fragment {

    public CourseFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Courses");
        View view = inflater.inflate(R.layout.course_fragment, container, false);

        Button btn=view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Uri uri = Uri.parse("https://www.uow.edu.au/?gclid=CjwKCAjw44jrBRAHEiwAZ9igKLc8CoTexjbme_wUedWv78i0E-x_H40rSYrj8aph59MODN2f_tqy4RoCjEMQAvD_BwE&gclsrc=aw.ds"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}
