package com.example.harun.getjob.JobSearch;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harun.getjob.JobSearch.JobUtils.MapHelperMethods;
import com.example.harun.getjob.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mayne on 15.04.2018.
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private static final String TAG = "MapViewFragment";
    GoogleMap mGoogleMap;
    private LatLng position;

    public MapViewFragment() {
    }

    @SuppressLint("ValidFragment")
    public MapViewFragment(LatLng position) {
        this.position = position;
        Log.d(TAG, "MapViewFragment: " + position);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mapfragment, container, false);

        mMapView = v.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);

            mMapView.getMapAsync(this);
            Log.d(TAG, "onCreateView: ");

        }

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: " + googleMap);


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions()
                .icon(getMarkerDrawable()).title("Deneme")
                .position(position));

        MapsInitializer.initialize(getActivity());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(position);
        LatLngBounds bounds = builder.build();
        // int padding = 0;
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        // Updates the location and zoom of the MapView

         CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.moveCamera(cameraUpdate);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

    }


    public BitmapDescriptor getMarkerDrawable() {
        Log.d(TAG, "getMarkerDrawable: ");
        Drawable drawable = getContext().getDrawable(R.drawable.markerjob);
        // BitmapDescriptor bitmapDescriptor =

        return MapHelperMethods.getDrawableMarkerAsBitmap(drawable);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
