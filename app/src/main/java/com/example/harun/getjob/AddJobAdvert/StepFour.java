package com.example.harun.getjob.AddJobAdvert;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.harun.getjob.JobSearch.DownloadAdressData;
import com.example.harun.getjob.JobSearch.JobUtils.DrawCircle;
import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel2;
import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.MyCustomToast;
import com.example.harun.getjob.Profile.Permissions;
import com.example.harun.getjob.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by mayne on 1.05.2018.
 */

public class StepFour extends Fragment implements Step, View.OnClickListener, OnMapReadyCallback, View.OnTouchListener, GoogleApiClient.OnConnectionFailedListener, TextView.OnEditorActionListener, GoogleMap.OnCameraChangeListener, DownloadAdressData.adressCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private static final String TAG = "StepFour";
    private boolean mPermissionGranted, isGPSEnabled;
    private LocationManager loc;
    FloatingActionButton myLocButton1;
    ImageView mapImage;
    Button getLocationBtn;
    TextView companyAdress;
    NestedScrollView nestedScroll;
    AVLoadingIndicatorView mapImageProgress;
    LinearLayout clearLayout, searchLayout;
    private View view;
    private View transparentView;
    private AutoCompleteTextView input_search;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationManager locationManager;
    private PlaceAutocomplete placeAutocomplete;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private AutocompleteFilter autocompleteFilter;
    private DrawCircle circle;
    private Marker mMarker;

    private boolean isFirstTime = false;
    LatLng coord;
    ViewSwitcher viewSwitcher;
    private DownloadAdressData mDownloadAdressData;
    JobAdvertModel2 jobAdvertModel2;

    //Entire World
    private static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(-85, -180), new LatLng(85, 180));
    private boolean choosenAdress = false;

    /**
     * Activty ilk açılışta 4 frragment birden yükleniyor ben bu fragmentin sadece görünür oldugunda yüklenip çalışmasını
     * istedğim için bu metodu override ettim buradaki mantık
     * fragment kullanıcı ekranında görünür oldugunda isVisibleToUser=true olur  ve getPermission methodu cagrılarak izin alma işlemleri
     * sadece ekran görünür oldugunda yapılacak getPermission methodu calısıtıgı zaman zaten izinler verilmiş ise
     * gatherView oda init
     * fonksiyonunu cagırarak kullanıcı ekranı olusturulacak .
     * Adres ile arama yaparken kullanılan mGoogleApiClient kullanıcı ekrandan ayrıldıgı zaman disconnect olmalı bu yüzden burada
     * ayrıca kontrolleri yapıldı
     * (mGoogleApiClient != null kontrolune girecek)
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) { //isResumed

            Log.d(TAG, "setUserVisibleHint: GÖRÜNÜR ");
            if (mGoogleApiClient != null) {
                Log.d(TAG, "setUserVisibleHint:mGoogleApiClient !=null ");
                if (mGoogleApiClient.isConnected()) {
                    Log.d(TAG, "setUserVisibleHint:mGoogleApiClient !=null and CONNNECTED");
                    mGoogleApiClient.stopAutoManage(getActivity());
                    mGoogleApiClient.disconnect();
                } else {
                    Log.d(TAG, "setUserVisibleHint:mGoogleApiClient !=null and NOT CONNECTED");
                    mGoogleApiClient.connect();

                }
            } else {
                Log.d(TAG, "setUserVisibleHint: mGoogleApiClient NULLL");
                getPermission();

            }

        } else {
            Log.d(TAG, "setUserVisibleHint: ŞUAN GÖRÜNMEZ");
            if (mGoogleApiClient != null) {
                Log.d(TAG, "setUserVisibleHintGÖRÜNMEZ:mGoogleApiClient !=null ");
                if (mGoogleApiClient.isConnected()) {
                    Log.d(TAG, "setUserVisibleHintGÖRÜNMEZ:mGoogleApiClient !=null and CONNNECTED");
                    mGoogleApiClient.stopAutoManage(getActivity());
                    mGoogleApiClient.disconnect();
                }

            }
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (mGoogleApiClient != null) {
            if (!mGoogleApiClient.isConnected())
                Log.d(TAG, "!mGoogleApiClient.isConnected() ");
            mGoogleApiClient.connect();
        }



       /* if (mGoogleApiClient != null) {
            Log.d(TAG, "onPause: mGoogleApiClient != null ");
            mGoogleApiClient.connect();
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: METODU ");
        //isFirstTime = true;
        if (mGoogleApiClient != null) {
            Log.d(TAG, "onPause: mGoogleApiClient != null ");
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.add_jobadvert_step4, container, false);

        if (getUserVisibleHint()) {
            Log.d(TAG, "onCreateView: GÖRÜNÜR HALDE ");

            setUserVisibleHint(true);
            getPermission();

        }
        return view;
    }

    private void getPermission() {

        loc = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Map için gerekli izinler varmı
        if (Permissions.checkPermissionArray(getActivity(), Permissions.MapPermission)) {
            Log.d(TAG, ":izinler verilmis  ");
            mPermissionGranted = true;
            islocationProviderEnable();
        } else {
            //Yoksa iste
            Log.d(TAG, ": izinler isteniyor ");
            //   Permissions.myrequestPermission(getActivity(), Permissions.MapPermission);
            requestPermissions(Permissions.MapPermission, 1);

        }

    }


    private void gatherViews() {
        Log.d(TAG, "gatherViews:GEldik yine@@@@@ ");
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.assign_map);
        transparentView = view.findViewById(R.id.transparentView);
        myLocButton1 = view.findViewById(R.id.myLocButton1);
        clearLayout = view.findViewById(R.id.clearLayout);
        searchLayout = view.findViewById(R.id.search_adressLayout);
        input_search = view.findViewById(R.id.input_search);
        mapImage = view.findViewById(R.id.mapImage);
        getLocationBtn = view.findViewById(R.id.getLocationBtn);
        mapImageProgress = view.findViewById(R.id.mapImageProgress);
        companyAdress = view.findViewById(R.id.companyAdress);
        viewSwitcher = view.findViewById(R.id.viewSwitch);
        nestedScroll = view.findViewById(R.id.nestedScroll);
        init();

    }

    private void init() {
        Log.d(TAG, "init: ");
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mDownloadAdressData = new DownloadAdressData(getActivity(), this, 2);
        mapFragment.getMapAsync(this);
        transparentView.setOnTouchListener(this);
        input_search.setOnEditorActionListener(this);
        myLocButton1.setOnClickListener(this);
        clearLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        getLocationBtn.setOnClickListener(this);
        viewSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
        viewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));
        if (getArguments() != null) {
            Log.d(TAG, "init:getArguments()!=null ");
            jobAdvertModel2 = getArguments().getParcelable("jobAdvertModel");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;

        setupMap(mMap);
        getDeviceLocation();

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("TR")
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient,
                BOUNDS, autocompleteFilter);

        input_search.setAdapter(mPlaceAutocompleteAdapter);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);

    }


    @SuppressLint("MissingPermission")
    private void setupMap(GoogleMap _mMap) {
        Log.d(TAG, "setupMap: ");
        _mMap.getUiSettings().setZoomControlsEnabled(true);
        _mMap.getUiSettings().setCompassEnabled(false);
        _mMap.getUiSettings().setMapToolbarEnabled(false);
        _mMap.setMyLocationEnabled(false);
        _mMap.getUiSettings().setMyLocationButtonEnabled(false);
        _mMap.setBuildingsEnabled(true);

    }


    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: ÇAĞRILDI");
        try {
            if (mPermissionGranted) {
                //   Log.d(TAG, "getDeviceLocation: mPermissionGranted " + mPermissionGranted);
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                Log.d(TAG, "onComplete: mLastKnownLocation" + mLastKnownLocation);
                                LatLng gps = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                drawMarkerMoveCamera(gps);
                                //moveCamera(gps);
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

    private void drawMarkerMoveCamera(LatLng gps) {

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(gps)));
        coord = gps;
        if (circle != null && mMarker != null) {
            Log.d(TAG, "drawMarkerMoveCamera: ");
            mMarker.setPosition(gps);
            circle.setCenterCircle(gps);
        } else {
            Log.d(TAG, "drawMarkerMoveCamera: ");
            mMarker = mMap.addMarker(
                    new MarkerOptions().position(gps).title("Buradasınız").
                            icon(MapHelperMethods.getNormalMarkerDrawable(getContext())));
            circle = new DrawCircle(MapHelperMethods.convertLatLngtoLocation(gps), 50, mMap, getContext(), 2);

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
        return new CameraPosition.Builder().target(_center).zoom(17f).tilt(30).build();
    }

    /**
     * getDeviceLocation methodunda hata olması durumunda konum bilgisi gps den saglanacak
     */
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
                // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                //         0, 10,this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                Log.d(TAG, "getCurrentLocation: isGPSEnabled ");
                //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                //          0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d(TAG, "getCurrentLocation: drawCurrentLocationMarker Çağrılıyor");
            mLastKnownLocation = location; ///Şuanki konum bilinen son konum olarak atanıyor .
            //drawCurrentLocationMarker(mLastKnownLocation);
            drawMarkerMoveCamera(MapHelperMethods.convertLocationtoLatLng(mLastKnownLocation));

        }

    }

    /**
     * Kullanıcının GPS konumunu açması için açılan settings penceresinin sonucu burada işleniyor.
     * Konumu Açarsa -> kontrol edilip islocationProviderEnable();
     * Açmaz ise Activity sonlandır.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    getActivity().finish();
                }

                break;
        }


    }

    /**
     * @param latLng alınan konumun adresini öğrenmek üzere DownloadAdressData methodunu cagırıyor.
     *               Bu method async method oldugunda her konum isteği için ayrı nesne oluşturmak zorundayız..
     *               1 nesne tüm konumların adresini öğrenemiyoruz . downloadAdressData nesnesi currentMarker konulurken
     *               çağrıldıgı için diger tüm konum istekleri yeni nesne ile gerçekleştirilmek zorunda ..
     */
    public void runAdressMethod(LatLng latLng) {

        if (mDownloadAdressData != null) {
            Log.d(TAG, "runTask: Yeni nesne Oluşturuldu ");
            // adresLoading.show();
            new DownloadAdressData(getActivity(), this, 2).execute(latLng);
        } else {
            Log.d(TAG, "runTask: eski nesne ");
            mDownloadAdressData.execute(latLng);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //     super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: ONREQUESTPERMİSSİONRESULT");
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
        Permissions.showAlertdilaog(getActivity(), "",
                "İZİN VERMEN LAZIM KARDEŞ ", 3000).show();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getActivity().finish();
            }
        };
        handler.postDelayed(r, 3000);
    }

    /**
     * Konum Bilgisi Alınıyorsa Telefonun konum alma işlemi açıkmı kapalımı  İşlemler gerçekleştirilecek.
     */
    public void islocationProviderEnable() {
        Log.d(TAG, "islocationProviderEnable: ÇAĞRILDI");
        isGPSEnabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            //konum alma açık Viewları yükle.
            Log.d(TAG, "islocationProviderEnable: " + isGPSEnabled);
            gatherViews();
        } else {
            //Konum acık değil bir aletr dialog ile kullanıcıyı bilgilendir.
            Log.d(TAG, "islocationProviderEnable: " + isGPSEnabled);
            Toast.makeText(getActivity(), "Please turn onn GPS", Toast.LENGTH_LONG).show();
            showGPSDisabledAlertToUser();

        }

    }

    /**
     * Kullanıcı GPS Açık değilse bir alert dialog ile Gps açması gerektiğini söylüyorum
     */
    private void showGPSDisabledAlertToUser() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                getActivity().finish();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }


    @Nullable
    @Override
    public VerificationError verifyStep() {

        if (validate()) {

            return new VerificationError("Lütfen Bir Adres Seçiniz");
        } else {

            Log.d(TAG, "verifyStep: ");
            createsuccesDialog();
            return null;
        }
    }

    private void createsuccesDialog() {
        Log.d(TAG, "createsuccesDialog: ");

        FragmentManager fragmentManager = getFragmentManager();
        SuccessDialogFragment successDialogFragment = new SuccessDialogFragment();
        Bundle b = new Bundle();
        b.putParcelable("jobAdvertModel", jobAdvertModel2);
        successDialogFragment.setArguments(b);
        successDialogFragment.show(fragmentManager, "SuccessDialog");


    }

    private boolean validate() {

        if (viewSwitcher.getChildAt(0) == viewSwitcher.getCurrentView()) {
            //Buradaki kosul -->Şuanki View emptyView (index:0) ise
            if (!companyAdress.getText().equals("")) {

                return true;
            } else {
                return false;
            }


        } else {
            return false;
        }


    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected: ");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError: ");
        MyCustomToast.showCustomToast(getContext(), "Lütfen Bir Adres Seçiniz ");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myLocButton1:

                Log.d(TAG, "onClick: ");
                getDeviceLocation();
                break;
            case R.id.clearLayout:

                Log.d(TAG, "onClick: ");
                input_search.setText("");
                input_search.clearFocus();
                break;
            case R.id.search_adressLayout:

                Log.d(TAG, "onClick: ");
                geoLocate();
                hideKeyboard();

                break;
            case R.id.getLocationBtn:
                // nestedScroll.scrollTo(0, nestedScroll.getBottom());
                // AddJobAdvert.smoothScroll();
                //Bulundugu parent view in genişlik ve yükseklik değerlerini alıyorum bu
                // değerlere göre static mapin size değerini ayarlıyorum
                choosenAdress = true;
                checkEmptyView();
                companyAdress.setText(""); //Adres bilgisi textini temizle
                View parent = (View) mapImage.getParent().getParent().getParent();
                Log.d(TAG, "onClick: " + parent.getWidth() + "\t" + parent.getHeight());
                String url =
                        "https://maps.googleapis.com/maps/api/staticmap?" +
                                "center=" +
                                coord.latitude +
                                "," +
                                coord.longitude +
                                "&zoom=17" +
                                "&size=" + parent.getWidth() / 4 + "x" + parent.getHeight() / 2 +
                                "&scale=2" +
                                "&markers=" + "color:red|" +
                                coord.latitude +
                                "," +
                                coord.longitude +
                                "&maptype=terrain" +
                                "&key=AIzaSyDEf8QNnqFNFsCz_uHOacvTVuCFNwk03sU";
                Log.d(TAG, "onClick: " + url);

                mapImageProgress.setVisibility(View.VISIBLE);
                Picasso.get().load(url).error(android.R.drawable.ic_dialog_alert).into(mapImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        mapImageProgress.setVisibility(View.GONE);
                        runAdressMethod(coord);
                    }

                    @Override
                    public void onError(Exception e) {
                        Permissions.showAlertdilaog(getActivity(), "Beklenmedik Bir Hata oluştu", "Lütfen Tekrar Deneyin"
                                , 2000).show();

                        //MyCustomToast.showCustomToast(getActivity(), "Beklenmedik bir Hata Meydana Geldi Bir Daha Deneyin!");
                        mapImageProgress.setVisibility(View.GONE);
                    }
                });

                break;

        }
    }

    private void setJobAdress() {
        Log.d(TAG, "setJobAdress: ");
        jobAdvertModel2.setCompanyAdress(companyAdress.getText().toString());
        jobAdvertModel2.setmPosition(new com.example.harun.getjob.AddJobAdvert.LatLng(coord.latitude, coord.longitude));

    }


    /**
     * View Switcher Kullanımı
     * Kullanıcı henüz bir adres bilgisi seçmemiş ise gösterilecek olan viewi ayarlayacak
     */
    private void checkEmptyView() {
        Log.d(TAG, "checkEmptyView: ");
        if (choosenAdress) {
            viewSwitcher.setDisplayedChild(1);
        } else {

            viewSwitcher.setDisplayedChild(0);
        }


    }

    /**
     * Map View Scroll View içinde oldugu için mapin scroll işlemlerinin dogru çalışması için override edildi .
     *
     * @param view
     * @param motionEvent
     * @return
     */

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (view.getId()) {
            case R.id.transparentView:

                if (action == MotionEvent.ACTION_DOWN) {

                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                if (action == MotionEvent.ACTION_UP) {

                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;

                }

                if (action == MotionEvent.ACTION_MOVE) {

                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }

            default:
                return true;
        }


        //  return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if (i == EditorInfo.IME_ACTION_SEARCH
                || i == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

            hideKeyboard();
            geoLocate();
        }


        return false;
    }

    private void hideKeyboard() {
        Log.d(TAG, "hideKeyboard: ");
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(input_search.getWindowToken(), 0);
    }

    private void geoLocate() {

        Log.d(TAG, "geoLocate: ");

        String searching_adress = input_search.getText().toString();
        if (Geocoder.isPresent() && !searching_adress.isEmpty()) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                List<Address> coordinats = geocoder.getFromLocationName(searching_adress, 1);

                if (coordinats.size() > 0) {
                    Log.d(TAG, "geoLocate: " + coordinats);
                    Address address = coordinats.get(0);

                    coord = new LatLng(address.getLatitude(), address.getLongitude());
                    drawMarkerMoveCamera(coord);

                } else {
                    Toast.makeText(getActivity(), "Belirttiğiniz Adresi Bulamadık ", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {

                MyCustomToast.showCustomToast(getActivity(), e.getMessage());
                e.printStackTrace();
            }

        }


    }

    /**
     * Haritada kamera her değiştinde marker ve circle pozisyonları değişecek..
     *
     * @param cameraPosition -
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng lat = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
        if (circle != null && mMarker != null) {
            Log.d(TAG, "drawMarkerMoveCamera: ");
            coord = lat;
            mMarker.setPosition(lat);
            circle.setCenterCircle(lat);
            if (viewSwitcher.getChildAt(0) != viewSwitcher.getCurrentView()) {
                //Buradaki kosul -->Şuanki View emptyView (index:0) Degil ise gösterilen view i  empty view olarak ayarla
                viewSwitcher.setDisplayedChild(0);
            }
        }
    }


    @Override
    public void adressCallbackResult(String result) {

        companyAdress.setText(result);
        setJobAdress();

    }

    @Override
    public void onCameraMove() {

        Log.d(TAG, "onCameraMove: ");
    }

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle: ");

    }
}
