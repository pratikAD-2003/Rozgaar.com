package com.example.rozgaar.MainScreen;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rozgaar.LoginSignup.ProfileModel;
import com.example.rozgaar.LoginSignup.createProfile;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUpdate extends BottomSheetDialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CircleImageView updatedPic;
    ImageView addUpdatedBtn;
    EditText updatedName, updatedEmail, updatedNumber;
    Spinner education_updated_spinner;
    Button updateBtn;
    String userName, email, number, profileUri, education, getNumberStatus, getEducation, getTime, getFcm;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    FirebaseFirestore db;
    StorageReference myStorage = FirebaseStorage.getInstance().getReference("Profile_Pics");
    String education_list[] = {"Select Option", "5th Class", "8th Class", "10th Class", "12th Class", "Undergraduate", "Postgraduate"};
    String preEmail = user.getEmail();
    String preNumber = user.getPhoneNumber();
    Uri cropped, myUri, newUri = null;

    public ProfileUpdate() {

    }

    public static ProfileUpdate newInstance(String param1, String param2) {
        ProfileUpdate fragment = new ProfileUpdate();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);
        updatedPic = view.findViewById(R.id.updated_profile_pic);
        addUpdatedBtn = view.findViewById(R.id.updated_profile_pic_btn);
        updatedName = view.findViewById(R.id.enter_your_updated_name);
        updatedEmail = view.findViewById(R.id.updated_email);
        updatedNumber = view.findViewById(R.id.updated_mobile_number);
        education_updated_spinner = view.findViewById(R.id.select_occupation_updated);
        updateBtn = view.findViewById(R.id.update_btn);

        db = FirebaseFirestore.getInstance();

        ArrayAdapter<String> a = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, education_list);
        education_updated_spinner.setAdapter(a);


        SharedPreferences sp = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        userName = sp.getString("MyName", "not");
        education = sp.getString("MyEducation", "not");
        email = sp.getString("MyEmail", "not");
        number = sp.getString("MyNumber", "not");
        getNumberStatus = sp.getString("getType", "false");
        getFcm = sp.getString("MyFcm", "not");

        updatedName.setText(userName);
        updatedEmail.setText(email);
        Glide.with(this).load(sp.getString("MyProfileUri", "not")).into(updatedPic);
        updatedNumber.setText(number);
        education_updated_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getEducation = adapterView.getItemAtPosition(position).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(16);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (preEmail != null) {
            updatedEmail.setText(preEmail);
            updatedEmail.setEnabled(false);
        }

        if (getNumberStatus.equals("true")) {
            updatedNumber.setText(preNumber);
            updatedNumber.setEnabled(false);
        }

        updatedNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (updatedNumber.getText().toString().length() > 10) {
                    updatedNumber.setError("Mobile number must be in 10 digits.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        updatedEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!updatedEmail.getText().toString().endsWith("@gmail.com")) {
                    updatedEmail.setError("Email should be ends with @gmail.com");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addUpdatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 124421);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatedEmail.getText().toString().equals("") && updatedName.getText().toString().equals("") && updatedNumber.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                } else if (updatedEmail.getText().toString().equals("")) {
                    updatedEmail.setError("Please enter your email");
                } else if (updatedName.getText().toString().equals("")) {
                    updatedName.setError("Please enter your name.");
                } else if (updatedNumber.getText().toString().equals("")) {
                    updatedNumber.setError("Please enter your number.");
                } else if (getEducation.equals("Select Option")) {
                    Toast.makeText(getContext(), "Please select an education option.", Toast.LENGTH_SHORT).show();
                } else if (updatedNumber.getText().toString().length() > 10) {
                    updatedNumber.setError("Mobile number must be in 10 digits.");
                } else if (!updatedEmail.getText().toString().endsWith("@gmail.com")) {
                    updatedEmail.setError("Email should be ends with @gmail.com");
                } else {
                    imageToUri();
                }
            }
        });
        return view;
    }


    public void startCrop(Uri uri) {
        String destinationFileName = "RozgaarCropImage";
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(1000, 1000);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getActivity(), ProfileUpdate.this);

    }


    public UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setMaxBitmapSize(10000);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        options.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
        options.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_dark_default_color_primary));

        options.setToolbarTitle("Cropped Image");

        return options;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 124421 && resultCode == RESULT_OK) {
            myUri = data.getData();
            if (myUri != null) {
                startCrop(myUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            newUri = UCrop.getOutput(data);
            if (newUri != null) {
                updatedPic.setImageURI(newUri);
//                Glide.with(this).load(newUri).into(updatedPic);
            }
        }
    }


    public void imageToUri() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating Profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (newUri == null) {
            SharedPreferences sp = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
            profileUri = sp.getString("MyProfileUri", "not");
            cropped = Uri.parse(profileUri);

            SharedPreferences.Editor editor = sp.edit();

            ProfileModel model = new ProfileModel(String.valueOf(cropped), updatedName.getText().toString(), getEducation, updatedEmail.getText().toString(), updatedNumber.getText().toString(), getFcm, uid);
            db.collection("Profile_Data").document(uid).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    editor.putString("MyProfileUri", String.valueOf(cropped));
                    editor.putString("MyName", updatedName.getText().toString());
                    editor.putString("MyEducation", getEducation);
                    editor.putString("MyEmail", updatedEmail.getText().toString());
                    editor.putString("MyNumber", updatedNumber.getText().toString());
                    editor.putString("MyFcm", getFcm);
                    editor.putString("MyUid", uid);
                    editor.putString("MyLoginStatus", "true");
                    editor.apply();
                    progressDialog.dismiss();
                    dismiss();
                }
            });
        } else {
            StorageReference fileRef = myStorage.child(System.currentTimeMillis() + ".jpg");
            fileRef.putFile(newUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            ProfileModel model = new ProfileModel(String.valueOf(uri), updatedName.getText().toString(), getEducation, updatedEmail.getText().toString(), updatedNumber.getText().toString(), getFcm, uid);
                            db.collection("Profile_Data").document(uid).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SharedPreferences sp = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("MyProfileUri", String.valueOf(uri));
                                    editor.putString("MyName", updatedName.getText().toString());
                                    editor.putString("MyEducation", getEducation);
                                    editor.putString("MyEmail", updatedEmail.getText().toString());
                                    editor.putString("MyNumber", updatedNumber.getText().toString());
                                    editor.putString("MyFcm", getFcm);
                                    editor.putString("MyUid", uid);
                                    editor.putString("MyLoginStatus", "true");
                                    editor.apply();
                                    progressDialog.dismiss();
                                    dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Please Try Again Later!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float per = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded : " + (int) per + "%");
                }
            });

        }
    }
}