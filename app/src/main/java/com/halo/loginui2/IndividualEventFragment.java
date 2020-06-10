package com.halo.loginui2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IndividualEventFragment extends Fragment {

    private ImageView eventImage;
    private TextView eventTitle, eventDescription, eventStartTime, eventEndTime;
    private Button eventWebsite;
    private Date startDate,endDate;
    private String startDateString, endDateString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitle("");
        View view = inflater.inflate(R.layout.individual_event_fragment, container, false);

        Bundle bundle = getArguments();
        final NearbyItem eventObject= (NearbyItem) bundle.getSerializable("eventObject");

        eventTitle = view.findViewById(R.id.eventTitle);
        eventStartTime = view.findViewById(R.id.eventStartTime);
        eventEndTime = view.findViewById(R.id.eventEndTime);
        eventImage = view.findViewById(R.id.eventImage);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventWebsite = view.findViewById(R.id.eventBookButton);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat onlyTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat checkFormat = new SimpleDateFormat("yyyyMMdd");


        try {
            startDate = myFormat.parse(eventObject.getEventStartTime());
            endDate = myFormat.parse(eventObject.getEventEndTime());


            SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a"); //here 'a' for AM/PM

            startDateString = df.format(startDate);
            endDateString = df.format(endDate);

//            if(checkFormat.parse(eventObject.getEventStartTime()).equals(checkFormat.parse(eventObject.getEventEndTime())))
//                endDateString = onlyTime.format(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        eventTitle.setText(eventObject.getEventTitle());
//        eventTime.setText(eventObject.getEventStartTime() + " " + eventObject.getEventEndTime());
        eventStartTime.setText(startDateString);
        eventEndTime.setText(endDateString);
        ((MainActivity)getActivity()).setActionBarTitle(eventObject.getEventTitle());
        eventDescription.setText(eventObject.getEventDescription());
        Picasso.get().load(eventObject.getEventHQImage()).into(eventImage);

        eventWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(eventObject.getEventURL()));
                startActivity(intent);

            }
        });

        return view;
    }
}
