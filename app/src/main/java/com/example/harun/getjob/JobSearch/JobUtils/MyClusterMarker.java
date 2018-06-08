package com.example.harun.getjob.JobSearch.JobUtils;

/**
 * Created by mayne on 14.04.2018.
 */

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


/**
 * Cluster İşlemleri..
 */
public class MyClusterMarker extends DefaultClusterRenderer<NearJobAdvertModel> {
    private static final String TAG = "MyClusterMarker";
    // public Drawable drawable;
    public Context mContext;
    public boolean should_zoom;
    //private final IconGenerator icon = new IconGenerator(getApplicationContext());

    public MyClusterMarker(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.mContext = context;

    }

    public void setShould_zoom(boolean should_zoom) {
        this.should_zoom = should_zoom;
    }

    @Override
    public void onClusterItemRendered(NearJobAdvertModel clusterItem, Marker marker) {

        super.onClusterItemRendered(clusterItem, marker);


    }


    @Override
    public void onClusterRendered(Cluster<NearJobAdvertModel> cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);


    }

    @Override
    public void onBeforeClusterItemRendered(NearJobAdvertModel item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getMarkerIcon());
    }


    @Override
    public void onBeforeClusterRendered(Cluster<NearJobAdvertModel> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);


    }

    //Camera Zoom  12 oldugunda  ve Marker sayısı 5 oldugunda en az clusterin calısması için override edildi.

    //should_zoom true ve  cluster marker >4 oldugunda calısacak.
    @Override
    public boolean shouldRenderAsCluster(Cluster<NearJobAdvertModel> cluster) {

        // Log.d(TAG, "shouldRenderAsCluster: " + should_zoom);
        return super.shouldRenderAsCluster(cluster) && should_zoom;

    }


    @Override
    public Marker getMarker(Cluster<NearJobAdvertModel> cluster) {
        Log.d(TAG, "getMarker: " + cluster);
        return super.getMarker(cluster);


    }

    @Override
    public Marker getMarker(NearJobAdvertModel clusterItem) {
        Log.d(TAG, "getMarker: ");
        return super.getMarker(clusterItem);
    }


    @Override
    public Cluster<NearJobAdvertModel> getCluster(Marker marker) {
        return super.getCluster(marker);
    }

    @Override
    public NearJobAdvertModel getClusterItem(Marker marker) {
        return super.getClusterItem(marker);
    }


}
