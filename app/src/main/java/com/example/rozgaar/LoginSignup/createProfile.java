package com.example.rozgaar.LoginSignup;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import static com.karumi.dexter.listener.SnackbarUtils.show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rozgaar.MainScreen.FirstActivity;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class createProfile extends AppCompatActivity {

    CircleImageView profilePic;
    ImageView addPic;
    EditText name, email, number;
    Spinner dob_spinner;
    Uri myUri, cropped;

    Button create;
    DatabaseReference reference;

    FirebaseFirestore db;
    StorageReference myStorage = FirebaseStorage.getInstance().getReference("Profile_Pics");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String getTime, getEducation, getFcm, getNumberStatus;

    String education_list[] = {
            "Select Option", "5th Class", "8th Class", "10th Class", "12th Class", "Undergraduate", "Postgraduate"
    };
    String preEmail = user.getEmail();
    String preNumber = user.getPhoneNumber();

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String checkStatus = sp.getString("MyLoginStatus", "false");
        if (checkStatus.equals("true")) {
            startActivity(new Intent(createProfile.this, FirstActivity.class));
            finish();
        } else {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Wait...");
            dialog.setMessage("We are fetching your details.");
            dialog.setCancelable(false);
            dialog.show();
            getFCMToken();
            try {
                db.collection("Profile_Data").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                String userID = documentSnapshot.getString("user_uid");
                                if (Objects.equals(uid, userID)) {
                                    cropped = Uri.parse(documentSnapshot.getString("profileUri"));
                                    name.setText(documentSnapshot.getString("name"));
                                    number.setText(documentSnapshot.getString("mobile_number"));
                                    Glide.with(createProfile.this).load(cropped).into(profilePic);
                                    db.collection("Profile_Data").document(uid).update("user_FCM", getFcm);
                                    editor.putString("MyProfileUri", String.valueOf(cropped));
                                    editor.putString("MyName", name.getText().toString());
                                    editor.putString("MyEducation", getEducation);
                                    editor.putString("MyEmail", email.getText().toString());
                                    editor.putString("MyNumber", number.getText().toString());
                                    editor.putString("MyFcm", getFcm);
                                    editor.putString("MyUid", uid);
                                    editor.putString("MyLoginStatus", "true");
                                    editor.apply();
                                    dialog.dismiss();
                                    startActivity(new Intent(createProfile.this, FirstActivity.class));
                                    finish();
                                } else {
                                    dialog.dismiss();
                                }
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        profilePic = findViewById(R.id.add_profile_pic);
        addPic = findViewById(R.id.add_profile_pic_btn);
        name = findViewById(R.id.enter_your_name);
        email = findViewById(R.id.email_entered_by_user);
        dob_spinner = findViewById(R.id.select_occupation);
        create = findViewById(R.id.create_user_profile_btn);
        number = findViewById(R.id.number_entered_by_user);

        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, education_list);
        dob_spinner.setAdapter(a);

        SharedPreferences sp = getSharedPreferences("USER_TYPE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        getNumberStatus = sp.getString("getType", "false");

        dob_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        if (preEmail != null) {
            email.setText(preEmail);
            email.setEnabled(false);
        }

        if (getNumberStatus.equals("true")) {
            number.setText(preNumber);
            number.setEnabled(false);
        }

        getFCMToken();

        permissionCheck();

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (number.getText().toString().length() > 10) {
                    number.setError("Mobile number must be in 10 digits.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10555);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") && name.getText().toString().equals("") && number.getText().toString().equals("")) {
                    Toast.makeText(createProfile.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().equals("")) {
                    email.setError("Please enter your email");
                } else if (name.getText().toString().equals("")) {
                    name.setError("Please enter your name.");
                } else if (number.getText().toString().equals("")) {
                    number.setError("Please enter your number.");
                } else if (getEducation.equals("Select Option")) {
                    Toast.makeText(createProfile.this, "Please select an education option.", Toast.LENGTH_SHORT).show();
                } else if (number.getText().toString().length() > 10) {
                    number.setError("Mobile number must be in 10 digits.");
                }else if (!email.getText().toString().endsWith("@gmail.com")) {
                    email.setError("Email should be ends with @gmail.com");
                }  else {
                    if (cropped != null) {
                        imageToUri(cropped);
                    } else {
                        Toast.makeText(createProfile.this, "Please add Profile Photo.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10555 && resultCode == RESULT_OK) {
            myUri = data.getData();
            if (myUri != null) {
                startCrop(myUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            cropped = UCrop.getOutput(data);
            if (cropped != null) {
                profilePic.setImageURI(cropped);
            }
        }
    }

    public void startCrop(Uri uri) {
        String destinationFileName = "RozgaarCropImage";
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(1000, 1000);
        uCrop.withOptions(getCropOptions());
        uCrop.start(createProfile.this);

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

    public void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 33) {
            Dexter.withContext(this).withPermissions(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).onSameThread().check();
        } else {
            Dexter.withContext(this).withPermissions(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).onSameThread().check();
        }
    }

    public void imageToUri(Uri uri) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dd = LocalDateTime.now();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            getTime = f.format(dd);
        }
        StorageReference fileRef = myStorage.child(System.currentTimeMillis() + ".jpg");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(createProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        ProfileModel model = new ProfileModel(String.valueOf(uri), name.getText().toString(), getEducation, email.getText().toString(), number.getText().toString(), getFcm, uid);
                        db.collection("Profile_Data").document(uid).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("MyProfileUri", String.valueOf(uri));
                                editor.putString("MyName", name.getText().toString());
                                editor.putString("MyEducation", getEducation);
                                editor.putString("MyEmail", email.getText().toString());
                                editor.putString("MyNumber", number.getText().toString());
                                editor.putString("MyFcm", getFcm);
                                editor.putString("MyUid", uid);
                                editor.putString("MyLoginStatus", "true");
                                editor.apply();

                                progressDialog.dismiss();
                                startActivity(new Intent(createProfile.this, FirstActivity.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createProfile.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
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

    public void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                getFcm = task.getResult();
            }
        });
    }

}