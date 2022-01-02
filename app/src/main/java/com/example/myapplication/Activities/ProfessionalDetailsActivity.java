package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfessionalDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Button orderBtn;
    String emailFromTv;
    TextView tvEmail;
    TextView tvName;
    TextView tvProf;
    TextView tvPhone;
    TextView tvDescription;
    TextView tvSenority;
    TextView tvRating;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);

        orderBtn = findViewById(R.id.orderBtn);
        tvEmail = findViewById(R.id.tvEmailDetails);
        tvName = findViewById(R.id.nameDetails);
        tvDescription = findViewById(R.id.tvDescriptionDetails);
        tvPhone = findViewById(R.id.phoneDetails);
        tvProf = findViewById(R.id.profDetails);
        //image = findViewById(R.id.imageDetails);
        tvSenority = findViewById(R.id.tvSeniority);
        tvRating = findViewById(R.id.ratingDetails);

        if(getIntent().hasExtra("selected_user")){
            User user = getIntent().getParcelableExtra("selected_user");
            String email = user.getEmail();
            String name = user.getFirstName() + " " + user.getLastName();
            String profession = user.getProfession();
            String description= user.getDescription();
            String phone = user.getPhone();
            String seniority= user.getSeniority();
            //String rating = String.valueOf(user.getSumRating() / user.getCountRating());
            //Uri uri = Uri.parse(user.getUriImage());

            tvName.setText(name);
            tvEmail.setText(email);
            tvDescription.setText(description);
            tvPhone.setText(phone);
            tvProf.setText(profession);
            tvSenority.setText(seniority);
            //tvRating.setText(rating);
          //  Picasso.get().load(uri).into(image);
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