package com.example.harun.getjob.JobSearch;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.JobSearch.JobUtils.AdvertDetails;
import com.example.harun.getjob.JobSearch.JobUtils.CategoryAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.CategoryModel;
import com.example.harun.getjob.JobSearch.JobUtils.DrawCircle;
import com.example.harun.getjob.JobSearch.JobUtils.GestureEvent;
import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.JobSearch.JobUtils.MyClusterMarker;
import com.example.harun.getjob.JobSearch.JobUtils.MyclusterManager;
import com.example.harun.getjob.JobSearch.JobUtils.UserLocationInfo;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.R;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mayne on 22.03.2018.
 */

public class JobSearch extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, LocationListener, DiscreteSeekBar.OnProgressChangeListener,
        View.OnClickListener, ClusterManager.OnClusterClickListener<JobAdvertModel>,
        ClusterManager.OnClusterItemClickListener<JobAdvertModel>,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraChangeListener,
        ClusterManager.OnClusterItemInfoWindowClickListener<JobAdvertModel>,
        AdvertDetails.ChangeMarkerCluster,
        GoogleMap.OnMarkerDragListener, DownloadAdressData.adressCallback {

    private static final String TAG = "JobSearch";
//    private static final Dot DOT = new Dot();

    //  private static final int PATTERN_GAP_LENGTH_PX = 200;
    //  private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    //  private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);


    //widgetlar
    RecyclerView categoryRecycler;
    TextView tvAdress, tvCircle;
    SupportMapFragment mapFragment;
    DiscreteSeekBar circleArea;
    Button btnListNearJob;
    TextView jobNameSlidingPanel, distanceToyou;
    LinearLayout touchHandler, circleAreaLayout;
    AVLoadingIndicatorView adresLoading;
    FloatingActionButton mylocButton, changeLocation, changeArea;
    //Sliding Widget From jobsearch 2
    // Sliding Layout
    ValueAnimator colorAnimator;
    CategoryAdapter mCategoryAdapter;
    private CategoryModel mCategoryModel;
    AdvertDetails.ViewHolder viewHolder;
    //local variable
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
    RelativeLayout slidingheader;
    LocationManager locationManager;
    LocationManager loc;
    MapStyleOptions style;
    private Location mLastKnownLocation;
    MyClusterMarker myClusterMarker;
    private static final int DEFAULT_ZOOM = 13;
    public ClusterManager<JobAdvertModel> myItemClusterManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    List<DrawCircle> mCircleList = new ArrayList<>(1);
    ArrayList<LatLng> locationList;
    CustomInfoWindowCluster customInfoWindowCluster;

    //Sliding Panel
    public SlidingUpPanelLayout mSlidingPanelLayout;
    ArrayList<JobAdvertModel> resultList;
    public NonHierarchicalDistanceBasedAlgorithm clusterManagerAlgorithm;
    UserLocationInfo mUserLocationInfo;

    LatLng[] denemekonumlar = {new LatLng(40.98636580, 28.66981733),
            new LatLng(40.981415, 28.6630712),
            new LatLng(40.978889, 28.6635097),
            new LatLng(40.994859, 28.6686097),
            new LatLng(40.993259, 28.6386097),
            new LatLng(40.99319, 28.6386497),
            new LatLng(40.99339, 28.680797),
            new LatLng(40.9953259, 28.67896097),
            new LatLng(40.9193259, 28.6673816097),
            new LatLng(40.99233259, 28.634586097),
            new LatLng(40.9933259, 28.63846097),
            new LatLng(40.992259, 28.647097),
            new LatLng(40.998889, 28.6687097),
            new LatLng(43.994859, 28.6686097),
            new LatLng(44.994859, 28.6686057),
            new LatLng(46.994859, 28.6685097),
            new LatLng(47.994859, 24.6685097),
            new LatLng(40.767554, 30.361397),
            new LatLng(40.770609, 30.367233),
            new LatLng(40.771794, 30.388820),
            new LatLng(47.994859, 24.6685097),
            new LatLng(40.765489, 30.372384),
            new LatLng(40.762930, 30.360252),
            new LatLng(40.764222, 30.351146),
            new LatLng(40.777247, 30.365028),
            new LatLng(40.772014, 30.364556),
            new LatLng(46.97859, 25.6685097),
            new LatLng(46.9954859, 26.85097),
            new LatLng(46.99444859, 27.6685097),
            new LatLng(44.194859, 29.6656097)
    };

    //Kategori recyler ListView in gerçeklenmesi .
    private void setRecylerCategoryList() {

        Log.d(TAG, "setRecylerCategoryList: ÇAĞRILDI");
        mCategoryAdapter = new CategoryAdapter(this, mCategoryModel.getDataModel());
        categoryRecycler.setAdapter(mCategoryAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecycler.setLayoutManager(linearLayoutManager2);
        categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryRecycler.setHasFixedSize(true);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobsearch23);

        locationList = new ArrayList<>();

        locationList.addAll(Arrays.asList(denemekonumlar));

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
        changeArea = findViewById(R.id.changeArea);
        circleAreaLayout = findViewById(R.id.circleAreaLayout);
        touchHandler = findViewById(R.id.touchHandler);
        mylocButton = findViewById(R.id.myLocButton);
        scrollableView = findViewById(R.id.scrollableView);
        slidingheader = findViewById(R.id.header);
        adresLoading = findViewById(R.id.adresLoading);
        init();
    }

// Değişkenlerin  İlklemeleri

    private void init() {
        Log.d(TAG, "init: ÇAĞRILDI");
        mCategoryModel = new CategoryModel();
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        resultList = new ArrayList<>();
        mUserLocationInfo = UserLocationInfo.getInstance();//USERLOCATİON INFO SİNGLETON NESNESİ ...
        style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro); //Map Style Retro
        downloadAdressData = new DownloadAdressData(this, this, 1);
        btnListNearJob.setOnClickListener(this);
        changeArea.setOnClickListener(this);
        slidingPanelListener();
        //touchHandlerListener();
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
            setRecylerCategoryList();

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

        myItemClusterManager = new ClusterManager<JobAdvertModel>(this, mMap);
        clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();

        myItemClusterManager.setAlgorithm(clusterManagerAlgorithm);

        myClusterMarker = new MyClusterMarker(this, mMap, myItemClusterManager);
        MyclusterManager.getInstance().setMyClusterMarker(myClusterMarker);
        customInfoWindowCluster = new CustomInfoWindowCluster(getApplicationContext());
        myItemClusterManager.setRenderer(myClusterMarker); //Cluster style
        myItemClusterManager.setOnClusterClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(myItemClusterManager);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(myItemClusterManager);
        myItemClusterManager.setOnClusterItemInfoWindowClickListener(this);
        myItemClusterManager.setOnClusterItemClickListener(this);
        getDeviceLocation();  // haritayı son konuma aktif etme
        drawJobLocation();
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
    }

    @SuppressLint("MissingPermission")
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

    private void getDeviceLocation() {

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
                                LatLng gps = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
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
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(mUserLocationInfo.getMyLocation())));
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

    /**
     * Firebaseden Gelecek olan koordinatlar buraya gelecek burada marker lanacak .
     */
    private void drawJobLocation() {

        Log.d(TAG, "drawJobLocation: ÇAĞRILDI");

       /* Map mCoordinate = new HashMap();
        mCoordinate.put("latitude", locationList.get(0).latitude);
        mCoordinate.put("longitude", locationList.get(0).longitude);
*/

        int i = 0;
        for (LatLng clustering : locationList) {
            Log.d(TAG, "addClusterItem: ITEMLER EKLENIYOR " + clustering);
            myItemClusterManager.addItem(new JobAdvertModel(
                    "MD BİLİŞİM SİSTEMLERİ" + String.valueOf(i++),
                    "ANDROİD DEVELOPER",
                    "asdasdasdasd",
                    "İstanbul",
                    clustering,
                    "01.01.2018",
                    "Tecrübesiz",
                    "100",
                    //   String.valueOf(getDistanceParce(
                    //          toRadiusMeters(getMyLocation(), clustering))),
                    "",
                    MapHelperMethods.getNormalMarkerDrawable((getApplicationContext())), "ADRES BİLGİSİ .."));

        }


    }

