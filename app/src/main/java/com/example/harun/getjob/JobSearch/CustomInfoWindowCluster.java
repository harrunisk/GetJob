package com.example.harun.getjob.JobSearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.harun.getjob.JobSearch.JobUtils.JobAdvertModel;
import com.example.harun.getjob.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by mayne on 3.04.2018.
 */

public class CustomInfoWindowCluster implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "CustomInfoWindowCluster";
    private View myContentsView;
    private LayoutInflater inflate;
    private TextView tvCompanyName, tvSnippet, publishDate, distance, newLocationDistance;
    private JobAdvertModel clickedItem;


    CustomInfoWindowCluster(Context context) {

        Log.d(TAG, "CustomInfoWindowCluster:Constructor Cagırıldı ");
        this.inflate = LayoutInflater.from(context);
        //   this.clickedItem = _clickedItem;
    }

    public void setClickedItem(JobAdvertModel clickedItem) {

        this.clickedItem = clickedItem;
    }


    @Override
    public View getInfoContents(Marker marker) {

        Log.d(TAG, "getInfoContents: İçerikler Dolduruluyor.");
        myContentsView = inflate.inflate(
                R.layout.custom_info_contents, null);
        tvCompanyName = myContentsView
                .findViewById(R.id.title);
        tvSnippet = myContentsView
                .findViewById(R.id.snippet);

        publishDate = myContentsView.findViewById(R.id.publishDate);
        distance = myContentsView.findViewById(R.id.distance);
        newLocationDistance = myContentsView.findViewById(R.id.distance2);
        tvCompanyName.setText(clickedItem.getCompanyName());
        tvSnippet.setText(clickedItem.getCompanyJob());
        publishDate.setText(clickedItem.getPublishDate());
        distance.setText(clickedItem.getCompanyDistance());
        newLocationDistance.setText(clickedItem.getNewLocationDistance());


        return myContentsView;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        // Log.d(TAG, "getInfoWindow: ");
        /*myContentsView = inflate.inflate(
                R.layout.custom_info_contents, null);
        tvCompanyName = myContentsView
                .findViewById(R.id.title);
        tvSnippet = myContentsView
                .findViewById(R.id.snippet);

        publishDate = myContentsView.findViewById(R.id.publishDate);
        distance = myContentsView.findViewById(R.id.distance);


        tvCompanyName.setText(marker.getTitle());
*/

        return null;
    }
}
