package com.example.harun.getjob.profileModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by mayne on 17.02.2018.
 */

public class egitimListModel implements Parcelable{

    private static final String TAG = "egitimListModel";
    public String okul;
    public String bolum;
    public String ogrenimTuru;
    public String bsYılı;
    public String btsYılı;


    // private ArrayList<String[]> SpinnerListe;
    //  private String[] yil={"2018","2017","2016","2015","2014","2013","2012"};


    public egitimListModel() {


    }
    public egitimListModel(String okul, String bolum, String ogrenimTuru, String bsYılı, String btsYılı) {
        Log.d("egitimListMMODEL", "Egitim Model Listesi Dolduruluyor ");

        this.okul = okul;
        this.bolum = bolum;
        this.ogrenimTuru = ogrenimTuru;
        this.bsYılı = bsYılı;
        this.btsYılı = btsYılı;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(okul);
        parcel.writeString(bolum);
        parcel.writeString(ogrenimTuru);
        parcel.writeString(bsYılı);
        parcel.writeString(btsYılı);
    }
    protected egitimListModel(Parcel in) {
        okul = in.readString();
        bolum = in.readString();
        ogrenimTuru = in.readString();
        bsYılı = in.readString();
        btsYılı = in.readString();
    }

    public static final Creator<egitimListModel> CREATOR = new Creator<egitimListModel>() {
        @Override
        public egitimListModel createFromParcel(Parcel in) {
            return new egitimListModel(in);
        }

        @Override
        public egitimListModel[] newArray(int size) {
            return new egitimListModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }


}
