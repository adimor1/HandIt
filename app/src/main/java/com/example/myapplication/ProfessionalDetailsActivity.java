package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfessionalDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Button orderBtn;
    String emailFromTv;
    TextView tvEmail;
    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_professional_details);
        orderBtn = findViewById(R.id.orderBtn);
        tvEmail = (TextView) findViewById(R.id.tvEmailDetails);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescriptionDetails);

        if(getIntent().hasExtra("selected_user")){
            User user = getIntent().getParcelableExtra("selected_user");
            String email = user.getEmail();
            String description= user.getDescription();

            tvEmail.setText(email);
            tvDescription.setText(description);
        }

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFromTv = tvEmail.getText().toString();
                showCustomDialog();
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(ProfessionalDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);
        final EditText timeEt = dialog.findViewById(R.id.timeEt);
        final EditText dateEt = dialog.findViewById(R.id.dateEt);

        Button submitButton = dialog.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = timeEt.getText().toString();
                String date = dateEt.getText().toString();
                addOrder(emailFromTv, time, date);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addOrder(String emailPro, String time, String date){
        Order order= new Order(emailPro, LoginUser.getLoginEmail(), time, date);
        db= FirebaseDatabase.getInstance();
        String key = db.getReference("Orders").push().getKey();
        reference= db.getReference("Orders");
        reference.child(key).setValue(order);
    }
}