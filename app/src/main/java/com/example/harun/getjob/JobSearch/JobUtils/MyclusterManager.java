package com.example.harun.getjob.JobSearch.JobUtils;

import android.util.Log;

import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by mayne on 20.04.2018.
 */


/**
 * Cluster Manager Nesnesini global bir snıfı alıyorum baska yerlerdende ulasmam gerekiy.r
 *
 */
public class MyclusterManager {
    private static final String TAG = "MyclusterManager";

    public ClusterManager<NearJobAdvertModel> myClusterManager;
    public MyClusterMarker myClusterMarker;
    public static MyclusterManager myclusterManager;

    public MyclusterManager() {
    }


    public static MyclusterManager getInstance() {
        if (myclusterManager == null) {
            myclusterManager = new MyclusterManager();
            return myclusterManager;
        } else {
            return myclusterManager;
        }
    }

    public MyClusterMarker getMyClusterMarker() {

        Log.d(TAG, "getMyClusterMarker: " + myClusterMarker);
        return myClusterMarker;
    }

    public void setMyClusterMarker(MyClusterMarker myClusterMarker) {
        Log.d(TAG, "setMyClusterMarker: " + myClusterMarker);
        this.myClusterMarker = myClusterMarker;
    }

    public void setMyClusterManager(ClusterManager<NearJobAdvertModel> myClusterManager) {
        Log.d(TAG, "setMyClusterManager: ");
        this.myClusterManager = myClusterManager;
    }

    public ClusterManager<NearJobAdvertModel> getMyClusterManager() {
        Log.d(TAG, "getMyClusterManager: ");
        return myClusterManager;
    }

    public void removeItemCluster(NearJobAdvertModel model) {

        Log.d(TAG, "removeItemCluster: ");
        myClusterManager.removeItem(model);

        myClusterManager.cluster();

    }


}
