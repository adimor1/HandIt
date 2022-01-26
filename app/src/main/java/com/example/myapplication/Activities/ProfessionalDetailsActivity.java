package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
    TextView tvlocation;
    private ImageView imageDetails;
    private RatingBar rBar;
    private Button btn;

    DatabaseReference databaseReference;
    StorageReference storageReference;

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
        imageDetails = findViewById(R.id.imageDetails);
        tvSenority = findViewById(R.id.tvSeniority);
        tvRating = findViewById(R.id.ratingDetails);
        rBar =  findViewById(R.id.ratingBar1);
        btn = findViewById(R.id.btnSumbit);
        tvlocation = findViewById(R.id.location);

        if(getIntent().hasExtra("selected_user")){
            User user = getIntent().getParcelableExtra("selected_user");
            String email = user.getEmail();
            String name = user.getFirstName() + " " + user.getLastName();
            String profession = user.getProfession();
            String description= user.getDescription();
            String phone = user.getPhone();
            String seniority= user.getSeniority();
            String location = user.getLocation();
            double sumRating = user.getSumRating();
            int countRating = user.getCountRating();

            tvName.setText(name);
            tvEmail.setText(email);
            tvDescription.setText(description);
            tvPhone.setText(phone);
            tvProf.setText(profession);
            tvSenority.setText(seniority);
            tvlocation.setText(location);

            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = storageReference.child("users/"+user.getEmail()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageDetails);
                }
            });

            if(countRating == 0){
                tvRating.setText(countRating + " Rated [0 Grade]");
            } else {

                Double calRating = finalRatingCal(sumRating / countRating);
                tvRating.setText(countRating + " Rated [" + calRating +" Grade]");
            }


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rating=String.valueOf(rBar.getRating());
                    Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();

                    HashMap User = new HashMap();
                    User.put("countRating", countRating+1);
                    User.put("sumRating", sumRating + rBar.getRating());

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

                    Double finalRating = (sumRating+rBar.getRating())/(countRating+1);
                    Double calRating = finalRatingCal(finalRating);

                    tvRating.setText(countRating +1  + " Rated [" + calRating +" Grade]");
                }
            });
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

    private double finalRatingCal(double finalRating){
        return Math.ceil(finalRating * 2) / 2;

    }
}