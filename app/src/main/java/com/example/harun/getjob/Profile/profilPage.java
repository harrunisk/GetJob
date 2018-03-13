package com.example.harun.getjob.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harun.getjob.FirebaseMethods.FirebaseMethods;
import com.example.harun.getjob.R;
import com.example.harun.getjob.profileModel.AllModelsList;
import com.example.harun.getjob.profileModel.deneyimListAdapter;
import com.example.harun.getjob.profileModel.egitimListAdapter;
import com.example.harun.getjob.profileModel.egitimListModel;
import com.example.harun.getjob.profileModel.yetenekListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class profilPage extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton editProfileFab;
    TextView aboutContentText;
    public FirebaseMethods mFirebaseMethod;
    ArrayList<egitimListModel> egitimlistFromFirebase;
    egitimListAdapter megitimListAdapter;
    deneyimListAdapter mdeneyimListAdapter;
    yetenekListAdapter myetenekListAdapter;
    RecyclerView recyclerEgitim;
    RecyclerView recyclerDeneyim;
    RecyclerView recyclerYetenek;
    ProgressBar progressBar,
            profilimageProgress;
    // AllModelsList models;

    //GenelBilgi widgets
    TextView tvTel, tvMail, tvDogumTarih, tvEhliyet, tvAskerlik, tvAbout_content, tvFullnameuser, tvUserJob, tvCity;
    CircularImageView userProfileImage;

    //Recyler Empty Mesaj

    TextView empty_message,
            empty_message1,
            empty_message2;
    Alerter alertDialog;
    public static AllModelsList sendData;
    //FİREBASE
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference myRef1;

    private static final String TAG = "profilPage";

    public void init() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        editProfileFab = findViewById(R.id.editProfileFab);

        // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.personal_collapsed_title);
        // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.personal_expanded_title);

        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setTitle("P R O F İ L E");
        // toolbar.setNavigationIcon(R.drawable.arrow);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTel = findViewById(R.id.tvTel);
        tvMail = findViewById(R.id.tvMail);
        tvDogumTarih = findViewById(R.id.tvDogumTarih);
        tvEhliyet = findViewById(R.id.tvEhliyet);
        tvAskerlik = findViewById(R.id.tvAskerlik);
        tvAbout_content = findViewById(R.id.about_content);
        tvFullnameuser = findViewById(R.id.fullnameuser);
        tvUserJob = findViewById(R.id.userJob);
        tvCity = findViewById(R.id.city);
        userProfileImage = findViewById(R.id.userProfileImage);
        empty_message = findViewById(R.id.empty_message);
        empty_message1 = findViewById(R.id.empty_message1);
        empty_message2 = findViewById(R.id.empty_message2);
        recyclerEgitim = findViewById(R.id.egitimList);
        recyclerDeneyim = findViewById(R.id.deneyimList);
        recyclerYetenek = findViewById(R.id.yetenekList);
        progressBar = findViewById(R.id.profileProgress);
        profilimageProgress = findViewById(R.id.profilimageProgress);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef1 = mFirebaseDatabase.getReference();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profil_frag);
        this.init();
        /*alertDialog = Permissions.showAlertdilaog(this, "Profiliniz Yükleniyor  ",
                "Lütfen Bekleyiniz", 5000);


        alertDialog.show();*/
        progressBar.setVisibility(View.VISIBLE);
        firebaseInit();


    }

    public void firebaseInit() {

        mFirebaseMethod = new FirebaseMethods(getApplicationContext());
        Log.d(TAG, "firebaseInit: ");
        egitimlistFromFirebase = new ArrayList<>();

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: PRFOİLPAGE");
                setProfileItems(mFirebaseMethod.getDataFromFirebase(dataSnapshot));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setProfileItems(@Nullable AllModelsList dataFromFirebase) {

        Log.d(TAG, "setProfileItems: \t" + dataFromFirebase);


        sendData = dataFromFirebase;
        if (dataFromFirebase != null) {

            Log.d(TAG, "setProfileItems: getMgenelBilgiModel " + dataFromFirebase.getMgenelBilgiModel());

            Log.d(TAG, "setProfileItems: getMgenelBilgiModel Not null ");

            tvTel.setText(dataFromFirebase.getMgenelBilgiModel().getPhone());
            tvMail.setText(dataFromFirebase.getMgenelBilgiModel().getE_mail());
            tvDogumTarih.setText(dataFromFirebase.getMgenelBilgiModel().getBirthday());
            tvEhliyet.setText(dataFromFirebase.getMgenelBilgiModel().getEhliyet());
            tvAskerlik.setText(dataFromFirebase.getMgenelBilgiModel().getAskerlik());


            tvAbout_content.setText(dataFromFirebase.getAbout_me());
            tvFullnameuser.setText(dataFromFirebase.getMfirebaseContent().getName());
            tvUserJob.setText(dataFromFirebase.getMfirebaseContent().getJob());
            tvCity.setText(dataFromFirebase.getMfirebaseContent().getLocation());
            String photoUrl = dataFromFirebase.getProfilePhotoUrl();


            //
            //Link zaten http olarak geliyor bu yzden append null
            Log.d(TAG, "initImageLoader: Image Yükleniyor..");
            UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
            ImageLoader.getInstance().init(universalImageLoader.configuration());

            UniversalImageLoader.setImage(photoUrl, userProfileImage, profilimageProgress, "");

            //Recycler Listeleri doldurma

            setRecyclerDeneyim(dataFromFirebase);
            setRecyclerEgitim(dataFromFirebase);
            setRecyclerYetenek(dataFromFirebase);

            progressBar.setVisibility(View.GONE);


        } else

        {
            progressBar.setVisibility(View.GONE);
            alertDialog = Permissions.showAlertdilaog(this, "Profiliniz Boş Gözüküyor  ",
                    "Profilinizi Güncelleyiniz.", 3000);


            alertDialog.show();


            Log.d(TAG, "setProfileItems: dataFromFirebase NULLL" + dataFromFirebase);
        }

    }

    private void setRecyclerYetenek(AllModelsList dataFromFirebase) {

        if (dataFromFirebase.getMyetenekListModel().size() == 0) {

            empty_message2.setVisibility(View.VISIBLE);
            recyclerYetenek.setVisibility(View.GONE);

        } else {


            myetenekListAdapter = new yetenekListAdapter(this, dataFromFirebase.getMyetenekListModel(), true);
            recyclerYetenek.setAdapter(myetenekListAdapter);
            empty_message2.setVisibility(View.INVISIBLE);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
            linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerYetenek.setLayoutManager(linearLayoutManager1);
            recyclerYetenek.setItemAnimator(new DefaultItemAnimator());
            recyclerYetenek.setHasFixedSize(true);
        }

    }

    private void setRecyclerDeneyim(AllModelsList dataFromFirebase) {

        if (dataFromFirebase.getMdeneyimListModel().size() == 0) {
            recyclerDeneyim.setVisibility(View.GONE);
            empty_message.setVisibility(View.VISIBLE);


        } else {

            mdeneyimListAdapter = new deneyimListAdapter(this, dataFromFirebase.getMdeneyimListModel(), true);
            recyclerDeneyim.setAdapter(mdeneyimListAdapter);
            Log.d(TAG, "setRecyclerDeneyim: " + mdeneyimListAdapter.getItemCount());
            empty_message.setVisibility(View.INVISIBLE);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
            linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerDeneyim.setLayoutManager(linearLayoutManager1);
            recyclerDeneyim.setItemAnimator(new DefaultItemAnimator());
            recyclerDeneyim.setHasFixedSize(true);


        }


    }

    private void setRecyclerEgitim(AllModelsList dataFromFirebase) {
        if (dataFromFirebase.getMegitimListModel().size() == 0) {
            recyclerEgitim.setVisibility(View.GONE);
            empty_message1.setVisibility(View.VISIBLE);


        } else {


            megitimListAdapter = new egitimListAdapter(getApplicationContext(), dataFromFirebase.getMegitimListModel(), true);
            recyclerEgitim.setAdapter(megitimListAdapter);
            empty_message1.setVisibility(View.INVISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerEgitim.setLayoutManager(linearLayoutManager);
            recyclerEgitim.setHasFixedSize(true);
            recyclerEgitim.setItemAnimator(new DefaultItemAnimator());

        }

    }

    public void editProfile(View view) {

        Intent intent = new Intent(getApplicationContext(), EditProfile.class);
        intent.putExtra("AllItems", sendData);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
