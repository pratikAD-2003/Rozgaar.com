package com.example.rozgaar;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.rozgaar.LoginSignup.login;
import com.example.rozgaar.LoginSignup.signup;
import com.google.firebase.auth.FirebaseAuth;

public class settings extends AppCompatActivity {

    LinearLayout logout, changeLanguage, share;
    ImageView backBtn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logout = findViewById(R.id.logoutBtn);
        changeLanguage = findViewById(R.id.changeLanguage_btn);
        backBtn = findViewById(R.id.back_from_settings);
        share = findViewById(R.id.invite_friend);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                builder.setTitle("Are you sure ?");
                builder.setMessage("Logout.");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                if (firebaseAuth.getCurrentUser() == null) {
                                    editor.putString("MyLoginStatus", "false");
                                    editor.apply();
                                    startActivity(new Intent(settings.this, signup.class));
                                } else {
                                }
                            }
                        };

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.addAuthStateListener(authStateListener);
                        firebaseAuth.signOut();
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLangFromSettings selectLangFromSettings = new selectLangFromSettings();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    selectLangFromSettings.show(getSupportFragmentManager(), getAttributionTag());
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "hello this is only for testing perpose");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_to_share)));
            }
        });
    }
}