package com.example.harun.getjob.JobSearch.JobUtils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by mayne on 14.04.2018.
 */

/**
 * Harita üzerinde dinamik olarak olusturulacak adres konum ve marker ,kapsama alanı bilgilerini  burada tutacagımm ..
 * <p>
 * Bu sınıfın tek nesnesi olacak oda JobSearch ilk açıldıgı zaman olusturuluyor
 * ve buradaki bilgilere o nesne aracılıgı ile ulasıyorum return mUserLocationInfo;.
 */
public class UserLocationInfo {
    private static final String TAG = "UserLocationInfo";
    public LatLng myLocation, newLocationLatLng;
    public Marker currentLocationMarker, newLocationMarker;
    public String myLocationAdress, newLocationAdress;
    public int circleArea;
    public static UserLocationInfo mUserLocationInfo;

    public UserLocationInfo(){
     //   super();
    }


    public static UserLocationInfo getInstance() {
        if (mUserLocationInfo == null) {
            Log.d(TAG, "getInstance: ");
            mUserLocationInfo = new UserLocationInfo();
            return mUserLocationInfo;
        } else {


            Log.d(TAG, "getInstance: NOTNULL");
            return mUserLocationInfo;
        }


    }

    public int getCircleArea() {
        return circleArea;
    }

    public void setCircleArea(int circleArea) {
        this.circleArea = circleArea;
    }

    public LatLng getMyLocation() {
        Log.d(TAG, "getMyLocation: ");
        return myLocation;
    }

    public void setMyLocation(LatLng myLocation) {
        Log.d(TAG, "setMyLocation: ");
        this.myLocation = myLocation;
    }

    public LatLng getNewLocationLatLng() {
        Log.d(TAG, "getNewLocationLatLng: ");
        return newLocationLatLng;
    }

    public void setNewLocationLatLng(LatLng newLocationLatLng) {
        Log.d(TAG, "setNewLocationLatLng: ");
        this.newLocationLatLng = newLocationLatLng;
    }

    public Marker getCurrentLocationMarker() {
        Log.d(TAG, "getCurrentLocationMarker: ");
        return currentLocationMarker;
    }

    public void setCurrentLocationMarker(Marker currentLocationMarker) {
        Log.d(TAG, "setCurrentLocationMarker: ");
        this.currentLocationMarker = currentLocationMarker;
    }

    public Marker getNewLocationMarker() {
        Log.d(TAG, "getNewLocationMarker: ");
        return newLocationMarker;
    }

    public void setNewLocationMarker(Marker newLocationMarker) {
        Log.d(TAG, "setNewLocationMarker: ");
        this.newLocationMarker = newLocationMarker;
    }

    public String getMyLocationAdress() {
        Log.d(TAG, "getMyLocationAdress: ");
        return myLocationAdress;
    }

    public void setMyLocationAdress(String myLocationAdress) {
        Log.d(TAG, "setMyLocationAdress: ");
        this.myLocationAdress = myLocationAdress;
    }

    public String getNewLocationAdress() {
        Log.d(TAG, "getNewLocationAdress: ");
        return newLocationAdress;
    }

    public void setNewLocationAdress(String newLocationAdress) {
        Log.d(TAG, "setNewLocationAdress: ");
        this.newLocationAdress = newLocationAdress;
    }

}