//
//    Bu methoda şimdilik gerek yok .
//    private void touchHandlerListener() {
//        touchHandler.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: EVENT TOUCH HANDLER 1 ");
//                mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//
//                return false;
//            }
//        });
//
//    }

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
     * 1.tıklayısı kırmızı olan gercek konumuna 2.tıklayısı yeşil olan yeni markere
     * yönlendirilecek
     *
     * @return 1--Kırmızı Gerçek konum ---2 Yeşil Yeni Konum
     */
    private int myLocButtonStateCheck() {

        //1 MArker varsa
        if (countLocation == 1) {

            return 1;
        } else {
            //1 marKer yoksa 2 marker vardır
            //İlk tıklayısta firstClick true ..
            // yani 1 .tıklayısta mavi 2.tıklayısta kırmızı olcak locationbutton daki marker rengi
            if (firstClick) {

                mylocButton.setImageDrawable(getDrawable(R.drawable.markeruser2));
                //mylocButton.setImageTintList(ColorStateList.valueOf(Color.BLUE));
                firstClick = false;
                return 1;

            } else {
                mylocButton.setImageDrawable(getDrawable(R.drawable.markeruser));

               // mylocButton.setImageTintList(ColorStateList.valueOf(Color.RED));
                firstClick = true;
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


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        //  Permission İzin Verilmemiş ise burada alert dialog konulacak .
        if (!mPermissionGranted) {

            Log.d(TAG, "onResumeFragments:");
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: ");
        return true;

    }



    /*ONLOCATİON CHANGE  BURASI HATALI ÇALIŞABİLİR TEST ETMEDİMM ... */

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnListNearJob:

                //Log.d(TAG, "onClick: " + mMap.getCameraPosition().zoom + "\t\t" + mMap.getMaxZoomLevel());
                startNewAnimation();
                getNearJobList();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Yakındaki daire içerisindeki işler listelenecek
                        Log.d(TAG, "onClick: ");
                        if (resultList.size() > 0) {
                            Log.d(TAG, "resultListSize>0: ");
                            final Intent i = new Intent(getApplicationContext(), NearJobListActivity.class);
                            i.putParcelableArrayListExtra("nearList", resultList);
                            i.putExtra("nearListSize", resultList.size());
                            // colorAnimator.cancel();
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                        } else {
                            MyCustomToast.showCustomToast(getApplicationContext(),"Kapsama alanınızda Size uygun iş bulunamadı.");
                            //Toast.makeText(getApplicationContext(), "Kapsama alanınızda Size uygun iş bulunamadı.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, 1000);
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
                newLocation();
                break;

            case R.id.changeArea:
                Log.d(TAG, "onClick: CHANGEAREa");
                if (!circleArea.isShown()) {

                    circleAreaLayout.setVisibility(View.VISIBLE);
                    circleAreaLayoutSetAnimation();
                    changeArea.setImageDrawable(getResources().getDrawable(R.drawable.circular_area_cancel));

                } else {
                    circleAreaLayout.setVisibility(View.GONE);
                    changeArea.setImageDrawable(getResources().getDrawable(R.drawable.circular_area2));
                }

                break;
        }
    }

    /**
     * Circle Area Animation ..
     */
    private void circleAreaLayoutSetAnimation() {

        Log.d(TAG, "circleAreaLayoutSetAnimation: ");

        int anim = R.anim.layout_anim2;
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), anim);
        circleAreaLayout.setLayoutAnimation(layoutAnimationController);


    }


    /**
     * Yakınımdakileri Listele Butonu Animasyonu .LayerList backLayerin Renk değiştirme animasyonu .
     */
    private void startNewAnimation() {
        int colorFrom = getResources().getColor(R.color.mycolor);
        int colorTo = getResources().getColor(R.color.Aqua);
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
        colorAnimator.setRepeatCount(Animation.ABSOLUTE);

        // colorAnimator.setRepeatMode(ValueAnimator.RESTART);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                Log.d(TAG, "onAnimationUpdate: ");
                                                int runColor;
                                                hsv[0] = 360 * valueAnimator.getAnimatedFraction();
                                                runColor = Color.HSVToColor(hsv);
                                                drawable.setColor(runColor);
                                                // float[] hsv=Color.HSVToColor((float[]) (colorAnimator.getAnimatedValue()));
                                                //  drawable.setColor((int) (colorAnimator.getAnimatedValue()));
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

    /**
     * Yeni lokasyon belirleme methodu
     */
    private void newLocation() {
        Log.d(TAG, "newLocation: Methodu Çağrıldı");

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
            changeLocation.setImageDrawable(getResources().getDrawable(R.drawable.succesholder));
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
    public boolean onClusterClick(Cluster<JobAdvertModel> cluster) {

        Log.d(TAG, "onClusterClick:Tıklandı");
        LatLngBounds.Builder builder = LatLngBounds.builder();


        //Sınır bölgelerini al
        for (ClusterItem item : cluster.getItems()) {

            builder.include(item.getPosition());
        }

        //Bunu Farklı bir fonksiyona alıcamm unutma....
        LatLngBounds bounds = builder.build();
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
            // adresLoading.show();
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
        // adresLoading.hide();
        //1 Kere çalışacak kkullanıcı bulundugu konumdaki marker işaretlenirken yapılan adres isteği buraya düşecek ve kullanıcı
        //adres bilgisini tutan değişkene atanacak ...
        if (isMyLocationAdress) {
            Log.d(TAG, "adressCallbackResult:isMyLocationAdress ");
            mUserLocationInfo.setMyLocationAdress(result);
            isMyLocationAdress = false;
            // return;
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
        //  adresLoading.smoothToHide();

    }

    /**
     * Circle kapsama alanına giren itemleri result list içersine ekliyorum.Bu method yakınımdakileri listele dedğinde
     * çalışacak ve Bu listeyi işlemek üzere NearJobListActivty 'e gönderecek ->YakınımdakileriListele Butonu .. ..
     */

    public void getNearJobList() {
        int distance;
        DrawCircle drawCircle = mCircleList.get(0);
        resultList.clear();//her çağırıldıgında önceki listeyi temizleyecek ..

        //Tüm Cluster edilmiş itemleri alıyorum ...
        ArrayList<JobAdvertModel> items = (ArrayList<JobAdvertModel>) clusterManagerAlgorithm.getItems();
        // denemeListe.addAll(items);
        UserLocationInfo.getInstance().setCircleArea(drawCircle.getCircleRadius());
        //  Log.d(TAG, "getNearJobList: " + items.get(0).toString() + circleArea.getProgress());
        for (JobAdvertModel item : items) {

            distance = MapHelperMethods.getDistanceParce(
                    MapHelperMethods.toRadiusMeters(
                            drawCircle.getCenterCircle(), item.getPosition()));

            item.setCompanyDistance(String.valueOf(distance)); //Circle merkezi ile markerlar arası mesafeyi ölçüp ekliyorum.
            // Circle alanı içerisinde girenler marker ları sonuç listesine  NearJobListActivtye gönderme üzere ekliyorum.
            if (distance <= drawCircle.getCircleRadius()) {
                Log.d(TAG, "getNearJobList: " + distance + "\t" + drawCircle.getCircleRadius() + "\t" + drawCircle.getCenterCircle());
                resultList.add(item);
            }
        }

        Log.d(TAG, "getNearJobList:SONUC Listesi@@@" + resultList);
    }


    /**
     * Clustering işlemine dahil olmus tüm iş markerleri için üzerine tıklandıgında gerçekleşecek işlemler
     *
     * @param myItem --> jobAdvertModel den bilgileri alınaacak .
     * @return
     */
    @Override
    public boolean onClusterItemClick(JobAdvertModel myItem) {

        Log.d(TAG, "onClusterItemClick: TIKLANDI");
        // myItem.showInfoWindow();
        myItem.setCompanyDistance(String.valueOf(
                MapHelperMethods.getDistanceParce(
                        MapHelperMethods.toRadiusMeters(
                                mUserLocationInfo.getMyLocation(), myItem.getPosition())))); // item üzerine tıklayınca distance hesaplatıyorum.

        if (mUserLocationInfo.getNewLocationMarker() != null) {

            myItem.setNewLocationDistance(String.valueOf(
                    MapHelperMethods.getDistanceParce(
                            MapHelperMethods.toRadiusMeters(
                                    mUserLocationInfo.getNewLocationLatLng(), myItem.getPosition()))));

        }
        customInfoWindowCluster.setClickedItem(myItem);// Tıkladıgım markerin bilgilerini ınfo windowa bildidiryorum ..

        mSlidingPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        jobNameSlidingPanel.setText(getString(R.string.slidingpanel_head, myItem.getCompanyJob(), myItem.getCompanyDistance(), myItem.getNewLocationDistance())); //Sliding Panel TExt
        distanceToyou.setText(myItem.getCompanyDistance());
        runAdressMethod(myItem.getPosition());

        viewHolder.clearMap();//AdvertDetails.ViewHolder methodu
        viewHolder.setData(myItem);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(myItem.getPosition()));
        return false; //-->true olunca ınfo window calışmıyor ..
    }


    /**
     * Sliding Panele (Marker üzerine tıklanınca alttan acılan pnael )
     * İlan  detayları sayfasını ekliyorum .. Ve Bu Dosyayı ViewHolder Klasorumde Saklıyorum ...
     * Tanımlamalarını click eventlerini o class ta gerçekleştiriyorum .
     */
    private void slidingPanelAdvertInfo() {
        Log.d(TAG, "slidingPanelAdvertInfo: ");
        LayoutInflater inflater = getLayoutInflater();

        View jobadvertdetails = inflater.inflate(R.layout.jobadvertdetails, null);
        // deneme33.getInstance().setmDenemeListener(this);
        viewHolder = new AdvertDetails.ViewHolder(jobadvertdetails, getApplicationContext(), this);

        scrollableView.addView(jobadvertdetails, 0);

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick: MYLOCATİN TIKLANDI ");
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        // Log.d(TAG, "onCameraChange: " + cameraPosition.target.latitude);
        // Log.d(TAG, "onCameraChange: " + cameraPosition.target.longitude);

        ///Lokasyon Değiştirme Butonu aktifleşmis ise new Location Markerin konumunu kamera her değiştirdiğinde değiştiriecek
        //Kameranın orta noktası alınacak cameraPosition.target.latitude, cameraPosition.target.longitud
        if (locationCheckChange) {
            LatLng lat = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

            mUserLocationInfo.getNewLocationMarker().setPosition(lat);
            //   newLocationMarker.setPosition(lat);
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
    public void onClusterItemInfoWindowClick(JobAdvertModel myItem) {

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
    public void changeMarker(JobAdvertModel item, boolean which) {
        Log.d(TAG, "changeMarker:İNTERFACEEE " + item);
        if (which) {
            Log.d(TAG, "changeMarker:bASvuru yapılmıs ");
            myClusterMarker.getMarker(item).setIcon(MapHelperMethods.getApplyMarkerDrawable(getApplicationContext()));
            item.setIcon(MapHelperMethods.getApplyMarkerDrawable(getApplicationContext()));
        }else
        {
            Log.d(TAG, "changeMarker: Kayıt işlemi yapılmıs.");
        }
    }

}