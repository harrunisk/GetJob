package com.example.harun.getjob.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class profilPage extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton editProfileFab;
    //    TextView aboutContentText;
    public FirebaseMethods mFirebaseMethod;
    ArrayList<egitimListModel> egitimlistFromFirebase;
    egitimListAdapter megitimListAdapter;
    deneyimListAdapter mdeneyimListAdapter;
    yetenekListAdapter myetenekListAdapter;
    private RecyclerView recyclerEgitim;
    private RecyclerView recyclerDeneyim;
    private RecyclerView recyclerYetenek;

    ImageView addYetenek_btn, addGenel_btn, addEgitim_btn, addDeneyim_btn, addAbout_btn;


    // AllModelsList models;
    AVLoadingIndicatorView progressBar, profilimageProgress;
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

        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef1 = mFirebaseDatabase.getReference();

        addDeneyim_btn.setVisibility(View.GONE);
        addEgitim_btn.setVisibility(View.GONE);
        addGenel_btn.setVisibility(View.GONE);
        addAbout_btn.setVisibility(View.GONE);
        addYetenek_btn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profil_frag);
        gatherViews();
        this.init();
        firebaseInit();

        /*alertDialog = Permissions.showAlertdilaog(this, "Profiliniz Yükleniyor  ",
                "Lütfen Bekleyiniz", 5000);


        alertDialog.show();*/



    }

    private void gatherViews() {

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        editProfileFab = findViewById(R.id.editProfileFab);

        // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.personal_collapsed_title);
        // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.personal_expanded_title);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setTitle("P R O F İ L E");
        // toolbar.setNavigationIcon(R.drawable.arrow);


        tvTel = findViewById(R.id.tvTel);
        tvMail = findViewById(R.id.tvMail);
        tvDogumTarih = findViewById(R.id.tvDogumTarih);
        tvEhliyet = findViewById(R.id.tvEhliyet);
        tvAskerlik = findViewById(R.id.tvAskerlik);
        tvAbout_content = findViewById(R.id.editAbout_content);
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

        addAbout_btn = findViewById(R.id.addAbout_btn);
        addYetenek_btn = findViewById(R.id.addYetenek_btn);
        addGenel_btn = findViewById(R.id.addGenel_btn);
        addEgitim_btn = findViewById(R.id.addEgitim_btn);
        addDeneyim_btn = findViewById(R.id.addDeneyim_btn);


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

    public void startAnimProgressBar() {

        progressBar.show();


    }

    public void stopAnim() {

        progressBar.hide();

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

            //progressBar.setVisibility(View.GONE);
            stopAnim();

        } else

        {
            stopAnim();
            // progressBar.setVisibility(View.GONE);
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
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            // dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
            recyclerYetenek.addItemDecoration(dividerItemDecoration);
            recyclerYetenek.setItemAnimator(new DefaultItemAnimator());
            recyclerYetenek.setHasFixedSize(true);
            /*recyclerYetenek.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL) {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int position = parent.getChildAdapterPosition(view);
                    // hide the divider for the last child
                    if (position == parent.getAdapter().getItemCount() - 1) {
                        outRect.setEmpty();
                    } else {
                        super.getItemOffsets(outRect, view, parent, state);
                    }
                }
            });
*/

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
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            // dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
            recyclerDeneyim.addItemDecoration(dividerItemDecoration);
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
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            // dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
            recyclerEgitim.addItemDecoration(dividerItemDecoration);
            recyclerEgitim.setHasFixedSize(true);
            recyclerEgitim.setItemAnimator(new DefaultItemAnimator());

        }

    }

    public void editProfile(View view) {

        Intent intent = new Intent(getApplicationContext(), EditProfile.class);
        intent.putExtra("AllItems", sendData);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
