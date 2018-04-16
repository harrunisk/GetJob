package com.example.harun.getjob.JobSearch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harun.getjob.JobSearch.JobUtils.CategoryAdapter;
import com.example.harun.getjob.JobSearch.JobUtils.CategoryModel;
import com.example.harun.getjob.JobSearch.JobUtils.DrawCircle;
import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.JobSearch.JobUtils.MyClusterMarker;
import com.example.harun.getjob.JobSearch.JobUtils.UserLocationInfo;
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
    TextView jobNameSlidingPanel;
    LinearLayout touchHandler;

    FloatingActionButton mylocButton, changeLocation;
    //Sliding Widget From jobsearch 2
    // Sliding Layout

    private ArrayList<CategoryModel> categoryModelArrayList;
    CategoryAdapter mCategoryAdapter;
    private CategoryModel mCategoryModel;

    //local variable
    private int countLocation;
    // private String myLocationAdress, newLocationAdress;
    DownloadAdressData downloadAdressData;
    private GoogleMap mMap;
    boolean mPermissionGranted;
    boolean isGPSEnabled;
    boolean isOpened = false;
    boolean firstClick = true;
    boolean isMyLocationAdress = false, isNewLocationAdress = false;
    boolean locationCheckChange = false;

    LocationManager locationManager;
    LocationManager loc;
    MapStyleOptions style;
    //  private LatLng myLocation, newLocationLatLng;
    private Location mLastKnownLocation;
    MyClusterMarker myClusterMarker;
    // Marker currentLocationMarker, newLocationMarker;
    private static final int DEFAULT_ZOOM = 13;
    private ClusterManager<JobAdvertModel> myItemClusterManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    List<DrawCircle> mCircleList = new ArrayList<>(1);
    ArrayList<LatLng> locationList;
    CustomInfoWindowCluster customInfoWindowCluster;
    //Sliding Panel
    private SlidingUpPanelLayout mLayout;
    ArrayList<JobAdvertModel> resultList;
    NonHierarchicalDistanceBasedAlgorithm clusterManagerAlgorithm;
    UserLocationInfo mUserLocationInfo;
    //public static boolean should_zoom;

    LatLng[] denemekonumlar = {new LatLng(40.98636580, 28.66981733),
            new LatLng(40.981415, 28.6630712),
            new LatLng(40.978889, 28.6635097),
            new LatLng(40.994859, 28.6686097),
            new LatLng(40.993259, 28.6386097),
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
        setContentView(R.layout.jobsearch2);

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
        mLayout = findViewById(R.id.sliding_layout);
        jobNameSlidingPanel = findViewById(R.id.JobName);
        touchHandler = findViewById(R.id.touchHandler);
        mylocButton = findViewById(R.id.myLocButton);
        init();
    }

