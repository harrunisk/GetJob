package com.example.harun.getjob.JobSearch;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mayne on 1.04.2018.
 */

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public MyItem(LatLng mPosition, String mTitle) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
