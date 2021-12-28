package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateUser extends AppCompatActivity {

    private Button updateUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        updateUser = findViewById(R.id.updateUser);

        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData(LoginUser.getLoginEmail(), "aaaaaa");
            }
        });
    }

    private void updateUserData(String email, String desctiption) {
        HashMap User = new HashMap();
        User.put("email", 2222);
        User.put("name", 2222);
        User.put("description", 22222);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("MrmW0U0eOPPjQda_4np").updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateUser.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateUser.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

