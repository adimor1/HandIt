package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateUser extends AppCompatActivity {

    private Button updateUser;
    private EditText description;
    private EditText seniority;
    private EditText profession;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        updateUser = findViewById(R.id.updateUser);
        description = findViewById(R.id.description);
        seniority = findViewById(R.id.seniority);
        profession = findViewById(R.id.profession);

        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_description = description.getText().toString();
                String txt_seniority = seniority.getText().toString();
                String txt_profession = profession.getText().toString();

                updateUserData(txt_description, txt_seniority, txt_profession);
            }
        });
    }

    private void updateUserData(String description, String seniority, String profession) {
        HashMap User = new HashMap();
        User.put("description", description);
        User.put("seniority", seniority);
        User.put("profession", profession);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("email").equalTo(LoginUser.getLoginEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    databaseReference.child(key).updateChildren(User);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

