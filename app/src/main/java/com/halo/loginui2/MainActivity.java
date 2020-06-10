package com.halo.loginui2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.fahmisdk6.avatarview.AvatarView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView _navName, _navEmail;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userID, _firstName, _lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        setNameAndEmail(header);
        //default fragment for home
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flMain, new HomeFragment());
        ft.commit();
    }

    private void setNameAndEmail(View header) {
        _navName = header.findViewById(R.id.navigationHeaderName);
        _navEmail = header.findViewById(R.id.navigationHeaderEmail);

        reference.addValueEventListener(new ValueEventListener() {
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
        try {

            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                UserInformation uInfo = new UserInformation();
                uInfo.setFirstName(ds.getValue(UserInformation.class).getFirstName());
                uInfo.setLastName(ds.getValue(UserInformation.class).getLastName());
                uInfo.setCountry(ds.getValue(UserInformation.class).getCountry());

                String _userID = ds.child("userID").getValue().toString();
                _firstName = uInfo.getFirstName();
                _lastName = uInfo.getLastName();

                String fullName = _firstName + ' ' + _lastName;
                AvatarView avatarView5 = findViewById(R.id.imgView_userProfile);
                avatarView5.bind(fullName, "");
                _navName.setText(fullName);
                _navEmail.setText(user.getEmail());

                if (userID.equals(_userID)) {
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Weather clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 0) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ProfileSettingsFragment());
            ft.addToBackStack(null).commit();
        }

        if (id == R.id.action_logout) {
            signOutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new HomeFragment());
            ft.addToBackStack(null).commit();
        } else if (id == R.id.nav_forum) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ForumFragment());
            ft.addToBackStack(null).commit();

        } else if (id == R.id.nav_hangouts) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ViewUsersFragment());
            ft.addToBackStack(null).commit();

        } else if (id == R.id.nav_nearby) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new NearbyFragment());
            ft.addToBackStack(null).commit();

        } else if (id == R.id.nav_unilife) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new UOWFragment());
            ft.addToBackStack(null).commit();

        } else if (id == R.id.nav_weather) {
//            Toast.makeText(Main_Page.this, "Weather clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, WeatherForecastActivity.class));
        } else if (id == R.id.nav_about) {
//            Toast.makeText(Main_Page.this, "Weather clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, signUpView.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutUser() {
        auth.signOut();
        finish();
        Intent i = new Intent(MainActivity.this, Activity_Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();

    }

}
