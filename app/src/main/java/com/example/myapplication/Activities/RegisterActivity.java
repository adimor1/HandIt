package com.example.myapplication.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button registerBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private EditText firstName;
    private EditText lastName;
    Switch isProf;
    TextView IsProfText;
    String txt_firstName, txt_lastName, txt_email, txt_password;
    Boolean isProfS;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        isProf = findViewById(R.id.isProf);
        IsProfText = findViewById(R.id.profText);
        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 txt_firstName = firstName.getText().toString();
                 txt_lastName = lastName.getText().toString();
                 txt_email = email.getText().toString();
                 txt_password = password.getText().toString();
                 isProfS = isProf.isChecked();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty((txt_password))) {
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() <6){
                    Toast.makeText(RegisterActivity.this, "Short Password", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password);
                }
            }
        });

        isProf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isProf.isChecked()){
                    IsProfText.setText("I'm Professional");
                } else {
                    IsProfText.setText("I'm Not Professional");
                }
            }
        });
    }

    private void addUser(String firstName, String lastName, String email, boolean isProf){
        User users= new User(firstName, lastName, email, isProf);
        db= FirebaseDatabase.getInstance();
        String key = db.getReference("Users").push().getKey();
        reference= db.getReference("Users");
        reference.child(key).setValue(users);
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Register User Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    addUser(txt_firstName, txt_lastName, txt_email, isProfS);
                    LoginUser.setLoginEmail(txt_email);
                } else {
                    Toast.makeText(RegisterActivity.this, "Register User Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}






