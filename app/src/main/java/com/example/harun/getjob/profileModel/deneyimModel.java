package com.example.harun.getjob.profileModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by mayne on 15.02.2018.
 */

public class deneyimModel implements Parcelable {

    public String pozisyon;
    public String kurum;
    public String lokasyon;
    public String ay;

    public deneyimModel(String pozisyon, String kurum, String lokasyon, String ay) {
        Log.d("deneyimModelConstructor","Deneyim Model Listesi Dolduruluyor ");

        this.pozisyon = pozisyon;
        this.kurum = kurum;
        this.lokasyon = lokasyon;
        this.ay = ay;
    }

    public deneyimModel() {

        Log.d("deneyimModelNoparameter","Deneyim Model Listesi Dolduruluyor ");
    }


    protected deneyimModel(Parcel in) {
        pozisyon = in.readString();
        kurum = in.readString();
        lokasyon = in.readString();
        ay = in.readString();
    }

    public static final Creator<deneyimModel> CREATOR = new Creator<deneyimModel>() {
        @Override
        public deneyimModel createFromParcel(Parcel in) {
            return new deneyimModel(in);
        }

        @Override
        public deneyimModel[] newArray(int size) {
            return new deneyimModel[size];
        }
    };

    public String getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
    }

    public String getKurum() {
        return kurum;
    }

    public void setKurum(String kurum) {
        this.kurum = kurum;
    }

    public String getLokasyon() {
        return lokasyon;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
    }

    public String getAy() {
        return ay;
    }

    public void setAy(String ay) {
        this.ay = ay;
    }

    @Override
    public String toString() {
        return "deneyimModel{" +
                "pozisyon='" + pozisyon + '\'' +
                ", kurum='" + kurum + '\'' +
                ", lokasyon='" + lokasyon + '\'' +
                ", ay='" + ay + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pozisyon);
        parcel.writeString(kurum);
        parcel.writeString(lokasyon);
        parcel.writeString(ay);
    }


/*
    //Deneme Amaclı liste doldurdum activityden cagıracagım.
    public static ArrayList<deneyimModel> denemeListe() {

        ArrayList<deneyimModel> denemeList = new ArrayList<deneyimModel>();

        String a = "güvenlik görevlisi";
        String b = "carrefour";
        String c = "edirne";
        String d = "7ay";

        for (int i = 0; i < 4; i++) {

            deneyimModel gecici = new deneyimModel();
            gecici.setPozisyon(a + i);
            gecici.setKurum(b + i);
            gecici.setLokasyon(c + i);
            gecici.setAy(d + i);


            denemeList.add(gecici);

        }


        return denemeList;

    }*/


}
