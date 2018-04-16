package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.example.harun.getjob.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by mayne on 14.04.2018.
 */

public class MapHelperMethods {

    private static final String TAG = "MapHelperMethods";

    /**
     * Verilen merkez koordinatına yine parametre olarak verilen mesafe uzaklıgınca koordinat gönderiri
     *
     * @param center-->Merkez    koordinatt
     * @param radiusMeters-->kaç metre uzaklıkta yarıcapın Mesafesi
     * @return --LatLng Değeri center.longitude + radiusAngle -->merkezin Sag tarafı
     */
    public static LatLng toRadiusLatLngRight(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / 6371009) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    /**
     * Verilen merkez koordinatına yine parametre olarak verilen mesafe uzaklıgınca koordinat gönderiri
     *
     * @param center->Merkez     koordinatt
     * @param radiusMeters-->kaç metre uzaklıkta Yarıçapın Mesafesi
     * @return --LatLng Değeri center.longitude - radiusAngle -->merkezin Sol tarafı
     */
    public static LatLng toRadiusLatLngLeft(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / 6371009) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude - radiusAngle);
    }

    /**
     * verilen Latlng tipindeki parametreyi Location Tipine dönüştürecek
     *
     * @param _latLng -
     * @return -->Location
     */
    public static Location convertLatLngtoLocation(LatLng _latLng) {
        Location targetLocation = new Location("");//provider name is unecessary
        targetLocation.setLatitude(_latLng.latitude);//your coords of course
        targetLocation.setLongitude(_latLng.longitude);
        return targetLocation;
    }

    /**
     * Drawable klasorundeki marker vektorel oldugu icin onu bitmap 'e çeviren fonksiyon yazıldı.
     * Vektörel drawable Bitmap ^e dönüştürülüyor
     *
     * @param currentMarker -->Drawable marker
     * @return
     */
    public static BitmapDescriptor getDrawableMarkerAsBitmap(Drawable currentMarker) {

        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(currentMarker.getIntrinsicWidth(), currentMarker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        currentMarker.setBounds(0, 0, currentMarker.getIntrinsicWidth(), currentMarker.getIntrinsicHeight());
        currentMarker.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);


    }

    public static  BitmapDescriptor getMarkerDrawable(Context context) {
        Log.d(TAG, "getMarkerDrawable: ");
        Drawable drawable = context.getDrawable(R.drawable.markerjob);
        // BitmapDescriptor bitmapDescriptor =

        return MapHelperMethods.getDrawableMarkerAsBitmap(drawable);
    }


    /**
     * @param _location--
     * @return -->Latlng
     */
    public static LatLng convertLocationtoLatLng(Location _location) {
        return new LatLng(_location.getLatitude(), _location.getLongitude());
    }


    /**
     * İki nokta arası uzaklık ölçülüyor
     *
     * @param center    -->bulundugunuz konum-->nereden
     * @param radius--> -->2.Konum .ç Ölçülecek mesafenin konumu-->nereye
     * @return ->double distance
     */
    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);


        return result[0];
    }


    /**
     * Gelen mesafe değerini parce eder
     *
     * @param distance --> double örn 1120,2121 gibi
     * @return --> 1120 m
     */
    public static int getDistanceParce(Double distance) {


        double km = distance / 1;
        DecimalFormat newFormat = new DecimalFormat("####");

        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = distance % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));


        // Log.d(TAG, "toRadiusMeters: " + km + "\t\t" + kmInDec + "\t\t" + meter + "\t\t" + meterInDec);

        return kmInDec;

    }

}
