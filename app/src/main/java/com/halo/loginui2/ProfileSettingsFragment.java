package com.halo.loginui2;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.fahmisdk6.avatarview.AvatarView;

public class ProfileSettingsFragment extends Fragment {

    public ProfileSettingsFragment() {

    }

    FirebaseAuth auth;
    DatabaseReference userRef;
    FirebaseUser currentUser;
    String currentUserID, firstName, lastName, fullName, userName, userEmail;
    AvatarView avatarView5;
    TextView nameText, emailText, userNameText;
    EditText password, newPassword;
    Button updatePasswordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Profile Settings");
        final View view = inflater.inflate(R.layout.profile_settings_fragment, container, false);

        avatarView5 = (AvatarView) view.findViewById(R.id.imgView_userProfile);
        initialise();
        getDetails(view);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(userEmail,password.getText().toString());

                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    currentUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"Password Changed Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void initialise() {

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = auth.getCurrentUser();
        currentUserID = currentUser.getUid();


    }

    private void getDetails(View header){

        nameText = header.findViewById(R.id.userFullName);
        userNameText = header.findViewById(R.id.userName);
        emailText = header.findViewById(R.id.userEmail);
        password = header.findViewById(R.id.userPassword);
        newPassword = header.findViewById(R.id.userRePassword);
        updatePasswordButton = header.findViewById(R.id.updatePasswordButton);
        userRef.addValueEventListener(new ValueEventListener() {
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

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            String _userID = ds.child("userID").getValue().toString();

            if (currentUserID.equals(_userID)){
                UserInformation uInfo = new UserInformation();
                uInfo.setFirstName(ds.getValue(UserInformation.class).getFirstName());
                uInfo.setLastName(ds.getValue(UserInformation.class).getLastName());
                uInfo.setEmailAddress(ds.getValue(UserInformation.class).getEmailAddress());
                uInfo.setUserName(ds.getValue(UserInformation.class).getUserName());
                firstName = uInfo.getFirstName();
                lastName = uInfo.getLastName();
                userName = uInfo.getUserName();
                userEmail = uInfo.getEmailAddress();

                fullName = firstName + ' ' + lastName;
                avatarView5.bind(fullName, "");
                nameText.setText(fullName);
                userNameText.setText(userName);
                emailText.setText(userEmail);
                break;
            }

        }
    }
}
