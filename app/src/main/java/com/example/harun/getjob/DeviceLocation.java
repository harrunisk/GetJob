package com.example.harun.getjob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by mayne on 25.06.2018.
 */

public class DeviceLocation {
    private static final String TAG = "DeviceLocation";
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private boolean mPermissionGranted, isGPSEnabled;
    //private LocationManager loc;
    private LocationManager locationManager;
    public LatLng gps;
    DeviceLocationCallback deviceLocationCallback;

    public DeviceLocation(Activity activity, DeviceLocationCallback deviceLocationCallback) {


        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        this.deviceLocationCallback = deviceLocationCallback;

    }

    public interface DeviceLocationCallback {

        void deviceLocationCallback(LatLng gps);
    }


    public void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: ÇAĞRILDI");
        try {
            // if (mPermissionGranted) {
            //   Log.d(TAG, "getDeviceLocation: mPermissionGranted " + mPermissionGranted);
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            Log.d(TAG, "onComplete: mLastKnownLocation" + mLastKnownLocation);
                            deviceLocationCallback.deviceLocationCallback(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
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
            //  }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
        //return gps;
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation:ÇAĞRILDI ");
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "getCurrentLocation: isNetworkEnabled" + isNetworkEnabled);
        Log.d(TAG, "getCurrentLocation: isGPSEnabled" + isGPSEnabled);
        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {

            Log.d(TAG, "getCurrentLocation: " + "Konum Bİlgisi Yok");

        } else {
            if (isNetworkEnabled) {
                Log.d(TAG, "getCurrentLocation: isNetworkEnabled");
                //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                //        0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                Log.d(TAG, "getCurrentLocation: isGPSEnabled ");
                //    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                //            0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d(TAG, "getCurrentLocation: Location=!null");
            mLastKnownLocation = location; ///Şuanki konum bilinen son konum olarak atanıyor .

            deviceLocationCallback.deviceLocationCallback(MapHelperMethods.convertLocationtoLatLng(mLastKnownLocation));
            // drawCurrentLocationMarker(mLastKnownLocation);

        } else {
            Log.d(TAG, "getCurrentLocation: ");
            deviceLocationCallback.deviceLocationCallback(null);


        }

    }

}
