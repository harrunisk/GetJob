package com.example.harun.getjob.FirebaseMethods;

/**
 * Created by mayne on 5.03.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bu class Her kullanıcının edit profile Sayfasındaki ana bilgilerini tutaacak
 */

public class MainContent implements Parcelable{

private String job;
private String location;
private String name;
private String profil_photo;


    public MainContent(String job, String location, String name) {
        this.job = job;
        this.location = location;
        this.name = name;
        //this.profil_photo = profil_photo;
    }

    public MainContent() {
    }


    protected MainContent(Parcel in) {
        job = in.readString();
        location = in.readString();
        name = in.readString();
        profil_photo = in.readString();
    }

    public static final Creator<MainContent> CREATOR = new Creator<MainContent>() {
        @Override
        public MainContent createFromParcel(Parcel in) {
            return new MainContent(in);
        }

        @Override
        public MainContent[] newArray(int size) {
            return new MainContent[size];
        }
    };

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfil_photo() {
        return profil_photo;
    }

    public void setProfil_photo(String profil_photo) {
        this.profil_photo = profil_photo;
    }


    @Override
    public String toString() {
        return "MainContent{" +
                "job='" + job + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
               // ", profil_photo='" + profil_photo + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(job);
        parcel.writeString(location);
        parcel.writeString(name);
        parcel.writeString(profil_photo);
    }
}
