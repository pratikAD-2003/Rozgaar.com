package com.example.rozgaar;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Struct;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link selectLangFromSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class selectLangFromSettings extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinner;
    Button confirmLang;
    String selectedLang;
    String[] arr = {"Select", "English", "हिंदी", "اردو", "ગુજરાતી"};

    public selectLangFromSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment selectLangFromSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static selectLangFromSettings newInstance(String param1, String param2) {
        selectLangFromSettings fragment = new selectLangFromSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_lang_from_settings, container, false);
        spinner = view.findViewById(R.id.lang_setting_spinner);
        confirmLang = view.findViewById(R.id.confirm_change_lag_btton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arr);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLang = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                confirmLang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Objects.equals(selectedLang, "English")) {
                            setLocale("en");
                        } else if (Objects.equals(selectedLang, "हिंदी")) {
                            setLocale("hi");
                        } else if (Objects.equals(selectedLang, "اردو")) {
                            setLocale("ur");
                        } else if (Objects.equals(selectedLang, "ગુજરાતી")) {
                            setLocale("gu");
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;

    }

    private void setLocale(String language) {
        SharedPreferences sp = getContext().getSharedPreferences("LANGUAGE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;

        getActivity().getBaseContext().getResources().updateConfiguration(configuration, getActivity().getBaseContext().getResources().getDisplayMetrics());
        editor.putString("lang", language);
        editor.apply();
        getActivity().recreate();
        dismiss();
    }
}