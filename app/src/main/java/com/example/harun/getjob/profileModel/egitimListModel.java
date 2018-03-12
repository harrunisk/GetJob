package com.example.harun.getjob.profileModel;

import android.util.Log;

/**
 * Created by mayne on 17.02.2018.
 */

public class egitimListModel {

    private String okul;
    private String bolum;
    private String ogrenimTuru;
    private String bsYılı;
    private String btsYılı;


   // private ArrayList<String[]> SpinnerListe;
  //  private String[] yil={"2018","2017","2016","2015","2014","2013","2012"};


    public egitimListModel(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı) {
        Log.d("egitimListMMODEL","Egitim Model Listesi Dolduruluyor ");

        this.okul = okul;
        this.bolum = bolum;
        this.ogrenimTuru = ogrenimTuru;
        this.bsYılı = bsYılı;
        this.btsYılı = btsYılı;
    }

    public String getOkul() {
        return okul;
    }

    public void setOkul(String okul) {
        this.okul = okul;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getOgrenimTuru() {
        return ogrenimTuru;
    }

    public void setOgrenimTuru(String ogrenimTuru) {
        this.ogrenimTuru = ogrenimTuru;
    }

    public String getBsYılı() {
        return bsYılı;
    }

    public void setBsYılı(String bsYılı) {
        this.bsYılı = bsYılı;
    }

    public String getBtsYılı() {
        return btsYılı;
    }

    public void setBtsYılı(String btsYılı) {
        this.btsYılı = btsYılı;
    }

    @Override
    public String toString() {
        return "egitimListModel{" +
                "okul='" + okul + '\'' +
                ", bolum='" + bolum + '\'' +
                ", ogrenimTuru='" + ogrenimTuru + '\'' +
                ", bsYılı='" + bsYılı + '\'' +
                ", btsYılı='" + btsYılı + '\'' +
                '}';
    }
}
