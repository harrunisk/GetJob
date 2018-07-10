package com.example.harun.getjob;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.AddJobAdvert.AddJobAdvert;
import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.HelperStaticMethods;
import com.example.harun.getjob.AddJobAdvert.MyAppliedAdvert;
import com.example.harun.getjob.AddJobAdvert.SuggestJobAdvert;
import com.example.harun.getjob.AddJobAdvert.SuggestionsKeysInterface;
import com.example.harun.getjob.AddJobAdvert.SuggestionsModel;
import com.example.harun.getjob.JobSearch.JobSearch;
import com.example.harun.getjob.JobSearch.JobUtils.AdvertDetails;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.SuggestJobAdvertAdapter;
import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.Profile.profilPage;
import com.example.harun.getjob.viewpagercards.SnappingRecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Harun on 10-Jan-18.
 */

public class UserIntro extends AppCompatActivity implements SuggestJobAdvert.SuggestJobAdvertInterface, DeviceLocation.DeviceLocationCallback, SuggestionsKeysInterface, MyAppliedAdvert.AppliedAdvertCallback, AdvertDetails.ChangeMarkerCluster, SuggestJobAdvertAdapter.AdvertOnclickCallback {
    private static final String TAG = "UserIntro";


    RelativeLayout profileLayout, emptyRecommendList;
    private SnappingRecyclerView recyclerViewJobAdvert;
    private SuggestJobAdvertAdapter mSuggestJobAdvertAdapter;
    //private SuggestJobAdvert suggestJobAdvert;
    private AVLoadingIndicatorView suggestListProgress;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mPermissionGranted;
    private LatLng myLocation;
    private LocationManager loc;
    private boolean isGPSEnabled;
    private HashMap<String, ArrayList<SuggestionsModel>> suggestionHash;
    private ArrayList<SuggestionsModel> keyList;
    private SuggestionsKeysInterface suggestionsKeys;
    private ArrayList<NearJobAdvertModel> callbackResultList2;
    private ArrayList<NearJobAdvertModel> fromJobSector;
    private ArrayList<NearJobAdvertModel> fromCompanyJobList;
    private int counter = 0;
    private int counter3 = 0;
    private int counter2 = 0;
    private int getNearJobCounter = 0;
    ViewGroup scrollableView;
    private ViewGroup aaa;
    private SlidingUpPanelLayout mSlidingPanelLayout;
    private MyAppliedAdvert myAppliedAdvert;
    private ArrayList<String> myAppliedAdvertJobIdLList;
    AdvertDetails.ViewHolder advertDetailsHolder;
    private boolean isOpened;

