package com.example.harun.getjob.profileModel;

import android.util.Log;

/**
 * Created by mayne on 15.02.2018.
 */

public class deneyimModel {

    private String pozisyon;
    private String kurum;
    private String lokasyon;
    private String ay;

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
