package com.example.rozgaar.MainScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rozgaar.ChatBox.chatList;
import com.example.rozgaar.NotificationManagement.NotificationList;
import com.example.rozgaar.PostManagement.postAdapter;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.PostManagement.postUpload;
import com.example.rozgaar.R;
import com.example.rozgaar.settings;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstActivity extends AppCompatActivity {

    CircleImageView profilePic;
    AutoCompleteTextView searchWork;
    TextView userName, userNumber;
    Button putWork;
    MainAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<postModel> list = new ArrayList<postModel>();
    FirebaseFirestore db;
    ImageView notificationList, myPost, myChats,openProfile,settings;
    String getCity;
    DatabaseReference reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    CircleImageView notification_Sign, message_sign;
    String[] myCities = {" Babai, Madhya Pradesh", " Bagli, Madhya Pradesh", " Badnawar, Madhya Pradesh", " Badwani, Madhya Pradesh", " Banda, Madhya Pradesh", " Bankhedi, Madhya Pradesh", " Bareli, Madhya Pradesh", " Barkheda, Madhya Pradesh", " Barwaha, Madhya Pradesh", " Berasia, Madhya Pradesh", " Bhainsdehi, Madhya Pradesh", " Bhander, Madhya Pradesh", " Biaora, Madhya Pradesh", " Bijawar, Madhya Pradesh", " Birsinghpur, Madhya Pradesh", " Budni, Madhya Pradesh", " Burhar, Madhya Pradesh", " Chanderi, Madhya Pradesh", " Chhapara, Madhya Pradesh", " Chhatarpur, Madhya Pradesh", " Dabra, Madhya Pradesh", " Deori, Madhya Pradesh", " Gadarwara, Madhya Pradesh", " Gohad, Madhya Pradesh", " Harda, Madhya Pradesh", " Hatta, Madhya Pradesh", " Itarsi, Madhya Pradesh", " Jaithari, Madhya Pradesh", " Jaisinghnagar, Madhya Pradesh", " Jawad, Madhya Pradesh", " Amla Khurd, Madhya Pradesh", " Badagaon, Madhya Pradesh", " Bagratawa, Madhya Pradesh", " Bamanwas, Madhya Pradesh", " Barwani Kalan, Madhya Pradesh", " Batiagarh, Madhya Pradesh", " Bela Ghat, Madhya Pradesh", " Bhatauli, Madhya Pradesh", " Bhavkhedi, Madhya Pradesh", " Bhikampur, Madhya Pradesh", " Bijalpur, Madhya Pradesh", " Bilheri, Madhya Pradesh", " Birsinghpur, Madhya Pradesh", " Budni, Madhya Pradesh", " Burhar, Madhya Pradesh", " Burhanpur, Madhya Pradesh", " Chanderi, Madhya Pradesh", " Chhapara, Madhya Pradesh", " Chhatarpur, Madhya Pradesh", " Dabra, Madhya Pradesh", " Deori, Madhya Pradesh", " Gadarwara, Madhya Pradesh", " Gohad, Madhya Pradesh", " Harda, Madhya Pradesh", " Hatta, Madhya Pradesh", " Itarsi, Madhya Pradesh", " Jaithari, Madhya Pradesh", " Jaisinghnagar, Madhya Pradesh", " Jawad, Madhya Pradesh", " Jhalon, Madhya Pradesh", " Jharkheda, Madhya Pradesh", " Kakadkheri, Madhya Pradesh", " Kachhar, Madhya Pradesh", " Kala Pipal, Madhya Pradesh", " Chandrakhuri, Madhya Pradesh", " Chichiwara, Madhya Pradesh", " Chilkahar, Madhya Pradesh", " Chiraidongri, Madhya Pradesh", " Dattigaon, Madhya Pradesh", " Devkund, Madhya Pradesh", " Dhamnod, Madhya Pradesh", " Dhamoni, Madhya Pradesh", " Ghoradongri, Madhya Pradesh", " Gyanpura, Madhya Pradesh", " Hardu Khurd, Madhya Pradesh", " Jagatpur, Madhya Pradesh", " Jhalon, Madhya Pradesh", " Jharkheda, Madhya Pradesh", " Kakadkheri, Madhya Pradesh", " Kachhar, Madhya Pradesh", " Kala Pipal, Madhya Pradesh", " Agar Malwa, Madhya Pradesh", " Alirajpur, Madhya Pradesh", " Anuppur, Madhya Pradesh", " Ashoknagar, Madhya Pradesh", " Balaghat, Madhya Pradesh", " Betul, Madhya Pradesh", " Bhind, Madhya Pradesh", " Bhopal, Madhya Pradesh", " Burhanpur, Madhya Pradesh", " Chhindwara, Madhya Pradesh", " Damoh, Madhya Pradesh", " Datia, Madhya Pradesh", " Dewas, Madhya Pradesh", " Dhar, Madhya Pradesh", " Dindori, Madhya Pradesh", " Guna, Madhya Pradesh", " Gwalior, Madhya Pradesh", " Hoshangabad, Madhya Pradesh", " Indore, Madhya Pradesh", " Jabalpur, Madhya Pradesh", " Jhabua, Madhya Pradesh", " Katni, Madhya Pradesh", " Khandwa, Madhya Pradesh", " Khargone, Madhya Pradesh", " Mandla, Madhya Pradesh", " Mandsaur, Madhya Pradesh", " Morena, Madhya Pradesh", " Narsinghpur, Madhya Pradesh", " Neemuch, Madhya Pradesh"
            , "Achhnera, Uttar Pradesh", "Achnera, Uttar Pradesh", "Agra, Uttar Pradesh", "Ahmadnagar, Uttar Pradesh", "Ahraura, Uttar Pradesh", "Ajitmal, Uttar Pradesh", "Akbarpur, Uttar Pradesh", "Aliganj, Uttar Pradesh", "Aligarh, Uttar Pradesh", "Allahabad, Uttar Pradesh", "Prayagraj, Uttar Pradesh", "Amethi, Uttar Pradesh", "Amroha, Uttar Pradesh", "Jyotiba Phule Nagar, Uttar Pradesh", "Aonla, Uttar Pradesh", "Arail Uparhar, Uttar Pradesh", "Ashoknagar, Uttar Pradesh", "Auraiya, Uttar Pradesh", "Ayodhya, Uttar Pradesh", "Faizabad, Uttar Pradesh", "Babarpur, Uttar Pradesh", "Babatpur, Uttar Pradesh", "Baberu, Uttar Pradesh", "Babina, Uttar Pradesh", "Bachhrawan, Uttar Pradesh", "Badaun, Uttar Pradesh", "Baghpat, Uttar Pradesh", "Bah, Uttar Pradesh", "Bahadurganj, Uttar Pradesh", "Baheri, Uttar Pradesh", "Bahjoi, Uttar Pradesh", "Bahraich, Uttar Pradesh", "Bairagnia, Uttar Pradesh", "Bairia, Uttar Pradesh", "Bakewar, Uttar Pradesh", "Baldeo, Uttar Pradesh", "Balrampur, Uttar Pradesh", "Banda, Uttar Pradesh", "Bangarmau, Uttar Pradesh", "Banipur, Uttar Pradesh", "Bansdih, Uttar Pradesh", "Barabanki, Uttar Pradesh", "Baragaon, Uttar Pradesh", "Baraut, Uttar Pradesh", "Bareilly, Uttar Pradesh", "Barha Bazar, Uttar Pradesh", "Barhalganj, Uttar Pradesh", "Baria, Uttar Pradesh", "Barla, Uttar Pradesh", "Barnahal, Uttar Pradesh", "Barra, Uttar Pradesh", "Barwar, Uttar Pradesh", "Basantpur, Uttar Pradesh", "Basgaon, Uttar Pradesh", "Basrehar, Uttar Pradesh", "Basti, Uttar Pradesh", "Bathua, Uttar Pradesh", "Behat, Uttar Pradesh", "Behror, Uttar Pradesh", "Bela Pratapgarh, Uttar Pradesh", "Belthara Road, Uttar Pradesh", "Beniganj, Uttar Pradesh", "Benti, Uttar Pradesh", "Bewar, Uttar Pradesh", "Bhadarsa, Uttar Pradesh", "Bhadaura, Uttar Pradesh", "Bhadohi, Uttar Pradesh", "Sant Ravidas Nagar, Uttar Pradesh", "Bhagwantnagar, Uttar Pradesh", "Bhanera, Uttar Pradesh", "Bhanpur, Uttar Pradesh", "Bharawan, Uttar Pradesh", "Bharthana, Uttar Pradesh", "Bhatni Bazar, Uttar Pradesh", "Bhatpura, Uttar Pradesh", "Bhatpurwa, Uttar Pradesh", "Bhatua, Uttar Pradesh", "Bhaupur, Uttar Pradesh", "Bheera, Uttar Pradesh", "Bhera, Uttar Pradesh", "Bhetia, Uttar Pradesh", "Bhikampur, Uttar Pradesh", "Bhikharipur, Uttar Pradesh", "Bhinga, Uttar Pradesh", "Bhognipur, Uttar Pradesh", "Bhogaon, Uttar Pradesh", "Bhongaon, Uttar Pradesh", "Bidhuna, Uttar Pradesh", "Sirsaganj, Uttar Pradesh", "Bhowali, Uttar Pradesh", "Bhusaula, Uttar Pradesh", "Bilariaganj, Uttar Pradesh", "Bilaspur, Uttar Pradesh", "Bilgram, Uttar Pradesh", "Bilhaur, Uttar Pradesh", "Bilram, Uttar Pradesh", "Bilsanda, Uttar Pradesh", "Bindki, Uttar Pradesh", "Bisalpur, Uttar Pradesh", "Bisanda Buzurg, Uttar Pradesh", "Bisauli, Uttar Pradesh", "Bisharatganj, Uttar Pradesh", "Biswan, Uttar Pradesh", "Bithur, Uttar Pradesh", "Budaun, Uttar Pradesh", "Budhana, Uttar Pradesh", "Bugrasi, Uttar Pradesh", "Bukhara, Uttar Pradesh", "Bulandshahr, Uttar Pradesh", "Burhanpur, Uttar Pradesh", "Chail, Uttar Pradesh", "Chak Imam Ali, Uttar Pradesh", "Chak Sikandar, Uttar Pradesh", "Chandauli, Uttar Pradesh", "Chandpur, Uttar Pradesh", "Charkhari, Uttar Pradesh", "Chaudagarh, Uttar Pradesh"
            , "Chaumukha, Uttar Pradesh", "Chaurakhera, Uttar Pradesh", "Raja Ka Rampur, Uttar Pradesh", "Chhata, Uttar Pradesh", "Chhatari, Uttar Pradesh", "Chhatnag, Uttar Pradesh", "Chhibramau, Uttar Pradesh", "Chhutmalpur, Uttar Pradesh", "Chillupar, Uttar Pradesh", "Chilmau, Uttar Pradesh", "Chiraigaon, Uttar Pradesh", "Chiraiyakot, Uttar Pradesh", "Chitrakoot, Uttar Pradesh", "Chitrakoot Dham, Uttar Pradesh", "Karwi, Uttar Pradesh", "Chobepur, Uttar Pradesh", "Chopan, Uttar Pradesh", "Chunar, Uttar Pradesh", "Churk Ghurma, Uttar Pradesh", "Churk Rani, Uttar Pradesh", "Churiyamau, Uttar Pradesh", "Churk, Uttar Pradesh", "Naugarh, Uttar Pradesh", "Dadri, Uttar Pradesh", "Dariyabad, Uttar Pradesh", "Dasna, Uttar Pradesh", "Dataganj, Uttar Pradesh", "Daurala, Uttar Pradesh", "Deoband, Uttar Pradesh", "Nagar, Uttar Pradesh", "Deogan, Uttar Pradesh", "Deoria, Uttar Pradesh", "Derapur, Uttar Pradesh", "Dhanghat, Uttar Pradesh", "Dhaurahra, Uttar Pradesh", "Nawabganj, Uttar Pradesh", "Suar, Uttar Pradesh", "Tikait Nagar, Uttar Pradesh", "Tikri, Uttar Pradesh", "Utraula, Uttar Pradesh", "Dhobi, Uttar Pradesh", "Dhodpur, Uttar Pradesh", "Kumbhi, Uttar Pradesh", "Dhoom Manikpur, Uttar Pradesh", "Dibai, Uttar Pradesh", "Anupshahr, Uttar Pradesh", "Didhaur, Uttar Pradesh", "Didhua, Uttar Pradesh", "Dig, Uttar Pradesh", "Digod, Uttar Pradesh", "Dilari, Uttar Pradesh", "Dipauli, Uttar Pradesh", "Dipalsui, Uttar Pradesh", "Dipura, Uttar Pradesh", "Domariaganj, Uttar Pradesh", "Dudhi, Uttar Pradesh", "Dudhnai, Uttar Pradesh", "Dudhaur, Uttar Pradesh", "Dullahapur, Uttar Pradesh", "Dulhipur, Uttar Pradesh", "Meja, Uttar Pradesh", "Dullapur, Uttar Pradesh", "Dumariaganj, Uttar Pradesh", "Dungarpur, Uttar Pradesh", "Durgapur, Uttar Pradesh", "Dwarahat, Uttar Pradesh", "Dwarkaganj, Uttar Pradesh", "Ekauna, Uttar Pradesh", "Ekdil, Uttar Pradesh", "Indergarh, Uttar Pradesh", "Naraini, Uttar Pradesh", "Pailani, Uttar Pradesh", "Pura Janakpur, Uttar Pradesh", "Tirwa, Uttar Pradesh", "Ugu, Uttar Pradesh", "Fatehpur Sikri, Uttar Pradesh", "Robertsganj, Uttar Pradesh", "Mughalsarai, Uttar Pradesh", "Rasra, Uttar Pradesh", "Siswa Bazar, Uttar Pradesh", "Pihani, Uttar Pradesh", "Sirsi, Uttar Pradesh", "Mihinpurwa, Uttar Pradesh", "Gaura Barhaj, Uttar Pradesh", "Saidpur, Uttar Pradesh", "Belhar, Uttar Pradesh", "Paraspur, Uttar Pradesh", "Kusmara, Uttar Pradesh", "Aambali, Maharashtra", "Aheri, Maharashtra", "Ahmednagar, Maharashtra", "Airoli, Maharashtra", "Akluj, Maharashtra", "Akola, Maharashtra", "Akole, Maharashtra", "Akot, Maharashtra", "Alandi, Maharashtra", "Alandi, Maharashtra", "Alibag, Maharashtra", "Amalner, Maharashtra", "Ambajogai, Maharashtra", "Ambaval, Maharashtra", "Ambivali, Maharashtra", "Amravati, Maharashtra", "Anandnagar, Maharashtra", "Anjangaon, Maharashtra", "Arangaon, Maharashtra", "Arjuni, Maharashtra", "Arvi, Maharashtra", "Asangaon, Maharashtra", "Ashti, Maharashtra", "Atali, Maharashtra", "Aundh, Maharashtra", "Babhaleshwar, Maharashtra", "Babhulgaon, Maharashtra", "Bajala, Maharashtra", "Ballarpur, Maharashtra", "Baramati, Maharashtra", "Barshi, Maharashtra", "Beed, Maharashtra", "Bhadgaon, Maharashtra", "Bhandara, Maharashtra", "Bhayander, Maharashtra", "Bhiwandi, Maharashtra", "Bhokardan, Maharashtra", "Bhoom, Maharashtra", "Bhudargad, Maharashtra", "Bid, Maharashtra", "Bijaipur, Maharashtra", "Biloli, Maharashtra", "Binhur, Maharashtra", "Birwadi, Maharashtra", "Bodwad, Maharashtra", "Boisar, Maharashtra", "Borgaon, Maharashtra", "Bramhapuri, Maharashtra", "Bubnal, Maharashtra", "Chalisgaon, Maharashtra", "Chandrapur, Maharashtra", "Chandwad, Maharashtra", "Chikhaldara, Maharashtra", "Chinchani, Maharashtra", "Chopda, Maharashtra", "Dahanu, Maharashtra", "Dapoli, Maharashtra", "Daryapur, Maharashtra", "Deolali, Maharashtra", "Deulgaon Raja, Maharashtra", "Dharmabad, Maharashtra", "Dharni, Maharashtra", "Digras, Maharashtra", "Dindori, Maharashtra", "Dondaicha, Maharashtra", "Gadhinglaj, Maharashtra", "Gadchiroli, Maharashtra", "Gangakhed, Maharashtra", "Georai, Maharashtra", "Ghatanji, Maharashtra", "Ghodegaon, Maharashtra", "Hinganghat, Maharashtra", "Hingoli, Maharashtra", "Ichalkaranji, Maharashtra", "Jalgaon, Maharashtra", "Jalna, Maharashtra", "Kalyan-Dombivli, Maharashtra", "Kamthi, Maharashtra", "Karad, Maharashtra", "Karmala, Maharashtra", "Katol, Maharashtra", "Khamgaon, Maharashtra", "Khopoli, Maharashtra", "Kolhapur, Maharashtra", "Kopargaon, Maharashtra", "Latur, Maharashtra", "Lonavla, Maharashtra", "Malegaon, Maharashtra", "Malkapur, Maharashtra", "Manmad, Maharashtra", "Miraj, Maharashtra", "Mumbai, Maharashtra", "Nagpur, Maharashtra", "Nanded, Maharashtra", "Nandurbar, Maharashtra", "Nashik, Maharashtra", "Navi Mumbai, Maharashtra", "Osmanabad, Maharashtra", "Palghar, Maharashtra", "Pandharpur, Maharashtra", "Panvel, Maharashtra", "Parbhani, Maharashtra", "Pimpri-Chinchwad, Maharashtra", "Pusad, Maharashtra", "Raigad, Maharashtra", "Rajapur, Maharashtra", "Ratnagiri, Maharashtra", "Sangamner, Maharashtra", "Sangli, Maharashtra", "Satara, Maharashtra", "Shegaon, Maharashtra", "Shirdi, Maharashtra", "Shirpur-Warwade, Maharashtra", "Solapur, Maharashtra", "Talegaon Dabhade, Maharashtra", "Thane, Maharashtra", "Udgir, Maharashtra", "Ulhasnagar, Maharashtra", "Vasai-Virar, Maharashtra", "Velhe, Maharashtra", "Vita, Maharashtra", "Wai, Maharashtra", "Wardha, Maharashtra", "Washim, Maharashtra", "Yavatmal, Maharashtra", "Yawal, Maharashtra", "Yedshi, Maharashtra", "Yeola, Maharashtra", "Yeotmal, Maharashtra"
            , "Adalaj, Gujarat", "Adipur, Gujarat", "Ahmedabad, Gujarat", "Ahwa, Gujarat", "Ambaji, Gujarat", "Anand, Gujarat", "Anjar, Gujarat", "Anklav, Gujarat", "Babra, Gujarat", "Bantva, Gujarat", "Barwala, Gujarat", "Bharuch, Gujarat", "Bhachau, Gujarat", "Bhadran, Gujarat", "Bhanvad, Gujarat", "Bharuch, Gujarat", "Bhavnagar, Gujarat", "Bhuj, Gujarat", "Botad, Gujarat", "Borsad, Gujarat", "Chala, Gujarat", "Champaner, Gujarat", "Chhala, Gujarat", "Chorvad, Gujarat", "Dabhoi, Gujarat", "Dahegam, Gujarat", "Dakor, Gujarat", "Daman, Gujarat", "Dandi, Gujarat", "Dahod, Gujarat", "Daman, Gujarat", "Devgadh Baria, Gujarat", "Dhanera, Gujarat", "Dharampur, Gujarat", "Dhari, Gujarat", "Dhrol, Gujarat", "Dholavira, Gujarat", "Dholka, Gujarat", "Dhoraji, Gujarat", "Dhrol, Gujarat", "Diu, Gujarat", "Dwarka, Gujarat", "Gadhada, Gujarat", "Gandevi, Gujarat", "Gandhidham, Gujarat", "Gandhinagar, Gujarat", "Gariadhar, Gujarat", "Garbada, Gujarat", "Ghogha, Gujarat", "Godhra, Gujarat", "Halol, Gujarat", "Halvad, Gujarat", "Himatnagar, Gujarat", "Idar, Gujarat", "Jambusar, Gujarat", "Jam Jodhpur, Gujarat", "Jamnagar, Gujarat", "Jasdan, Gujarat", "Jetpur, Gujarat", "Jetpur Navagadh, Gujarat", "Junagadh, Gujarat", "Kadi, Gujarat", "Kamrej, Gujarat", "Kandla, Gujarat", "Kapadvanj, Gujarat", "Karamsad, Gujarat", "Keshod, Gujarat", "Khambhat, Gujarat", "Kheralu, Gujarat", "Kundla, Gujarat", "Kutch, Gujarat", "Lalpur, Gujarat", "Lathi, Gujarat", "Limdi, Gujarat", "Lunawada, Gujarat", "Mahuva, Gujarat", "Mahemdabad, Gujarat", "Mahuva, Gujarat", "Mandvi, Gujarat", "Manavadar, Gujarat", "Mangrol, Gujarat", "Mehsana, Gujarat", "Meghraj, Gujarat", "Modasa, Gujarat", "Morbi, Gujarat", "Mundra, Gujarat", "Nadiad, Gujarat", "Nakhatrana, Gujarat", "Nalsarovar, Gujarat", "Nandod, Gujarat", "Narayan Sarovar, Gujarat", "Narmada, Gujarat", "Nasvadi, Gujarat", "Navagam, Gujarat", "Navsari, Gujarat", "Nizar, Gujarat", "Okha, Gujarat", "Palanpur, Gujarat", "Palitana, Gujarat", "Pardi, Gujarat", "Patan, Gujarat", "Pavagadh, Gujarat", "Petlad, Gujarat", "Porbandar, Gujarat", "Radhanpur, Gujarat", "Rajkot, Gujarat", "Rajpipla, Gujarat", "Rajula, Gujarat", "Rann of Kutch, Gujarat", "Sanand, Gujarat", "Sankheda, Gujarat", "Saputara, Gujarat", "Sasan Gir, Gujarat", "Savarkundla, Gujarat", "Shamlaji, Gujarat", "Sidhpur, Gujarat", "Siddhpur, Gujarat", "Sihor, Gujarat", "Sojitra, Gujarat", "Songadh, Gujarat", "Surat, Gujarat", "Surendranagar, Gujarat", "Talaja, Gujarat", "Tharad, Gujarat", "Thol, Gujarat", "Una, Gujarat", "Umreth, Gujarat", "Upleta, Gujarat", "Valsad, Gujarat", "Vadnagar, Gujarat", "Vadodara, Gujarat", "Vaghodia, Gujarat", "Valia, Gujarat", "Vallabhipur, Gujarat", "Valsad, Gujarat", "Vapi, Gujarat", "Vaso, Gujarat", "Vav, Gujarat", "Vejalpur, Gujarat", "Veraval, Gujarat", "Vijalpor, Gujarat", "Vijapur, Gujarat", "Vithal Udyognagar, Gujarat", "Vyara, Gujarat", "Wadhwan, Gujarat", "Wankaner, Gujarat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        profilePic = findViewById(R.id.profile_pic_main_screen);
        searchWork = findViewById(R.id.search_work);
        userName = findViewById(R.id.user_Name_mS);
        userNumber = findViewById(R.id.user_Number_mS);
        putWork = findViewById(R.id.put_work_btn);
        recyclerView = findViewById(R.id.main_screen_recyclerview);
        notificationList = findViewById(R.id.notification_btn);
        myPost = findViewById(R.id.my_posts_btn);
        notification_Sign = findViewById(R.id.notification_signal);
        myChats = findViewById(R.id.message_list);
        message_sign = findViewById(R.id.message_signal);
        openProfile = findViewById(R.id.open_profile_details);
        settings = findViewById(R.id.apps_settings);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit_first);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);


        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        userName.setText(sp.getString("MyName", "error"));
        userNumber.setText(sp.getString("MyNumber", "error"));
        Glide.with(this).load(sp.getString("MyProfileUri", "error")).into(profilePic);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MainAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        db.collection("Users_work").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        postModel model = documentSnapshot.toObject(postModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        ArrayAdapter<String> MP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, myCities);
        searchWork.setThreshold(1);
        searchWork.setAdapter(MP);


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, com.example.rozgaar.settings.class));
            }
        });

        searchWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCity = parent.getItemAtPosition(position).toString();
                filter(getCity);
            }
        });

        openProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileUpdate profileUpdate = new ProfileUpdate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    profileUpdate.show(getSupportFragmentManager(), getAttributionTag());
                }
            }
        });

        putWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, postUpload.class));
            }
        });

        notificationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, NotificationList.class));
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(FirstActivity.this, OurPost.class));
                Intent intent = new Intent(FirstActivity.this, OurPost.class);
