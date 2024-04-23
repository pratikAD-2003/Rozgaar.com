package com.example.rozgaar.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPass_with_email extends AppCompatActivity {

    EditText getEmail;
    Button forgetPass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_with_email);
        getEmail = findViewById(R.id.get_forget_email);
        forgetPass = findViewById(R.id.forget_pass_btn);

        mAuth = FirebaseAuth.getInstance();
        getEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!getEmail.getText().toString().endsWith("@gmail.com")) {
                    getEmail.setError("Email should be ends with @gmail.com");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEmail.getText().toString().equals("")) {
                    getEmail.setError("Please enter your email.");
                } else if (!getEmail.getText().toString().endsWith("@gmail.com")) {
                    getEmail.setError("Email should be ends with @gmail.com");
                } else {
                    mAuth.sendPasswordResetEmail(getEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(forgetPass_with_email.this, "Check your email for password recovery.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgetPass_with_email.this, login.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(forgetPass_with_email.this, "Facing Technical Issue...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}