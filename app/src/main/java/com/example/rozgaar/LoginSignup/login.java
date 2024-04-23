package com.example.rozgaar.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText getEmail;
    TextInputEditText getPass;
    TextView createNew, forgetPass;
    Button login;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getEmail = findViewById(R.id.get_email);
        getPass = findViewById(R.id.getPassword);
        createNew = findViewById(R.id.create_new_acc_text_btn);
        forgetPass = findViewById(R.id.forget_pass_with_email);
        login = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getEmail.getText().toString();
                String pass = getPass.getText().toString();
                if (email.equals("") && pass.equals("")) {
                    Toast.makeText(login.this, "Please fill required fields.", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    getEmail.setError("Please enter your email.");
                } else if (pass.equals("")) {
                    getPass.setError("Please enter your password.");
                } else {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getEmail.setText("");
                                getPass.setText("");
                                SharedPreferences sp = getSharedPreferences("USER_TYPE", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("getType", "false");
                                editor.apply();
                                Toast.makeText(login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(login.this, createProfile.class));
                                finish();
                            } else {
                                Toast.makeText(login.this, "email or password not matched.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.rozgaar.LoginSignup.login.this, signup.class));
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.rozgaar.LoginSignup.login.this, forgetPass_with_email.class));
            }
        });

    }
}