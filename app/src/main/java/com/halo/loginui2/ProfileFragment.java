package com.halo.loginui2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.fahmisdk6.avatarview.AvatarView;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference usersRef, reference;
    FirebaseUser currentUser;
    String currentUserID, firstName, lastName, fullName, userName, userEmail, friendName, friendID, countryString;
    AvatarView avatarView5;
    TextView nameText, emailText, userNameText, country;
    Button messageButton;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        messageButton = view.findViewById(R.id.messagebutton);

        Bundle bundle = getArguments();
        if (bundle != null) {
//            getSupportActionBar().setTitle(bundle.getString("userInfo"));
            GetUserID(bundle.getString("friendInfo"));
            ((MainActivity) getActivity()).setActionBarTitle(bundle.getString("friendInfo"));
        }

        avatarView5 = view.findViewById(R.id.imgView_userProfile);
        initialise();
        getDetails(view);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userInfo", friendName);
                startActivity(intent);

            }
        });

        return view;

    }

    private void initialise() {

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
//        currentUser = auth.getCurrentUser();
//        currentUserID = currentUser.getUid();


    }

    private void getDetails(View header) {

        nameText = header.findViewById(R.id.userFullName);
        userNameText = header.findViewById(R.id.userName);
        emailText = header.findViewById(R.id.userEmail);
        country = header.findViewById(R.id.countryName);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String _userID = ds.child("userID").getValue().toString();

            if (friendID.equals(_userID)) {
                UserInformation uInfo = new UserInformation();
                uInfo.setFirstName(ds.getValue(UserInformation.class).getFirstName());
                uInfo.setLastName(ds.getValue(UserInformation.class).getLastName());
                uInfo.setEmailAddress(ds.getValue(UserInformation.class).getEmailAddress());
                uInfo.setUserName(ds.getValue(UserInformation.class).getUserName());
                uInfo.setCountry(ds.getValue(UserInformation.class).getCountry());
                firstName = uInfo.getFirstName();
                lastName = uInfo.getLastName();
                userName = uInfo.getUserName();
                userEmail = uInfo.getEmailAddress();
                countryString = uInfo.getCountry();
                fullName = firstName + ' ' + lastName;
                avatarView5.bind(fullName, "");
                nameText.setText(fullName);
                userNameText.setText(userName);
                emailText.setText(userEmail);
                country.setText(countryString);
                break;
            }

        }
    }

    private void GetUserID(final String name) {

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        reference = FirebaseDatabase.getInstance().getReference();
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fName = dataSnapshot.child("firstName").getValue().toString();
                String lName = dataSnapshot.child("lastName").getValue().toString();
                if ((fName + " " + lName).equals(name)) {
                    friendID = dataSnapshot.child("userID").getValue().toString();
                    friendName = name;
                }
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
    }
}





