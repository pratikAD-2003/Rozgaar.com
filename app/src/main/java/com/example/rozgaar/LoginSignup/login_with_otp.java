package com.example.rozgaar.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login_with_otp extends AppCompatActivity {

    EditText getNumber, o1, o2, o3, o4, o5, o6;
    Button getOTP, login;
    TextView resendOTP, otherWays, showNumber, mobileNum_text;
    String otp;
    FirebaseAuth mAuth;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        getNumber = findViewById(R.id.enter_mobile_number);
        getOTP = findViewById(R.id.get_otp);
        login = findViewById(R.id.login_btn_with_number);
        resendOTP = findViewById(R.id.resend_otp_login);
        otherWays = findViewById(R.id.other_ways_to_login);
        o1 = findViewById(R.id.one_otp);
        o2 = findViewById(R.id.two_otp);
        o3 = findViewById(R.id.three_otp);
        o4 = findViewById(R.id.four_otp);
        o5 = findViewById(R.id.five_otp);
        o6 = findViewById(R.id.six_otp);
        showNumber = findViewById(R.id.show_mobile_number);
        layout = findViewById(R.id.otp_layout);
        mobileNum_text = findViewById(R.id.dfaufj);
        mAuth = FirebaseAuth.getInstance();


        changeCursorNext(o1, o2);
        changeCursorNext(o2, o3);
        changeCursorNext(o3, o4);
        changeCursorNext(o4, o5);
        changeCursorNext(o5, o6);

        getNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getNumber.getText().toString().length() > 10) {
                    getNumber.setError("Mobile number must be in 10 digits.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_OTP();
                showNumber.setText("OTP Sent on +91" + getNumber.getText().toString());
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_OTP();
            }
        });

        otherWays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_with_otp.this, signup.class));
                finish();
            }
        });
    }

    public void get_OTP() {
        String number = getNumber.getText().toString().trim();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+91" + number).setTimeout(30L, TimeUnit.SECONDS).setActivity(login_with_otp.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInUser(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(login_with_otp.this, "OTP Sent!", Toast.LENGTH_SHORT).show();
                mobileNum_text.setVisibility(View.INVISIBLE);
                getNumber.setVisibility(View.INVISIBLE);
                getOTP.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resendOTP.setVisibility(View.VISIBLE);
                    }
                }, 30000);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otp = (o1.getText().toString() + o2.getText().toString() + o3.getText().toString() + o4.getText().toString() + o5.getText().toString() + o6.getText().toString());
                        if (s.isEmpty()) {
                            return;
                        }
                        if (otp.equals("")) {
                            Toast.makeText(login_with_otp.this, "Please Enter Sent OTP!", Toast.LENGTH_SHORT).show();
                        } else if (otp.length() != 6) {
                            Toast.makeText(login_with_otp.this, "Please Enter Valid OTP!", Toast.LENGTH_SHORT).show();
                        } else {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, otp);
                            signInUser(credential);
                        }

                    }
                });
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(login_with_otp.this, "Failed to Sent OTP!", Toast.LENGTH_SHORT).show();
            }

        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void changeCursorNext(EditText editText, EditText editText2) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().length() == 1) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    SharedPreferences sp = getSharedPreferences("USER_TYPE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("getType", "true");
                    editor.apply();
                    Toast.makeText(login_with_otp.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login_with_otp.this, createProfile.class));
                    finish();
                } else {
                    Toast.makeText(login_with_otp.this, "Please Try Some Time Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}