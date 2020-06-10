package com.halo.loginui2;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StarterPackFragment extends Fragment implements View.OnClickListener{

    CardView electronic,document,summer,winter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Starter Pack");
        View view = inflater.inflate(R.layout.starter_pack_fragment, container, false);

        electronic=view.findViewById(R.id.electronics);
        document=view.findViewById(R.id.documents);
        summer=view.findViewById(R.id.summer);
        winter=view.findViewById(R.id.winter);


        electronic.setOnClickListener(this);
        document.setOnClickListener(this);
        summer.setOnClickListener(this);
        winter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        Fragment fragment = new StarterPackDetailedFragment();

        switch (v.getId())
        {
            case R.id.documents:

                bundle.putString("Title","Documents");
                bundle.putString("Disc","1. Passport\n" +
                        "2. e-Visa\n" +
                        "3. Airport pickup confirmation email (If you have availed the services of UOW's free shuttle)\n" +
                        "4. Current Academic transcripts of tertiary studies (Study Abroad/Exchange Students)\n" +
                        "5. UWA documents – Offer letter and Confirmation of Enrolment (CoE)\n" +
                        "6. Overseas Healthcare Card or documents related to the same\n" +
                        "7. Australian currency (For emergency and expenses for the first few weeks\n" +
                        "8. Credit card (or Australian bank card)\n" +
                        "9. Souvenirs from your home country");

                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
                break;
            case R.id.summer:
                bundle.putString("Title","Summer");
                bundle.putString("Disc","1. Hats: \n " +
                        "   Could be either Peak/bucket hat \n" +
                        "2. Thongs or Sandals \n" +
                        "3. Sunnies \n" +
                        "4. Swimmers/ Bathers/ Swimming costume \n" +
                        "5. Waterproof/windproof jacket");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
                break;
            case R.id.winter:
                bundle.putString("Title","Winter");
                bundle.putString("Disc","1. Beanie \n" +
                        "2. Sneakers \n" +
                        "3. Scarf \n" +
                        "4. Thermals \n" +
                        "5. Warm jacket \n" +
                        "6. Warm bed socks \n" +
                        "7. Gloves \n");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
                break;
            case R.id.electronics:
                bundle.putString("Title","Electronic Devices");
                bundle.putString("Disc","1. Universal Adapter\n " +
                        "Because there’s only one thing sadder than having no charge in your phone… And also it will be useful for other non-Australian appliances so try to have atleast 4-5 adaptors.\n\n" +
                        "2. Powerboard \n" +
                        " Same use as the adaptor if you do not want to carry lots of adaptors. Try to get a board which also has universal socket ports.");
                fragment.setArguments(bundle);
                fr.replace(R.id.flMain,fragment);
                fr.addToBackStack(null).commit();
                break;


        }

    }
}
