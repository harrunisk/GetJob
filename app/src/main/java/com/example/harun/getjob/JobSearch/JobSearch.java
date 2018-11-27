package com.example.harun.getjob.JobSearch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.AddJobAdvert.ApplyAdvertModel;
import com.example.harun.getjob.AddJobAdvert.MyAppliedAdvert;
import com.example.harun.getjob.AddJobAdvert.RetainJobAdvertFromFirebase;
import com.example.harun.getjob.DeviceLocation;
import com.example.harun.getjob.JobSearch.JobUtils.AdvertDetails;
import com.example.harun.getjob.JobSearch.JobUtils.CategoryAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.CategoryModel;
import com.example.harun.getjob.JobSearch.JobUtils.DrawCircle;
import com.example.harun.getjob.JobSearch.JobUtils.FilterJobSearch;
import com.example.harun.getjob.JobSearch.JobUtils.GestureEvent;
import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.JobSearch.JobUtils.MyClusterMarker;
import com.example.harun.getjob.JobSearch.JobUtils.MyclusterManager;
import com.example.harun.getjob.JobSearch.JobUtils.NearJobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.UserLocationInfo;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * Created by mayne on 22.03.2018.
 */

public class JobSearch extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, FilterJobSearch.FilterCallbackInterface,
        LocationListener,
        DiscreteSeekBar.OnProgressChangeListener,
        View.OnClickListener,
        ClusterManager.OnClusterClickListener<NearJobAdvertModel>,
        ClusterManager.OnClusterItemClickListener<NearJobAdvertModel>,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnCameraChangeListener,
        ClusterManager.OnClusterItemInfoWindowClickListener<NearJobAdvertModel>,
        AdvertDetails.ChangeMarkerCluster,
        GoogleMap.OnMarkerDragListener,
        DownloadAdressData.adressCallback,
        RetainJobAdvertFromFirebase.JobAdvertFromFirebase,
        CategoryModel.doneCallback,
        CategoryAdapter.CategoryAdapterInterface,
        MyAppliedAdvert.AppliedAdvertCallback,
        DeviceLocation.DeviceLocationCallback, TextView.OnEditorActionListener {

    private static final String TAG = "JobSearch";

    //widgetlar

    private ImageView job_back2, job_back, searchjobImage2;
    private LinearLayout clearImage;
    private AutoCompleteTextView input_searchJob;
    private ArrayAdapter<String> jobListAdapter;
    private String choosenJob;
    private RecyclerView categoryRecycler;
    private TextView tvAdress, tvCircle, tvbasvuru_count;
    private SupportMapFragment mapFragment;
    private DiscreteSeekBar circleArea;
    private Button btnListNearJob;
    private TextView jobNameSlidingPanel, distanceToyou, changeAreaTv;
    private LinearLayout touchHandler, circleAreaLayout, searchDetail;
    AVLoadingIndicatorView adresLoading, mapProgress, categoryProgress;
    FloatingActionButton mylocButton, changeLocation, changeArea;
    ValueAnimator colorAnimator;
    CategoryAdapter mCategoryAdapter;
    private CategoryModel mCategoryModel;
    AdvertDetails.ViewHolder viewHolder;
    private int countLocation;
    DownloadAdressData downloadAdressData;
    private GoogleMap mMap;
    boolean mPermissionGranted;
    boolean isGPSEnabled;
    boolean isOpened = false;
    boolean firstClick = true;
    boolean isMyLocationAdress = false, isNewLocationAdress = false;
    boolean locationCheckChange = false;
    ViewGroup scrollableView;
    RelativeLayout slidingheader, searchLayout, searchDetailView;
    LocationManager locationManager;
    LocationManager loc;
    MapStyleOptions style;
    private Location mLastKnownLocation;
    MyClusterMarker myClusterMarker;
    private static final int DEFAULT_ZOOM = 13;
    public ClusterManager<NearJobAdvertModel> myItemClusterManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    List<DrawCircle> mCircleList = new ArrayList<>(1);
    //  ArrayList<LatLng> locationList;
    CustomInfoWindowCluster customInfoWindowCluster;
    private RetainJobAdvertFromFirebase retainJobAdvertFromFirebase;
    public SlidingUpPanelLayout mSlidingPanelLayout;
    private ArrayList<NearJobAdvertModel> nearJobList;
    private ArrayList<NearJobAdvertModel> allJobList;
    private ArrayList<String> jobList;
    private ArrayList<ApplyAdvertModel> myAppliedAdvertArrayList;
    public NonHierarchicalDistanceBasedAlgorithm clusterManagerAlgorithm;
    UserLocationInfo mUserLocationInfo;
    //private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String databaseName = "jobAdvert";
    private String sektorName = "";
    private LatLngBounds.Builder builder;
    private ProgressBar btnListNearJobProgress;
    private MyAppliedAdvert myAppliedAdvert;
    private Handler mainThreadHandler;
    private Runnable runNearJobList;
    //  private Task<ArrayList<NearJobAdvertModel>> task;
    // private TaskCompletionSource<ArrayList<NearJobAdvertModel>> arrayListTask;
    private int newOrCurrentLoc = 1;//1 bulundugun konum 2 yeni konum

    /**
     * Yayında olan
     * İlanları almak için Firebase baglantısını acıyoruz actıgımız baglantıyı retainJobAdvert AsyncTask methoduna gönderiyoruz.
     */
    private void getAllJobLocation() {
        Log.d(TAG, "GetAllJobLocation: ÇAĞRILDI");
        sektorName = "Tüm İlanlar";
        retainJobAdvertFromFirebase = new RetainJobAdvertFromFirebase(this, this);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "GetAllJobLocation->onDataChange: dataSnapShot");
                retainJobAdvertFromFirebase.execute(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ERRORR");
            }
        });
    }

    /**
     * Daha öncesinden basvuru yapmıs oldugum ilanları getir
     */
    private void getMyAppliedAdvert() {

        mapProgress.setVisibility(View.VISIBLE);
        myAppliedAdvert = new MyAppliedAdvert(this);
        FirebaseDatabase.getInstance().getReference().child("users_data").child(UserIntro.userID).child("applyAdvert").
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

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart:@@@@@@@@@@@@@@@q ");
        //getAllJobLocation();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  btnListNearJobProgress.setVisibility(View.GONE);
        //  btnListNearJob.clearAnimation();
        // colorAnimator.end();
        Log.d(TAG, "onPause: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@qqqq");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobsearch23);

        loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        /**
         * Burada permission kontrolleri kaldırılabilir . Çünkü uygulama ilk acıldıgında UserIntro Activty de bu izinleri alıyorum
         * Burdaa Sadece konum bilgisi alınnnıp alınmadıgının kontrolu yapılacak.
         */
        if (Permissions.checkPermissionArray(this, Permissions.MapPermission)) {
            Log.d(TAG, "onCreate İzinler Verilmiş.");
            mPermissionGranted = true;
            islocationProviderEnable();

        } else {
            Log.d(TAG, "onCreate: İzinler Verilmemiş ");
            Permissions.myrequestPermission(this, Permissions.MapPermission);

        }

    }


    //Widget Xml Baglantıları
    private void gatherViews() {
        Log.d(TAG, "gatherViews: ÇAĞRILDI");
        categoryRecycler = findViewById(R.id.categoryList);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        tvAdress = findViewById(R.id.tvAdress);
        tvCircle = findViewById(R.id.tvCircle);
        circleArea = findViewById(R.id.circleArea);
        btnListNearJob = findViewById(R.id.btnListNearJob);
        changeLocation = findViewById(R.id.changeLocation);
        mSlidingPanelLayout = findViewById(R.id.sliding_layout);
        jobNameSlidingPanel = findViewById(R.id.JobName);
        distanceToyou = findViewById(R.id.distanceToyou);
        tvbasvuru_count = findViewById(R.id.tvbasvuru_count);
        changeArea = findViewById(R.id.changeArea);
        changeAreaTv = findViewById(R.id.changeAreaTv);
        circleAreaLayout = findViewById(R.id.circleAreaLayout);
        touchHandler = findViewById(R.id.touchHandler);
        mylocButton = findViewById(R.id.myLocButton);
        scrollableView = findViewById(R.id.scrollableView);
        slidingheader = findViewById(R.id.header);
        adresLoading = findViewById(R.id.adresLoading);
        mapProgress = findViewById(R.id.mapProgress);
        categoryProgress = findViewById(R.id.categoryProgress);
        job_back = findViewById(R.id.job_back);
        job_back2 = findViewById(R.id.job_back2);
        btnListNearJobProgress = findViewById(R.id.btnListNearJobProgress);
        //clearImage = findViewById(R.id.clearLayout);
        searchjobImage2 = findViewById(R.id.search);
        input_searchJob = findViewById(R.id.input_searchJob);
        searchLayout = findViewById(R.id.searchLayout);
        searchDetail = findViewById(R.id.searchDetail);
        init();
    }

