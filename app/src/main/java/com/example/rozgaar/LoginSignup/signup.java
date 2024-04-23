package com.example.rozgaar.LoginSignup;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rozgaar.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class signup extends AppCompatActivity {

    EditText email, password, cPassword;
    Button create;
    TextView alreadyHave;
    FirebaseAuth mAuth;
    ImageView google, number;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(signup.this, createProfile.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.enter_email);
        password = findViewById(R.id.enter_pass);
        cPassword = findViewById(R.id.enter_confirm_pass);
        create = findViewById(R.id.create_acc_with_emailPass);
        alreadyHave = findViewById(R.id.alreadyHaveAccWithEmailPass);
        google = findViewById(R.id.create_acc_with_google);
        number = findViewById(R.id.create_acc_with_number);

        mAuth = FirebaseAuth.getInstance();

        processREQUEST();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!email.getText().toString().endsWith("@gmail.com")) {
                    email.setError("Email should be ends with @gmail.com");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().length() < 8) {
                    password.setError("Password length should be more than 8 characters.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!password.getText().toString().equals(cPassword.getText().toString())) {
                    cPassword.setError("Password should be match.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                String getPass = password.getText().toString().trim();
                String getCpass = cPassword.getText().toString().trim();
                if (getCpass.equals("") && getPass.equals("") && getEmail.equals("")) {
                    Toast.makeText(signup.this, "Please filled all required details.", Toast.LENGTH_SHORT).show();
                } else if (getCpass.equals("") && getPass.equals("")) {
                    Toast.makeText(signup.this, "Please filled all required details.", Toast.LENGTH_SHORT).show();
                } else if (getPass.equals("") && getEmail.equals("")) {
                    Toast.makeText(signup.this, "Please filled all required details.", Toast.LENGTH_SHORT).show();
                } else if (getCpass.equals("") && getEmail.equals("")) {
                    Toast.makeText(signup.this, "Please filled all required details.", Toast.LENGTH_SHORT).show();
                } else if (getPass.equals("")) {
                    password.setError("Please enter password.");
                } else if (getEmail.equals("")) {
                    email.setError("Please enter email address.");
                } else if (getCpass.equals("")) {
                    cPassword.setError("Please enter confirm password.");
                } else if (!getEmail.endsWith("@gmail.com")) {
                    Toast.makeText(signup.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        mAuth.createUserWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    email.setText("");
                                    password.setText("");
                                    cPassword.setText("");
                                    startActivity(new Intent(signup.this, login.class));
                                    finish();
                                } else {
                                    Toast.makeText(signup.this, "Failed at this moment.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                    ;
                }
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();
            }
        });


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login_with_otp.class));
            }
        });

        alreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this,login.class));
            }
        });
    }

    private void loginProcess() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, 1001);
    }

    private void processREQUEST() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Error Getting Google Info", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(signup.this, "Google logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signup.this, createProfile.class));
                    finish();
                } else {
                    Toast.makeText(signup.this, "Failed To login...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}