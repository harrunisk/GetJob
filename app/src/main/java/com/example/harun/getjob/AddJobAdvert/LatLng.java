package com.example.harun.getjob.AddJobAdvert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayne on 5.06.2018.
 */

/**
 * com.google.firebase.database.DatabaseException:
 * Class com.google.android.gms.maps.model.LatLng is missing a constructor with no arguments
 * <p>
 * JobAdvertModel2 model2 = child.getValue(JobAdvertModel2.class); bu kod calıstıgı zaman normal latlng değerlerini
 * Firebaseden alamıyoruz yukarıda belirtmiş oldugum hatayı veriyor .Bu yüzden
 * Latlng classını kendim olusturdum buradan kullanıyorum. com.google.android.gms.maps.model.LatLng sınıfının yani google 'ın
 * Latlng kütüphanesinde Parametresiz constructor olmadıgı için  hata veriyor .
 */
public class LatLng implements Parcelable {

    private Double latitude;
    private Double longitude;
  // private com.google.android.gms.maps.model.LatLng mapsLatLng;

    protected LatLng(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
     //   mapsLatLng = in.readParcelable(com.google.android.gms.maps.model.LatLng.class.getClassLoader());
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

//    public com.google.android.gms.maps.model.LatLng getMapsLatLng() {
//        return mapsLatLng;
//    }
//
//    public void setMapsLatLng(com.google.android.gms.maps.model.LatLng mapsLatLng) {
//        this.mapsLatLng = mapsLatLng;
//    }

    public LatLng() {
    }

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
//        this.mapsLatLng =
//                new com.google.android.gms.maps.model.LatLng(latitude,
//                        longitude);
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
       // parcel.writeParcelable(mapsLatLng, i);
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