    //SlidingPanel Widgets
    private ImageView companyLogo1;
    private TextView companyname, sector, tvbasvuru_count, tvjobname, publishdate, tvdistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_mainpage);

        loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (Permissions.checkPermissionArray(this, Permissions.MapPermission)) {
            Log.d(TAG, "onCreate İzinler Verilmiş.");
            mPermissionGranted = true;
            islocationProviderEnable();

        } else {
            Log.d(TAG, "onCreate: İzinler Verilmemiş ");
            Permissions.myrequestPermission(this, Permissions.MapPermission);

        }


    }

    /**
     * Konum Bilgisi Alınıyorsa  İşlemler gerçekleştirilecek.
     */
    public void islocationProviderEnable() {
        Log.d(TAG, "islocationProviderEnable: ÇAĞRILDI");
        isGPSEnabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            Log.d(TAG, "islocationProviderEnable: " + isGPSEnabled);
            gatherView();
            // setRecylerCategoryList();
            //  mCategoryModel.getDataModel();

        } else {
            Log.d(TAG, "islocationProviderEnable: " + isGPSEnabled);
            Toast.makeText(this, "Please turn onn GPS", Toast.LENGTH_LONG).show();
            showGPSDisabledAlertToUser();

        }

    }


    public void profilPage(View view) {

        Intent intent = new Intent(getApplicationContext(), profilPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public void jobSearch(View view) {


        Intent intent = new Intent(this, JobSearch.class);
        startActivity(intent);

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }

    public void addJobAdvert(View view) {

        Intent intent = new Intent(this, AddJobAdvert.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }

    /**
     * Kullanıcının GPS konumunu açması için açılan settings penceresinin sonucu burada işleniyor.
     * Konumu Açarsa -> kontrol edilip islocationProviderEnable();
     * Açmaz ise Activity sonlandır.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 1" + resultCode + requestCode);

        //isGPSEnabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER);

        switch (requestCode) {

            case 4:
                if (loc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    Log.d(TAG, "onActivityResult: " + resultCode);
                    isGPSEnabled = true;
                    islocationProviderEnable();

                } else {
                    //finish();
                }

                break;
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "onRequestPermissionsResult: İzin VERİLMİŞ " + permissions[0]);
                    mPermissionGranted = true;
                    islocationProviderEnable();

                } else {
                    mPermissionGranted = false;
                    permissionNotGrantedDialog();

                    Log.d(TAG, "onRequestPermissionsResult: İzin Verilmemiş ");


                }

        }


    }

    /**
     * 3 Saniyeliğine bir alert mesajı gösterip Activityi sonlandırıyorum ..
     */
    public void permissionNotGrantedDialog() {
        Log.d(TAG, "permissionNotGrantedDialog: ");
        Permissions.showAlertdilaog(UserIntro.this, "",
                "İZİN VERMEN LAZIM KARDEŞ ", 3000).show();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                //finish();


            }
        };
        handler.postDelayed(r, 3000);
    }

    public void gatherView() {

        //profile = (Button) findViewById(R.id.profileButon);
        profileLayout = findViewById(R.id.profileLayout);
        emptyRecommendList = findViewById(R.id.emptyRecommendList);
        recyclerViewJobAdvert = findViewById(R.id.recommendedList);
        suggestListProgress = findViewById(R.id.suggestListProgress);
        mSlidingPanelLayout = findViewById(R.id.main_sliding_panel);
        scrollableView = findViewById(R.id.scrollableView2);
        companyLogo1 = findViewById(R.id.companyLogo1);
        companyname = findViewById(R.id.companyname);
        sector = findViewById(R.id.sector);
        tvbasvuru_count = findViewById(R.id.tvbasvuru_count2);
        tvjobname = findViewById(R.id.tvjobname);
        publishdate = findViewById(R.id.publishdate);
        tvdistance = findViewById(R.id.tvdistance);
        init();
    }

    private void init() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        new DeviceLocation(this, this).getDeviceLocation();
        suggestionsKeys = this;
        suggestionHash = new HashMap<String, ArrayList<SuggestionsModel>>();
        callbackResultList2 = new ArrayList<>();
        fromJobSector = new ArrayList<>();
        fromCompanyJobList = new ArrayList<>();
        myAppliedAdvertJobIdLList = new ArrayList<>();
        slidingPanelListener();
        slidingPanelInit();


    }

    private void slidingPanelInit() {
        Log.d(TAG, "slidingPanelInit: ");
        ///  LayoutInflater layoutInflater =
        View jobadvertdetails = getLayoutInflater().inflate(R.layout.jobadvertdetails, null);
        advertDetailsHolder = new AdvertDetails.ViewHolder(jobadvertdetails, this, this);
        scrollableView.addView(jobadvertdetails);

    }


    @Override
    public void deviceLocationCallback(LatLng gps) {
        myLocation = gps;
        getMyAppliedAdvert();
    }

    private void getMyAppliedAdvert() {


        myAppliedAdvert = new MyAppliedAdvert(this);
        FirebaseDatabase.getInstance().getReference().child("users_data").child(MainActivity.userID).child("applyAdvert").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "getMyAppliedAdvert->onDataChange: " + dataSnapshot);
                        myAppliedAdvert.execute(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });


    }

    /**
     * Basvuru yapılmıs ve kayıt altına alınmıs ilanların önerilenler listesinde gösterilmemesi için
     * MyAppliedAdvert ten gelen ilanların JobId lerini listeye alıyorum
     *
     * @param result
     */

    @Override
    public void getApliedAdvert(ArrayList<ApplyAdvertModel> result) {

        if (result != null && !result.isEmpty()) {

            for (ApplyAdvertModel applyAdvertModel : result) {

                Log.d(TAG, "getApliedAdvert: " + applyAdvertModel.getJobID());
                myAppliedAdvertJobIdLList.add(applyAdvertModel.getJobID());


            }


        }
        getSuggestionsKey();

    }

    /**
     * get Suggestions Key
     */
    private void getSuggestionsKey() {

        /*
        Suggestion Keylerini alıyoruz var ise keylere göre itemler çekiyoruz.
         */

        FirebaseDatabase.getInstance().getReference()
                .child("users_data")
                .child(MainActivity.userID)
                .child("suggestionKey").
                addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot singleSnaphot : dataSnapshot.getChildren()) {
                                    keyList = new ArrayList<SuggestionsModel>();

                                    Log.d(TAG, "onDataChange:singleSnaphot.getKey() " + singleSnaphot.getKey());

                                    for (DataSnapshot childValue : singleSnaphot.getChildren()) {
                                        keyList.add(new SuggestionsModel(childValue.getKey(), (Boolean) childValue.getValue()));
                                        counter++; //kac tane ilanın çekildiğini sayıyor getSuggestionsKeysCallback de kullanılmak üzere .Çnkü bu fonksyionun
                                        //durması için kaç tane ilanın çekildiğini bilmemiz lazım .
                                    }
                                    suggestionHash.put(singleSnaphot.getKey(), keyList);
                                }
                                suggestionsKeys.getSuggestionsKeysCallback(suggestionHash);
                                Log.d(TAG, "onDataChange:suggestionHash.entrySet() " + suggestionHash.entrySet());
                                Log.d(TAG, "onDataChange: suggestionHash.values().size()" + suggestionHash.values().size());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }


    /**
     * Alınan Keyleri ayrıştırıp ona göre ilanlar getiriyorum .
     *
     * @param keyResultHash
     */
    @Override
    public void getSuggestionsKeysCallback(HashMap<String, ArrayList<SuggestionsModel>> keyResultHash) {

        //***SUGGEST KEY YOK İSE BANA YAKIN TÜM İLANLARDAN ÖNERİ LİSTESİ OLUSTURULACAK ..
        if (keyResultHash.size() <= 0) {

            getJobNearMe();

        } else {
            //Suggest Keyler var ise Sektör bilgisine ve meslelğe ait ilanlar getirilecek
            for (Map.Entry<String, ArrayList<SuggestionsModel>> result : keyResultHash.entrySet()) {
                final SuggestJobAdvert mSuggestJobAdvert2 = new SuggestJobAdvert(this, myLocation, "Sektör Bilgisine Göre ", true, myAppliedAdvertJobIdLList);
                FirebaseDatabase.getInstance().getReference()
                        .child("jobAdvert").child("publishedAdverts")
                        .orderByChild("jobInfo/jobSector").equalTo(result.getKey())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //  sektoregore = true;
                                counter3++;
                                mSuggestJobAdvert2.execute(dataSnapshot);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                for (SuggestionsModel suggestionsModel : result.getValue()) {
                    final SuggestJobAdvert mSuggestJobAdvert = new SuggestJobAdvert(this, myLocation, suggestionsModel.getType(), false, myAppliedAdvertJobIdLList);
                    FirebaseDatabase.getInstance().getReference()
                            .child("jobAdvert").child("publishedAdverts")
                            .orderByChild("jobInfo/companyJob").equalTo(suggestionsModel.getJobInfo())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // sektoregore = false;

                                    mSuggestJobAdvert.execute(dataSnapshot);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                }


            }
        }

    }

    private void getJobNearMe() {
        getNearJobCounter++;
        final SuggestJobAdvert mSuggestJobAdvert = new SuggestJobAdvert(this, myLocation, "Yakınlarımda", true, myAppliedAdvertJobIdLList);
        Log.d(TAG, "SuggestJobAdvertCallback:else if (counter == 0)--> " + counter);
        FirebaseDatabase.getInstance().getReference()
                .child("jobAdvert")
                .child("publishedAdverts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        if (dataSnapshot.hasChildren()) {
                            mSuggestJobAdvert.execute(dataSnapshot);
                            counter++;
                        } else {
                            emptyRecommendList.setVisibility(View.VISIBLE);
                            suggestListProgress.smoothToHide();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    /**
     * @param callbackResultList -->Suggest Job Advert ten gelen ilanlar burada işleniyor.
     * @param sector->
     */
    @Override
    public void SuggestJobAdvertCallback(ArrayList<NearJobAdvertModel> callbackResultList, boolean sector) {

        Log.d(TAG, "SuggestJobAdvertCallback: " + callbackResultList);
        counter2++;
        //sektörden gelenler fromJobSector meslege göre gelenler fromCompanyJobList alınır .
        if (sector) {
            fromJobSector.addAll(callbackResultList);

        } else {
            fromCompanyJobList.addAll(callbackResultList);
        }

        //counter -->kac tane mesleğe göre ilan getirildiğini . Ayrıca suggestionkey yoksa kactane yakınımda ilan getirdğini sayıyıor
        //counter3-->kac tane sektöre göre ilan getirildiğini
        //Sonuc olarak gelen her ilan için bu method tetiklenir ve counter 2 artırılır .
        //meslege göre gelen ilanlar + sektöre göre gelen ilanlar =bu methodun kac kere calııstıgı -->>> eşit olursa da  liste doldurulur ç
        if (counter + counter3 == counter2) {
            Log.d(TAG, "SuggestJobAdvertCallback: " + counter2 + "\t" + counter + "\t" + counter3);
            if (!fromJobSector.isEmpty()) {


                if (fromCompanyJobList.isEmpty()) {
                    Log.d(TAG, "SuggestJobAdvertCallback:fromCompanyJobList.isEmpty() ");
                    setRecommendList(fromJobSector);

                } else {
                    // callbackResultList2 =

                    setRecommendList(checkList());
                }


            } else {
                Log.d(TAG, "SuggestJobAdvertCallback:JOB SEKTÖR LİSTESİ BOS YANIMDAKİLER ALINACAK . ");
                //yani olduda öenerilen listem dolu ama ben ordaki ilanların hepsine basvurmusum bana önerilecek olarak gelen ilan kalmamıs
                //ozaman yapcak bişe yok yakınımdaki tüm ilanları yeniden alcaz sayacları sıfırlıcaz.Her halükarda Sektör Listesi dolu olacagı için
                //bos oldugunda burası çalışcak .
                counter = 0;
                counter2 = 0;
                counter3 = 0;
                //getNearJobMe methodu ikikez çalıştı ve boş geldiyse empty View göster.
                if (getNearJobCounter > 2) {
                    emptyRecommendList.setVisibility(View.VISIBLE);
                    suggestListProgress.smoothToHide();
                } else {
                    getJobNearMe();

                }

            }
        }

    }

    private void setRecommendList(ArrayList<NearJobAdvertModel> _recommendList) {
        if (_recommendList != null && !_recommendList.isEmpty()) {
            Collections.sort(_recommendList, new Comparator<NearJobAdvertModel>() {
                @Override
                public int compare(NearJobAdvertModel nearJobAdvertModel, NearJobAdvertModel t1) {
                    //    Log.d(TAG, "compare: " + t1.getCompanyDistance() + "\t" + nearJobAdvertModel.getCompanyDistance());
                    return nearJobAdvertModel.getCompanyDistance().compareTo(t1.getCompanyDistance());
                }
            });

            mSuggestJobAdvertAdapter = new SuggestJobAdvertAdapter(this, _recommendList, this);
            recyclerViewJobAdvert.setAdapter(mSuggestJobAdvertAdapter);
            recyclerViewJobAdvert.enableViewScaling(true);
            recyclerViewJobAdvert.setItemAnimator(new DefaultItemAnimator());
            recyclerViewJobAdvert.setHasFixedSize(true);
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animt3);
            recyclerViewJobAdvert.setLayoutAnimation(layoutAnimationController);
            suggestListProgress.smoothToHide();
        }

    }

    /**
     * Burada sektöre göre getirdim ilanlar meslege göre getirdiğim ilanları kapsadıgı için aynı ilan iki kere eklenmesin diye
     * sektör listesinden ikilenen ilanları çıkarıyorum ve callbackResultList2 olusutuyorum .
     *
     * @return
     */
    private ArrayList<NearJobAdvertModel> checkList() {
        //  Log.d(TAG, "checkList: CAGRILDI ");
        callbackResultList2.addAll(fromCompanyJobList);
        for (NearJobAdvertModel asJob : fromCompanyJobList) {
            for (Iterator<NearJobAdvertModel> iterator = fromJobSector.iterator(); iterator.hasNext(); ) {
                NearJobAdvertModel model = iterator.next();
                if (asJob.getJobAdvertModel2().getCompanyJob().equals(model.getJobAdvertModel2().getCompanyJob())) {
                    iterator.remove();
                }
            }
        }
        callbackResultList2.addAll(fromJobSector);
        return callbackResultList2;

    }

    /**
     * Kullanıcı GPS Açık değilse bir alert dialog ile Gps açması gerektiğini söylüyorum
     */

    private void showGPSDisabledAlertToUser() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Cihazınızdan Konum Bilgisi Alınamıyor Açmak İçin Tamam'a Basın ").setCancelable(false)
                .setPositiveButton("Konumu Açın", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(myIntent, 4); //--->OnActivityRESULT


                    }
                });

        alertDialogBuilder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    @Override
    public void changeMarker(NearJobAdvertModel item, boolean isBasvuruOrSave) {
        Log.d(TAG, "changeMarker: GEREKSİZ BU SUAN ");

        mSuggestJobAdvertAdapter.notifyDataSetChanged();

    }

    @Override
    public void advertOnclickListener(NearJobAdvertModel model) {


        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        companyname.setText(model.getCompanyName());
        sector.setText(model.getJobSector());
        tvbasvuru_count.setText(String.valueOf(model.getCountApply()).concat(" Basvuru"));
        tvjobname.setText(model.getCompanyJob());
        publishdate.setText(HelperStaticMethods.getDateDifference(model.getPublishDate()));
        tvdistance.setText(model.getCompanyDistance().concat(" Uzaklıkta"));
        advertDetailsHolder.clearMap();
        advertDetailsHolder.setData(model);

    }

    private void slidingPanelListener() {
        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        // mSlidingPanelLayout.setAnchorPoint(0.7f);
        mSlidingPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {


            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //previous state Dragging
                // new State Expanded iken State Dragging olursa  yeni state hidden yapıyorum
                //Sliding Panel açıldıgında açıldıgını bir local değişkene bildiriyorum

                if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED &&
                        newState == SlidingUpPanelLayout.PanelState.DRAGGING) {

                    //  Log.d(TAG, "onPanelStateChanged: ACİLDİ ");
                    isOpened = true;

                }

                //Acıkken kapalı duruma geçmiş ise gizliyorum...
                if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {

                    if (isOpened) {
                        // Log.d(TAG, "onPanelStateChanged: KAPANIYORRR");
                        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        isOpened = false;
                    }

                }


            }
        });

    }
}
