package com.halo.loginui2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Activity_Login extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    EditText email, password;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(Activity_Login.this, MainActivity.class));
            return;
        }

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);
        if (firstStart) {
            showOnBoarding();
        }
        requestPermissions();


        setContentView(R.layout.activity_login);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        initializeComponents();

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private void showOnBoarding() {

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        startActivity(new Intent(Activity_Login.this, signUpView.class));
    }

    private void initializeComponents() {
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
    }

    public void b_signUp(View view) {
        startActivity(new Intent(Activity_Login.this, Signup_Activity.class));
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(Activity_Login.this, ForgotPassword.class));
    }

    public void b_signIn(View view) {

        String _emailID = email.getText().toString();
        String _password = password.getText().toString();

        if (_password.equals("")) {
            password.setError("Please enter password");
            password.requestFocus();
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_emailID).matches()) {
            email.setError("Please enter valid email address");
            email.requestFocus();
        }
        if (email.getText().toString().equals("")) {
            email.setError("Please enter email address");
            email.requestFocus();
        }
        if (!_emailID.equals("") &&
                password.getText().toString().length() >= 6 &&
                !password.getText().toString().trim().equals("")
                && android.util.Patterns.EMAIL_ADDRESS.matcher(_emailID).matches()) {
            progressDialog.setMessage("Logging in...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);
            auth.signInWithEmailAndPassword(_emailID, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(Activity_Login.this, MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


