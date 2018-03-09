package com.example.harun.getjob.FirebaseMethods;

/**
 * Created by mayne on 5.03.2018.
 */


/**
 * Bu class Her kullanıcının edit profile Sayfasındaki ana bilgilerini tutaacak
 */

public class firebaseContent {

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
}
