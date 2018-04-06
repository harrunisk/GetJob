package com.example.harun.getjob.JobSearch;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mayne on 1.04.2018.
 */

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle, job, distance, newLocationDistance, publishDate;
    private String mSnippet;

    public MyItem(LatLng mPosition, String mTitle, String job, String publishDate) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.job = job;
        //  this.distance = distance;
        this.publishDate = publishDate;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public String getJob() {
        return job;
    }

    public String getDistance() {
        return distance + "m";
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getNewLocationDistance() {
      if (newLocationDistance == null) {
           return "";
       } else {
            return newLocationDistance + "m";
       }

    }

    public void setNewLocationDistance(String newLocationDistance) {
        this.newLocationDistance = newLocationDistance;
    }
}
