package com.example.harun.getjob.JobSearch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayne on 22.03.2018.
 */

public class JobSearch extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, LocationListener, DiscreteSeekBar.OnProgressChangeListener, View.OnClickListener, ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>, GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraChangeListener {
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
    //Sliding Widget From jobsearch 2
    // Sliding Layout
    TextView jobName;
    LinearLayout touchHandler;
    FloatingActionButton mylocButton;
    private ArrayList<CategoryModel> categoryModelArrayList;
    CategoryAdapter mCategoryAdapter;
    private CategoryModel mCategoryModel;


    private String myLocationAdress;
    LocationAdress mLocationAdress;
    //Map local değişkenleri
    private GoogleMap mMap;
    boolean mPermissionGranted;
    boolean isGPSEnabled;
    LocationManager locationManager;
    LocationManager loc;
    MapStyleOptions style;
    private LatLng myLocation;
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 13;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //  private ArrayList<Marker> markerList;
    ArrayList<LatLng> locationList;
    boolean isOpened = false;
    Marker currentLocationMarker;
    private ClusterManager<MyItem> myItemClusterManager;

    public static boolean should_zoom;
    LatLng deneme1 = new LatLng(40.98636580, 28.66981733);
    LatLng deneme2 = new LatLng(40.981415, 28.6630712);
    LatLng deneme3 = new LatLng(40.978889, 28.6635097);
    LatLng deneme4 = new LatLng(40.998889, 28.6687097);
    LatLng deneme5 = new LatLng(40.994859, 28.6686097);

