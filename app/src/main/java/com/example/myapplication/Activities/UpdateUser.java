package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class UpdateUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button updateUser;
    private EditText description;
    private EditText seniority;
    private ImageView imageProfile;
    private Button changeImage;
    private EditText phone;
    private EditText location;
    private Spinner profession;

    String spinnerTxt = "lala";

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateUser = findViewById(R.id.updateUser);
        description = findViewById(R.id.description);
        seniority = findViewById(R.id.seniority);
        profession = findViewById(R.id.profession);
        changeImage = findViewById(R.id.changeImage);
        imageProfile = findViewById(R.id.imageProfile);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("email").equalTo(LoginUser.getLoginEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    databaseReference.child(key);
                    description.setText(String.valueOf(snapshot.child(key).child("description").getValue()));

                    String prof = String.valueOf(snapshot.child(key).child("profession").getValue());

                    if(prof.equals("Not entered")){
                        profession.setSelection(0);
                    }

                    if(prof.equals("Mechanic")){
                        profession.setSelection(1);
                    }

                    if(prof.equals("Renovator")){
                        profession.setSelection(2);
                    }

                    if(prof.equals("Plumber")){
                        profession.setSelection(3);
                    }

                    if(prof.equals("Painter")){
                        profession.setSelection(4);
                    }

                    phone.setText(String.valueOf(snapshot.child(key).child("phone").getValue()));
                    seniority.setText(String.valueOf(snapshot.child(key).child("seniority").getValue()));
                    location.setText(String.valueOf(snapshot.child(key).child("location").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.profession, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(adapter);
        profession.setOnItemSelectedListener(this);

        //Get image and put in the image prfile
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+LoginUser.getLoginEmail()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfile);
            }
        });

        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_description = description.getText().toString();
                String txt_seniority = seniority.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_location = location.getText().toString();

                if(!txt_location.equals("")){
                    Location loc = getLocationForAddress(UpdateUser.this, txt_location);
                    if(loc == null){
                        Toast.makeText(UpdateUser.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if(txt_description.equals("") || txt_seniority.equals("")|| txt_phone.equals("")){
                            Toast.makeText(UpdateUser.this, "Details are missing!", Toast.LENGTH_SHORT).show();
                        }else{
                            updateUserData(txt_description, txt_seniority, spinnerTxt, txt_phone, txt_location);
                            Toast.makeText(UpdateUser.this, "Details saved!", Toast.LENGTH_SHORT).show();
                        }
                }
                }else{
                    Toast.makeText(UpdateUser.this, "Details are missing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Uri imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
            uploadImageForFirebase(imageUri);
        }
    }

    private void uploadImageForFirebase(Uri imageUri){
        StorageReference fileRef = storageReference.child("users/"+LoginUser.getLoginEmail()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        updateImageUri(uri);
                        Picasso.get().load(uri).into(imageProfile);
                        Toast.makeText(UpdateUser.this, "image sucssed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUser.this, "image failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(String description, String seniority, String profession, String phone, String location) {
        HashMap User = new HashMap();
        User.put("description", description);
        User.put("seniority", seniority);
        User.put("profession", profession);
        User.put("phone", phone);
        User.put("location", location);

        Location loc = getLocationForAddress(UpdateUser.this, location);
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();

        User.put("latitude", latitude);
        User.put("longitude", longitude);

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

    private void updateImageUri(Uri uri) {
        String stringUri = uri.toString();
        HashMap User = new HashMap();
        User.put("uriImage", stringUri);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        spinnerTxt = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public Location getLocationForAddress (Context context, String address){
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        Location loc = new Location("getLocationForAddress");;
        try {
            addresses = geocoder.getFromLocationName(address, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() == 1) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            return loc;
        }
        else
            return null;
    }
}

