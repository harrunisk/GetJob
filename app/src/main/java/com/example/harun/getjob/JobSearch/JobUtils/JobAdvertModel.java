package com.example.harun.getjob.JobSearch.JobUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.altervista.andrearosa.statebutton.StateButton;

/**
 * Created by mayne on 20.03.2018.
 */


/**
 * Bir iş ilanının içerdiği bilgiler
 */
public class JobAdvertModel implements ClusterItem, Parcelable {

    private static final String TAG = "JobAdvertModel";

    private String companyName, companyJob, jobDescpriction, companyLocation, companyDistance, companyLogoUrl;
    private LatLng mPosition;
    private String mTitle, experienceinfo, countApply, newLocationDistance, publishDate;
    private boolean isSave = false;
    private int basvuruDurumu = 0;


    public StateButton.BUTTON_STATES getBasvuruDurumu() {

        if (basvuruDurumu == 0) {

            return StateButton.BUTTON_STATES.ENABLED;//Yani basvurulabiliri duruma gec basvuru yapılmamıs isse

        } else {
            return StateButton.BUTTON_STATES.DISABLED; //Basvuru yapılmıs ise butonu devre dısı bırak .
        }
    }


    public void setBasvuruDurumu(int _basvuruDurumu) { //İlana Basvuru yapılmısmı
        this.basvuruDurumu = _basvuruDurumu;
    }


    public boolean isSave() {   //İlan kayıt edilmişmi ..
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public JobAdvertModel() {

    }


    public String getExperienceinfo() {
        return experienceinfo;
    }

    public void setExperienceinfo(String experienceinfo) {
        this.experienceinfo = experienceinfo;
    }

    public JobAdvertModel(String companyName,
                          String companyJob,
                          String jobDescpriction,
                          String companyLocation,
                          String companyDistance,
                          //LatLng mPosition,
                          //    String publishDate,
                          String companyLogoUrl) {

        Log.d(TAG, "JobAdvertModel: Constructor");

        this.companyName = companyName;
        this.companyJob = companyJob;
        this.jobDescpriction = jobDescpriction;
        this.companyLocation = companyLocation;
        this.companyDistance = companyDistance;
        // this.mPosition = mPosition;
        //   this.publishDate = publishDate;
        this.companyLogoUrl = companyLogoUrl;
    }


    public JobAdvertModel(String companyName,
                          String companyJob,
                          String jobDescpriction,
                          String companyLocation,
                          LatLng mPosition,
                          String publishDate,
                          String experienceinfo,
                          String countApply,
                          // String companyDistance,
                          String companyLogoUrl) {

        Log.d(TAG, "JobAdvertModel: Constructor");

        this.companyName = companyName;
        this.companyJob = companyJob;
        this.jobDescpriction = jobDescpriction;
        this.companyLocation = companyLocation;
        this.countApply = countApply;
        this.mPosition = mPosition;
        this.experienceinfo = experienceinfo;
        this.publishDate = publishDate;
        //this.companyDistance = companyDistance;
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCountApply() {
        return countApply + "\nBasvuru";
    }

    public void setCountApply(String countApply) {
        this.countApply = countApply;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyJob() {
        return companyJob;
    }

    public void setCompanyJob(String companyJob) {
        this.companyJob = companyJob;
    }

    public String getJobDescpriction() {
        return jobDescpriction;
    }

    public void setJobDescpriction(String jobDescpriction) {
        this.jobDescpriction = jobDescpriction;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getCompanyDistance() {
        return companyDistance + "m";
    }

    public void setCompanyDistance(String companyDistance) {
        this.companyDistance = companyDistance;
    }

    public String getNewLocationDistance() {
        if (newLocationDistance == null) {
            return "";
        } else {
            return newLocationDistance + "m";
        }
    }

    public void setNewLocationDistance(String newLocationDistance) {
        this.newLocationDistance = newLocationDistance;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }


    protected JobAdvertModel(Parcel in) {
        companyName = in.readString();
        companyJob = in.readString();
        jobDescpriction = in.readString();
        companyLocation = in.readString();
        companyDistance = in.readString();
        companyLogoUrl = in.readString();
        mPosition = in.readParcelable(LatLng.class.getClassLoader());
        mTitle = in.readString();
        experienceinfo = in.readString();
        countApply = in.readString();
        newLocationDistance = in.readString();
        publishDate = in.readString();
    }

    public static final Creator<JobAdvertModel> CREATOR = new Creator<JobAdvertModel>() {
        @Override
        public JobAdvertModel createFromParcel(Parcel in) {
            return new JobAdvertModel(in);
        }

        @Override
        public JobAdvertModel[] newArray(int size) {
            return new JobAdvertModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(companyName);
        parcel.writeString(companyJob);
        parcel.writeString(jobDescpriction);
        parcel.writeString(companyLocation);
        parcel.writeString(companyDistance);
        parcel.writeString(companyLogoUrl);
        parcel.writeParcelable(mPosition, i);
        parcel.writeString(mTitle);
        parcel.writeString(experienceinfo);
        parcel.writeString(countApply);
        parcel.writeString(newLocationDistance);
        parcel.writeString(publishDate);
    }


}
