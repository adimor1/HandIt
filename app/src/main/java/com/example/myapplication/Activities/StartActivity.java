package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Models.LoginUser;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {

    private Button register;
    private Button login;
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register= findViewById(R.id.register);
        login= findViewById(R.id.login);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        //loginBtn = findViewById(R.id.loginBtn);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if(txt_email.equals("") || txt_password.equals("")){
                    Toast.makeText(StartActivity.this, "Missing password or email", Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(txt_email, txt_password);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }


    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    LoginUser.setLoginEmail(email);
                }
                else {
                    Toast.makeText(StartActivity.this, "Incorrect password or email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}