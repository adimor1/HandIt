package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfessionalDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Button order;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        order = findViewById(R.id.order);
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

        order.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
             //   Toast.makeText(ProfessionalDetailsActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
           //     addOrder(emailPro);
            }
        });
    }

  //  private void addOrder(String emailPro){
    //    Order order= new Order(emailPro, LoginUser.getLoginEmail());
      //  db= FirebaseDatabase.getInstance();
        //String key = db.getReference("Orders").push().getKey();
        //reference= db.getReference("Orders");
        //reference.child(key).setValue(order);
    //}
}