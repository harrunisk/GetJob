package com.example.harun.getjob.JobSearch;

import android.util.Log;

/**
 * Created by mayne on 20.03.2018.
 */

public class JobAdvertModel {

    private static final String TAG = "JobAdvertModel";

    private String companyName, companyJob, jobDescpriction, companyLocation, companyDistance, companyLogoUrl;

    public JobAdvertModel(String companyName, String companyJob, String jobDescpriction, String companyLocation, String companyDistance, String companyLogoUrl) {
        Log.d(TAG, "JobAdvertModel: Constructor");

        this.companyName = companyName;
        this.companyJob = companyJob;
        this.jobDescpriction = jobDescpriction;
        this.companyLocation = companyLocation;
        this.companyDistance = companyDistance;
        this.companyLogoUrl = companyLogoUrl;
    }

    public JobAdvertModel() {
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
        return companyDistance;
    }

    public void setCompanyDistance(String companyDistance) {
        this.companyDistance = companyDistance;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    @Override
    public String toString() {
        return "JobAdvertModel{" +
                "companyName='" + companyName + '\'' +
                ", companyJob='" + companyJob + '\'' +
                ", jobDescpriction='" + jobDescpriction + '\'' +
                ", companyLocation='" + companyLocation + '\'' +
                ", companyDistance='" + companyDistance + '\'' +
                ", companyLogoUrl='" + companyLogoUrl + '\'' +
                '}';
    }

}
