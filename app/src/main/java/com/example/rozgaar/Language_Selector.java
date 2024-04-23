package com.example.rozgaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rozgaar.LoginSignup.signup;

import java.util.Locale;
import java.util.Objects;

public class Language_Selector extends AppCompatActivity {

    String[] arr = {"Select", "English", "हिंदी", "اردو","ગુજરાતી"};
    Spinner spinner;
    String selectedLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);
        spinner = findViewById(R.id.select_lan_spinner);


        checkIfSelected();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLang = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                if (Objects.equals(selectedLang, "English")) {
                    setLocale("en");
                } else if (Objects.equals(selectedLang, "हिंदी")) {
                    setLocale("hi");
                } else if (Objects.equals(selectedLang, "اردو")) {
                    setLocale("ur");
                }
                else if (Objects.equals(selectedLang,"ગુજરાતી")){
                    setLocale("gu");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setLocale(String language) {
        SharedPreferences sp = getSharedPreferences("LANGUAGE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        editor.putString("lang", language);
        editor.apply();
        startActivity(new Intent(Language_Selector.this, signup.class));
        finish();
    }

    private void checkIfSelected() {
        SharedPreferences sp = getSharedPreferences("LANGUAGE", MODE_PRIVATE);
        setLocale(sp.getString("lang", ""));
    }
}