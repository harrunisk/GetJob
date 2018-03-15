package com.example.harun.getjob.FirebaseMethods;

/**
 * Created by mayne on 5.03.2018.
 */


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bu class Her kullanıcının edit profile Sayfasındaki ana bilgilerini tutaacak
 */

public class firebaseContent implements Parcelable{

private String job;
private String location;
private String name;
private String profil_photo;


    public firebaseContent(String job, String location, String name) {
        this.job = job;
        this.location = location;
        this.name = name;
        //this.profil_photo = profil_photo;
    }

    public firebaseContent() {
    }


    protected firebaseContent(Parcel in) {
        job = in.readString();
        location = in.readString();
        name = in.readString();
        profil_photo = in.readString();
    }

    public static final Creator<firebaseContent> CREATOR = new Creator<firebaseContent>() {
        @Override
        public firebaseContent createFromParcel(Parcel in) {
            return new firebaseContent(in);
        }

        @Override
        public firebaseContent[] newArray(int size) {
            return new firebaseContent[size];
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
        return "firebaseContent{" +
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
