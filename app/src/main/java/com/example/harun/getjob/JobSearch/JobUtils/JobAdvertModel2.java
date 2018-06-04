package com.example.harun.getjob.JobSearch.JobUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by mayne on 24.05.2018.
 */

public class JobAdvertModel2 implements Parcelable {
    private static final String TAG = "JobAdvertModel2";
    private String companyName, companyJob, jobSector, jobDescpriction, companyLogoUrl, companyAdress,
            educationLevel, expLevel, employeeHour, drivingLicence, military, gender, publishDate;
    private ArrayList<String> jobPossibility;
    private LatLng mPosition;
    private int countApply;

    public JobAdvertModel2() {
        Log.d(TAG, "JobAdvertModel2: No PARAMATER CONSTRUCTOR");
    }

    public JobAdvertModel2(String companyName, String companyJob, String jobSector,
                           String jobDescpriction, String companyLogoUrl,
                           String companyAdress, String educationLevel,
                           String expLevel, String employeeHour,
                           String drivingLicence,
                           String military, String gender,
                           String publishDate, int countApply,
                           ArrayList<String> jobPossibility,
                           LatLng mPosition) {
        Log.d(TAG, "JobAdvertModel2: PARAMETERS CONSTRUCTOR");
        this.companyName = companyName;
        this.companyJob = companyJob;
        this.jobSector = jobSector;
        this.jobDescpriction = jobDescpriction;
        this.companyLogoUrl = companyLogoUrl;
        this.companyAdress = companyAdress;
        this.educationLevel = educationLevel;
        this.expLevel = expLevel;
        this.employeeHour = employeeHour;
        this.drivingLicence = drivingLicence;
        this.military = military;
        this.gender = gender;
        this.publishDate = publishDate;
        this.countApply = countApply;
        this.jobPossibility = jobPossibility;
        this.mPosition = mPosition;
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

    public String getJobSector() {
        return jobSector;
    }

    public void setJobSector(String jobSector) {
        this.jobSector = jobSector;
    }

    public String getJobDescpriction() {
        return jobDescpriction;
    }

    public void setJobDescpriction(String jobDescpriction) {
        this.jobDescpriction = jobDescpriction;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCompanyAdress() {
        return companyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(String expLevel) {
        this.expLevel = expLevel;
    }

    public String getEmployeeHour() {
        return employeeHour;
    }

    public void setEmployeeHour(String employeeHour) {
        this.employeeHour = employeeHour;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public String getMilitary() {
        return military;
    }

    public void setMilitary(String military) {
        this.military = military;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getCountApply() {
        return countApply;
    }

    public void setCountApply(int countApply) {
        this.countApply = countApply;
    }

    public ArrayList<String> getJobPossibility() {
        return jobPossibility;
    }

    public void setJobPossibility(ArrayList<String> jobPossibility) {
        Log.d(TAG, "setJobPossibility: " + jobPossibility.size());
        this.jobPossibility = jobPossibility;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public void setmPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    protected JobAdvertModel2(Parcel in) {
        companyName = in.readString();
        companyJob = in.readString();
        jobSector = in.readString();
        jobDescpriction = in.readString();
        companyLogoUrl = in.readString();
        companyAdress = in.readString();
        educationLevel = in.readString();
        expLevel = in.readString();
        employeeHour = in.readString();
        drivingLicence = in.readString();
        military = in.readString();
        gender = in.readString();
        publishDate = in.readString();
        countApply = in.readInt();
        jobPossibility = in.createStringArrayList();
        mPosition = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<JobAdvertModel2> CREATOR = new Creator<JobAdvertModel2>() {
        @Override
        public JobAdvertModel2 createFromParcel(Parcel in) {
            return new JobAdvertModel2(in);
        }

        @Override
        public JobAdvertModel2[] newArray(int size) {
            return new JobAdvertModel2[size];
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
        parcel.writeString(jobSector);
        parcel.writeString(jobDescpriction);
        parcel.writeString(companyLogoUrl);
        parcel.writeString(companyAdress);
        parcel.writeString(educationLevel);
        parcel.writeString(expLevel);
        parcel.writeString(employeeHour);
        parcel.writeString(drivingLicence);
        parcel.writeString(military);
        parcel.writeString(gender);
        parcel.writeString(publishDate);
        parcel.writeInt(countApply);
        parcel.writeStringList(jobPossibility);
        parcel.writeParcelable(mPosition, i);
    }

    @Override
    public String toString() {
        return "JobAdvertModel2{" +
                "companyName='" + companyName + '\'' +
                ", companyJob='" + companyJob + '\'' +
                ", jobSector='" + jobSector + '\'' +
                ", jobDescpriction='" + jobDescpriction + '\'' +
                ", companyLogoUrl='" + companyLogoUrl + '\'' +
                ", companyAdress='" + companyAdress + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", expLevel='" + expLevel + '\'' +
                ", employeeHour='" + employeeHour + '\'' +
                ", drivingLicence='" + drivingLicence + '\'' +
                ", military='" + military + '\'' +
                ", gender='" + gender + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", jobPossibility=" + jobPossibility +
                ", mPosition=" + mPosition +
                ", countApply=" + countApply +
                '}';
    }
}
