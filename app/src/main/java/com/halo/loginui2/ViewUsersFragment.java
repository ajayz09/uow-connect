package com.halo.loginui2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewUsersFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference usersRef;
    FirebaseUser currentUser;
    String currUserId, currUserName, filterCountry = "All";
    TextView userText;
    HashMap<String, String> hashMap = new HashMap<>();
    ArrayList<String> arraylist = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public ViewUsersFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle("Registered Users");
        final View view = inflater.inflate(R.layout.activity_view_users_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currUserId = currentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currUserName = getUserName(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        arraylist.clear();

        // FirebaseDatabase.getInstance().getReference("Users/"+currUserId).

        Spinner staticSpinner = view.findViewById(R.id.selectedCountry);

        // Create an ArrayAdapter using the string array and a default spinner
        @SuppressLint("ResourceType") ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.filter_country_array,
                        R.menu.spinner_fliterlist);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCountry = parent.getSelectedItem().toString();
                if (hashMap != null) {
                    populateArray();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currUserId);

        FirebaseDatabase.getInstance().getReference("Users").child(currUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && (dataSnapshot.hasChild("firstName")) && (dataSnapshot.hasChild("lastName"))) {

                    String fName = dataSnapshot.child("firstName").getValue().toString();
                    String lName = dataSnapshot.child("lastName").getValue().toString();


                    final ListView listView = view.findViewById(R.id.friendsListview);

                    adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.activity_listview, arraylist);

                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            Intent intent = new Intent(getActivity(), ChatActivity.class);
//                            intent.putExtra("userInfo", listView.getItemAtPosition(i).toString());
//                            startActivity(intent);
                            Bundle args = new Bundle();
                            args.putString("friendInfo", listView.getItemAtPosition(i).toString());
                            ProfileFragment newFragment = new ProfileFragment();
                            newFragment.setArguments(args);

                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.flMain, newFragment);
                            fr.addToBackStack(null).commit();
                        }
                    });

                    usersRef.orderByChild("firstName").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String fName = dataSnapshot.child("firstName").getValue().toString();
                            String lName = dataSnapshot.child("lastName").getValue().toString();
                            String tempUserID = dataSnapshot.child("userID").getValue().toString();
                            String userCountry = dataSnapshot.child("country").getValue().toString();
                            String tempUserName = fName + ' ' + lName;
                            if (!tempUserID.equals(currUserId)) {
                                hashMap.put(tempUserName, userCountry);
                                populateArray();
                            }


//                            if (!tempUserID.equals(currUserId)) {
//                                if (!filterCountry.equals("All")){
//                                    if (userCountry.equals(filterCountry)){
//                                        arraylist.add(tempUserName);
//                                    }
//
//                                }else{
//                                    arraylist.add(tempUserName);
//                                }
//
//
//                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Cancelled!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void populateArray() {
        arraylist.clear();

        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue().toString();
            String key = entry.getKey().toString();
            if (filterCountry.equals("All")) {
                arraylist.add(key);
            } else if (filterCountry.equals(value)) {
                arraylist.add(key);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private String getUserName(DataSnapshot dataSnapshot) {
        String fullName = "";
//        for(DataSnapshot ds : dataSnapshot.getChildren()){
        UserInformation uInfo = new UserInformation();
        uInfo.setFirstName(dataSnapshot.child(currUserId).getValue(UserInformation.class).getFirstName());
        uInfo.setLastName(dataSnapshot.child(currUserId).getValue(UserInformation.class).getLastName());

        String _firstName = uInfo.getFirstName();
        String _lastName = uInfo.getLastName();

        fullName = _firstName + ' ' + _lastName;
//        }
        return fullName;
    }

}
