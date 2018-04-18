package com.example.harun.getjob.JobSearch.JobUtils;

/**
 * Created by mayne on 14.04.2018.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


/**
 * Cluster İşlemleri..
 */
public class MyClusterMarker extends DefaultClusterRenderer<JobAdvertModel> {
    private static final String TAG = "MyClusterMarker";
   // public Drawable drawable;
    public Context mContext;
    public  boolean should_zoom;
    //private final IconGenerator icon = new IconGenerator(getApplicationContext());

    public MyClusterMarker(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.mContext = context;

    }

        /*public MyClusterMarker(Context context, GoogleMap map, ClusterManager<JobAdvertModel> clusterManager, Drawable drawable) {
            super(context, map, clusterManager);
            this.drawable = drawable;
        }
*/

    public  void setShould_zoom(boolean should_zoom) {
        this.should_zoom = should_zoom;
    }

    @Override
    protected void onBeforeClusterItemRendered(JobAdvertModel item, MarkerOptions markerOptions) {

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.markerjob);

        BitmapDescriptor jobMarker = MapHelperMethods.getDrawableMarkerAsBitmap(drawable);

        markerOptions.icon(jobMarker);

     //   markerOptions.title("Yakınımdaki İşler");


    }


    @Override
    protected void onClusterItemRendered(JobAdvertModel clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<JobAdvertModel> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    //Camera Zoom  12 oldugunda  ve Marker sayısı 5 oldugunda en az clusterin calısması için override edildi.

    //should_zoom true ve  cluster marker >4 oldugunda calısacak.
    @Override
    protected boolean shouldRenderAsCluster(Cluster<JobAdvertModel> cluster) {

       // Log.d(TAG, "shouldRenderAsCluster: " + should_zoom);
        return super.shouldRenderAsCluster(cluster) && should_zoom;

    }
}
