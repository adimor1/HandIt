package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.LocationUtil;
import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.User;
import com.example.myapplication.ProfAdapter;
import com.example.myapplication.R;
import com.example.myapplication.TaskListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class ProfessionalsListActivity extends AppCompatActivity implements ProfAdapter.ProfListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    ProfAdapter myAdapter;
    ArrayList<User> list;
    ArrayList<Double> disList;

    private TaskListener location_listener;
    private LocationUtil locationUtil;
    private final static int ACCESS_COARSE_LOCATION = 1;
    private final static int ACCESS_FINE_LOCATION = 2;
    private boolean userDidNotWantToTurnOnGps = false;
    private boolean isLocationPermissionGranted = false;
    private boolean noLocationFound;
    private boolean is_location_process_end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationutil_handle();
    }

    @Override
    public void profClick(int position) {
        Intent intent = new Intent(this, ProfessionalDetailsActivity.class);
        intent.putExtra("selected_user", list.get(position));
        startActivity(intent);
    }


    private void locationutil_handle() {
        noLocationFound = false;
        userDidNotWantToTurnOnGps = false;
        isLocationPermissionGranted = false;
        location_listener = new TaskListener() {
            @Override
            public void taskComplete(boolean status) {
                if (locationUtil.getIsLocationPermissionGranted()) {
                    isLocationPermissionGranted = true;
                    if (locationUtil.getIsuserDidNotWantToTurnOnGps()) {
                        userDidNotWantToTurnOnGps = true;
                    } else {
                        String location_address = "No location found";
                        if (locationUtil.getAddress() != null) {
                            location_address = locationUtil.getAddress();
                            LoginUser.setLoginUserAddress(location_address);
                            Toast.makeText(ProfessionalsListActivity.this, location_address, Toast.LENGTH_SHORT).show();
                        }
                        else
                            noLocationFound = true;
                    }
                }

                if ( !isLocationPermissionGranted || userDidNotWantToTurnOnGps) {
                    is_location_process_end = true;
                    adapterHandler(false);
                }
                else {
                    is_location_process_end = true;
                    adapterHandler(true);
                }
            }
        };
        locationUtil = new LocationUtil(this, location_listener);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    locationUtil.setIsLocationPermissionGranted(true);
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    locationUtil.setIsLocationPermissionGranted(false);
                    Toast.makeText(this, "not grant location permission", Toast.LENGTH_LONG).show();
                }
            }
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationUtil.startFindLocation();
                }
            }
        }
    }

    private void adapterHandler(boolean permission){
        recyclerView = findViewById(R.id.professionalsList);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        disList = new ArrayList<>();

        myAdapter = new ProfAdapter(this, list, locationUtil,  this);
        recyclerView.setAdapter(myAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);
                    if(user.isProf()){
                        if(!(user.getEmail()).equals(LoginUser.getLoginEmail())){
                            double dis;
                            if(permission){
                                 dis = locationUtil.getDistnace(user.getLatitude(), user.getLongitude());
                            }
                            else {
                                 dis = -1;
                            }
                            user.setDistance(dis);
                            list.add(user);
                            list.sort(Comparator.comparing(User::getDistance));
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}