// Değişkenlerin  İlklemeleri

    private void init() {
        Log.d(TAG, "init: ÇAĞRILDI");
        categoryModelArrayList = new ArrayList<>();
        mCategoryModel = new CategoryModel();
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        resultList = new ArrayList<>();
        mUserLocationInfo = UserLocationInfo.getInstance();
        style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro); //Map Style Retro
        downloadAdressData = new DownloadAdressData(getApplicationContext(), this);
        btnListNearJob.setOnClickListener(this);
        slidingPanelListener();
        touchHandlerListener();
        DiscreteSeekBarRange();

        // gpsTracker = new NewGPSTracker(getApplicationContext());


    }

    @Override
    public void onBackPressed() {

        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            // Log.d(TAG, "onBackPressed: SLİDİNG PANEL KAPANDI");
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
        // Instantiate the cluster manager algorithm as is done in the ClusterManager
        clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();

        // Set this local algorithm in clusterManager
        myItemClusterManager.setAlgorithm(clusterManagerAlgorithm);

        myClusterMarker = new MyClusterMarker(this, mMap, myItemClusterManager);
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
        _mMap.getUiSettings().setZoomControlsEnabled(true);
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
            DrawCircle drawCircle = new DrawCircle(location, 1000, mMap, getApplicationContext());//İlk Circle 1-km Alanı kapsayacak
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
        Drawable drawable = getResources().getDrawable(R.drawable.markeruser);
        drawable.setTint(getColor(R.color.jumbo));
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


        for (LatLng clustering : locationList) {
            Log.d(TAG, "addClusterItem: ITEMLER EKLENIYOR " + clustering);
            myItemClusterManager.addItem(new JobAdvertModel(
                    "MD BİLİŞİM SİSTEMLERİ",
                    "ANDROİD DEVELOPER",
                    "asdasdasdasd",
                    "İstanbul",
                    clustering,
                    "01.01.2018",
                    "Tecrübesiz",
                    "100",
                    //   String.valueOf(getDistanceParce(
                    //          toRadiusMeters(getMyLocation(), clustering))),
                    ""));


        }

    }


    private void touchHandlerListener() {
        touchHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: EVENT TOUCH HANDLER 1 ");
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

                return false;
            }
        });

    }

    private void slidingPanelListener() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {


            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //previous state Dragging
                // new State Expanded iken State Draggin olursa  yeni state hidden yapıyorum
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
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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
            //1 marKer yoksa 2 marker vardır ozaman
            //İlk tıklayısta firstClick true .. yani 1 .tıklayısta mavi 2.tıklayısta kırmızı olcak locationbutton daki marker rengi
            if (firstClick) {

                mylocButton.setImageTintList(ColorStateList.valueOf(Color.BLUE));
                firstClick = false;
                return 1;

            } else {
                mylocButton.setImageTintList(ColorStateList.valueOf(Color.RED));
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
        DrawCircle drawCircle = mCircleList.get(0);
        drawCircle.setCenterCircle(MapHelperMethods.convertLocationtoLatLng(location)); //-->Circle kaldırıyorum
        // mCircleList.clear();//-->Circle listesini temizliyorum
        mUserLocationInfo.getCurrentLocationMarker().remove(); //Şuanki markeri kaldırıyorum
        mLastKnownLocation = location; //değişen lokasyon değerlerini son bilinen lokasyonuma eşitliyorum
        drawCurrentLocationMarker(mLastKnownLocation); //son lokasyona marker, koyuyorum.
        locationManager.removeUpdates(this);

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

                //Yakındaki daire içerisindeki işler listelenecek
                getNearJobList();
                if (resultList.size() > 0) {
                    Log.d(TAG, "resultListSize>0: ");
                    Intent i = new Intent(this, NearJobListActivity.class);
                    i.putParcelableArrayListExtra("nearList", resultList);
                    i.putExtra("nearListSize",resultList.size());
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    break;
                } else {
                    Toast.makeText(this, "Kapsama alanınızda Size uygun iş bulunamadı.", Toast.LENGTH_SHORT).show();
                    break;
                }


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
        }
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
     *               1 nesne tüm konumların adresini öğrenemiyoruz calışmıyor .. downloadAdressData nesnesi currentMarker konulurken
     *               çağrıldıgı için diger tüm konum istekleri yeni nesne ile gerçekleştirilmek zorunda ..
     */
    public void runAdressMethod(LatLng latLng) {

        if (downloadAdressData != null) {
            Log.d(TAG, "runTask: Yeni nesne Oluşturuldu ");
            new DownloadAdressData(getApplicationContext(), this).execute(latLng);
        } /*else {

            Log.d(TAG, "runTask: eski nesne ");
            downloadAdressData.execute(latLng);
        }*/


    }

    /**
     * Circle kapsama alanına giren itemleri result list içersine ekliyorum.Bu method yakınımdakileri listele dedğinde çalışacak
     * ve Bu listeyi işlemek üzere NearJobListActivty 'e gönderecek ..
     */

    public void getNearJobList() {
        int distance;
        DrawCircle drawCircle = mCircleList.get(0);
        resultList.clear();//her çağırıldıgında önceki listeyi temizleyecek ..

        //Tüm Cluster edilmiş itemleri alıyorum ...
        ArrayList<JobAdvertModel> items = (ArrayList<JobAdvertModel>) clusterManagerAlgorithm.getItems();
        UserLocationInfo.getInstance().setCircleArea(drawCircle.getCircleRadius());
        //  Log.d(TAG, "getNearJobList: " + items.get(0).toString() + circleArea.getProgress());
        for (JobAdvertModel item : items) {

            distance = MapHelperMethods.getDistanceParce(MapHelperMethods.toRadiusMeters(drawCircle.getCenterCircle(), item.getPosition()));
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
        myItem.setCompanyDistance(String.valueOf(MapHelperMethods.getDistanceParce(MapHelperMethods.toRadiusMeters(mUserLocationInfo.getMyLocation(), myItem.getPosition())))); //distance item üzerine tıklayınca hesaplatıyorum.
        if (mUserLocationInfo.getNewLocationMarker() != null) {

            myItem.setNewLocationDistance(String.valueOf(
                    MapHelperMethods.getDistanceParce(
                            MapHelperMethods.toRadiusMeters(
                                    mUserLocationInfo.getNewLocationLatLng(), myItem.getPosition()))));

        }
        customInfoWindowCluster.setClickedItem(myItem);// Tıkladıgım markerin bilgilerini ınfo windowa bildidiryorum ..

        //myClusterMarker.getMarker(myItem).showInfoWindow();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //jobNameSlidingPanel.setText(myItem.getCompanyJob() + "\t" + myItem.getCompanyDistance() + "\t" + myItem.getNewLocationDistance()); //Sliding Panel TExt
        jobNameSlidingPanel.setText(getString(R.string.slidingpanel_head, myItem.getCompanyJob(), myItem.getCompanyDistance(), myItem.getNewLocationDistance())); //Sliding Panel TExt
        runAdressMethod(myItem.getPosition());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myItem.getPosition()));
        return false; //-->true olunca ınfo window calışmıyor ..
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
            runAdressMethod(lat);       //Kamera her hareketinde adres değiştiriyor KALDIRILABİLİR ....

            myItemClusterManager.onCameraIdle();  ///-->Camera Zoom  13  oldugunda clusterin calısması için
            myClusterMarker.setShould_zoom(cameraPosition.zoom < 14);

        } else {

            myItemClusterManager.onCameraIdle();  ///-->Camera Zoom  13  oldugunda clusterin calısması için
            myClusterMarker.setShould_zoom(cameraPosition.zoom < 14);
        }


    }

    @Override
    public void onClusterItemInfoWindowClick(JobAdvertModel myItem) {

        Log.d(TAG, "onClusterItemInfoWindowClick:Tıklandı ");
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
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

    //Adress Callback İnterface .. .
    @Override
    public void adressCallbackResult(String result) {

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

    }

}