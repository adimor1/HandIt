package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.LocationUtil;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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
    private DatePickerDialog datePickerDialog;
    Button timeButton;
    int hour;
    int minute;
    Button dateButton;
    EditText phoneOrder;
    EditText addressOrder;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    databaseReference.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        Button submitButton = dialog.findViewById(R.id.submit_button);
        timeButton = dialog.findViewById(R.id.timeButton);
        initDatePicker();
        dateButton=dialog.findViewById(R.id.dateButton);
        dateButton.setText(getTodayDate());
        timeButton.setText("00:00");
        phoneOrder = dialog.findViewById(R.id.phoneOrder);
        addressOrder = dialog.findViewById(R.id.addressOrder);

        String place = LoginUser.getLoginUserAddress();
        if(place.contains(", Israel")){
            place = place.replace(", Israel", "");
        }

        addressOrder.setText(place);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateButton.getText().toString();
                String time = timeButton.getText().toString();
                String phone = phoneOrder.getText().toString();
                String address = addressOrder.getText().toString();

                if(phone.equals("") || address.equals("")){
                    Toast.makeText(ProfessionalDetailsActivity.this, "Missing Details", Toast.LENGTH_SHORT).show();
                } else {
                    addOrder(emailFromTv, time, date, phone, address);
                    dialog.dismiss();
                    Toast.makeText(ProfessionalDetailsActivity.this, "Order saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void addOrder(String emailPro, String time, String date, String phone, String address){
        Order order= new Order(emailPro, LoginUser.getLoginEmail(), time, date, phone, address);
        db= FirebaseDatabase.getInstance();
        String key = db.getReference("Orders").push().getKey();
        reference= db.getReference("Orders");
        reference.child(key).setValue(order);
    }

    private double finalRatingCal(double finalRating){
        return Math.ceil(finalRating * 2) / 2;

    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minute = minute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("select time");
        timePickerDialog.show();
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
         datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        if(month==1){
            return "JAN";
        }
        if(month==2){
            return "FEB";
        }
        if(month==3){
            return "MAR";
        }
        if(month==4){
            return "APR";
        }
        if(month==5){
            return "MAY";
        }
        if(month==6){
            return "JUN";
        }
        if(month==7){
            return "JUL";
        }
        if(month==8){
            return "AUG";
        }
        if(month==9){
            return "SEP";
        }
        if(month==10){
            return "OCT";
        }
        if(month==11){
            return "NOV";
        }
        if(month==12){
            return "DEC";
        }
        return "JAN";
    }

    public void popDatePicker(View view){
        datePickerDialog.show();
    }
}