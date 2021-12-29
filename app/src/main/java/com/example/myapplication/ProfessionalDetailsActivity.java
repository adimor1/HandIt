package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;

public class ProfessionalDetailsActivity extends AppCompatActivity {


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_professional_details);

        TextView tvEmail = (TextView) findViewById(R.id.tvEmailDetails);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescriptionDetails);

        if(getIntent().hasExtra("selected_user")){
            User user = getIntent().getParcelableExtra("selected_user");
            String email = user.getEmail();
            String description= user.getDescription();

            tvEmail.setText(email);
            tvDescription.setText(description);
        }

    }
}