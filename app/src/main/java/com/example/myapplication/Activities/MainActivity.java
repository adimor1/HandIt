package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private Button add;
    private Button userList;
    private Button userProfile;
    private Button professionals;
    private Button orders;
    private Button flash;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout = findViewById(R.id.logout);
        add = findViewById(R.id.add);
        userProfile = findViewById(R.id.userProfile);
        professionals = findViewById(R.id.professionals);
        orders = findViewById(R.id.orders);


            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.orderByChild("email").equalTo(LoginUser.getLoginEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot childSnapshot: snapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        databaseReference.child(key);
                        Boolean prof = (Boolean) snapshot.child(key).child("prof").getValue();

                        String phone = (String) snapshot.child(key).child("phone").getValue();
                        String location = (String) snapshot.child(key).child("location").getValue();
                        String seniority = (String) snapshot.child(key).child("seniority").getValue();
                        String profession = (String) snapshot.child(key).child("profession").getValue();
                        String description = (String) snapshot.child(key).child("description").getValue();

                        if(!prof){
                            userProfile.setVisibility(View.GONE);
                            orders.setVisibility(View.GONE);
                        } else {
                            if(phone.equals("") && location.equals("") && seniority.equals("") && profession.equals("") && description.equals("")){
                                userProfile.setBackgroundColor(Color.RED);
                                Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        starFlash();
                                    }
                                },1000);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                LoginUser.setLoginEmail("");
                finish();
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateUser.class));
            }
        });

        professionals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfessionalsListActivity.class));
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfOrdersActivity.class));
            }
        });
    }

    public void starFlash() {
        Animation mAnimation = new AlphaAnimation(1,0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(10);
        mAnimation.setRepeatMode(Animation.REVERSE);
        userProfile.startAnimation(mAnimation);
    }
}