// Değişkenlerin  İlklemeleri

    private void init() {
        Log.d(TAG, "init: ÇAĞRILDI");
        myRef = FirebaseDatabase.getInstance().getReference().child(databaseName).child("publishedAdverts");
        mCategoryModel = new CategoryModel(getApplicationContext(), this, null);
        //getAllSectorFromFirebase = new GetAllSectorFromFirebase(getApplicationContext(), this);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        nearJobList = new ArrayList<>();
        allJobList = new ArrayList<>();
        jobList = new ArrayList<>();
        mUserLocationInfo = UserLocationInfo.getInstance();//USERLOCATİON INFO SİNGLETON NESNESİ ...
        style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro); //Map Style Retro
        downloadAdressData = new DownloadAdressData(this, this, 1);
        btnListNearJob.setOnClickListener(this);
        changeArea.setOnClickListener(this);
        job_back2.setOnClickListener(this);
        searchjobImage2.setOnClickListener(this);
        searchDetail.setOnClickListener(this);
        input_searchJob.setOnEditorActionListener(this);
        //  clearImage.setOnClickListener(this);
        job_back.setOnClickListener(this);
        slidingPanelListener();
        DiscreteSeekBarRange();
        slidingPanelAdvertInfo();
        slidingheader.setOnTouchListener(new GestureEvent(this));

    }


    @Override
    public void onBackPressed() {

        if (mSlidingPanelLayout != null &&
                (mSlidingPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlidingPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            // Log.d(TAG, "onBackPressed: SLİDİNG PANEL KAPANDI");
            mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            //  Log.d(TAG, "onBackPressed: TELEFONUN GERİ tusuna Basıldı");
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
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
            gatherViews();
            // setRecylerCategoryList();
            mCategoryModel.getSectorList();


        } else {
            Log.d(TAG, "islocationProviderEnable: " + isGPSEnabled);
            Toast.makeText(this, "Please turn onn GPS", Toast.LENGTH_LONG).show();
            showGPSDisabledAlertToUser();

        }

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
        Permissions.showAlertdilaog(JobSearch.this, "",
                "İZİN VERMEN LAZIM KARDEŞ ", 3000).show();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                finish();
            }
        };
        handler.postDelayed(r, 3000);
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
                    finish();
                }

                break;
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: ÇAĞRILDI");
        mMap = googleMap;

        setupMap(mMap); //--->Harita Özelliklerini gerçekleştir.

        mylocButton.setOnClickListener(this);
        changeLocation.setOnClickListener(this);
        myItemClusterManager = new ClusterManager<NearJobAdvertModel>(this, mMap);
        clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();
        myItemClusterManager.setAlgorithm(clusterManagerAlgorithm);
        myClusterMarker = new MyClusterMarker(this, mMap, myItemClusterManager);
        MyclusterManager.getInstance().setMyClusterMarker(myClusterMarker);
        customInfoWindowCluster = new CustomInfoWindowCluster(getApplicationContext());
        // myItemClusterManager.setRenderer(myClusterMarker); //Cluster style
        myItemClusterManager.setRenderer(myClusterMarker); //Cluster style
        myItemClusterManager.setOnClusterClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(myItemClusterManager);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(myItemClusterManager);
        myItemClusterManager.setOnClusterItemInfoWindowClickListener(this);
        myItemClusterManager.setOnClusterItemClickListener(this);
        //  getDeviceLocation();  // haritayı son konuma aktif etme
        new DeviceLocation(this, this).getDeviceLocation();
        getMyAppliedAdvert();
        // getAllJobLocation();
        MyclusterManager.getInstance().setMyClusterManager(myItemClusterManager);//clsuterManager Global bir sınıfa alıyorum ..
        circleArea.setOnProgressChangeListener(this);
        mMap.setInfoWindowAdapter(myItemClusterManager.getMarkerManager());
        myItemClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(customInfoWindowCluster);

        myItemClusterManager.cluster();

    }


    //Haritanın özelliklerini bu fonksiyonda ayarlıyorum .
    @SuppressLint("MissingPermission")
    public void setupMap(GoogleMap _mMap) {
        Log.d(TAG, "setupMap:Çagırıldı ");
        _mMap.setMapStyle(style);
        _mMap.getUiSettings().setZoomControlsEnabled(false);
        _mMap.getUiSettings().setCompassEnabled(false);
        _mMap.getUiSettings().setMapToolbarEnabled(false);
        _mMap.setMyLocationEnabled(false);
        _mMap.getUiSettings().setMyLocationButtonEnabled(false);
        _mMap.setBuildingsEnabled(true);
        _mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        _mMap.setPadding(0, 280, 0, 0);
    }

   /* @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation:ÇAĞRILDI ");
        // isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {

            Log.d(TAG, "getCurrentLocation: " + "Konum Bİlgisi Yok");

        } else {
            if (isNetworkEnabled) {
                Log.d(TAG, "getCurrentLocation: isNetworkEnabled");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                Log.d(TAG, "getCurrentLocation: isGPSEnabled ");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d(TAG, "getCurrentLocation: drawCurrentLocationMarker Çağrılıyor");
            mLastKnownLocation = location; ///Şuanki konum bilinen son konum olarak atanıyor .
            drawCurrentLocationMarker(mLastKnownLocation);

        }
    }
*/
 /*   private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: ÇAĞRILDI");
        try {
            if (mPermissionGranted) {
                //   Log.d(TAG, "getDeviceLocation: mPermissionGranted " + mPermissionGranted);
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                Log.d(TAG, "onComplete: mLastKnownLocation" + mLastKnownLocation);
                                //LatLng gps = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                drawCurrentLocationMarker(mLastKnownLocation);
                            } else {
                                Log.d(TAG, "onComplete: mLastKnownLocation NULLL GET CURRENT LOCATİON ");
                                getCurrentLocation();
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            getCurrentLocation();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
*/

    /**
     * Kullanıcının buludugu konuma marker ekler
     *
     * @param location-->Kullanıcının bulungu konum
     */
    private void drawCurrentLocationMarker(Location location) {

        if (checkReadyMap()) {   //Map hazırmı değilmi kontrolü
            Log.d(TAG, "drawCurrentLocationMarker: ÇAĞRILDI");
            countLocation = 1;

            Drawable currentMarkerStyle = getResources().getDrawable(R.drawable.markeruser);
            BitmapDescriptor icon = MapHelperMethods.getDrawableMarkerAsBitmap(currentMarkerStyle);
            LatLng gps = MapHelperMethods.convertLocationtoLatLng(location);
            isMyLocationAdress = true;
            mUserLocationInfo.setMyLocation(gps);  //-->Bulundugum konumu kayıt et..
            downloadAdressData.execute(gps);//--->adres Bilgisini alıyoruz..
            Marker currentLocationMarker1;
            currentLocationMarker1 = mMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .icon(icon)
                    .title("Buradasınız"));

            currentLocationMarker1.showInfoWindow();
            mUserLocationInfo.setCurrentLocationMarker(currentLocationMarker1);

           /* mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    getCameraPosition(mUserLocationInfo.getMyLocation())),
                    new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            Log.d(TAG, "onFinish: mMap.animateCamera burası düzenlenecek.");
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "onCancel: mMap.animateCamera");

                        }
                    });*/
            DrawCircle drawCircle = new DrawCircle(location, 1000, mMap, getApplicationContext(), 1);//İlk Circle 1-km Alanı kapsayacak
            mCircleList.add(drawCircle); //Bulunan konuma 1 circle atıyorum bu circle daha sonra ulasabilmek adına circle olusturan sınıfı
            //nesnesini listede tutuyorum Daha sonraki circle değişimleri bu nesne üzerinden gerçekleştiriyorum..

        }

    }

    /**
     * Yeni Lokasyon Markeri
     *
     * @param center
     */
    private void drawNewLocationMarker(LatLng center) {

        countLocation = 2;
        Drawable drawable = getResources().getDrawable(R.drawable.markeruser2);
        // drawable.setTint(getColor(R.color.jumbo));
        BitmapDescriptor bitmapDescriptorFactory = MapHelperMethods.getDrawableMarkerAsBitmap(drawable);
        mUserLocationInfo.setNewLocationLatLng(center);//Yeni lokasyonun latlng değerini kayıt ediyorum daha sonra ulasabilmek adına ..
        Marker newLocationMarker1;
        newLocationMarker1 = mMap.addMarker(
                new MarkerOptions().position(center).title("Yeni konumunuz").icon(bitmapDescriptorFactory));
        mUserLocationInfo.setNewLocationMarker(newLocationMarker1);
        runAdressMethod(center);

    }


    private void slidingPanelListener() {
        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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

    /**
     * Kullanıcının 2 Lokasyonu varsa location butonuna
     * 1.tıklayısı mor olan gercek konumuna 2.tıklayısı mavi olan yeni markere
     * yönlendirilecek
     *
     * @return 1--mor Gerçek konum ---2 mavi Yeni Konum
     */
    private int myLocButtonStateCheck() {

        //1 MArker varsa
        if (countLocation == 1) {

            newOrCurrentLoc = 1;
            return 1;
        } else {
            //1 marKer yoksa 2 marker vardır
            //İlk tıklayısta firstClick true ..
            // yani 1 .tıklayısta mavi 2.tıklayısta kırmızı olcak locationbutton daki marker rengi
            if (firstClick) {

                mylocButton.setImageDrawable(getDrawable(R.drawable.markeruser2));
                //mylocButton.setImageTintList(ColorStateList.valueOf(Color.BLUE));
                firstClick = false;
                newOrCurrentLoc = 1;
                return 1;

            } else {
                mylocButton.setImageDrawable(getDrawable(R.drawable.markeruser));

                // mylocButton.setImageTintList(ColorStateList.valueOf(Color.RED));
                firstClick = true;
                newOrCurrentLoc = 2;
                return 2;

            }

        }


    }


    private boolean checkReadyMap() {
        if (mMap == null) {
            Toast.makeText(this, "MAP READY CONTROL", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /*  @Override
      protected void onResumeFragments() {
          super.onResumeFragments();
          //  Permission İzin Verilmemiş ise burada alert dialog konulacak .
          if (!mPermissionGranted) {

              Log.d(TAG, "onResumeFragments:");
          }
      }

  */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: ");
        return true;

    }


    /**
     * BURASI HATALI ÇALIŞABİLİR TEST ETMEDİMM ...
     * <p>
     * ------------------DÜZENLENECEK METHOD----------------------
     */

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged:MEthodu  " + location.getLatitude());

        // for (DrawCircle drawCircle : mCircleList) {
        try {
            if (!mCircleList.isEmpty()) {
                Log.d(TAG, "onLocationChanged: " + mCircleList);
                DrawCircle drawCircle = mCircleList.get(0);

                if (drawCircle != null) {
                    Log.d(TAG, "onLocationChanged: DRAWCİRCLE NOT NULL" + drawCircle);
                    drawCircle.setCenterCircle(MapHelperMethods.convertLocationtoLatLng(location)); //-->Circle kaldırıyorum
                    // mCircleList.clear();//-->Circle listesini temizliyorum
                    mUserLocationInfo.getCurrentLocationMarker().remove(); //Şuanki markeri kaldırıyorum
                    mLastKnownLocation = location; //değişen lokasyon değerlerini son bilinen lokasyonuma eşitliyorum
                    drawCurrentLocationMarker(mLastKnownLocation); //son lokasyona marker, koyuyorum.
                    locationManager.removeUpdates(this);
                } else {
                    Log.d(TAG, "onLocationChanged: DRAWCİRLCE NULL ");
                    mUserLocationInfo.getCurrentLocationMarker().remove(); //Şuanki markeri kaldırıyorum
                    mLastKnownLocation = location; //değişen lokasyon değerlerini son bilinen lokasyonuma eşitliyorum
                    drawCurrentLocationMarker(mLastKnownLocation); //son lokasyona marker, koyuyorum.
                    locationManager.removeUpdates(this);

                }
            } else {
                Log.d(TAG, "onLocationChanged: mCircleList NULL");
                mUserLocationInfo.getCurrentLocationMarker().remove(); //Şuanki markeri kaldırıyorum
                mLastKnownLocation = location; //değişen lokasyon değerlerini son bilinen lokasyonuma eşitliyorum
                drawCurrentLocationMarker(mLastKnownLocation); //son lokasyona marker, koyuyorum.
                locationManager.removeUpdates(this);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     * Seek Bar 10 Km e kadar normal değer 10dan sonra 15 20 25 30 100 Olacagı icin bir transform işlemi yapıldı.
     */
    public void DiscreteSeekBarRange() {


        circleArea.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {

                if (value <= 10) {

                    return value;
                } else if (value == 11) {
                    return (value - 1) + 5;
                } else if (value == 12) {

                    return (value - 2) + 10;
                } else if (value == 13) {
                    return (value - 3) + 15;
                } else if (value == 14) {
                    return (value - 4) + 20;
                } else if (value == 15) {
                    return (value - 5) * 5;
                } else if (value == 16) {
                    return (value - 6) * 10;
                } else
                    return value;

            }
        });


    }

    //SEEK BAR METHODLARI
    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if (fromUser) {
            int updatedValue = 0;
            if (value <= 10) {   //10 a kadar olan bölümde normal artış

                updatedValue += value;
            } else if (value == 11) {
                updatedValue += (value - 1) + 5;//15
            } else if (value == 12) {
                updatedValue += (value - 2) + 10;//20
            } else if (value == 13) {
                updatedValue += (value - 3) + 15;//25
            } else if (value == 14) {
                updatedValue += (value - 4) + 20;//30
            } else if (value == 15) {
                updatedValue += (value - 5) * 5;//50
            } else if (value == 16) {
                updatedValue += (value - 6) * 10;//100
            }
            tvCircle.setText(updatedValue + "km");

            //mCircle Listesinden şu anki  circle alınarak yeni circle verilen uzaklık değerine göre(yarıçap)  olusturuluyor
            //
            DrawCircle drawCircle = mCircleList.get(0);
            drawCircle.setCircleRadius(updatedValue);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//       mMap.clear();
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }


    /**
     * Circle kapsama alanına giren itemleri result list içersine ekliyorum.Bu method yakınımdakileri listele dedğinde
     * çalışacak ve Bu listeyi işlemek üzere NearJobListActivty 'e gönderecek ->YakınımdakileriListele Butonu .. ..
     */

    public void getNearJobList() {

        btnListNearJobProgress.setVisibility(View.VISIBLE);

        try {
            double distance;
            DrawCircle drawCircle = mCircleList.get(0);
            nearJobList.clear(); //her çağırıldıgında önceki listeyi temizleyecek ..
            allJobList.clear();
            //Tüm Cluster edilmiş itemleri alıyorum ...
            ArrayList<NearJobAdvertModel> items = (ArrayList<NearJobAdvertModel>) clusterManagerAlgorithm.getItems();
            Log.d(TAG, "getNearJobList: clusterManagerAlgorithm.getItems() " + items);
            UserLocationInfo.getInstance().setCircleArea(drawCircle.getCircleRadius());
            //  Log.d(TAG, "getNearJobList: " + items.get(0).toString() + circleArea.getProgress());
            for (NearJobAdvertModel item : items) {

                Log.d(TAG, "getNearJobList: NearJobAdvertModel item : items " + item);

                distance = MapHelperMethods.getDistanceParce(
                        MapHelperMethods.toRadiusMeters(
                                drawCircle.getCenterCircle(), item.getPosition()));

                item.setCompanyDistance(String.format(Locale.getDefault(), "%.2f", distance)); //Circle merkezi ile markerlar arası mesafeyi ölçüp ekliyorum.
                //  item.setCompanyDistance(String.valueOf(distance)); //Circle merkezi ile markerlar arası mesafeyi ölçüp ekliyorum.
                allJobList.add(item);
                // Circle alanı içerisinde girenler marker ları sonuç listesine  NearJobListActivtye gönderme üzere ekliyorum.
                if (distance <= (drawCircle.getCircleRadius())) {
                    Log.d(TAG, "getNearJobList: " + distance + "\t" + drawCircle.getCircleRadius() + "\t" + drawCircle.getCenterCircle());
                    nearJobList.add(item);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "getNearJobList:SONUC Listesi@@@" + nearJobList);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnListNearJob:
                startNewAnimation();
                getNearJobList();
                //MainThread den 4 saniye gecikmeli  bu methodu çalıştırıyorum . Progress işlemi doğru calışması için
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Yakındaki daire içerisindeki işler listelenecek
                        //     if (nearJobList.size() > 0) {
                        if (allJobList.size() > 0) {
                            Log.d(TAG, "resultListSize>0: ");
                            final Intent i = new Intent(getApplicationContext(), NearJobListActivity.class);
                            // i.setFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                            i.putParcelableArrayListExtra("nearList", nearJobList);
                            i.putParcelableArrayListExtra("allJobList", allJobList);
                            i.putExtra("nearListSize", nearJobList.size());
                            i.putExtra("allJobListSize", allJobList.size());
                            i.putExtra("Category", sektorName);
                            i.putExtra("whichLocation", newOrCurrentLoc);
                            // colorAnimator.cancel();
                            btnListNearJob.clearAnimation();
                            btnListNearJobProgress.setVisibility(View.GONE);
                            colorAnimator.end();
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        } else {
                            MyCustomToast.showCustomToast(getApplicationContext(), "Bu Sektörde iş bulunamadı.");
                            //Toast.makeText(getApplicationContext(), "Kapsama alanınızda Size uygun iş bulunamadı.", Toast.LENGTH_SHORT).show();
                            btnListNearJob.clearAnimation();
                            btnListNearJobProgress.setVisibility(View.GONE);
                            colorAnimator.end();
                        }
                    }
                }, 4000);
                break;

            case R.id.myLocButton:
                Log.d(TAG, "onClick: MYLOC BUTTON TIKLANDI");
                DrawCircle drawCircle = mCircleList.get(0);
                if (myLocButtonStateCheck() == 1) {
                    mUserLocationInfo.getCurrentLocationMarker().showInfoWindow();
                    tvAdress.setText(mUserLocationInfo.getMyLocationAdress());
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(mUserLocationInfo.getMyLocation())));
                    drawCircle.setCenterCircle(mUserLocationInfo.getMyLocation());

                } else {
                    Log.d(TAG, "onClick: NewLocationButon Tıklandı");
                    mUserLocationInfo.getNewLocationMarker().showInfoWindow();
                    tvAdress.setText(mUserLocationInfo.getNewLocationAdress());
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(mUserLocationInfo.getNewLocationLatLng())));
                    drawCircle.setCenterCircle(mUserLocationInfo.getNewLocationLatLng());
                }


                break;

            case R.id.changeLocation:
                Log.d(TAG, "onClick: Change Location");
                newLocation(view, "Sabitle");

                break;
            case R.id.searchDetail:
                Log.d(TAG, "onClick: FilterSearch");
                Bundle b = new Bundle();
                FilterJobSearch filterJobSearch = new FilterJobSearch();
                filterJobSearch.show(getSupportFragmentManager(), "FilterSearch");
                b.putString("SektorName", sektorName);
                b.putString("JobName", choosenJob);
                filterJobSearch.setArguments(b);

                break;
            case R.id.changeArea:
                Log.d(TAG, "onClick: CHANGEAREa");
                if (!circleArea.isShown()) {
                    circleAreaLayout.setVisibility(View.VISIBLE);
                    changeAreaTv.setVisibility(View.GONE);
                    circleAreaLayoutSetAnimation(circleAreaLayout);
                    changeArea.setImageDrawable(getResources().getDrawable(R.drawable.circular_area_cancel));

                } else {
                    circleAreaLayout.setVisibility(View.GONE);
                    changeAreaTv.setVisibility(View.VISIBLE);
                    changeArea.setImageDrawable(getResources().getDrawable(R.color.white));
                    changeAreaTv.setText(String.valueOf(mCircleList.get(0).getCircleRadius()).concat("km"));
                }

                break;

