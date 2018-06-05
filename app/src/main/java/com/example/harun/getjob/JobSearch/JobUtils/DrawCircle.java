package com.example.harun.getjob.JobSearch.JobUtils;

/**
 * Created by mayne on 14.04.2018.
 */


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;


/**
 * Circle Class
 */
public class DrawCircle {

    private static final String TAG = "DrawCircle";
    private Marker mRadiusMarker, mRadiusMarker2;
    private Circle mCircle;
    private double mRadiusMeters;
    private GoogleMap mMap;
    private Context mContext;
    private LatLng circleLocation;

    public DrawCircle(Location center, double radiusMeters, GoogleMap _mMap, Context context, int mode) {
        Log.d(TAG, "DrawCircle:Circle Çiziliyor ");

        this.mRadiusMeters = radiusMeters;
        this.mMap = _mMap;
        this.mContext = context;
        circleLocation = MapHelperMethods.convertLocationtoLatLng(center);


        // deneme FillColorRenk Kodlarım
        //rgba(143, 230, 150, 1)    // int  mFillColorArgb=587267853;
        // 453902080 470679296
        // Color.argb(100, 243, 85, 133
        // )Color.argb(1, 143, 230, 150)
        int mFillColorArgb = 637597695;
        // rgba(209, 255, 163, 0.68)
        // rgba(184, 226, 121, 0.28)

        //Mode 1 JobSearch Circle
        if (mode == 1) {
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(circleLocation)
                    // .strokePattern(PATTERN_DOTTED)
                    .strokeColor(Color.WHITE)
                    .strokeWidth(5)
                    .fillColor(mFillColorArgb)
                    .radius(mRadiusMeters));


        } else if (mode == 2) {  //Mode 2 AddJobAdvert Step4 Circle

            mCircle = mMap.addCircle(new CircleOptions()
                    .center(circleLocation)
                    // .strokePattern(PATTERN_DOTTED)
                    .strokeColor(mContext.getResources().getColor(R.color.Brown))
                    .strokeWidth(5)
                    .fillColor(0x11d59563)
                    .radius(mRadiusMeters));


        }

        // Log.d(TAG, "DrawCircle: " + circleArea.getProgress() * 1000);

    }


    /**
     * Bu method Circle kapsama alanını çizer
     *
     * @param circleRadius --> SeekBar dan gelen km Değerine göre çizilen alan belirlenir yarıçap ayarlanır. .
     */
    public void setCircleRadius(int circleRadius) {

        this.mCircle.setRadius((circleRadius * 1000));
        //Log.d(TAG, "setCircleRadius: " + circleArea.getProgress() * 1000);
        //Log.d(TAG, "setCircleRadius: " + (circleRadius * 1000));


        /**

         Seek Bardan gelen km değerine göre çizilen her circle için kamerada circle in sağ ve solunda verilen
         koordinatlara göre kamera açısını belirler
         Sağ konum -->toRadiusLatLngRight(myLocation, (circleRadius * 1000) merkez kooordinattan parametre olarak gelen uzaklığın konumu
         Sol konum -->toRadiusLatLngLeft(myLocation, (circleRadius * 1000)  Bu konumlar sınır olarak belirlenir.


         */

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(MapHelperMethods.toRadiusLatLngLeft(getCenterCircle(), (circleRadius * 1000)));
        builder.include(MapHelperMethods.toRadiusLatLngRight(getCenterCircle(), (circleRadius * 1000)));

        final LatLngBounds bounds = builder.build();

        final int zoomWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        final int zoomHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        final int zoomPadding = (int) (zoomWidth * 0.10); // Genişliğin 0,10 u kadar kenarlardan boşluk


        ///  Log.d(TAG, "setCircleRadius: zoomWİDHT \t" + zoomHeight + "\t" + zoomWidth + "\t" + zoomPadding);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, zoomWidth, zoomHeight, zoomPadding));
    }

    public int getCircleRadius() {

        return ((int) mCircle.getRadius());

    }


    public void setCenterCircle(LatLng _center) {

        Log.d(TAG, "setCenterCircle: " + _center);
        mCircle.setCenter(_center);

    }

    public LatLng getCenterCircle() {

        return mCircle.getCenter();
    }

    public void removeCirle() {
        mCircle.remove();
        //mCircleList.clear();

    }
}