//                intent.putExtra("GET_USERNAME", sp.getString("MyName", "error"));
//                intent.putExtra("GET_USERNAME", sp.getString("MyUid", "error"));
                intent.putExtra("GET_USERNAME", sp.getString("MyFcm", "error"));
                startActivity(intent);
            }
        });

        myChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, chatList.class));
            }
        });

        notificationStatus();
        ChatListStatus();
    }

    public void notificationStatus() {
        reference.child("NotificationStatus").child(uid).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String check = snapshot.getValue(String.class);
                    if (Objects.equals(check, "true")) {
                        notification_Sign.setVisibility(View.VISIBLE);
                    } else {
                        notification_Sign.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void filter(String text) {
        List<postModel> temp = new ArrayList();
        if (text.isEmpty()) {
            temp.addAll(list);
        } else {
            for (postModel d : list) {
                if (d.getPost_location().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            adapter.updateList(temp);
        }
    }

    public void ChatListStatus() {
        reference.child("ChatListStatus").child(uid).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String check = snapshot.getValue(String.class);
                    if (Objects.equals(check, "true")) {
                        message_sign.setVisibility(View.VISIBLE);
                    } else {
                        message_sign.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("OnAppStatus").child(uid).child("active").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.child("OnAppStatus").child(uid).child("active").setValue("false");
    }
}