//            case R.id.search_job:
//                hideKeyboard();
//                searchJob();
//
//                break;

            case R.id.job_back2:
                unRevealAnim();
                break;
            case R.id.search:

                if (searchLayout.isShown()) {

                    unRevealAnim();
                } else {
                    circularRevealAnim();

                }

                break;

            case R.id.clearLayout:

                input_searchJob.setText("");
                input_searchJob.clearFocus();


                break;

            case R.id.job_back:

                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;


        }
    }

    private void unRevealAnim() {

        float finalRadius = (float) (Math.max(searchLayout.getWidth(), searchLayout.getHeight() * 1.1));
        Animator animationUtils = ViewAnimationUtils.createCircularReveal(searchLayout, searchLayout.getWidth(), searchLayout.getHeight() / 2, finalRadius, 0);
        animationUtils.setInterpolator(new AccelerateDecelerateInterpolator());
        animationUtils.setDuration(400);

        animationUtils.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                searchLayout.setVisibility(View.GONE);
                hideKeyboard();

            }
        });


        animationUtils.start();

    }

    private void circularRevealAnim() {
        float finalRadius = (float) (Math.max(searchLayout.getWidth(), searchLayout.getHeight() * 1.1));
        Animator animationUtils = ViewAnimationUtils.createCircularReveal(searchLayout, searchLayout.getWidth(), searchLayout.getHeight() / 2, 0, finalRadius);
        animationUtils.setInterpolator(new AccelerateDecelerateInterpolator());
        animationUtils.setDuration(400);
        searchLayout.setVisibility(View.VISIBLE);
      /*  animationUtils.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
*/
        animationUtils.start();


    }

    private SimpleTooltip createToolTip(View view, String message) {

        return new SimpleTooltip.Builder(this)
                .anchorView(view)
                .text(message)
                .gravity(Gravity.START).dismissOnOutsideTouch(false)
                //   .overlayMatchParent(false)
                .animated(true).backgroundColor(getColor(R.color.white)).arrowColor(getColor(R.color.white))
                //  .transparentOverlay(false)
                .build();

    }


    private void searchJob() {
        if (choosenJob != null && !choosenJob.isEmpty()) {
            mapProgress.setVisibility(View.VISIBLE);
            Log.d(TAG, "onClick searchJobImage: " + choosenJob);
            final RetainJobAdvertFromFirebase retainJobAdvertFromFirebase1 = new RetainJobAdvertFromFirebase(this, this);
            Query query = myRef.orderByChild("jobInfo/companyJob").equalTo(choosenJob);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    retainJobAdvertFromFirebase1.execute(dataSnapshot);
                    Log.d(TAG, "LoadAdvertWithSectorInfo->>onDataChange: " + dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError);
                }
            });


        } else {


            Log.d(TAG, "onClicksearchJobImage: BOSSSSSS" + choosenJob);
        }

    }

    /**
     * Circle Area Animation ..
     *
     * @param layout
     */
    private void circleAreaLayoutSetAnimation(LinearLayout layout) {

        Log.d(TAG, "circleAreaLayoutSetAnimation: ");

        int anim = R.anim.layout_anim2;
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), anim);
        layout.setLayoutAnimation(layoutAnimationController);


    }


    /**
     * Yakınımdakileri Listele Butonu Animasyonu .LayerList backLayerin Renk değiştirme animasyonu .
     */
    private void startNewAnimation() {
        //  int colorFrom = getResources().getColor(R.color.mycolor);
        //  int colorTo = getResources().getColor(R.color.Aqua);
        Log.d(TAG, "startNewAnimation: ");
        LayerDrawable shape = (LayerDrawable) btnListNearJob.getBackground();
        final GradientDrawable drawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.back_layer);
        //    final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), 0, 1);
        colorAnimator = ValueAnimator.ofFloat(0, 1);
        final float[] hsv;
        hsv = new float[3]; // Transition color
        hsv[1] = 1;
        hsv[2] = (float) 0.8;
        colorAnimator.setDuration(2000);
        //  colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.setRepeatCount(Animation.INFINITE);

        // colorAnimator.setRepeatMode(ValueAnimator.RESTART);

        colorAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "onAnimationEnd: ");
                colorAnimator.reverse();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                // Log.d(TAG, "onAnimationUpdate: ");
                                                int runColor;
                                                hsv[0] = 360 * valueAnimator.getAnimatedFraction();
                                                runColor = Color.HSVToColor(hsv);
                                                drawable.setColor(runColor);
                                                btnListNearJobProgress.getIndeterminateDrawable().setColorFilter(runColor, PorterDuff.Mode.SRC_IN);

                                            }
                                        }
        );
        colorAnimator.start();

    }

    /**
     * Normal Kamera pozisyonu ...
     *
     * @param _center-->latlng
     * @return
     */
    private CameraPosition getCameraPosition(LatLng _center) {
        //bearing(90)
        return new CameraPosition.Builder().target(_center).zoom(DEFAULT_ZOOM).tilt(30).build();
    }


    private void animateCameraWithBounds(LatLngBounds _bounds) {

        try {
            //  LatLngBounds bounds = boundsWithCenterAndLatLngDistance(centerPointPosition, 12000, 12000); // 12000 in meters

            // Log.d(TAG, "animateCameraWithBounds: ");
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(_bounds, 150));
            //  mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Yeni lokasyon belirleme methodu
     *
     * @param view
     * @param message
     */
    private void newLocation(View view, String message) {
        Log.d(TAG, "newLocation: Methodu Çağrıldı");

        newOrCurrentLoc = 2;//Yeni konum oldugunu belirtiyorum
        //Burada önce camreanın tam ortası bir marker koyuyorum Kamera Değiştikçe Pozisyonunu değiştircem..

        /**
         * Lokasyon değiştirme butonuna tıkladıgında ilk tıklamada
         * Cameranın orta noktası alınıp marker koyulacak ...
         */

        final LatLng center = mMap.getCameraPosition().target;
        DrawCircle drawCircle = mCircleList.get(0);
        if (!locationCheckChange) {
            //İlk defa tıklandıgı durumdaki marker koyma işlemleri ...
            locationCheckChange = true;
            createToolTip(view, message).show();
            changeLocation.setImageDrawable(getResources().getDrawable(R.drawable.markeranchor));
            Log.d(TAG, "changeMyLocation: " + center);
            if (mUserLocationInfo.getNewLocationMarker() == null) {
                //Daha öncesinde koyulmus bir marker yoksa
                Log.d(TAG, "changeMyLocation: Marker NulL  Yenisi yapılacak");
                drawNewLocationMarker(center);

            } else {
                //---> önceden koyulmus olan marker varsa markeri sil circle center güncelle ..
                mUserLocationInfo.getNewLocationMarker().remove();
                drawCircle.setCenterCircle(center);
                drawNewLocationMarker(center);
            }


        } else {
            ///-->İŞlemler tamamlandıgında
            locationCheckChange = false;
            changeLocation.setImageDrawable(getResources().getDrawable(R.drawable.placeholders));
            mUserLocationInfo.setNewLocationLatLng(mMap.getCameraPosition().target);
            isNewLocationAdress = true; //Artık kullanıcı yeni yeri seçtiğinde seçtiği son yer adresi olarak atanacak..
            runAdressMethod(mUserLocationInfo.getNewLocationLatLng());
            drawCircle.setCenterCircle(mUserLocationInfo.getNewLocationLatLng());

        }


    }


    //Clustere tıklanınca içerisindeki itemlerin oldugu bölgenin açılıp kameranın oraya hareket etmesini istiyorum .
    @Override
    public boolean onClusterClick(Cluster<NearJobAdvertModel> cluster) {

        Log.d(TAG, "onClusterClick:Tıklandı");
        LatLngBounds.Builder builder2 = LatLngBounds.builder();


        //Sınır bölgelerini al
        for (ClusterItem item : cluster.getItems()) {

            builder2.include(item.getPosition());
        }
        LatLngBounds bounds = builder2.build();
        //final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        // final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        // final int zoomPadding = (int) (zoomWidth * 0.10); // Genişliğin 0,10 u kadar kenarlardan boşluk
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * @param latLng alınan konumun adresini öğrenmek üzere DownloadAdressData methodunu cagırıyor.
     *               Bu method async method oldugunda her konum isteği için ayrı nesne oluşturmak zorundayız..
     *               1 nesne tüm konumların adresini öğrenemiyoruz . downloadAdressData nesnesi currentMarker konulurken
     *               çağrıldıgı için diger tüm konum istekleri yeni nesne ile gerçekleştirilmek zorunda ..
     */
    public void runAdressMethod(LatLng latLng) {

        if (downloadAdressData != null) {
            Log.d(TAG, "runTask: Yeni nesne Oluşturuldu ");
            new DownloadAdressData(this, this, 1).execute(latLng);
        } /*else {

            Log.d(TAG, "runTask: eski nesne ");
            downloadAdressData.execute(latLng);
        }*/


    }

    /**
     * Adress Callback İnterface .. .
     * DownloadAdressData Async sınıfından gelen adres bilgisi . .. l
     */
    @Override
    public void adressCallbackResult(String result) {
        //1 Kere çalışacak kkullanıcı bulundugu konumdaki marker işaretlenirken yapılan adres isteği buraya düşecek ve kullanıcı
        //adres bilgisini tutan değişkene atanacak ...
        if (isMyLocationAdress) {
            Log.d(TAG, "adressCallbackResult:isMyLocationAdress ");
            mUserLocationInfo.setMyLocationAdress(result);
            isMyLocationAdress = false;
        }


        ///Kullanıcı yeni bir lokasyon seçmiş ise yeni lokasyonun adresini set etdiyoruz.isNewLocationAdress
        /// Adres değişkenini false yapıyoruz kullanıcı yeni bir yer seçene kadar ...
        if (isNewLocationAdress) {
            Log.d(TAG, "adressCallbackResult: isNewLocationAdress");
            mUserLocationInfo.setNewLocationAdress(result);

            isNewLocationAdress = false;
        }
        Log.d(TAG, "adressCallbackResult: Normal Çalışıyor..");
        tvAdress.setText(result);

    }

    /**
     * Clustering işlemine dahil olmus tüm iş markerleri için üzerine tıklandıgında gerçekleşecek işlemler
     *
     * @param myItem --> jobAdvertModel den bilgileri alınaacak .
     * @return
     */
    @Override
    public boolean onClusterItemClick(NearJobAdvertModel myItem) {

        Log.d(TAG, "onClusterItemClick: TIKLANDI");
        // myItem.showInfoWindow();
        myItem.setCompanyDistance(String.format(Locale.getDefault(), "%.2f",
                MapHelperMethods.getDistanceParce(
                        MapHelperMethods.toRadiusMeters(
                                mCircleList.get(0).getCenterCircle(), myItem.getPosition())))); // item üzerine tıklayınca distance hesaplatıyorum.
        distanceToyou.setText(myItem.getCompanyDistance());
        customInfoWindowCluster.setClickedItem(myItem);// Tıkladıgım markerin bilgilerini ınfo windowa bildidiryorum ..

        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        // jobNameSlidingPanel.setText(getString(R.string.slidingpanel_head, myItem.getCompanyJob(), myItem.getCompanyDistance(), myItem.getNewLocationDistance())); //Sliding Panel TExt
        jobNameSlidingPanel.setText(myItem.getCompanyJob());
        tvbasvuru_count.setText(String.valueOf(myItem.getCountApply()).concat(" Basvuru"));
        runAdressMethod(myItem.getPosition());

        viewHolder.clearMap();//AdvertDetails.ViewHolder methodu
        viewHolder.setData(myItem);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(myItem.getPosition()));
        return false; //-->true olunca ınfo window calışmıyor ..
    }


    /**
     * Sliding Panele (Marker üzerine tıklanınca alttan acılan pnael )
     * İlan  detayları sayfasını ekliyorum .. Ve Bu Dosyayı ViewHolder Klasorumde Saklıyorum ...
     * İlan detayları sayfasını birçok yerde kullanıdıgım için tek bir Classta bu işlemleri gerçekleştiriyorum Tekrar Tekrar aynı
     * şeylerini yapmamak için .
     * Tanımlamalarını click eventlerini o class ta gerçekleştiriyorum .--->
     */
    private void slidingPanelAdvertInfo() {
        Log.d(TAG, "slidingPanelAdvertInfo: ");
        LayoutInflater inflater = getLayoutInflater();

        View jobadvertdetails = inflater.inflate(R.layout.jobadvertdetails, null);
        viewHolder = new AdvertDetails.ViewHolder(jobadvertdetails, this, this, 0);

        scrollableView.addView(jobadvertdetails, 0);

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick: MYLOCATİN TIKLANDI ");
    }

    /**
     * Kullanıcı 2.konumu seçmek istediğinde locationCheckChange==true olur ve kamera her değiştiğinde
     * NewLocationMarker olusturulur ve poziyon bilgisi Kameranın orta noktası olacak şekilde değiştirilir
     * Aynı Şekilde Kapsama alanınında pozisyonu newLocationMarkerin orta noktasını içerecek şekilde değiştirilir  .
     * Ve Adres methoduna pozisyon bilgisi gönderilerek o pozisyonun adresi alınır .
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        // Log.d(TAG, "onCameraChange: " + cameraPosition.target.latitude);
        // Log.d(TAG, "onCameraChange: " + cameraPosition.target.longitude);

        ///Lokasyon Değiştirme Butonu aktifleşmis ise new Location Markerin konumunu kamera her değiştirdiğinde değiştiriecek
        //Kameranın orta noktası alınacak cameraPosition.target.latitude, cameraPosition.target.longitud
        if (locationCheckChange) {
            LatLng lat = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

            mUserLocationInfo.getNewLocationMarker().setPosition(lat);
            DrawCircle drawCircle = mCircleList.get(0);
            drawCircle.setCenterCircle(lat); //circle merkezi güncelle
            runAdressMethod(lat);       //Kamera her hareketinde adres değiştiriyor  ....

            myItemClusterManager.onCameraIdle();
            myClusterMarker.setShould_zoom(cameraPosition.zoom < 14); ///-->Camera Zoom  13  oldugunda clusterin calısması için

        } else {

            myItemClusterManager.onCameraIdle();
            myClusterMarker.setShould_zoom(cameraPosition.zoom < 14); ///-->Camera Zoom  13  oldugunda clusterin calısması için
        }


    }

    @Override
    public void onClusterItemInfoWindowClick(NearJobAdvertModel myItem) {

        Log.d(TAG, "onClusterItemInfoWindowClick:Tıklandı ");
        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

        Log.d(TAG, "onMarkerDragStart: ");

    }

    @Override
    public void onMarkerDrag(Marker marker) {


        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        //  Log.d(TAG, "onMarkerDragEnd: " + downloadAdressData.execute(marker.getPosition()));
    }

    /**
     * Burada marker şeklini değiştircem .
     *
     * @param item
     * @param which -->İlana basvurumu yapıldı kayıtmı yapıldı ona göre marker işlemini değiştiriyorum
     *              basvuru yapıldı ->true (AdvertDetail den geliyor ) marker yeşil yapıyorum .
     *              kayıt için suan bir işlem yok
     */
    @Override
    public void changeMarker(NearJobAdvertModel item, boolean which) {
        Log.d(TAG, "changeMarker:İNTERFACEEE " + item);
        if (which) {
            Log.d(TAG, "changeMarker:bASvuru yapılmıs ");
            myClusterMarker.getMarker(item).setIcon(MapHelperMethods.getApplyMarkerDrawable(getApplicationContext()));
            item.setMarkerIcon(MapHelperMethods.getApplyMarkerDrawable(getApplicationContext()));
        } else {
            Log.d(TAG, "changeMarker: Kayıt işlemi yapılmıs.");
        }
    }


    /**
     * RetainJobAdvert Async İşleminden dönen Callback
     * ->Orada ilanlar firebaseden alınıp işlem tamamlandıgında burada dolduruluyor .
     *
     * @param result-->İş ilanlarını tutan dizi
     */
    @Override
    public void JobAdvertFromFirebaseCallback(ArrayList<JobAdvertModel2> result) {
        Log.d(TAG, "JobAdvertFromFirebaseCallback: ");

        if (!result.isEmpty()) {
            myItemClusterManager.clearItems();
            builder = LatLngBounds.builder();
            for (JobAdvertModel2 model2 : result) {
                // Log.d(TAG, "JobAdvertFromFirebaseCallback:JobAdvertModel2 model2  " + model2);
                myItemClusterManager.addItem(
                        new NearJobAdvertModel(model2.getJobID(),
                                model2.getCompanyName(),
                                model2.getCompanyJob(),
                                model2.getJobSector(),
                                model2.getJobDescpriction(),
                                model2.getCompanyLogoUrl(),
                                model2.getCompanyAdress(),
                                model2.getEducationLevel(),
                                model2.getExpLevel(),
                                model2.getEmployeeHour(),
                                model2.getDrivingLicence(),
                                model2.getMilitary(),
                                model2.getGender(),
                                model2.getPublishDate(),
                                model2.getCountApply(),
                                model2.getJobPossibility(),
                                model2.getmPosition(),
                                false, false,
                                MapHelperMethods.getNormalMarkerDrawable((getApplicationContext())),
                                "", ""
                        ));
                includeBounds(model2.getmPosition()); //Sınır bolgelerini ekliyorum
                myItemClusterManager.cluster();
            }
            editClusterItem();
            mapProgress.setVisibility(View.GONE);
            // Log.d(TAG, "JobAdvertFromFirebaseCallback: " + myItemClusterManager.getAlgorithm().getItems().size());
            animateCameraWithBounds(includeBounds(mUserLocationInfo.getMyLocation())); //Kullanıcı lokasyonunu eklyorum
            MyclusterManager.getInstance().setMyClusterManager(myItemClusterManager);
        } else {
            // Log.d(TAG, "JobAdvertFromFirebaseCallback:");
            mapProgress.setVisibility(View.GONE);
            Permissions.showAlertdilaog(this, sektorName, "Bu Sektörde İş Yok Gibi", 1000).show();
            myItemClusterManager.clearItems();
            myItemClusterManager.cluster();
        }


    }

    /**
     * Burada daha önceden basvurusu yapılmıs veya
     * kayıt işlemi yapılan ilanların basvuru ve kayıt parametreleri doldurulmakta --> değiştirilmekte . .
     */
    private void editClusterItem() {
        for (ApplyAdvertModel basvurulan : myAppliedAdvertArrayList) {
            for (NearJobAdvertModel joblist : myItemClusterManager.getAlgorithm().getItems()) {
                //    Log.d(TAG, "editClusterItem:basvurulan.getJobID()--> " + basvurulan.getJobID());
                //   Log.d(TAG, "editClusterItem:list.getJobID() -->" + list.getJobID());
                if (joblist.getJobID().equals(basvurulan.getJobID())) {
                    //     Log.d(TAG, "editClusterItem:list.getJobID().equals(basvurulan.getJobID() ");
                    if (basvurulan.getApply()) {
                        //    Log.d(TAG, "editClusterItem:basvurulan.getApply->>> " + basvurulan.getApply());
                        joblist.setMarkerIcon(MapHelperMethods.getApplyMarkerDrawable((getApplicationContext())));
                        joblist.setBasvuruDurumu(true);
                        //Log.d(TAG, "editClusterItem:basvurulan.getSave() " + basvurulan.getSave());
                    }
                    if (basvurulan.getSave()) {
                        //  Log.d(TAG, "editClusterItem: basvurulanSAVE-->" + basvurulan.getSave());
                        // Log.d(TAG, "editClusterItem: LİST SAVE -->" + list.isSave());
                        //  myItemClusterManager.removeItem(list);
                        joblist.setSave(true);
                        //  Log.d(TAG, "editClusterItem: List SAVE 2 -->" + list.isSave());
                        //  myItemClusterManager.addItem(list);
                    }
                }
            }

        }
    }

    /**
     * Gelen Map String ve button İçeriyor String Filtrenini hangisinde uygulanacagı ->Egitim Tecrübe Çalışma Şekli ..
     * Button ise Filtrenini ne oldugunu içeriyor . ->Üni.Mezunu ,Tecrübeli,Stajyer.
     *
     * @param filterContent
     */
    @Override
    public void filterCallback(Set<Map.Entry<String, Button>> filterContent) {
        mapProgress.setVisibility(View.VISIBLE);
        Log.d(TAG, "filterCallback: " + filterContent);
        //
        for (Map.Entry<String, Button> filter : filterContent) {

            String filtreTuru = filter.getKey();
            String filtreIcerigi = filter.getValue().getText().toString();

            for (NearJobAdvertModel model : myItemClusterManager.getAlgorithm().getItems()) {
                Log.d(TAG, "runFilter: " + filtreIcerigi);
                Log.d(TAG, "runFilter: " + filtreTuru);
                if (!model.getEducationLevel().equalsIgnoreCase(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Eğitim")) {

                    Log.d(TAG, "runFilter:Egitim " + model.getEducationLevel());
                    myItemClusterManager.removeItem(model);

                }
                if (!model.getExpLevel().contains(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Tecrübe")) {

                    Log.d(TAG, "runFilter: Tecrube" + model.getExpLevel());
                    myItemClusterManager.removeItem(model);


                }
                if (!model.getEmployeeHour().equalsIgnoreCase(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Çalışma Şekli")) {

                    Log.d(TAG, "runFilter: Çalışma Şekli" + model.getExpLevel());
                    myItemClusterManager.removeItem(model);


                }
                if (!model.getDrivingLicence().contains(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Ehliyet")) {

                    Log.d(TAG, "runFilter: Ehliyet" + model.getExpLevel());
                    myItemClusterManager.removeItem(model);


                }
                if (!model.getGender().equalsIgnoreCase(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Cinsiyet")) {

                    Log.d(TAG, "runFilter: Cinsiyet" + model.getExpLevel());
                    myItemClusterManager.removeItem(model);


                }
                if (!model.getMilitary().equalsIgnoreCase(filtreIcerigi) && filtreTuru.equalsIgnoreCase("Askerlik")) {

                    Log.d(TAG, "runFilter: Askerlik" + model.getExpLevel());
                    myItemClusterManager.removeItem(model);


                }

            }


        }
        checkfilteredAdverts();



    }

    /**
     * İlanlar Filtre İşleminden gectikten sonra bu method calısıyor
     * Bu method filtreden gecen ilanlardan uygun ilan bulunamaması durumunda
     * kullanıcıya Bir Toast mesajı gösterecek
     * Eger uygun ilan  var ise o ilanların koordinatlarını sınıra ekleyıp (includeBounds() )kamerayı o ilanlara göre ayarlama
     * işlemini yapıyor .
     */
    private void checkfilteredAdverts() {
        builder = LatLngBounds.builder();
        if (myItemClusterManager.getAlgorithm().getItems().isEmpty()) {

            MyCustomToast.showCustomToast(getApplicationContext(), "Filtrenize Uygun İlan Bulunamadı.");
            myItemClusterManager.cluster();
            mapProgress.setVisibility(View.GONE);
            animateCameraWithBounds(includeBounds(mUserLocationInfo.getMyLocation())); //Kullanıcı lokasyonunu eklyorum

        }
        else {
            for (NearJobAdvertModel model : myItemClusterManager.getAlgorithm().getItems()) {

                Log.d(TAG, "eklenen sınırlar ");
                includeBounds(model.getmPosition()); //Sınır bolgelerini ekliyorum
                myItemClusterManager.cluster();
            }
            mapProgress.setVisibility(View.GONE);
            animateCameraWithBounds(includeBounds(mUserLocationInfo.getMyLocation())); //Kullanıcı lokasyonunu eklyorum

        }

    }

    /**
     * Alınan koordinatları sınır bölgeleri olarak ekliyoruz.
     *
     * @param _latlng
     * @return
     */
    private LatLngBounds includeBounds(LatLng _latlng) {
        // Log.d(TAG, "changeCamera");

        //Sınır bölgelerini al
        builder.include(_latlng);
        //final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        // final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        // final int zoomPadding = (int) (zoomWidth * 0.10); // Genişliğin 0,10 u kadar kenarlardan boşluk

        return builder.build();
    }


    /**
     * SEİÇLEN SEKTÖR BİLGİSİ --sektorName
     * Seçilen sektörü alan  callback SekTör listesinden seçilen item (CATEGORYADAPTER CLİCK METHODUNDAN GELIYOR ) buraya geliyor buradanda REtainJobAdvertFromFirebase
     * Async Task methodunda gönderilerek işlenecek.
     *
     * @param sectorName-->Seçilen sektör İsmi
     */
    @Override
    public void LoadAdvertWithSectorInfo(final String sectorName) {
        Log.d(TAG, "LoadAdvertWithSectorInfo: " + sectorName);
        mapProgress.setVisibility(View.VISIBLE);
        sektorName = sectorName;
        final RetainJobAdvertFromFirebase mretainJobAdvertFromFirebase =
                new RetainJobAdvertFromFirebase(this, this);

        if (sectorName.equals("Tüm İlanlar")) {
            // sektorName="Tüm İlanlar";
            Log.d(TAG, "LoadAdvertWithSectorInfo: TÜM İLANLAR ");
            getAllJobLocation();
        } else {
            Query query = myRef.orderByChild("jobInfo/jobSector").equalTo(sectorName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mretainJobAdvertFromFirebase.execute(dataSnapshot);
                    Log.d(TAG, "LoadAdvertWithSectorInfo->>onDataChange: " + dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError);
                }
            });
        }

    }

    /**
     * Sektör Listesini(İsimleri ve ilan sayılarını ) asenkron olarak arka planda tek tek çekiyoruz  ve bu method sektör listesinde  kayıtlı 21 adaet sektör
     * bilgisini almayı tamamlandıgında RecylerView i olusturuyor .
     *
     * @param categoryModelArrayList
     */
    @Override
    public void getCategoryCompleteCallback(ArrayList<CategoryModel> categoryModelArrayList, int size, HashMap<String, ArrayList<String>> sectorHash) {
        // Log.d(TAG, "getCategoryCompleteCallback: " + categoryModelArrayList);
        //    Log.d(TAG, "getCategoryCompleteCallback:SİZE  " + size);
        //   Log.d(TAG, "getCategoryCompleteCallback:CATEGORYLER " + categoryModelArrayList);
        if (categoryModelArrayList.size() == size) {
            setJobSearchBar(sectorHash);
            categoryProgress.smoothToHide();
            // Log.d(TAG, "getCategoryCompleteCallback:categoryModelArrayList.size() == 21 ");
            Log.d(TAG, "getCategoryCompleteCallback: " + categoryModelArrayList);
            mCategoryAdapter = new CategoryAdapter(this,
                    categoryModelArrayList, this);
            categoryRecycler.setAdapter(mCategoryAdapter);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
            linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryRecycler.setLayoutManager(linearLayoutManager2);
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animt3);
            categoryRecycler.setLayoutAnimation(layoutAnimationController);

            categoryRecycler.setHasFixedSize(true);

        }


    }

    /**
     * JobSearch Bar'a tüm mesleklerin listesi yerleşitiriliyor.
     *
     * @param sectorHash
     */
    private void setJobSearchBar(HashMap<String, ArrayList<String>> sectorHash) {
        Log.d(TAG, "setJobSearchBar: ");
        for (ArrayList<String> _jobList : sectorHash.values()) {
            jobList.addAll(_jobList);

        }

        jobListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, jobList);
        input_searchJob.setAdapter(jobListAdapter);
        input_searchJob.setThreshold(0);

        input_searchJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenJob = (String) adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemClick: " + choosenJob);

            }
        });


    }

    /**
     * Basvurdugum ilan listesini içeren Callback ınterface method MyAppliedAdvert tan geliyor ..
     *
     * @param result --> basvurudugum ilan ıdlerini içeriyor.
     */
    @Override
    public void getApliedAdvert(ArrayList<ApplyAdvertModel> result) {
        Log.d(TAG, "getApliedAdvert: " + result);
        myAppliedAdvertArrayList = result;
        getAllJobLocation();

    }

    /**
     * Son konum verisi getir --> DeviceLocation Sınıfından gelen callback
     *
     * @param gps
     */
    @Override
    public void deviceLocationCallback(LatLng gps) {

        if (gps != null) {
            drawCurrentLocationMarker(MapHelperMethods.convertLatLngtoLocation(gps));

        } else {

            Log.d(TAG, "deviceLocationCallback: GPS VERİSİ ALINAMADI NULL ");


        }


    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


        if (i == EditorInfo.IME_ACTION_SEARCH
                || i == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
            hideKeyboard();
            searchJob();
        }

        return false;
    }

    private void hideKeyboard() {
        Log.d(TAG, "hideKeyboard: ");
        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null && mgr.isActive()) {

            mgr.hideSoftInputFromWindow(input_searchJob.getWindowToken(), 0);

        }
    }


//    private void switchFilterType(String filtreTuru, String filtreIcerigi) {
//
//        switch (filtreTuru) {
//
//            case "Eğitim":
//
//                runFilter(filtreIcerigi);
//
//                break;
//
//        }
//
//    }
//
//    private void runFilter(String filtreIcerigi) {
//
//        ClusterManager<NearJobAdvertModel> tempCluster = myItemClusterManager;
//        for (NearJobAdvertModel model : myItemClusterManager.getAlgorithm().getItems()) {
//
//            Log.d(TAG, "runFilter: " + filtreIcerigi);
//            Log.d(TAG, "runFilter: " + model.getEducationLevel());
//
//            if (!model.getEducationLevel().equalsIgnoreCase(filtreIcerigi)) {
//
//                myItemClusterManager.removeItem(model);
//                Log.d(TAG, "runFilter: " + myItemClusterManager.getAlgorithm().getItems());
//
//            }
//
//
//        }
//
//        myItemClusterManager.cluster();
//
//    }
}