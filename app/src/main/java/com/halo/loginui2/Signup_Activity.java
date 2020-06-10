package com.halo.loginui2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Signup_Activity extends AppCompatActivity {

    EditText fName, lName, email, userName, password, confirmPassword;
    Spinner country;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_);

        initializeComponents();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

    }

    private void initializeComponents() {
        fName = findViewById(R.id.editFirstName);
        lName = findViewById(R.id.editLastName);
        userName = findViewById(R.id.editUserName);
        email = findViewById(R.id.editEmail);
        country = findViewById(R.id.countryDropdown);
        password = findViewById(R.id.editPassword);
        confirmPassword = findViewById(R.id.editConfirmPassword);

        Spinner staticSpinner = findViewById(R.id.countryDropdown);

        // Create an ArrayAdapter using the string array and a default spinner
        @SuppressLint("ResourceType") ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.country_array,
                        R.menu.spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
    }

    public void backToLogin(View view) {
        startActivity(new Intent(Signup_Activity.this, Activity_Login.class));
    }

    public void onRegister(View view) {

        String pass, cpass;
        pass = password.getText().toString();
        cpass = confirmPassword.getText().toString();

        if (!pass.equals(cpass)) {
            //Show Error Message
            return;
        }

        final String firstName, lastName, uName, emailID, countryName;

        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        uName = userName.getText().toString();
        emailID = email.getText().toString();
        countryName = country.getSelectedItem().toString();
        progressDialog.setMessage("Logging in...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        auth.createUserWithEmailAndPassword(emailID, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String id = firebaseUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id);

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("userID", id);
                    hashMap.put("userName", uName);
                    hashMap.put("firstName", firstName);
                    hashMap.put("lastName", lastName);
                    hashMap.put("emailAddress", emailID);
                    hashMap.put("country", countryName);

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(Signup_Activity.this, MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Signup_Activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

}
