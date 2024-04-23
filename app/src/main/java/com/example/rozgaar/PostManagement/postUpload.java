package com.example.rozgaar.PostManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class postUpload extends AppCompatActivity {

    postAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Uri> list = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    EditText workTitle, workDescription;
    Spinner cities_spinner, state_spinner;
    TextView titleLength, desLength;
    Button uploadWork;
    FirebaseFirestore db;
    StorageReference reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String getTime, pushId, getCity, getStatus;
    List<String> stringList = new ArrayList<>();
    boolean checkFinish = false;
    String[] dummy = {"Select City"};
    String[] states = {"Select States", "Madhya Pradesh", "Uttar Pradesh", "Gujarat", "Maharashtra"};
    String[] cities_MP = {"Select City", " Agar Malwa ", " Alirajpur ", " Anuppur ", " Ashoknagar ", " Babai ", " Badagaon ", " Badnawar ", " Badwani ", " Bagli ", " Bagratawa ", " Bamanwas ", " Bankhedi ", " Bareli ", " Barkheda ", " Barwaha ", " Barwani Kalan ", " Batiagarh ", " Bela Ghat ", " Bhainsdehi ", " Bhander ", " Bhatauli ", " Bhavkhedi ", " Bhikampur ", " Bijawar ", " Bilheri ", " Birsinghpur ", " Budni ", " Burhar ", " Burhanpur ", " Chanderi ", " Chhapara ", " Chhatarpur ", " Dabra ", " Deori ", " Gadarwara ", " Gohad ", " Harda ", " Hatta ", " Itarsi ", " Jaithari ", " Jaisinghnagar ", " Jawad ", " Jhalon ", " Jharkheda ", " Kakadkheri ", " Kachhar ", " Kala Pipal ", " Chandrakhuri ", " Chichiwara ", " Chilkahar ", " Chiraidongri ", " Dattigaon ", " Devkund ", " Dhamnod ", " Dhamoni ", " Ghoradongri ", " Gyanpura ", " Hardu Khurd ", " Jagatpur ", " Jhalon ", " Jharkheda ", " Kakadkheri ", " Kachhar ", " Kala Pipal ", " Agar Malwa ", " Alirajpur ", " Anuppur ", " Ashoknagar ", " Balaghat ", " Betul ", " Bhind ", " Bhopal ", " Burhanpur ", " Chhindwara ", " Damoh ", " Datia ", " Dewas ", " Dhar ", " Dindori ", " Guna ", " Gwalior ", " Hoshangabad ", " Indore ", " Jabalpur ", " Jhabua ", " Katni ", " Khandwa ", " Khargone ", " Mandla ", " Mandsaur ", " Morena ", " Narsinghpur ", " Neemuch ", " Panna ", " Raisen ", " Rajgarh ", " Ratlam ", " Rewa ", " Sagar ", " Satna ", " Sehore ", " Seoni ", " Shahdol ", " Shajapur ", " Sheopur ", " Shivpuri ", " Sidhi ", " Singrauli ", " Tikamgarh ", " Ujjain ", " Umaria ", " Vidisha "};
    String[] cities_UP = {"Select City", "Achhnera", "Achnera", "Agra", "Ahmadnagar", "Ahraura", "Ajitmal", "Akbarpur", "Aliganj", "Aligarh", "Allahabad", "Prayagraj", "Amethi", "Amroha", "Jyotiba Phule Nagar", "Aonla", "Arail Uparhar", "Ashoknagar", "Auraiya", "Ayodhya", "Faizabad", "Babarpur", "Babatpur", "Baberu", "Babina", "Bachhrawan", "Badaun", "Baghpat", "Bah", "Bahadurganj", "Baheri", "Bahjoi", "Bahraich", "Bairagnia", "Bairia", "Bakewar", "Baldeo", "Balrampur", "Banda", "Bangarmau", "Banipur", "Bansdih", "Barabanki", "Baragaon", "Baraut", "Bareilly", "Barha Bazar", "Barhalganj", "Baria", "Barla", "Barnahal", "Barra", "Barwar", "Basantpur", "Basgaon", "Basrehar", "Basti", "Bathua", "Behat", "Behror", "Bela Pratapgarh", "Belthara Road", "Beniganj", "Benti", "Bewar", "Bhadarsa", "Bhadaura", "Bhadohi", "Sant Ravidas Nagar", "Bhagwantnagar", "Bhanera", "Bhanpur", "Bharawan", "Bharthana", "Bhatni Bazar", "Bhatpura", "Bhatpurwa", "Bhatua", "Bhaupur", "Bheera", "Bhera", "Bhetia", "Bhikampur", "Bhikharipur", "Bhinga", "Bhognipur", "Bhogaon", "Bhongaon", "Bidhuna", "Sirsaganj", "Bhowali", "Bhusaula", "Bilariaganj", "Bilaspur", "Bilgram", "Bilhaur", "Bilram", "Bilsanda", "Bindki", "Bisalpur", "Bisanda Buzurg", "Bisauli", "Bisharatganj", "Biswan", "Bithur", "Budaun", "Budhana", "Bugrasi", "Bukhara", "Bulandshahr", "Burhanpur", "Chail", "Chak Imam Ali", "Chak Sikandar", "Chandauli", "Chandpur", "Charkhari", "Chaudagarh", "Chaumukha", "Chaurakhera", "Raja Ka Rampur", "Chhata", "Chhatari", "Chhatnag", "Chhibramau", "Chhutmalpur", "Chillupar", "Chilmau", "Chiraigaon", "Chiraiyakot", "Chitrakoot", "Chitrakoot Dham", "Karwi", "Chobepur", "Chopan", "Chunar", "Churk Ghurma", "Churk Rani", "Churiyamau", "Churk", "Naugarh", "Dadri", "Dariyabad", "Dasna", "Dataganj", "Daurala", "Deoband", "Nagar", "Deogan", "Deoria", "Derapur", "Dhanghat", "Dhaurahra", "Nawabganj", "Suar", "Tikait Nagar", "Tikri", "Utraula", "Dhobi", "Dhodpur", "Kumbhi", "Dhoom Manikpur", "Dibai", "Anupshahr", "Didhaur", "Didhua", "Dig", "Digod", "Dilari", "Dipauli", "Dipalsui", "Dipura", "Domariaganj", "Dudhi", "Dudhnai", "Dudhaur", "Dullahapur", "Dulhipur", "Meja", "Dullapur", "Dumariaganj", "Dungarpur", "Durgapur", "Dwarahat", "Dwarkaganj", "Ekauna", "Ekdil", "Indergarh", "Naraini", "Pailani", "Pura Janakpur", "Tirwa", "Ugu", "Fatehpur Sikri", "Robertsganj", "Mughalsarai", "Rasra", "Siswa Bazar", "Pihani", "Sirsi", "Mihinpurwa", "Gaura Barhaj", "Saidpur", "Belhar", "Paraspur", "Kusmara"};
    String[] cities_Maharashtra = {"Select City", "Aambali", "Aheri", "Ahmednagar", "Airoli", "Akluj", "Akola", "Akole", "Akot", "Alandi", "Alandi", "Alibag", "Amalner", "Ambajogai", "Ambaval", "Ambivali", "Amravati", "Anandnagar", "Anjangaon", "Arangaon", "Arjuni", "Arvi", "Asangaon", "Ashti", "Atali", "Aundh", "Babhaleshwar", "Babhulgaon", "Bajala", "Ballarpur", "Baramati", "Barshi", "Beed", "Bhadgaon", "Bhandara", "Bhayander", "Bhiwandi", "Bhokardan", "Bhoom", "Bhudargad", "Bid", "Bijaipur", "Biloli", "Binhur", "Birwadi", "Bodwad", "Boisar", "Borgaon", "Bramhapuri", "Bubnal", "Chalisgaon", "Chandrapur", "Chandwad", "Chikhaldara", "Chinchani", "Chopda", "Dahanu", "Dapoli", "Daryapur", "Deolali", "Deulgaon Raja", "Dharmabad", "Dharni", "Digras", "Dindori", "Dondaicha", "Gadhinglaj", "Gadchiroli", "Gangakhed", "Georai", "Ghatanji", "Ghodegaon", "Hinganghat", "Hingoli", "Ichalkaranji", "Jalgaon", "Jalna", "Kalyan-Dombivli", "Kamthi", "Karad", "Karmala", "Katol", "Khamgaon", "Khopoli", "Kolhapur", "Kopargaon", "Latur", "Lonavla", "Malegaon", "Malkapur", "Manmad", "Miraj", "Mumbai", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Navi Mumbai", "Osmanabad", "Palghar", "Pandharpur", "Panvel", "Parbhani", "Pimpri-Chinchwad", "Pusad", "Raigad", "Rajapur", "Ratnagiri", "Sangamner", "Sangli", "Satara", "Shegaon", "Shirdi", "Shirpur-Warwade", "Solapur", "Talegaon Dabhade", "Thane", "Udgir", "Ulhasnagar", "Vasai-Virar", "Velhe", "Vita", "Wai", "Wardha", "Washim", "Yavatmal", "Yawal", "Yedshi", "Yeola", "Yeotmal"};
    String[] cities_gujarat = {"Select City", "Adalaj", "Adipur", "Ahmedabad", "Ahwa", "Ambaji", "Anand", "Anjar", "Anklav", "Babra", "Bantva", "Barwala", "Bharuch", "Bhachau", "Bhadran", "Bhanvad", "Bharuch", "Bhavnagar", "Bhuj", "Botad", "Borsad", "Chala", "Champaner", "Chhala", "Chorvad", "Dabhoi", "Dahegam", "Dakor", "Daman", "Dandi", "Dahod", "Daman", "Devgadh Baria", "Dhanera", "Dharampur", "Dhari", "Dhrol", "Dholavira", "Dholka", "Dhoraji", "Dhrol", "Diu", "Dwarka", "Gadhada", "Gandevi", "Gandhidham", "Gandhinagar", "Gariadhar", "Garbada", "Ghogha", "Godhra", "Halol", "Halvad", "Himatnagar", "Idar", "Jambusar", "Jam Jodhpur", "Jamnagar", "Jasdan", "Jetpur", "Jetpur Navagadh", "Junagadh", "Kadi", "Kamrej", "Kandla", "Kapadvanj", "Karamsad", "Keshod", "Khambhat", "Kheralu", "Kundla", "Kutch", "Lalpur", "Lathi", "Limdi", "Lunawada", "Mahuva", "Mahemdabad", "Mahuva", "Mandvi", "Manavadar", "Mangrol", "Mehsana", "Meghraj", "Modasa", "Morbi", "Mundra", "Nadiad", "Nakhatrana", "Nalsarovar", "Nandod", "Narayan Sarovar", "Narmada", "Nasvadi", "Navagam", "Navsari", "Nizar", "Okha", "Palanpur", "Palitana", "Pardi", "Patan", "Pavagadh", "Petlad", "Porbandar", "Radhanpur", "Rajkot", "Rajpipla", "Rajula", "Rann of Kutch", "Sanand", "Sankheda", "Saputara", "Sasan Gir", "Savarkundla", "Shamlaji", "Sidhpur", "Siddhpur", "Sihor", "Sojitra", "Songadh", "Surat", "Surendranagar", "Talaja", "Tharad", "Thol", "Una", "Umreth", "Upleta", "Valsad", "Vadnagar", "Vadodara", "Vaghodia", "Valia", "Vallabhipur", "Valsad", "Vapi", "Vaso", "Vav", "Vejalpur", "Veraval", "Vijalpor", "Vijapur", "Vithal Udyognagar", "Vyara", "Wadhwan", "Wankaner"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        recyclerView = findViewById(R.id.post_recyclerview);
        workTitle = findViewById(R.id.enter_work_titile);
        workDescription = findViewById(R.id.enter_work_des);
        state_spinner = findViewById(R.id.enter_work_location);
        cities_spinner = findViewById(R.id.enter_work_city);
        titleLength = findViewById(R.id.title_length);
        desLength = findViewById(R.id.description_length);
        uploadWork = findViewById(R.id.upload_work);

        db = FirebaseFirestore.getInstance();

        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select images related to your work"), 1055552);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new postAdapter(this, list);
        recyclerView.setAdapter(adapter);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dd = LocalDateTime.now();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            getTime = f.format(dd);
        }

        workTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int l = workTitle.length();
//                titleLength.setText("max size - " + (200 - l) + " left words");
                titleLength.setText(String.valueOf(200 - l));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        workDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int l = workDescription.length();
//                desLength.setText("max size - " + (500 - l) + " left words");
                desLength.setText(String.valueOf(500 - l));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArrayAdapter<String> MP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state_spinner.setAdapter(MP);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getStatus = adapterView.getItemAtPosition(position).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                if (getStatus.equals("Madhya Pradesh")) {
                    ArrayAdapter<String> a = new ArrayAdapter<>(postUpload.this, android.R.layout.simple_spinner_dropdown_item, cities_MP);
                    cities_spinner.setAdapter(a);
                } else if (getStatus.equals("Uttar Pradesh")) {
                    ArrayAdapter<String> a = new ArrayAdapter<>(postUpload.this, android.R.layout.simple_spinner_dropdown_item, cities_UP);
                    cities_spinner.setAdapter(a);
                } else if (getStatus.equals("Maharashtra")) {
                    ArrayAdapter<String> a = new ArrayAdapter<>(postUpload.this, android.R.layout.simple_spinner_dropdown_item, cities_Maharashtra);
                    cities_spinner.setAdapter(a);
                } else if (getStatus.equals("Gujarat")) {
                    ArrayAdapter<String> a = new ArrayAdapter<>(postUpload.this, android.R.layout.simple_spinner_dropdown_item, cities_gujarat);
                    cities_spinner.setAdapter(a);
                } else {
                    ArrayAdapter<String> a = new ArrayAdapter<>(postUpload.this, android.R.layout.simple_spinner_dropdown_item, dummy);
                    cities_spinner.setAdapter(a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        cities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getCity = adapterView.getItemAtPosition(position).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(16);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        uploadWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getStatus.equals("Select States")) {
                    Toast.makeText(postUpload.this, "Please select state.", Toast.LENGTH_SHORT).show();
                } else if (getCity.equals("Select Location")) {
                    Toast.makeText(postUpload.this, "Please select city.", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(postUpload.this);
                    progressDialog.setTitle("Posting...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    DocumentReference ref = db.collection("Users_work").document();
                    pushId = ref.getId();
                    String profileUri = sp.getString("MyProfileUri", "https://firebasestorage.googleapis.com/v0/b/rozgaar-d0598.appspot.com/o/profile_128px.png?alt=media&token=63f376c4-62d4-4106-96d3-85a03a16daee");
                    String fcm = sp.getString("MyFcm", "notFound");
                    postModel model = new postModel(uid, workTitle.getText().toString(), workDescription.getText().toString(), getTime, profileUri, stringList, getCity + " ," + getStatus, pushId, stringList.get(0), fcm);
                    db.collection("Users_work").document(pushId).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(postUpload.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1055552 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                if (count >= 5) {
                    Toast.makeText(getApplicationContext(), "You can select only 3 images.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        list.add(imageUri);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (data.getData() != null) {
                Uri imagePath = data.getData();
                list.add(imagePath);
                //do something with the image (save it to some directory or whatever you need to do with it here)
                adapter.notifyDataSetChanged();
            }
            uploadImages(list);
        } else {
            finish();
        }
    }

    private void uploadImages(List<Uri> imageUris) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Posting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);

//        for (Uri uri : imageUris) {
        for (int i = 0; i < imageUris.size(); i++) {
            reference = FirebaseStorage.getInstance().getReference("Work_Pics");
            StorageReference storageRef = reference.child(System.currentTimeMillis() + ".jpg");
            storageRef.putFile(imageUris.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> downloadUrlTask) {
                                if (downloadUrlTask.isSuccessful()) {
                                    Uri downloadUrl = downloadUrlTask.getResult();
                                    stringList.add(String.valueOf(downloadUrl));
                                    progressDialog.dismiss();
                                } else {

//                                    Log.e(TAG, "Failed to get download URL: ", downloadUrlTask.getException());
                                }
                            }
                        });
                    } else {

//                        Log.e(TAG, "Upload failed: ", task.getException());
                    }
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