    List<DrawCircle> mCircle = new ArrayList<>(1);
    //Sliding Panel
    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobsearch2);

        locationList = new ArrayList<>();
        locationList.add(0, deneme1);
        locationList.add(1, deneme2);
        locationList.add(2, deneme3);
        locationList.add(3, deneme4);
        locationList.add(4, deneme5);


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


    @Override
    public void onBackPressed() {

        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            Log.d(TAG, "onBackPressed: SLİDİNG PANEL KAPANDI");
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
            Log.d(TAG, "onBackPressed: TELEFONUN GERİ tusuna Basıldı");
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }
    }

    /**
     * Konum Bilgisi Alınıyorsa işlemler İşlemler gerçekleştirilecek.
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


    /*
        Kullanıcının GPS konumunu açması için açılan settings penceresinin sonucu burada işleniyor.
        Konumu Açarsa -> kontrol edilip islocationProviderEnable();
        Açmaz ise Activity sonlandır.
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
            myItemClusterManager.addItem(new MyItem(clustering, "ITEM "));

        }

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

    private void drawCurrentLocationMarker(Location location) {

        if (checkReadyMap()) {   //Map hazırmı değilmi kontrolü
            Log.d(TAG, "drawCurrentLocationMarker: ÇAĞRILDI");
            Drawable currentMarkerStyle = getResources().getDrawable(R.drawable.markeruser);
            BitmapDescriptor icon = getDrawableMarkerAsBitmap(currentMarkerStyle);
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());


            currentLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .icon(icon)
                    .title("Buradasınız"));

            currentLocationMarker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, DEFAULT_ZOOM));
            setMyLocationAdress(gps); //Adresi set ediyoruz Bulundugumuz konumun
            tvAdress.setText(getMyLocationAdress());
            DrawCircle drawCircle = new DrawCircle(location, 1000);//İlk Circle 1-km Alanı kapsayacak
            mCircle.add(drawCircle); //Eklenen her circle Listeye alınıyor .

        }

    }

    /**
     * Drawable klasorundeki marker vektorel oldugu icin onu bitmap 'e çeviren fonksiyon yazışdı.
     *
     * @param currentMarker -->Drawable marker
     * @return
     */
    public BitmapDescriptor getDrawableMarkerAsBitmap(Drawable currentMarker) {

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(currentMarker.getIntrinsicWidth(), currentMarker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        currentMarker.setBounds(0, 0, currentMarker.getIntrinsicWidth(), currentMarker.getIntrinsicHeight());
        currentMarker.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);


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
        mLayout = findViewById(R.id.sliding_layout);

        jobName = findViewById(R.id.JobName);
        touchHandler = findViewById(R.id.touchHandler);
        mylocButton = findViewById(R.id.myLocButton);
        //touchHandler2 = findViewById(R.id.touchHandler2);

        init();
    }

    // Değişkenlerin  İlklemeleri

    private void init() {
        Log.d(TAG, "init: ÇAĞRILDI");
        categoryModelArrayList = new ArrayList<>();
        //  markerList = new ArrayList<>();
        mCategoryModel = new CategoryModel();
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro); //Map Style Retro
        DiscreteSeekBarRange();
        mLocationAdress = new LocationAdress(this);//Adres alma methodlarını topladıgım sınıftan nesnee alıyorum
        btnListNearJob.setOnClickListener(this);
        slidingPanelListener();
        touchHandlerListener();

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
                //Log.d(TAG, "onPanelSlide: ");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //previous state Dragging
                // new State Expanded iken State Draggin olursa  yeni state hidden yapıyorum

                //  Log.d(TAG, "onPanelStateChanged: " + previousState + newState);
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
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: ÇAĞRILDI");
        mMap = googleMap;
        setupMap(mMap); //--->Harita Özelliklerini gerçekleştir.
        mylocButton.setOnClickListener(this);

        myItemClusterManager = new ClusterManager<MyItem>(this, mMap);
        //    mMap.setOnCameraIdleListener(myItemClusterManager);
        myItemClusterManager.setRenderer(new MyClusterMarker(this, mMap, myItemClusterManager)); //Cluster style
        myItemClusterManager.setOnClusterClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(myItemClusterManager);
        mMap.setOnMyLocationClickListener(this);
        myItemClusterManager.setOnClusterItemClickListener(this);
        // mMap.setOnMyLocationClickListener(this); //Lokasyonuma tıklandıgında
        circleArea.setOnProgressChangeListener(this);
        getDeviceLocation();  // haritayı son konuma aktif etme
        drawJobLocation();    //

        myItemClusterManager.cluster();

    }


    //Haritanın özelliklerini bu fonksiyonda ayarlıyorum .
    @SuppressLint("MissingPermission")
    public void setupMap(GoogleMap _mMap) {
        Log.d(TAG, "setupMap: ");
        _mMap.setMapStyle(style);
        _mMap.getUiSettings().setZoomControlsEnabled(true);
        _mMap.getUiSettings().setMapToolbarEnabled(false);
        _mMap.setMyLocationEnabled(false);
        _mMap.getUiSettings().setMyLocationButtonEnabled(false);
        _mMap.setBuildingsEnabled(true);
    }

    private boolean checkReadyMap() {
        if (mMap == null) {
            Toast.makeText(this, "MAP READY CONTROL", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        //  Permission İzin Verilmemiş ise burada alert dialog konulacak .
        if (!mPermissionGranted) {

            Log.d(TAG, "onResumeFragments:");
        }
    }


    //Bulundugum konumun adresini setEdiyorum..
    public void setMyLocationAdress(LatLng gps) {
        myLocationAdress = mLocationAdress.getAdress(gps);
    }


    public String getMyLocationAdress() {
        return myLocationAdress;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: ");
        return true;

    }



    /*ONLOCATİON CHANGE */

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.getLatitude());

        mLastKnownLocation = location;
        drawCurrentLocationMarker(mLastKnownLocation);
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
            for (DrawCircle drawCircle : mCircle)
                drawCircle.setCircleRadius(updatedValue);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMap.clear();
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

                Intent i = new Intent(this, NearJobListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                break;

            case R.id.myLocButton:
                Log.d(TAG, "onClick: MYLOC BUTTON TIKLANDI");
                currentLocationMarker.showInfoWindow();
                tvAdress.setText(getMyLocationAdress());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14), 1500, null);

                break;
        }
    }


    //Clustere tıklanınca içerisindeki itemlerin oldugu bölgenin açılıp kameranın oraya hareket etmesini istiyorum .
    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {

        Log.d(TAG, "onClusterClick: ");
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

    @Override
    public boolean onClusterItemClick(MyItem myItem) {

        Log.d(TAG, "onClusterItemClick: TIKLANDI");
        // myItem.showInfoWindow();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        double result = toRadiusMeters(myLocation, myItem.getPosition());
        double result2 = SphericalUtil.computeDistanceBetween(myLocation, myItem.getPosition());
        jobName.setText(myItem.getTitle() + "\t" + String.valueOf(result)); //Sliding Panel TExt
        Log.d(TAG, "onClusterItemClick: DENEME UZAKKLIK \t " + result + "\t\t" + result2);

        tvAdress.setText(mLocationAdress.getAdress(myItem.getPosition())); //ADres Bilgisi Yazdırılıyor.

        mMap.animateCamera(CameraUpdateFactory.newLatLng(myItem.getPosition()));
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick: MYLOCATİN TIKLANDI ");
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        myItemClusterManager.onCameraIdle();  ///-->Camera Zoom  13  oldugunda clusterin calısması için
        should_zoom = cameraPosition.zoom < 14;
        // Log.d(TAG, "onCameraChange: " + should_zoom + cameraPosition);

    }


    /**
     * Circle Class
     */
    private class DrawCircle {
        private Marker mRadiusMarker, mRadiusMarker2;
        private Circle mCircle;
        private double mRadiusMeters;


        public DrawCircle(Location center, double radiusMeters) {
            this.mRadiusMeters = radiusMeters;
            Log.d(TAG, "DrawCircle: ");
            myLocation = new LatLng(center.getLatitude(), center.getLongitude());

            // deneme FillColorRenk Kodlarım
            //rgba(143, 230, 150, 1)    // int  mFillColorArgb=587267853;
            // 453902080 470679296
            // Color.argb(100, 243, 85, 133
            // )Color.argb(1, 143, 230, 150)
            int mFillColorArgb = 637597695;
            // rgba(209, 255, 163, 0.68)
            // rgba(184, 226, 121, 0.28)


            mCircle = mMap.addCircle(new CircleOptions()
                    .center(myLocation)
                    // .strokePattern(PATTERN_DOTTED)
                    .strokeColor(Color.WHITE)
                    .strokeWidth(5)
                    .fillColor(mFillColorArgb)
                    .radius(mRadiusMeters));


            // Log.d(TAG, "DrawCircle: " + circleArea.getProgress() * 1000);

        }

        /**
         * Bu method Circle kapsama alanını çizer
         *
         * @param circleRadius --> SeekBar dan gelen km Değerine göre çizilen alan belirlenir yarıçap ayarlanır. .
         */
        public void setCircleRadius(int circleRadius) {

            this.mCircle.setRadius((circleRadius * 1000));
            //Log.d(TAG, "setCircleRadius: " + circleArea.getProgress() * 1000);
            //Log.d(TAG, "setCircleRadius: " + (circleRadius * 1000));


            /**

             Seek Bardan gelen km değerine göre çizilen her circle için kamerada circle in sağ ve solunda verilen
             koordinatlara göre kamera açısını belirler
             Sağ konum -->toRadiusLatLngRight(myLocation, (circleRadius * 1000) merkez kooordinattan parametre olarak gelen uzaklığın konumu
             Sol konum -->toRadiusLatLngLeft(myLocation, (circleRadius * 1000)  Bu konumlar sınır olarak belirlenir.


             */

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(toRadiusLatLngLeft(myLocation, (circleRadius * 1000)));
            builder.include(toRadiusLatLngRight(myLocation, (circleRadius * 1000)));

            final LatLngBounds bounds = builder.build();

            final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
            final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
            final int zoomPadding = (int) (zoomWidth * 0.10); // Genişliğin 0,10 u kadar kenarlardan boşluk


            ///  Log.d(TAG, "setCircleRadius: zoomWİDHT \t" + zoomHeight + "\t" + zoomWidth + "\t" + zoomPadding);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth, zoomHeight, zoomPadding));
        }
    }

    /**
     * Verilen merkez koordinatına yine parametre olarak verilen mesafe uzaklıgınca koordinat gönderiri
     *
     * @param center-->Merkez    koordinatt
     * @param radiusMeters-->kaç metre uzaklıkta yarıcapın Mesafesi
     * @return --LatLng Değeri center.longitude + radiusAngle -->merkezin Sag tarafı
     */
    private LatLng toRadiusLatLngRight(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / 6371009) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    /**
     * Verilen merkez koordinatına yine parametre olarak verilen mesafe uzaklıgınca koordinat gönderiri
     *
     * @param center->Merkez     koordinatt
     * @param radiusMeters-->kaç metre uzaklıkta Yarıçapın Mesafesi
     * @return --LatLng Değeri center.longitude - radiusAngle -->merkezin Sol tarafı
     */
    private LatLng toRadiusLatLngLeft(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / 6371009) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude - radiusAngle);
    }

    /**
     * İki nokta arası uzaklık ölçülüyor
     *
     * @param center    -->bulundugunuz konum-->nereden
     * @param radius--> -->2.Konum .ç Ölçülecek mesafenin konumu-->nereye
     * @return ->double distance
     */
    private double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }


    /**
     * Cluster İşlemleri..
     */
    private class MyClusterMarker extends DefaultClusterRenderer<MyItem> {

        public Drawable drawable;
        private final IconGenerator icon = new IconGenerator(getApplicationContext());

        public MyClusterMarker(Context context, GoogleMap map, ClusterManager clusterManager) {
            super(context, map, clusterManager);
        }

        public MyClusterMarker(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager, Drawable drawable) {
            super(context, map, clusterManager);
            this.drawable = drawable;
        }


        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

            Drawable drawable = getResources().getDrawable(R.drawable.markerjob);

            BitmapDescriptor jobMarker = getDrawableMarkerAsBitmap(drawable);

            markerOptions.icon(jobMarker);

            markerOptions.title("Yakınımdaki İşler");


        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        //Camera Zoom  12 oldugunda  ve Marker sayısı 5 oldugunda en az clusterin calısması için override edildi.

        //should_zoom true ve  cluster marker >4 oldugunda calısacak.
        @Override
        protected boolean shouldRenderAsCluster(Cluster<MyItem> cluster) {

            //Log.d(TAG, "shouldRenderAsCluster: " + should_zoom);
            return super.shouldRenderAsCluster(cluster) && should_zoom;

        }
    }
}
