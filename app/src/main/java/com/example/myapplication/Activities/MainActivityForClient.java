package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityForClient extends AppCompatActivity {


    private Button logout;
    private Button add;
    private Button userProfile;
    private Button professionals;
    private Button orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_client);

        logout = findViewById(R.id.logout);
        add = findViewById(R.id.add);
        userProfile = findViewById(R.id.userProfile);
        professionals = findViewById(R.id.professionals);
        orders = findViewById(R.id.orders);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivityForClient.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityForClient.this, StartActivity.class));
                LoginUser.setLoginEmail("");
                finish();
            }
        });

        professionals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityForClient.this, ProfessionalsListActivity.class));
            }
        });

    }
}